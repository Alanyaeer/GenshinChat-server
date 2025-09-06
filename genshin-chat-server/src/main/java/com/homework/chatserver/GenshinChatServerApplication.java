package com.homework.chatserver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class GenshinChatServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(GenshinChatServerApplication.class, args);
    }
}
