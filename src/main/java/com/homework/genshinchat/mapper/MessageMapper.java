package com.homework.genshinchat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.homework.genshinchat.entity.Message;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 吴嘉豪
 * @date 2023/10/22 18:57
 */
@Mapper
public interface MessageMapper extends BaseMapper<Message> {
}
