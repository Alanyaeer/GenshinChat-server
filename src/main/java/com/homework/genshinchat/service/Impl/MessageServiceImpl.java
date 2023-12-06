package com.homework.genshinchat.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.homework.genshinchat.dto.MessageDto;
import com.homework.genshinchat.entity.Message;
import com.homework.genshinchat.mapper.MessageMapper;
import com.homework.genshinchat.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 吴嘉豪
 * @date 2023/10/22 18:59
 */
@Service
@Slf4j
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService{
    @Autowired
    private MessageMapper messageMapper;
    @Override
    public List<Message> getlistById(MessageDto messageDto) {
        String myId = messageDto.getMyId();
        String friendId = messageDto.getFriendId();
        String msg = messageDto.getMsg();
        LambdaQueryWrapper<Message> Wrapper = new LambdaQueryWrapper<>();
        log.info("{}", friendId);
        log.info("{}", (myId != null || (!myId.isEmpty())));
        Wrapper.eq(myId.length() != 0 ,Message::getMyId, myId);

        Wrapper.eq(friendId.length() != 0,Message::getFriendId, friendId);
        if(msg != null)
        Wrapper.eq(!msg.isEmpty(),Message::getMsg, msg);
        List<Message> messageList = messageMapper.selectList(Wrapper);
        if((messageDto.getFriendId().equals(messageDto.getMyId()))){
            int cnt = 0;
            List<Message> newMessageList = new ArrayList<>();
            for(int i = 0 ;i < messageList.size(); ++i){
                if(i % 2 == 0){
                    newMessageList.add(messageList.get(i));
                }
            }
            return newMessageList;
        }
        return messageList;
    }
}
