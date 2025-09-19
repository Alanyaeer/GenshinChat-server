package com.homework.genshinchatclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.homework.genshinchatclient",
        "com.homework.genshinchatcore"
})
public class GenshinChatClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(GenshinChatClientApplication.class, args);
    }

}
