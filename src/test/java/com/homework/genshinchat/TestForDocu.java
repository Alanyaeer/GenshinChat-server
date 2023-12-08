package com.homework.genshinchat;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.homework.genshinchat.entity.Message;
import com.homework.genshinchat.mapper.MessageMapper;
import com.homework.genshinchat.service.MessageService;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

/**
 * @author 吴嘉豪
 * @date 2023/12/7 21:07
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestForDocu {
    private RestHighLevelClient client;
    @Autowired
    private MessageService messageService;
    @Autowired
    private MessageMapper messageMapper;
    @Test
    void testAddDocument () throws IOException {
        Message message = messageService.getById(1732754839943503874L);
        IndexRequest request = new IndexRequest("message").id(message.getId().toString());
        System.out.println(JSON.toJSONString(message));
        request.source(JSON.toJSONString(message), XContentType.JSON);
        client.index(request, RequestOptions.DEFAULT);
    }
    @Test
    void testgetDocument() throws IOException {
        GetRequest request = new GetRequest("message", "1732754839943503874");
        GetResponse response = client.get(request, RequestOptions.DEFAULT);
        String json = response.getSourceAsString();
        Message message = JSON.parseObject(json, Message.class);
        System.out.println(json);

    }
    @Test
    void testUpdateDocument () throws IOException {
        UpdateRequest request = new UpdateRequest("message", "1732754839943503874");

        // 更新参数
        request.doc(
                "price", "952",
                "starName", "四钻"
        );
        client.update(request, RequestOptions.DEFAULT);
    }
    @Test
    void testDeleteDocument() throws IOException {
        DeleteRequest request = new DeleteRequest("message", "1732754839943503874");
        client.delete(request, RequestOptions.DEFAULT);
    }
    @Test
    void testBulkRequest() throws IOException {
        List<Message> list = messageService.list();
        BulkRequest request = new BulkRequest();
        for (Message message : list) {
            request.add(new IndexRequest("message").id(message.getId().toString()).source(JSON.toJSONString(message), XContentType.JSON));
        }
        client.bulk(request, RequestOptions.DEFAULT);
    }
    @Test
    void testBulkDelete() {
        LambdaQueryWrapper<Message> WrapperMessage1 = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<Message> WrapperMessage2 = new LambdaQueryWrapper<>();
        WrapperMessage1.eq(Message::getMyId, "test114513").eq(Message::getFriendId, "test114514");
        WrapperMessage2.eq(Message::getMyId, "test114514").eq(Message::getFriendId, "test114513");

        BulkRequest request = new BulkRequest();
        List<Message> messageList = messageMapper.selectList(WrapperMessage1);
        messageList.addAll(messageMapper.selectList(WrapperMessage2));
        System.out.println(messageList.toString());

        for (Message message : messageList) {
            System.out.println(message.getId().toString());
            request.add(new DeleteRequest().index("message").id(message.getId().toString()));
        }
        try {
            client.bulk(request,RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
