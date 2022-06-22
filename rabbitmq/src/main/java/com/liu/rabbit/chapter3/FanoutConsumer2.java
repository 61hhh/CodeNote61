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
public class FanoutConsumer2 {

    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

        /*
         * 随机生成一个临时队列
         * 当消费者与该队列断开连接时，队列自动删除
         */
        String queueName = channel.queueDeclare().getQueue();
        // 把该临时队列绑定到exchange，其中routingkey(也称之为 binding key)为空字符串
        channel.queueBind(queueName, EXCHANGE_NAME, "");
        System.out.println("等待接收消息。。。。。。。。。。输出控制台");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String msg = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("接收到的消息：" + msg);
        };
        channel.basicConsume(queueName, true, deliverCallback, consumer -> {
        });
    }
}
