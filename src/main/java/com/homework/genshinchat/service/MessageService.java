package com.homework.genshinchat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.homework.genshinchat.dto.MessageDto;
import com.homework.genshinchat.entity.Message;

import java.util.List;

/**
 * @author 吴嘉豪
 * @date 2023/10/22 18:58
 */
public interface MessageService extends IService<Message> {
    List<Message> getlistById(MessageDto messageDto);
}
