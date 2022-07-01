package com.liu.esapi;

import com.alibaba.fastjson.JSONObject;
import com.liu.esapi.model.Customer;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.FuzzyQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;

@SpringBootTest
class EsApiApplicationTests {

    @Autowired
    @Qualifier("restHighLevelClient")
    private RestHighLevelClient client;

    // 创建索引
    @Test
    void createIndexTest() throws IOException {
        // 1.创建索引的请求对象
        CreateIndexRequest request = new CreateIndexRequest("leslie_test");
        // 2.执行请求 获得响应
        CreateIndexResponse response = client.indices().create(request, RequestOptions.DEFAULT);
        System.out.println(response);
    }

    // 获取索引
    @Test
    void isExistTest() throws IOException {
        // 判断索引是否存在
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
    void deleteIndexTest() throws IOException {
        DeleteIndexRequest request = new DeleteIndexRequest("test3");
        AcknowledgedResponse response = client.indices().delete(request, RequestOptions.DEFAULT);
        System.out.println(response.isAcknowledged());
    }

    // 创建文档
    @Test
    void createDocTest() throws IOException {
        // 创建对象与请求
        Customer customer = new Customer("leslie-update", 23);
        IndexRequest request = new IndexRequest("leslie_test");

        //规则 put/test1/_doc/1
        request.id("5");
        request.timeout("1s");

        // 将json数据放入es
        request.source(JSONObject.toJSONString(customer), XContentType.JSON);

        // 客户端发送请求
        IndexResponse response = client.index(request, RequestOptions.DEFAULT);
        System.out.println(response.toString());
        // 命令返回状态 创建为CREATED、更新为OK
        System.out.println(response.status());
    }

    // 判断文档是否存在
    @Test
    void docExistTest() throws IOException {
        GetRequest request = new GetRequest("leslie_test", "3");
        // 不获取返回的 _source 上下文
        request.fetchSourceContext(new FetchSourceContext(false));

        boolean exists = client.exists(request, RequestOptions.DEFAULT);
        System.out.println(exists);
    }

    // 获取文档信息
    @Test
    void docGetTest() throws IOException {
        GetRequest request = new GetRequest("leslie_test", "3");

        GetResponse response = client.get(request, RequestOptions.DEFAULT);
        System.out.println(response.getSourceAsString()); // 打印文档内容
        System.out.println(response); // 返回的全部内容
    }

    // 更新文档信息
    @Test
    void docUpdateTest() throws IOException {
        UpdateRequest request = new UpdateRequest("leslie_test", "3");
        request.timeout(TimeValue.timeValueSeconds(1));

        Customer customer = new Customer("jay_up", 18);
        request.doc(JSONObject.toJSONString(customer), XContentType.JSON);
        UpdateResponse response = client.update(request, RequestOptions.DEFAULT);
        System.out.println(response.status());
    }

    // 更新文档信息
    @Test
    void docDeleteTest() throws IOException {
        DeleteRequest request = new DeleteRequest("leslie_test", "3");
        request.timeout(TimeValue.timeValueSeconds(1));

        DeleteResponse response = client.delete(request, RequestOptions.DEFAULT);
        System.out.println(response.status());
    }

    // 批量处理
    @Test
    void docBulkTest() throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout("1s");

        ArrayList<Customer> list = new ArrayList<>();
        list.add(new Customer("leslie_jay1", 18));
        list.add(new Customer("leslie_jay2", 18));
        list.add(new Customer("leslie_jay3", 18));
        list.add(new Customer("leslie_jay4", 18));
        list.add(new Customer("leslie_jay5", 18));

        for (int i = 0; i < list.size(); i++) {
            // 批量操作 参照上面的CURD修改对应请求即可
            bulkRequest.add(new IndexRequest("leslie_test").
                    id("pk_" + (i + 1)).
                    source(JSONObject.toJSONString(list.get(i)), XContentType.JSON));
        }

        BulkResponse bulk = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        System.out.println(bulk.hasFailures());
    }

    /**
     * 查询
     * 更多的查询方法通过QueryBuilder构建
     */
    @Test
    void docQueryTest() throws IOException {
        SearchRequest searchRequest = new SearchRequest("leslie_test");
        // 构建搜索条件
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        /*
         * sourceBuilder.query() 参数是查询条件QueryBuilder
         * 可以QueryBuilders.xxx快捷生成-- QueryBuilders.termQuery("name", "leslie1")
         * 也可以new QueryBuilder接口下的实现类-- new TermQueryBuilder("name","leslie1");
         */
        QueryBuilders.termQuery("name", "leslie");

        sourceBuilder.size(8).
                query(QueryBuilders.termQuery("name", "leslie")).
                timeout(TimeValue.timeValueSeconds(1));
        searchRequest.source(sourceBuilder);
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println(JSONObject.toJSONString(response.getHits()));
        System.out.println("------------------------");
        for (SearchHit hit : response.getHits().getHits()) {
            System.out.println(hit.getSourceAsMap());
        }
    }


}