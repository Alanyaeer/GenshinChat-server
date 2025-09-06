package com.homework.genshinchatapi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.homework.common.entity.Message;
import com.homework.common.entity.dto.MessageDto;

import java.util.List;

/**
 * @author 吴嘉豪
 * @date 2023/10/22 18:58
 */
public interface MessageService extends IService<Message> {
    List<Message> getlistById(MessageDto messageDto);
}
