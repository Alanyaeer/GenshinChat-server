package com.homework.genshinchat.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.homework.genshinchat.common.R;
import com.homework.genshinchat.dto.FriendDto;
import com.homework.genshinchat.dto.MessageDto;
import com.homework.genshinchat.entity.Friend;
import com.homework.genshinchat.entity.Message;
import com.homework.genshinchat.entity.User;
import com.homework.genshinchat.entity.UserInfo;
import com.homework.genshinchat.service.FriendService;
import com.homework.genshinchat.service.MessageService;
import com.homework.genshinchat.service.UserInfoService;
import com.homework.genshinchat.service.UserService;
import com.homework.genshinchat.utils.LivePerson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.homework.genshinchat.constants.RedisConstants.*;
import static java.lang.Math.abs;

/**
 * @date 2023/10/21 19:29
 */
@RestController
@Slf4j
@RequestMapping("/friend")
@Api(tags = "聊天相关操作")
public class MessageController {
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserService userService;
    @Autowired
    private FriendService friendService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private RestHighLevelClient client;
    /**
     * @Author 吴凯煜
     * @Params map
     * @return R<Integer>
     */
    @PostMapping("/friendList")
    @ApiOperation("获取好友")
    public R<List<FriendDto>> getFriendList(@RequestBody Map map){
        String curid = map.get("id").toString();
        List<Friend> friendList = null;
        List<String> stringList = redisTemplate.opsForList().range(FRIEND_PERSON_KEY + ":" + curid, 0, 30);
        LivePerson.inLivePerson(curid);
        if(CollectionUtil.isEmpty(stringList) == false){
            List<Friend> friendListinner = stringList.stream().map((e) -> {
                Friend friendDto = JSON.parseObject(e, Friend.class);
                return friendDto;
            }).collect(Collectors.toList());
            friendList = friendListinner;
        }else{
            friendList = friendService.findAllFriend(map.get("id").toString());
            List<String> list = friendList.stream().map((e) -> {
                e.setCreateTime(null);
                e.setUpdateTime(null);
                String listitem = JSON.toJSONString(e);
                return listitem;
            }).collect(Collectors.toList());
            if(CollectionUtil.isEmpty(list) == false)
            redisTemplate.opsForList().rightPushAll(FRIEND_PERSON_KEY + ":" + curid, list);
        }
        //TODO 获取所有好友, 同时在查询过程中判断用户是否在线 （从User表中查询用户的status）
        List<UserInfo> userInfoList = new ArrayList<>();
        friendList.forEach(friend -> {
            String id = friend.getFriendId();
            String itemtemp = (String)redisTemplate.opsForHash().get(USER_INFO_KEY, id);
            UserInfo redisData = null;
            if(StrUtil.isBlank(itemtemp) == false){
                 redisData = JSON.parseObject(itemtemp, UserInfo.class);
                userInfoList.add(redisData);
            }
            else{
                UserInfo item = userInfoService.findid(id);
                redisTemplate.opsForHash().put(USER_INFO_KEY, id, JSON.toJSONString(item));
                userInfoList.add(item);
            }
        });
        if(userInfoList == null||userInfoList.size() == 0){
            List<FriendDto> userInfoListTemp = new ArrayList<>();
            return R.success(userInfoListTemp);
        }
        List<FriendDto> friendDtoList = userInfoList.stream().map((userInfo)->{
            FriendDto friendDto = new FriendDto();
            // 这里可能要拿redis优化当前status 为 true的用户
            friendDto.setHeadImg(userInfo.getUserimg());
            friendDto.setId(userInfo.getUserid());
            friendDto.setDetail(userInfo.getUserdetail());
            friendDto.setName(userInfo.getUsername());
            friendDto.setImg("");
            friendDto.setLastMsg("");
            if(LivePerson.checkPersonInLive(userInfo.getUserid()))
                friendDto.setStatus(true);
            else friendDto.setStatus(false);
            return friendDto;
        }).collect(Collectors.toList());


        return R.success(friendDtoList);

    }
    /**
     * @Author 吴国烨
     * @Params messageDto
     * @return R<Message>
     */
    @PostMapping("chatMsg")
    @ApiOperation("获取好友消息")
    public R<List<Message>> getChatMsg(@RequestBody MessageDto messageDto)  {
        List<String> MessageStringList = redisTemplate.opsForList().range(CHATLIST_PERSON_KEY + messageDto.getMyId() + ":" + messageDto.getFriendId(), 0, 100);
        log.info("正在查询是否有缓存消息");
        if(CollectionUtil.isEmpty(MessageStringList) != true){
            List<Message> messageList = MessageStringList.stream().map((e) -> {
                Message message = JSON.parseObject(e, Message.class);
                return message;
            }).collect(Collectors.toList());
            return R.success(messageList);
        }
        log.info(messageDto.getFriendId());
        if(messageDto.getFriendId() == null || messageDto.getFriendId().equals("")){
            SearchRequest request = new SearchRequest("message");
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
            boolQuery.should(QueryBuilders.termQuery("myId", messageDto.getMyId()));
            boolQuery.should(QueryBuilders.termQuery("friendId", messageDto.getMyId()));
            boolQuery.filter(QueryBuilders.matchQuery("msg", messageDto.getMsg()));
            request.source().query(boolQuery);
            SearchResponse search = null;
            try {
                search = client.search(request, RequestOptions.DEFAULT);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return R.success(handleResponse(search));
        }

        List<Message> messageList = messageService.getlistById(messageDto);
        new Thread(()->{
            for (Message message : messageList) {
                redisTemplate.opsForList().rightPush(CHATLIST_PERSON_KEY + messageDto.getMyId() + ":" + messageDto.getFriendId(), JSON.toJSONString(message));
//                redisTemplate.opsForList().rightPush(CHATLIST_PERSON_KEY + messageDto.getFriendId()+ ":" + messageDto.getMyId(), JSON.toJSONString(message));
            }
        }).start();
        if(messageList == null) return R.error("快去和好友聊天吧!!!");
        return R.success(messageList);
    }
    private static List<Message>  handleResponse(SearchResponse response) {
        SearchHits searchHits = response.getHits();
        Long total = searchHits.getTotalHits().value;
        System.out.println("共搜索到" + total +"条数据");
        SearchHit[] hits = searchHits.getHits();
        List<Message> messageList = new ArrayList<>();
        Long lastid = -1L;
        for(SearchHit hit: hits){
            String json = hit.getSourceAsString();
            Message message = JSON.parseObject(json, Message.class);
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            // 这是lambda 优化代码
            Long nowid = message.getId();
            Optional.ofNullable(highlightFields)
                    .map(e -> e.get("name"))
                    .map(e -> e.getFragments()[0].toString())
                    .ifPresent(message::setMsg);
            if(message.getMyId().equals(message.getUid()))
            messageList.add(message);
            lastid = nowid;
        }
        return messageList;
    }
}
