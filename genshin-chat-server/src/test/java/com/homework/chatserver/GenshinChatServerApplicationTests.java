package com.homework.chatserver;

import com.homework.common.entity.User;
import com.homework.genshinchatapi.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Slf4j
class GenshinChatServerApplicationTests {
    @Resource
    private UserService userService;
    @Test
    void contextLoads() {
        List<User> allPerson = userService.findAllPerson();
        UserService mock = Mockito.mock(userService);
        List<User> mockAllPerson = mock.findAllPerson();
        log.info("{}: {}" ,allPerson, mockAllPerson);
    }

}
