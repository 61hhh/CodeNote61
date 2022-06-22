package com.liu.rabbit.chapter3;


import com.liu.rabbit.chapter1.RabbitMQUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * @author LiuYi
 * @description Description about the file
 * @since 2022/6/21
 */
public class FanoutConsumer1 {

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
        System.out.println("等待接收消息。。。。。。。。。。保存到文件");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String msg = new String(delivery.getBody(), StandardCharsets.UTF_8);
            File file = new File("E:\\data\\rabbitmq.txt");
            FileUtils.writeStringToFile(file, msg, StandardCharsets.UTF_8);
            System.out.println("数据写入文件成功");
        };
        channel.basicConsume(queueName, true, deliverCallback, consumer -> {
        });
    }
}
