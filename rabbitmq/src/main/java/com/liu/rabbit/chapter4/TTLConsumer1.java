package com.liu.rabbit.chapter4;

import com.liu.rabbit.chapter1.RabbitMQUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.util.HashMap;

/**
 * @author LiuYi
 * @description Description about the file
 * @since 2022/6/22
 */
public class TTLConsumer1 {

    public static final String EXCHANGE_NORMAL = "normalExchange";
    public static final String EXCHANGE_DEAD = "deadExchange";

    public static final String QUEUE_NORMAL = "normalQueue";
    public static final String QUEUE_DEAD = "deadQueue";

    public static final String ROUTING_KEY1 = "ZhangSan"; //普通队列routing-key
    public static final String ROUTING_KEY2 = "LiSi";     //死信队列routing-key


    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        // 声明普通和死信交换机 normal-exchange在生产者处已经声明
        channel.exchangeDeclare(EXCHANGE_NORMAL, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(EXCHANGE_DEAD, BuiltinExchangeType.DIRECT);

        /*
         * 通过额外参数指定在什么条件下讲消息转发到死信队列，其中key值是rabbitmq固定的
         * value1：TTL时间，一般由生产者指定
         * value2：死信交换机名称
         * value3：死信交换机的routing-key
         * value4：指定队列能够积压消息的数量，超出该范围的消息将进入死信队列
         */
        HashMap<String, Object> params = new HashMap<>();
        /*params.put("x-dead-letter-ttl", 10000);*/
        params.put("x-dead-letter-exchange", EXCHANGE_DEAD);
        params.put("x-dead-letter-routing-key", ROUTING_KEY2);
//        params.put("x-max-length", 6);

        channel.queueDelete(QUEUE_NORMAL); //变更queueDeclare的params参数后，原队列要删除
        // 声明正常队列
        channel.queueDeclare(QUEUE_NORMAL, false, false, false, params);
        channel.queueBind(QUEUE_NORMAL, EXCHANGE_NORMAL, ROUTING_KEY1);
//        // 声明死信队列
//        channel.queueDeclare(QUEUE_DEAD, false, false, false, null);
//        channel.queueBind(QUEUE_DEAD, EXCHANGE_DEAD, ROUTING_KEY2);

        System.out.println("----------等待接收消息----------");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String msg = new String(delivery.getBody());
            if (msg.equals("INFO_5")) {
                System.out.println("TTLConsumer1拒收消息----->" + msg);
                /*
                 * requeue设置为false表示拒绝重新入队
                 * 该队列如果配置了死信队列，交换机就会将消息发送到死信队列，否则会丢弃
                 */
                channel.basicReject(delivery.getEnvelope().getDeliveryTag(), false);
            } else {
                System.out.println("TTLConsumer1接收到----->" + msg);
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
            }
        };
        channel.basicConsume(QUEUE_NORMAL, false, deliverCallback, consumer -> {
        });


    }

}
