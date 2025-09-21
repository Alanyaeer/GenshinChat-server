package com.homework.genshinchatapi;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@Slf4j
@SpringBootApplication
@MapperScan("com.homework.genshinchatapi.mapper")
@ComponentScan(basePackages = {
        "com.homework.genshinchatapi",
        "com.homework.genshinchatcore"
})
public class GenshinChatApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(GenshinChatApiApplication.class, args);
    }

}
