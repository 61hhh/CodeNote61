package com.liu.rabbit.chapter4;

import com.liu.rabbit.chapter1.RabbitMQUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.util.HashMap;

/**
 * Description:
 *
 * @author LiuYi
 * @since 2022/7/14
 */
public class PriorityProducer {
    public static final String EXCHANGE_NAME = "FanoutExchange"; //交换机名称

    public static void main(String[] args) throws Exception {
        final Channel channel = RabbitMQUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

        // 对队列设置优先级 范围在0-255之间
        HashMap<String, Object> map = new HashMap<>(1);
        map.put("x-max-priority", 10);
        channel.queueDeclare("PriorityQueue", false, false, false, map);
        channel.queueBind("PriorityQueue", EXCHANGE_NAME, "", null);

        for (int i = 0; i < 10; i++) {
            String msg = "INFO_" + i;
            if (i % 3 == 0) {
                final AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().priority(5).build();
                channel.basicPublish(EXCHANGE_NAME, "", properties, msg.getBytes());
            } else {
                channel.basicPublish(EXCHANGE_NAME, "", null, msg.getBytes());
            }
        }
        System.out.println("-----消息发送完毕-----");
    }
}
