package com.liu.boot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * @author LiuYi
 * @description Description about the file
 * @since 2022/6/23
 */
@Slf4j
@RequestMapping("ttl")
@RestController
public class SendMsgController {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @GetMapping("sendMsg/{msg}")
    public void sendMsg(@PathVariable String msg) {
        log.info("当前时间----->{},发送消息给两个TTL队列----->{}", new Date(), msg);
        rabbitTemplate.convertAndSend("ex_x", "RK_XA", "消息存放于ttl=10s队列QA中----->" + msg);
        rabbitTemplate.convertAndSend("ex_x", "RK_XB", "消息存放于ttl=10s队列QB中----->" + msg);
    }

    @GetMapping("sendExpirationMsg/{msg}/{ttlTime}")
    public void sendMsgWithTTL(@PathVariable String msg, @PathVariable String ttlTime) {
        rabbitTemplate.convertAndSend("ex_x", "RK_XC", msg, correlationData -> {
            correlationData.getMessageProperties().setExpiration(ttlTime);
            return correlationData;
        });
        log.info("当前时间----->{},发送一个TTL为{}的消息给队列C----->{}", new Date(), ttlTime, msg);
    }

    // 延时插件
    @RequestMapping("/plugins/sendMsg/{msg}/{time}")
    public void sendMsgByPlugin(@PathVariable String msg, @PathVariable Integer time) {
        MessageProperties properties = new MessageProperties();
        properties.setDelay(time);
        Message message = new Message(msg.getBytes(StandardCharsets.UTF_8), properties);
        log.info("当前时间：{},发送过期时间为{}毫秒的消息到延时插件，内容为：{}", new Date(), time, msg);
        rabbitTemplate.convertAndSend("delayed.exchange", "delayed.routingKey", message);
    }
}
