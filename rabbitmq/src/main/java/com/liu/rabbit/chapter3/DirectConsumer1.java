package com.liu.rabbit.chapter3;

import com.liu.rabbit.chapter1.RabbitMQUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

/**
 * @author LiuYi
 * @description Description about the file
 * @since 2022/6/21
 */
public class DirectConsumer1 {

    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        String queueName = "disk";
        // 队列声明
        channel.queueDeclare(queueName, false, false, false, null);
        // 队列绑定
        channel.queueBind(queueName, EXCHANGE_NAME, "error");
        System.out.println("error等待接收消息。。。");

        // 回调函数
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String msg = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("接收绑定键：" + delivery.getEnvelope().getRoutingKey() + ",消息：" + msg);
        };
        channel.basicConsume(queueName, true, deliverCallback, consumer -> {
        });
    }
}
