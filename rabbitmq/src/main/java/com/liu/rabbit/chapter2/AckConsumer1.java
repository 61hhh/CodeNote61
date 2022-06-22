package com.liu.rabbit.chapter2;

import com.liu.rabbit.chapter1.RabbitMQUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.util.concurrent.TimeUnit;

/**
 * @author LiuYi
 * @description 消息应答：确认应答
 * @since 2022/6/20
 */
public class AckConsumer1 {

    private static final String QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();

        // 消费回调逻辑
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            System.out.println("开始消费");
            try {
                System.out.println("模拟实际业务操作，耗时20秒");
                TimeUnit.SECONDS.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("接收到消息：" + new String(delivery.getBody()));

            /*  手动应答
             *  参数一：消息标记tag
             *  参数二：是否批量消费消息(true：应答队列中的所有消息 | false：应答接收到的消息)
             */
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println(consumerTag + "--->消费者取消了消费接口");
        };

        System.out.println("Work02   等待消费消息........");
        channel.basicConsume(QUEUE_NAME, false, deliverCallback, cancelCallback);
    }
}
