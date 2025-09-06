package com.homework.genshinchatapi;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootApplication
@MapperScan("com.homework.genshinchatapi.mapper")
public class GenshinChatApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(GenshinChatApiApplication.class, args);
    }
    @Bean
    public RestHighLevelClient client() {
        return new RestHighLevelClient(RestClient.builder(
                HttpHost.create("http://127.0.0.1:9200")
        ));
    }
}
