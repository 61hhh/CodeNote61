package com.liu.rabbit.chapter1;

import com.rabbitmq.client.Channel;

import java.util.Scanner;

/**
 * @author LiuYi
 * @description description about the class
 * @since 2022/6/13
 */
public class Task01 {

    private static final String QUEUE_NAME = "rabbit61";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String msg = scanner.next();
            channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
            System.out.println("task01发送了消息：" + msg);
        }
    }
}
