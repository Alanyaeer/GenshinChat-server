package com.homework.genshinchat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.homework.genshinchat.dto.FriendDto;
import com.homework.genshinchat.entity.User;

import java.util.List;

/**
 * @author 吴嘉豪
 * @date 2023/10/21 19:34
 */
public interface UserService extends IService<User> {
    public boolean findPerson(User user);

    public boolean checkpassword(String id, String password);

    void changestatus(String id);

    List<User> findAllPerson();

    FriendDto findPersonid(String id);
}
