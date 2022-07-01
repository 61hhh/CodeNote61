package com.liu.esjd.utils;

import com.liu.esjd.pojo.Content;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 *
 * @author LiuYi
 * @since 2022/6/29
 */
@Component
public class HtmlParseUtil {

    public List<Content> parseJD(String keyword) throws IOException {
        String remoteUrl = "https://search.jd.com/Search?keyword=" + keyword;
        Document document = Jsoup.connect(remoteUrl).get();
        assert document != null;
        Element element = document.getElementById("J_goodsList");
        Elements elements = element.getElementsByTag("li");

        List<Content> list = new ArrayList<>();

        for (Element el : elements) {
            // source-data-lazy-img
            String img = el.getElementsByTag("img").eq(0).attr("data-lazy-img");
            String price = el.getElementsByClass("p-price").eq(0).text();
            String title = el.getElementsByClass("p-name").eq(0).text();
            String shop = el.getElementsByClass("p-shopnum").eq(0).text();

            Content content = new Content(title, img, price, shop);
            list.add(content);
        }
        return list;
    }
}
