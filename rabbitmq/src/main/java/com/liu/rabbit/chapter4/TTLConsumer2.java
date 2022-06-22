package com.liu.rabbit.chapter4;

import com.liu.rabbit.chapter1.RabbitMQUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * @author LiuYi
 * @description Description about the file
 * @since 2022/6/22
 */
public class TTLConsumer2 {

    public static final String EXCHANGE_DEAD = "deadExchange";
    public static final String QUEUE_DEAD = "deadQueue";
    public static final String ROUTING_KEY2 = "LiSi";     //死信队列routing-key


    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_DEAD, BuiltinExchangeType.DIRECT);
        channel.queueDeclare(QUEUE_DEAD, false, false, false, null);
        channel.queueBind(QUEUE_DEAD, EXCHANGE_DEAD, ROUTING_KEY2);

        System.out.println("----------等待接收消息----------");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String msg = new String(delivery.getBody());
            System.out.println("TTLConsumer2接收到----->" + msg);
        };
        channel.basicConsume(QUEUE_DEAD, true, deliverCallback, consumer -> {
        });


    }
}
