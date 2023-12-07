package com.homework.genshinchat;

import com.homework.genshinchat.common.R;
import com.homework.genshinchat.entity.Friend;
import com.homework.genshinchat.entity.Message;
import com.homework.genshinchat.entity.User;
import com.homework.genshinchat.entity.UserInfo;
import com.homework.genshinchat.service.FriendService;
import com.homework.genshinchat.service.MessageService;
import com.homework.genshinchat.service.UserInfoService;
import com.homework.genshinchat.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j

class WechatApplicationTests {
    @Autowired
    private UserService userService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private FriendService friendService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Test
    void insertPerson() {
        User user = new User();
        UserInfo userInfo =new UserInfo();
        String ids = "114514";
        String friendIds = "";
        for (int i = 0; i < 500; i++) {
            user.setStatus(true);
            String id = UUID.randomUUID().toString().substring(0, 9);
//            if(i == 0) ids = id;
//            else if(i == 1) friendIds= id;
            friendIds = id;
            user.setId(id);
            user.setPassword(UUID.randomUUID().toString().substring(0,9 ));
            String username = UUID.randomUUID().toString().substring(0,9 );
            user.setName(username);
            user.setCreateTime(LocalDateTime.now());
            user.setUpdateTime(LocalDateTime.now());
            userService.findPerson(user);

            userInfo.setUserid(id);
            userInfo.setUsername(username);
            userInfoService.save(userInfo);
            Friend friend = new Friend();
            friend.setFriendId(friendIds);
            friend.setId(ids);
            friendService.save(friend);

            friend.setId(friendIds);
            friend.setFriendId(ids);
            friendService.save(friend);
        }
        // 添加好友



//        Message message = new Message();
//        for(int i = 0; i < 5; ++i){
//            String s = UUID.randomUUID().toString();
//            message.setMyId(ids);
//            message.setFriendId(friendIds);
//            message.setChatType(0);
//            message.setMsg(s);
////            message
//            messageService.save(message);
//            message.setMyId(friendIds);
//            message.setFriendId(ids);
//            messageService.save(message);
//        }
//        log.info("{}, {}", ids, friendIds);
    }
    @Test
    void testforTrim(){
        for(char ch = 'a'; ch <= 'g'; ++ch){
            String key = "";
            key += ch;
            redisTemplate.opsForList().rightPush(key, "111");
        }
        redisTemplate.delete("a");
//        redisTemplate.opsForList().trim("a", 0, 2);
    }
}
