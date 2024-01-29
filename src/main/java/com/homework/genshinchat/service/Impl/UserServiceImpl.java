package com.homework.genshinchat.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.homework.genshinchat.dto.FriendDto;
import com.homework.genshinchat.entity.User;
import com.homework.genshinchat.entity.UserInfo;
import com.homework.genshinchat.mapper.UserMapper;
import com.homework.genshinchat.service.UserInfoService;
import com.homework.genshinchat.service.UserService;
import com.homework.genshinchat.utils.LivePerson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;

/**
 * @author 吴嘉豪
 * @date 2023/10/21 19:36
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserInfoService userInfoService;

    @Override
    public boolean checkpassword(String id, String password) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getId , id);
        String encryptedPasswrd = DigestUtils.md5DigestAsHex(password.getBytes());
        User isuserone = userMapper.selectOne(wrapper);
        //说明数据库中没有这个数据
        if(isuserone == null){
            return false;
        }
        log.info("{} == {}",isuserone.getPassword(), encryptedPasswrd);
        if(encryptedPasswrd.equals(isuserone.getPassword())){
            return true;

        }
        else return false;
    }

    @Override
    public void changestatus(String id) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getId , id);
        User user = userMapper.selectOne(wrapper);
        user.setStatus(true);
        userMapper.updateById(user);

    }

    @Override
    public List<User> findAllPerson() {
        LambdaQueryWrapper<User> Wrapper = new LambdaQueryWrapper<>();
        //查找所有人的？ 但是它的创造时间
        Wrapper.orderByDesc(User::getCreateTime);

        return userMapper.selectList(Wrapper);
    }
    @Override
    public boolean findPerson(User user) {
        LambdaQueryWrapper<User> Wrapper = new LambdaQueryWrapper<>();
        //这里就是类似于select * from user where xx == ‘xxx’
        Wrapper.eq(User::getId, user.getId());
        User isuserone = userMapper.selectOne(Wrapper);
        String password = user.getPassword();
        // md5加密
        String encryptedPasswrd = DigestUtils.md5DigestAsHex(password.getBytes());
        user.setPassword(encryptedPasswrd);

        if(isuserone == null){
            user.setStatus(false);
            // 也就是说没有这个用户， 直接插入进来
            userMapper.insert(user);
            return true;
        }
        return false;
    }

    @Override
    public FriendDto findPersonid(String id) {
        LambdaQueryWrapper<User> Wrapper = new LambdaQueryWrapper<>();
        //这里就是类似于select * from user where xx == ‘xxx’
        Wrapper.eq(User::getId, id);
        User isuserone = userMapper.selectOne(Wrapper);
        if(isuserone == null) return null;
        UserInfo userInfo = userInfoService.getUserInfo(id);
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
    }
}
