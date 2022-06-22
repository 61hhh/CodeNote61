package com.liu.rabbit.chapter4;


import com.liu.rabbit.chapter1.RabbitMQUtils;
import com.rabbitmq.client.Channel;

/**
 * @author LiuYi
 * @description Description about the file
 * @since 2022/6/22
 */
public class TestMyConsumer {

    public static final String QUEUE_NAME = "MyQueue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        channel.basicConsume(QUEUE_NAME,new MyConsumer(channel));
    }
}
