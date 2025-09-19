package com.homework.genshinchatapi.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.homework.common.entity.User;
import com.homework.common.entity.UserInfo;
import com.homework.common.entity.constants.RedisConstants;
import com.homework.genshinchatapi.mapper.UserInfoMapper;
import com.homework.genshinchatapi.mapper.UserMapper;
import com.homework.genshinchatapi.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * @author 吴嘉豪
 * @date 2023/10/22 12:02
 */
@Service
@Slf4j
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {
    public static final String USER_PREFIX_NAME = "用户-";
    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private UserMapper userMapper;

    @Override
    public UserInfo getUserInfo(String id) {
        UserInfo userInfo = userInfoMapper.selectById(id);
        return userInfo;
    }

    @Override
    public List<UserInfo> findbyIds(List<String> ids) {
//        return userInfoMapper.select
        if(ids == null ||ids.size() == 0)return null;
        return userInfoMapper.selectBatchIds(ids);
    }

    @Override
    public boolean saveUserInfo(UserInfo userInfo) {
        String id = userInfo.getUserid();
        UserInfo userInfo_issue = userInfoMapper.selectById(id);

        // 如果没有存储过， 那么保存
        if(userInfo_issue == null){
            return false;
        }
        userInfoMapper.updateById(userInfo);
        // 否则更新
        return true;
    }

    @Override
    public UserInfo findid(String id) {
        return userInfoMapper.selectById(id);
    }

    @Override
    public String quickLogin() {
        // 用户注册完成
        Long increment = redisTemplate.opsForValue().increment(RedisConstants.USER_COUNT);
        String userNickName = USER_PREFIX_NAME + increment;
        UUID uuid = UUID.randomUUID();
        String randomPassword = uuid.toString().replace("-", "");
        User user = new User();
        user.setPassword(randomPassword);
        user.setName(userNickName);
        user.setId(randomPassword);
        userMapper.insert(user);
        
        // 用户信息注册完成
        UserInfo userInfo = new UserInfo();
        userInfo.setUserid(user.getId());
        userInfo.setUsername(userNickName);
        userInfo.setUserdetail(userNickName);
        String img = getRandomImg();
        userInfo.setUserimg(img);
        userInfo.setUserimmg(img);
        userInfoMapper.insert(userInfo);
    
        
        // 用户加入聊天室, 需要直接加入全员群聊
        
        
        return "";
    }

    @Override
    public Integer getTotalPeople() {
        return Integer.parseInt((String) redisTemplate.opsForValue().get(RedisConstants.USER_COUNT));
    }

    private String getRandomImg(){
       String[] imgUrlList = {
               "https://res-aigc.coocaa.com/learning/image/20250919/a84e6a6e6b0e449581d462f01b85e486.jpg",
               "https://res-aigc.coocaa.com/learning/image/20250919/2fa7debbcb164af8bd1a1d7976fb9377.jpg",
               "https://res-aigc.coocaa.com/learning/image/20250919/c13905b4692c40228b527de965ce170e.jpg",
               "https://res-aigc.coocaa.com/learning/image/20250919/74f6cc077db8453991a2c18f31aa9f57.jpg",
               "https://res-aigc.coocaa.com/learning/image/20250919/f6f1612db4084456a08a632c8f22bb53.jpg",
               "https://res-aigc.coocaa.com/learning/image/20250919/d3d520d37e7f4c95afd80cc337ace061.jpg",
               "https://res-aigc.coocaa.com/learning/image/20250919/7524ed806dad42d688d6e92fb2011936.jpg",
               "https://res-aigc.coocaa.com/learning/image/20250919/676805edf78d478fac00ce8c28c9ab64.jpg",
               "https://res-aigc.coocaa.com/learning/image/20250919/1711e6bb763c48e2b8cf6659dc5d6f29.jpg",
               "https://res-aigc.coocaa.com/learning/image/20250919/747cc7951d9044ac8d9fb6fd098d10e9.jpg",
               "https://res-aigc.coocaa.com/learning/image/20250919/a93f71bb64df40b1b754d35860acfc49.jpg",
               "https://res-aigc.coocaa.com/learning/image/20250919/b9fd7f8a8d3b45d68ebfcdeaf0910176.jpg",
               "https://res-aigc.coocaa.com/learning/image/20250919/a19e45b7a252434fbfb7573d844bfd2c.jpg",
               "https://res-aigc.coocaa.com/learning/image/20250919/b043049d43914d15bae7c58d87f1f7bf.jpg",
               "https://res-aigc.coocaa.com/learning/image/20250919/9159fab24a4249a7a9060745f8355b49.jpg",
               "https://res-aigc.coocaa.com/learning/image/20250919/06a18f43ace841f38ccf8ab9f91192b9.jpg",
               "https://res-aigc.coocaa.com/learning/image/20250919/ea2ebca66d7940588443e68f6eb48dc2.jpg",
               "https://res-aigc.coocaa.com/learning/image/20250919/6cc64b88fad1401089e6265db53839a9.jpg",
               "https://res-aigc.coocaa.com/learning/image/20250919/0047a98788674d879393fb94586a8515.jpg",
               "https://res-aigc.coocaa.com/learning/image/20250919/438ce1ffca3242c8b125c3a591b3a582.jpg",
               "https://res-aigc.coocaa.com/learning/image/20250919/b2c2d447dd9e4f10af67b94623ae4026.jpg",
               "https://res-aigc.coocaa.com/learning/image/20250919/37a8c5f3d76b420cbe102c2da1f3946e.jpg",
               "https://res-aigc.coocaa.com/learning/image/20250919/1069d8e9e8734c3b80a07b70d57973ae.jpg",
               "https://res-aigc.coocaa.com/learning/image/20250919/011218ffb3ec4de08e533d9101b3117c.jpg",
               "https://res-aigc.coocaa.com/learning/image/20250919/64768922914f436ca72ac7cc828cc62b.jpg",
               "https://res-aigc.coocaa.com/learning/image/20250919/4ffa519e23db4465967e15cd66e3ae76.jpg",
               "https://res-aigc.coocaa.com/learning/image/20250919/b862e3fcf3b142288b4d5f2910dd0ff6.jpg",
               "https://res-aigc.coocaa.com/learning/image/20250919/99e72c2f5000471d97ab45d3bd87e100.jpg",
               "https://res-aigc.coocaa.com/learning/image/20250919/91455281e9d946e6ae06b949bcd1b650.jpg",
               "https://res-aigc.coocaa.com/learning/image/20250919/fd5c01f580f74c3e8dd1999901dd6c7f.jpg",
               "https://res-aigc.coocaa.com/learning/image/20250919/a07f183b3fc246ee85bed3d299ffea9a.jpg",
               "https://res-aigc.coocaa.com/learning/image/20250919/246cbbda05cc4c90bb9c822d9d08830f.jpg",
               "https://res-aigc.coocaa.com/learning/image/20250919/92c104a77dd341529661a6292e82ab4f.jpg",
               "https://res-aigc.coocaa.com/learning/image/20250919/915196f20abf4bbbbc3997b6e58d71e0.jpg",
               "https://res-aigc.coocaa.com/learning/image/20250919/89ee6462efb94de2be4617ac77162ed4.jpg",
               "https://res-aigc.coocaa.com/learning/image/20250919/85c64560fb534bdfa6ee8dd451296750.jpg",
               "https://res-aigc.coocaa.com/learning/image/20250919/b4c0efac53484514b258afa5d859c1a6.jpg",
               "https://res-aigc.coocaa.com/learning/image/20250919/6b9585b648e543a3b99a64bb82815cea.jpg",
               "https://res-aigc.coocaa.com/learning/image/20250919/9430df77a37f41caa358f0b2135da3ae.jpg"
       };
        int imgSize = imgUrlList.length;
        Random random = new Random();
        int index = random.nextInt(0, imgSize);
        return imgUrlList[index];
    }

}
