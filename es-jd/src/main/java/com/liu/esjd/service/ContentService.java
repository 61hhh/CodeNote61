package com.liu.esjd.service;

import com.alibaba.fastjson.JSONObject;
import com.liu.esjd.pojo.Content;
import com.liu.esjd.utils.HtmlParseUtil;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Description: 模拟基本业务操作
 *
 * @author LiuYi
 * @since 2022/6/29
 */
@Service
public class ContentService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    //1.解析数据放入es中
    public void parseContent(String keyword) throws IOException {
        List<Content> contents = new HtmlParseUtil().parseJD(keyword);
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout("1m");
        for (int i = 0; i < contents.size(); i++) {
            bulkRequest.add(new IndexRequest("jd_goods").
                    id("gd_" + (i + 1)).
                    source(JSONObject.toJSONString(contents.get(i)), XContentType.JSON));
        }
        BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        System.out.println(!bulk.hasFailures());
    }

    // 从es获取数据 实现基本搜索功能
    public List<Map<String, Object>> searchPage(String keyword, int pageNo, int pageSize) throws IOException {
        parseContent(keyword);
        if (pageNo < 1) {
            pageNo = 1;
        }
        // 条件搜索
        SearchRequest request = new SearchRequest("jd_goods");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("title");
        highlightBuilder.preTags("<span style='color:red'>");
        highlightBuilder.postTags("</span>");
        highlightBuilder.requireFieldMatch(false);
        sourceBuilder.from(pageNo).size(pageSize).
                timeout(TimeValue.timeValueSeconds(10))
                .query(QueryBuilders.termQuery("title", keyword)).
                highlighter(highlightBuilder);
        request.source(sourceBuilder);
        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        // 解析结果
        List<Map<String, Object>> list = new ArrayList<>();
        for (SearchHit hit : response.getHits().getHits()) {
            HighlightField title = hit.getHighlightFields().get("title");
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            // 将原来的字段替换为高亮字段
            if (title != null) {
                Text[] fragments = title.fragments();
                StringBuilder new_title = new StringBuilder();
                for (Text fragment : fragments) {
                    new_title.append(fragment);
                }
                sourceAsMap.put("title", new_title.toString());
            }
            list.add(sourceAsMap);
        }
        System.out.println(list.size());
        return list;
    }
}
