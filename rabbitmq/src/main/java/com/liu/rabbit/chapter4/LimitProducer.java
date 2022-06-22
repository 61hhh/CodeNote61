package com.liu.rabbit.chapter4;


import com.liu.rabbit.chapter1.RabbitMQUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author LiuYi
 * @description Description about the file
 * @since 2022/6/22
 */
public class LimitProducer {

    public static final String QUEUE_NAME = "slowQueue";
    public static final String EXCHANGE_NAME = "fastExchange";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "limit");

        // 使用线程池模拟短时间大量消息发送
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(5, 10, 100, TimeUnit.SECONDS, new LinkedBlockingDeque<>());
        for (int i = 0; i < 2000; i++) {
            String msg = Thread.currentThread().getName()+"_"+i;
            try {
                channel.basicPublish(EXCHANGE_NAME, "limit", null, msg.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        threadPool.shutdown();
        while (!threadPool.isTerminated()) {

        }
        System.out.println("所有消息发送完成");
    }
}
