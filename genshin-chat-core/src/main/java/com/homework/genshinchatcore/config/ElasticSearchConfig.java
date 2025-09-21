package com.homework.genshinchatcore.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 嘉豪舞团-吴嘉豪
 * @date 2025/9/16
 */
@Configuration
public class ElasticSearchConfig {
    @Value("${es.host}")
    private String host;
    @Bean
    public RestHighLevelClient client() {
        String uri = "https://" + host + ":9200";
        return new RestHighLevelClient(RestClient.builder(
                HttpHost.create(uri)
        ));
    }
}
