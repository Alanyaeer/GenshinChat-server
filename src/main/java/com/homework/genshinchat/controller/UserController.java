package com.homework.genshinchat.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.homework.genshinchat.common.R;
import com.homework.genshinchat.entity.Friend;
import com.homework.genshinchat.entity.User;
import com.homework.genshinchat.entity.UserInfo;
import com.homework.genshinchat.mapper.FriendMapper;
import com.homework.genshinchat.mapper.UserInfoMapper;
import com.homework.genshinchat.service.FriendService;
import com.homework.genshinchat.service.UserInfoService;
import com.homework.genshinchat.service.UserService;
import com.homework.genshinchat.utils.LivePerson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

import static com.homework.genshinchat.constants.RedisConstants.FRIEND_PERSON_KEY;
import static com.homework.genshinchat.constants.RedisConstants.USER_INFO_KEY;


/**
 * @date 2023/10/21 19:29
 */
@Slf4j
@RestController
@RequestMapping("/api")
@Api(tags = "用户相关操作")

public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private FriendService friendService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @PostConstruct
    private void init() {
        log.info("用户信息预热");
        LambdaQueryWrapper<UserInfo> wrapper = new LambdaQueryWrapper<>();
        List<UserInfo> userInfoList = userInfoMapper.selectList(wrapper);
        userInfoList.forEach(e->{
            redisTemplate.opsForHash().put(USER_INFO_KEY, e.getUserid(), JSON.toJSONString(e));
        });
        log.info("用户信息预热完毕");

    }
    /**
     * @Author 吴嘉豪
     * @param user
     * @return R<Integer>
     */
    @PostMapping("/register")
    @ApiOperation("用户注册")

    public R<Integer> register(@RequestBody User user){
        log.info("注册用户");
        // 判断 数据库里面是否存在id
        if(userService.findPerson(user)){
            log.info("成功");
            UserInfo userInfo = new UserInfo();
            userInfo.setUserid(user.getId());
            userInfoService.save(userInfo);
            Friend friend = new Friend();
            friend.setId(user.getId());
            friend.setFriendId(user.getId());
            redisTemplate.opsForList().rightPush(FRIEND_PERSON_KEY + ":"+ user.getId(), JSON.toJSONString(friend));
            friendService.save(friend);
            return R.success(1);
        }
        else {
            log.info("已存在相同用户");
            return R.error("已存在该id");
        }

        // 看响应接口返回什么？
    }
    /**
     * @Author 吴嘉豪
     * @param map
     * @return R<Integer>
     */
    @PostMapping("/login")
    @ApiOperation("用户登录")

    public R<Integer> login(@RequestBody Map map){
        String id = map.get("id").toString();
        String password = map.get("password").toString();
        if(userService.checkpassword(id, password)){
            LivePerson.inLivePerson(id);
            // 删除所有缓存，重新获取。
            redisTemplate.delete(FRIEND_PERSON_KEY + ":"+ id);
            // 没有找到 id 对应的 userInfo 对象
            return R.success(1);
        }
        else return R.error("请检查密码或者账号");
    }
    /**
     * @Author 吴嘉豪
     * @param map
     * @return R<UserInfoDto>
     */
    @PostMapping("/getuserInfo")
    @ApiOperation("用户信息")

    public R<UserInfo> getuserInfo (@RequestBody Map map){
        String id = map.get("id").toString();
        String itemtemp = (String) redisTemplate.opsForHash().get(USER_INFO_KEY, id);
        log.info("获取的用户json为:{}", itemtemp);
        if(StrUtil.isBlank(itemtemp) == false){
            UserInfo redisData = JSON.parseObject(itemtemp, UserInfo.class);
            return R.success(redisData);
        }
        UserInfo userInfo = userInfoService.getUserInfo(id);
        log.info(String.valueOf(userInfo));
        if(userInfo != null){
            redisTemplate.opsForHash().put(USER_INFO_KEY, id, JSON.toJSONString(userInfo));

        }
        UserInfo info = new UserInfo();
        info.setUserid(id);
        return R.success(info);
    }
    /**
     * @Author 吴国烨
     * @param userInfo
     * @return R<Interger>
     */
    @PostMapping("/save")
    @ApiOperation("保存用户信息")

    public R<Integer> saveuserInfo(@RequestBody UserInfo userInfo){
        //TODO 存储用户的信息，查看文档
        log.info("{}", userInfo);
        new Thread(()->{
            if(userInfoService.saveUserInfo(userInfo) == false){
                userInfoService.save(userInfo);

            }
        }).start();
        redisTemplate.opsForHash().put(USER_INFO_KEY, userInfo.getUserid(), JSON.toJSONString(userInfo));

        return R.success(1);
    }
    /**
     * @Author 祝华笙
     * @param map
     * @return R<String>
     */
    @PostMapping("/logout")
    @ApiOperation("用户退出")

    public R<Integer> logout(@RequestBody Map map){
        String id = map.get("id").toString();
        LivePerson.outLivePerson(id);

        log.info("退出登录");
        return R.success(1);
    }
}
