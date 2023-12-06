package com.homework.genshinchat.service.Impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.homework.genshinchat.entity.Friend;
import com.homework.genshinchat.entity.Message;
import com.homework.genshinchat.entity.User;
import com.homework.genshinchat.mapper.FriendMapper;
import com.homework.genshinchat.mapper.MessageMapper;
import com.homework.genshinchat.service.FriendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 吴嘉豪
 * @date 2023/10/22 18:59
 */
@Service
@Slf4j
public class FriendServiceImpl extends ServiceImpl<FriendMapper, Friend> implements FriendService {
    @Autowired
    private FriendMapper friendMapper;
    @Autowired
    private MessageMapper messageMapper;
    @Override
    public Friend findById(String id) {
        Friend friend = friendMapper.selectById(id);
        if(friend != null) return friend;
        else   return null;

    }

    @Override
    public List<Friend> findAllFriend(String id) {
        LambdaQueryWrapper<Friend> Wrapper = new LambdaQueryWrapper<>();
        Wrapper.eq(Friend::getId, id);
        Wrapper.orderByDesc(Friend::getUpdateTime);
        List<Friend> friendList = friendMapper.selectList(Wrapper);
        return friendList;
    }

    @Override
    public void deleteById(Friend friend) {
        LambdaQueryWrapper<Friend> Wrapper = new LambdaQueryWrapper<>();
        String id = friend.getId();
        String friendId = friend.getFriendId();
        Wrapper.eq(Friend::getId, friend.getId());
        Wrapper.eq(Friend::getFriendId, friend.getFriendId());
        LambdaQueryWrapper<Message> WrapperMessage1 = new LambdaQueryWrapper<>();
        WrapperMessage1.eq(Message::getMyId, id);
        WrapperMessage1.eq(Message::getFriendId, friendId);
        LambdaQueryWrapper<Message> WrapperMessage2 = new LambdaQueryWrapper<>();
        WrapperMessage2.eq(Message::getMyId, friendId);
        WrapperMessage2.eq(Message::getFriendId, id);
        messageMapper.delete(WrapperMessage1);
        messageMapper.delete(WrapperMessage2);
        friendMapper.delete(Wrapper);
    }

    @Override
    public void update(Friend friend) {
        friendMapper.updateById(friend);

    }

    @Override
    public boolean issue(Friend friend) {
        LambdaQueryWrapper<Friend> Wrapper = new LambdaQueryWrapper<>();
        Wrapper.eq(Friend::getId, friend.getId());
        Wrapper.eq(Friend::getFriendId, friend.getFriendId());
        Friend issueFriend = friendMapper.selectOne(Wrapper);
        log.info("{}", issueFriend);
        if(issueFriend == null) return true;
        return false;
    }

}
