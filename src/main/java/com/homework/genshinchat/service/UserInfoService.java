package com.homework.genshinchat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.homework.genshinchat.entity.UserInfo;

import java.util.List;

/**
 * @author 吴嘉豪
 * @date 2023/10/22 12:01
 */
public interface UserInfoService extends IService<UserInfo> {
    public UserInfo getUserInfo(String id);

    List<UserInfo> findbyIds(List<String> ids);

    boolean saveUserInfo(UserInfo userInfo);

    UserInfo findid(String id);
}
