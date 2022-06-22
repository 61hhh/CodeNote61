package com.liu.rabbit.chapter4;

import com.liu.rabbit.chapter1.RabbitMQUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

/**
 * @author LiuYi
 * @description Description about the file
 * @since 2022/6/22
 */
public class TTLProducer {

    public static final String EXCHANGE_NAME = "normalExchange";
    public static final String ROUTING_KEY = "ZhangSan";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        // 设置消息的TTL为10秒
//        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().expiration("10000").build();
        // 演示队列个数限制
        for (int i = 1; i < 11; i++) {
            String msg = "INFO_" + i;
            channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, null, msg.getBytes());
            System.out.println(msg + "发送完成");
        }
    }
}
