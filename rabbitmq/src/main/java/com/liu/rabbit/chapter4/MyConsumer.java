package com.liu.rabbit.chapter4;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;

/**
 * @author LiuYi
 * @description 自定义消费者,抽取消费成功和取消的回调函数,后续消费者
 * @since 2022/6/22
 */
public class MyConsumer extends DefaultConsumer {

    public MyConsumer(Channel channel) {
        super(channel);
    }

    @Override
    public void handleCancel(String consumerTag) throws IOException {
        System.out.println("消费者执行了取消操作");
    }


    /**
     * 正常的消息消费回调
     *
     * @param consumerTag 消费标签
     * @param envelope    信封,存放生产者相关消息
     * @param properties  消息配置
     * @param body        消息体
     */
    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        System.out.println("消费者接收了消息：\n" +
                "-----消费标签----->" + consumerTag + "\n" +
                "-----消息信封----->" + envelope + "\n" +
                "-----信息配置----->" + properties + "\n" +
                "-----消息内容----->" + new String(body) + "\n"
        );
        super.getChannel().basicAck(envelope.getDeliveryTag(), false);
    }
}
