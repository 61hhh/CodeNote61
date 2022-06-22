package com.liu.rabbit.chapter1;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author LiuYi
 * @description 抽取获取连接、开启通道部分的公共代码
 * @since 2022/6/13
 */
public class RabbitMQUtils {

    public static Channel getChannel() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.204.127");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");

        Connection connection = factory.newConnection();
        return connection.createChannel();

    }
}

