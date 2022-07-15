package com.liu.boot.controller;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author LiuYi
 * @description Description about the file
 * @since 2022/6/23
 */
@Slf4j
@Component
public class DLQueueConsumer {

    @RabbitListener(queues = "QD")
    public void receiveD(Message message, Channel channel) {
        String msg = new String(message.getBody());
        log.info("当前时间----->{},从死信队列获取到的消息----->{}", new Date(), msg);
    }

    // 延时插件消费
    @RabbitListener(queues = "delayed.queue")
    public void receiveDelayQueue(Message msg, Channel channel) {
        String message = new String(msg.getBody());
        log.info("当前时间：{}，收到延时消息：{}", new Date(), message);
    }
}
