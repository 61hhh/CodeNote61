package com.liu.esjd.controller;

import com.liu.esjd.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @author LiuYi
 * @since 2022/6/29
 */
@RestController
@RequestMapping("/content")
public class ContentController {

    @Autowired
    private ContentService contentService;

    @GetMapping("/parse/{keyword}")
    public void parseContent(@PathVariable("keyword") String keyword) throws IOException {
        contentService.parseContent(keyword);
    }

    @GetMapping("/search/{keyword}/{pageNo}/{pageSize}")
    public List<Map<String, Object>> searchKeyword(@PathVariable("keyword") String keyword,
                                                   @PathVariable("pageNo") int pageNo,
                                                   @PathVariable("pageSize") int pageSize) throws IOException {
        return contentService.searchPage(keyword, pageNo, pageSize);
    }

}
