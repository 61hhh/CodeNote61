package com.liu.rabbit.chapter2;

import com.liu.rabbit.chapter1.RabbitMQUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.util.Scanner;

/**
 * @author LiuYi
 * @description Description about the file
 * @since 2022/6/22
 */
public class DurationProducer {

    private static final String QUEUE_NAME = "dur_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        // durable参数设置为true：队列持久化,重启rabbitmq该队列不会删除
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String msg = scanner.next();
            /*
             * basicProperties设置为PERSISTENT_TEXT_PLAIN：以text/plain形式将消息持久化到磁盘中
             * 消息持久化并不能完全保证消息不丢失，因为持久化需要时间
             */
            channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, msg.getBytes());
            System.out.println("task03发送了消息：" + msg);
        }
    }
}
