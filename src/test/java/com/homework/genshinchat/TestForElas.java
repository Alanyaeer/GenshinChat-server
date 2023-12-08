package com.homework.genshinchat;

import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.homework.genshinchat.constants.chatConstants.MAPPING_TEMPLATE;

/**
 * @author 吴嘉豪
 * @date 2023/12/7 20:50
 */
public class TestForElas {

    private RestHighLevelClient client;
    @Test
    void init () {
        System.out.println(client);
    }
    @Test
    void createMessageIndex() throws IOException {
        CreateIndexRequest request = new CreateIndexRequest("message");
        request.source(MAPPING_TEMPLATE, XContentType.JSON);
        client.indices().create(request, RequestOptions.DEFAULT);
    }
    @Test
    void testDeleteHotelIndex () throws IOException {
        // 1. 创建Request 对象
        DeleteIndexRequest request = new DeleteIndexRequest("message");

        // 3. 发送请求

        client.indices().delete(request, RequestOptions.DEFAULT);
    }
    @Test
    void testExistHotelIndex () throws IOException {
        GetIndexRequest request = new GetIndexRequest("message");
        boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
        // 判断是否存在
        System.out.println(exists ? "存在": "不存在");

    }
}
