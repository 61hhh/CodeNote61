package com.liu.rabbit.chapter1;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * @author LiuYi
 * @description 通过IDEA的运行设置, 允许multi instance实现多线程运行
 * @since 2022/6/13
 */
public class Work01 {

    private static final String QUEUE_NAME = "rabbit61";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String receiveMsg = new String(delivery.getBody());
            System.out.println("Worker01接收到消息：" + receiveMsg);
        };
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println(consumerTag + "--->消费者取消了消费接口");
        };

        System.out.println("线程01启动   等待消费消息........");
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
    }
}

