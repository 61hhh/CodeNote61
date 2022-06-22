package com.liu.rabbit.chapter4;

import com.liu.rabbit.chapter1.RabbitMQUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.util.Scanner;

/**
 * @author LiuYi
 * @description Description about the file
 * @since 2022/6/22
 */
public class TestMyProducer {

    public static final String QUEUE_NAME = "MyQueue";
    public static final String EXCHANGE_NAME = "MyExchange";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "testMy");

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String msg = scanner.nextLine();
            AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().
                    appId("123456").
                    contentType("application/text").
                    build();
            channel.basicPublish(EXCHANGE_NAME, "testMy", properties, msg.getBytes());
            System.out.println("消息发送完成----->" + msg);
        }
    }
}
