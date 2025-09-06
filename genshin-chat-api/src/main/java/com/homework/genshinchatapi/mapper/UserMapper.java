package com.homework.genshinchatapi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.homework.common.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 吴嘉豪
 * @date 2023/10/21 19:35
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
