package com.liu.rabbit.chapter3;


import com.liu.rabbit.chapter1.RabbitMQUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

/**
 * @author LiuYi
 * @description Description about the file
 * @since 2022/6/21
 */
public class ReturnConsumer {

    private static final String EXCHANGE_NAME = "return_exchange";
    private static final String VIP_KEY = "vip";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        String queueName = "queue_vip";
        channel.queueDeclare(queueName, false, false, false, null);
        channel.queueBind(queueName, EXCHANGE_NAME, VIP_KEY);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            System.out.println("VIP接收到的消息：" + new String(delivery.getBody(), StandardCharsets.UTF_8));
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };
        CancelCallback cancelCallback = consumer -> {
            System.out.println("取消消费！");
        };
        channel.basicConsume(queueName, false, deliverCallback, cancelCallback);
    }
}
