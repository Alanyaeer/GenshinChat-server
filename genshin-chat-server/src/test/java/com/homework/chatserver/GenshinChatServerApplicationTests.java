package com.homework.chatserver;

import com.homework.common.entity.User;
import com.homework.genshinchatapi.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Slf4j
class GenshinChatServerApplicationTests {
    @Mock
    private UserService userService;
    @Test
    void contextLoads() {
        List<User> userArrayList = new ArrayList<User>();
        User user = new User();
        user.setId("32423");
        userArrayList.add(user);
        Mockito.when(userService.findAllPerson()).thenReturn(userArrayList);
        List<User> allPerson = userService.findAllPerson();
        log.info("{}" ,allPerson);
    }

}
