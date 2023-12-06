package com.homework.genshinchat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.homework.genshinchat.entity.Friend;
import com.homework.genshinchat.entity.User;

import java.util.List;

/**
 * @author 吴嘉豪
 * @date 2023/10/22 18:58
 */
public interface FriendService extends IService<Friend> {
    Friend findById(String id);

    List<Friend> findAllFriend(String id);
    void deleteById(Friend friend);

    void update(Friend friend);

    boolean issue(Friend friend);
}
