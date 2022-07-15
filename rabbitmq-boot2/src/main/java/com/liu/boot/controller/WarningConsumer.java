package com.liu.boot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Description:
 *
 * @author LiuYi
 * @since 2022/7/15
 */
@Slf4j
@Component
public class WarningConsumer {

    public static final String WARNING_QUEUE = "warning.queue";

    @RabbitListener(queues = WARNING_QUEUE)
    public void receiveWarningMsg(Message message) {
        String msg = new String(message.getBody());
        log.info("不可路由消息告警：{}", msg);
    }
}
