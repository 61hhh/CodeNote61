package com.liu.boot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * Description:
 *
 * @author LiuYi
 * @since 2022/7/15
 */
@RestController
@Slf4j
@RequestMapping("/confirm")
public class ProducerController {

    public static final String CONFIRM_EXCHANGE = "confirm.exchange";
    @Resource
    private RabbitTemplate rabbitTemplate;
    @Resource
    private MyCallBack myCallBack;

    // 依赖注入rabbitTemplate后设置回调对象
    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(myCallBack);
    }

    @GetMapping("/sendMsg/{msg}")
    public void sendMsg(@PathVariable String msg) {
        CorrelationData correlationData1 = new CorrelationData("1");
        String routingKey = "confirmKey1";
        rabbitTemplate.convertAndSend(CONFIRM_EXCHANGE, routingKey, msg + routingKey, correlationData1);
        log.info(routingKey + "--发送消息内容：{}", msg + routingKey);

        CorrelationData correlationData2 = new CorrelationData("2");
        routingKey = "confirmKey2";
        rabbitTemplate.convertAndSend(CONFIRM_EXCHANGE, routingKey, msg + routingKey, correlationData2);
        log.info(routingKey + "--发送消息内容：{}", msg + routingKey);
    }
}
