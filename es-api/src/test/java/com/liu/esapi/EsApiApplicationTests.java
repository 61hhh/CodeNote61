package com.liu.esapi;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.liu.esapi.model.Customer;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class EsApiApplicationTests {

    @Autowired
    @Qualifier("restHighLevelClient")
    private RestHighLevelClient client;

    // 创建索引
    @Test
    void createIndexTest() throws IOException {
        // 1.创建索引的请求对象
        CreateIndexRequest request = new CreateIndexRequest("liulll");
        // 2.执行请求 获得响应
        CreateIndexResponse response = client.indices().create(request, RequestOptions.DEFAULT);
        System.out.println(response);
    }

    // 获取索引
    @Test
    void isExist() throws IOException {
        GetIndexRequest request = new GetIndexRequest("liulll");
        boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
        if (exists) {
            GetIndexResponse response = client.indices().get(request, RequestOptions.DEFAULT);
            System.out.println(response);
        } else {
            System.out.println(exists);
        }
    }

    // 删除索引
    @Test
    void deleteIndex() throws IOException {
        DeleteIndexRequest request = new DeleteIndexRequest("test3");
        AcknowledgedResponse response = client.indices().delete(request, RequestOptions.DEFAULT);
        System.out.println(response.isAcknowledged());
    }

    // 创建文档
    @Test
    void createDoc() throws IOException {
        Customer customer = new Customer("leslie",23);
        IndexRequest request = new IndexRequest("test1");

        //规则 put/test1/_doc/1
        request.id("3");
        request.timeout(TimeValue.timeValueSeconds(1));
        request.timeout("1s");

        // 将json数据放入es
        String s = JSONObject.toJSONString(customer);
        request.source(s, XContentType.JSON);

        // 客户端发送请求
        IndexResponse response = client.index(request, RequestOptions.DEFAULT);
        System.out.println(response.toString());
    }

}
