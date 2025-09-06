package com.homework.genshinchatapi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.homework.common.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 吴嘉豪
 * @date 2023/10/22 12:00
 */
@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {
}
