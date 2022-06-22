package com.liu.rabbit.chapter4;


import com.liu.rabbit.chapter1.RabbitMQUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.util.concurrent.TimeUnit;

/**
 * @author LiuYi
 * @description Description about the file
 * @since 2022/6/22
 */
public class LimitConsumer {

    public static final String QUEUE_NAME = "slowQueue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        // 消费成功回调
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            try {
                TimeUnit.SECONDS.sleep(1); // 模拟业务操作
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("收到的消息为：" + new String(delivery.getBody()));
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };
        // 消费取消回调
        CancelCallback cancelCallback = cancel -> {
            System.out.println("取消消费");
        };

        /*
         * 参数一：单条消息大小限制，一般为0不限制
         * 参数二：一次性消费的消息数量
         * 参数三：限流设置应用于channel(true)还是consumer(false)
         */
//        channel.basicQos(0, 10, false);
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
    }
}
