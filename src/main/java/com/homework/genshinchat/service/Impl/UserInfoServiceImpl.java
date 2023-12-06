package com.homework.genshinchat.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.homework.genshinchat.entity.UserInfo;
import com.homework.genshinchat.mapper.UserInfoMapper;
import com.homework.genshinchat.mapper.UserMapper;
import com.homework.genshinchat.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 吴嘉豪
 * @date 2023/10/22 12:02
 */
@Service
@Slf4j
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {
    @Autowired
    private UserInfoMapper userInfoMapper;
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
}
