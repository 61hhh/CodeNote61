package com.liu.rabbit.chapter4;

import com.liu.rabbit.chapter1.RabbitMQUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * Description:
 *
 * @author LiuYi
 * @since 2022/7/14
 */
public class PriorityConsumer {
    public static final String QUEUE_NAME = "PriorityQueue"; //交换机名称

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();

        DeliverCallback deliverCallback = (consumerTag, msg) -> {
            System.out.println("接收到消息：" + new String(msg.getBody()));
            channel.basicAck(msg.getEnvelope().getDeliveryTag(), false);
        };
        channel.basicConsume(QUEUE_NAME, false, deliverCallback, consumer -> {});
    }
}
