package com.liu.boot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:
 *
 * @author LiuYi
 * @since 2022/6/30
 */
@RestController
@RequestMapping("/rabbit")
public class RabbitController {

    @Autowired
    private SimpleSender simpleSender;
    @Autowired
    private WorkSender workSender;

    @GetMapping(value = "/simple")
    @ResponseBody
    public void simpleTest() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            simpleSender.send();
            Thread.sleep(1000);
        }
    }

    @GetMapping(value = "/work")
    public void workTest() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            workSender.send(i);
            Thread.sleep(1000);
        }
    }
}
