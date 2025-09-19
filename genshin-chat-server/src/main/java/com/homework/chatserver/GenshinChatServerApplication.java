package com.homework.chatserver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@Slf4j
@SpringBootApplication
@ComponentScan(basePackages = {
        "com.homework.chatserver",
        "com.homework.genshinchatcore"
})
public class GenshinChatServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(GenshinChatServerApplication.class, args);
    }
}
