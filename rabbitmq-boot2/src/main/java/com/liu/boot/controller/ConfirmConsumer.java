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
@Component
@Slf4j
public class ConfirmConsumer {
    public static final String CONFIRM_QUEUE = "confirm.queue";

    @RabbitListener(queues = CONFIRM_QUEUE)
    public void receiveMsg(Message message) {
        String msg = new String(message.getBody());
        log.info("收到队列confirm.queue的消息：{}", msg);
    }
}
