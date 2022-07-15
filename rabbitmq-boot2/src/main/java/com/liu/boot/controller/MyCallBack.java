package com.liu.boot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * Description:
 *
 * @author LiuYi
 * @since 2022/7/15
 */
@Component
@Slf4j
public class MyCallBack implements RabbitTemplate.ConfirmCallback {

    /**
     * 交换机不管是否收到消息，都执行MyBack回调
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData != null ? correlationData.getId() : "";
        if (ack) {
            log.info("交换器收到id为：{}的消息", id);
        } else {
            log.info("交换器未收到id为：{}的消息,原因是：{}", id, cause);
        }
    }
}
