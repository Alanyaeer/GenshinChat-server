package com.homework.genshinchat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.homework.genshinchat.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 吴嘉豪
 * @date 2023/10/21 19:35
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
