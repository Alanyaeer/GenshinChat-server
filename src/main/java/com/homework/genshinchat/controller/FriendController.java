package com.homework.genshinchat.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.homework.genshinchat.common.R;
import com.homework.genshinchat.dto.FriendDto;
import com.homework.genshinchat.entity.Friend;
import com.homework.genshinchat.entity.RedisData;
import com.homework.genshinchat.entity.User;
import com.homework.genshinchat.entity.UserInfo;
import com.homework.genshinchat.service.FriendService;
import com.homework.genshinchat.service.MessageService;
import com.homework.genshinchat.service.UserInfoService;
import com.homework.genshinchat.service.UserService;
import com.homework.genshinchat.utils.CacheClient;
import com.homework.genshinchat.utils.LivePerson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.homework.genshinchat.constants.RedisConstants.*;
import static java.util.Collections.swap;

/**
 * @date 2023/10/21 19:29
 */

@RestController
@Slf4j
@RequestMapping("/api")
@Api(tags = "好友相关操作")
public class FriendController {
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserService userService;
    @Autowired
    private FriendService friendService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private MessageService messageService;
    @Autowired
    private CacheClient client;
    private static final ExecutorService CACHE_REBUILD_EXECUTOR = Executors.newFixedThreadPool(5);

    @PostConstruct
    private void init(){
        List<FriendDto> list = FriendList();
        log.info("数据预热");
        client.setWithLogicalExpire(FRIEND_ALL_KEY, list, FRIEND_ALL_TTL, TimeUnit.SECONDS);
        log.info("预热完毕");
    }
    public List<FriendDto> FriendList() {
        List<User> userList = userService.findAllPerson();
        List<String> ids = userList.stream().map((user)->{
            String id = user.getId();
            // 可能这里会出现 Integer 转换为 boolean的问题？
            return id;
        }).collect(Collectors.toList());

        List<UserInfo> userInfoList = userInfoService.findbyIds(ids);
        if(userInfoList==null){
            return null;
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
        return friendDtoList;
    }
    /**
     * @Author 吴嘉豪
     * @param
     * @return R<Integer>
     */
    @PostMapping("/searchfriends")
    @ApiOperation( "获取好友列表")
    public R<List<FriendDto>> getFriendList(){
        //TODO 获取所有人, 同时在查询过程中判断用户是否在线 （从User表中查询用户的status）
        List<FriendDto> friendDtoList = client
                .querywithLogicalExpireFriend(FRIEND_ALL_KEY, FriendDto.class, this::FriendList, FRIEND_ALL_TTL, TimeUnit.SECONDS);
        return R.success(friendDtoList);
    }

    /**
     * @Author 祝华笙
     * @param  friend
     * @return R<Integer>
     *
     */
    @PostMapping("/addfriend")
    @ApiOperation( "增加好友")

    public R<Integer> addFriend(@RequestBody Friend friend){
        String friendId = friend.getFriendId();
        String myId = friend.getId();
        if(friend.getId().trim() == "" || friend.getFriendId().trim() == ""){
            return R.error("不存在该用户");
        }

        if(friendService.issue(friend)){
            CACHE_REBUILD_EXECUTOR.execute(()->{
                redisTemplate.opsForList().rightPush(FRIEND_PERSON_KEY + ":"+friendId, JSON.toJSONString(friend));
                redisTemplate.opsForList().rightPush(FRIEND_PERSON_KEY + ":"+myId, JSON.toJSONString(friend));
                friendService.save(friend);
            });
        }
        else return R.error("已经添加该用户");
        return R.success(1);
    }

    /**
     * @Author 吴凯煜
     * @param  friend
     * @return  R<Integer>
     *
     */
    @PostMapping("/deletefriend")
    @ApiOperation( "删除好友")

    public R<Integer> deleteFriend(@RequestBody Friend friend){
        String friendId = friend.getFriendId();
        String myId = friend.getId();
        CACHE_REBUILD_EXECUTOR.submit(()->{
            boolean issue = friendService.issue(friend);
            if(issue) return ;
            redisTemplate.opsForList().remove(FRIEND_PERSON_KEY + ":"+friendId, 0, redisTemplate.opsForList().size(FRIEND_PERSON_KEY + ":"+friendId));
            redisTemplate.opsForList().remove(FRIEND_PERSON_KEY + ":"+myId, 0, redisTemplate.opsForList().size(FRIEND_PERSON_KEY + ":"+myId));
            friendService.deleteById(friend);
        });
        return R.success(1);
    }
    /**
     * @Author   祝华笙
     * @param  friend
     * @return  R<Integer>
     *
     */
    @PostMapping("/updatefriend")
    @ApiOperation( "更新好友")

    public R<Integer> updateFriend(@RequestBody Friend friend){
        // TODO 就是修改传送过来的好友time
        friendService.update(friend);

        return R.success(1);
    }
    @GetMapping("/findFriend")
    @ApiOperation("查找用户接口")
    public R<FriendDto> findFriend( String id) {
        return R.success(userService.findPersonid(id));
    }
}
