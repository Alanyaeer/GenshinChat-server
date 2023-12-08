package com.homework.genshinchat;

import com.alibaba.fastjson.JSON;
import com.homework.genshinchat.entity.Message;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

/**
 * @author 吴嘉豪
 * @date 2023/12/7 22:00
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestForSearch {
    private RestHighLevelClient client;
    @Test
    void init () {
        System.out.println(client);
    }
    @Test
    void searchTest() throws IOException {
        SearchRequest request = new SearchRequest("message");
        request.source().query(QueryBuilders.matchQuery("msg", "吃"));
        SearchResponse search = client.search(request, RequestOptions.DEFAULT);
        handleResponse(search);
    }

    private static void  handleResponse(SearchResponse response) {
        SearchHits searchHits = response.getHits();
        Long total = searchHits.getTotalHits().value;
        System.out.println("共搜索到" + total +"条数据");
        SearchHit[] hits = searchHits.getHits();
        for(SearchHit hit: hits){
            String json = hit.getSourceAsString();
            Message message = JSON.parseObject(json, Message.class);
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            // 这是lambda 优化代码
            Optional.ofNullable(highlightFields)
                    .map(e -> e.get("name"))
                    .map(e -> e.getFragments()[0].toString())
                    .ifPresent(message::setMsg);
            System.out.println("message = " + message);
        }
    }

}
