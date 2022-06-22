package com.liu.rabbit.chapter2;

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
public class FairDispatch {

    private static final String QUEUE_NAME = "dur_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();

        // 消费回调逻辑
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            System.out.println("开始消费");
            try {
                System.out.println("模拟实际业务操作，耗时1秒");
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("接收到消息：" + new String(delivery.getBody()));
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println(consumerTag + "--->消费者取消了消费接口");
        };

        System.out.println("Work03   等待消费消息........");
        /*
         * prefetch参数：预取值，限制未确认消息的数量，表示当前消费者每次最多接收的消息个数
         * 例如prefetch设为1，表示Work04每次只接收一条消息，处理完手动应答后才会接收下一条消息
         */
        channel.basicQos(1);
        channel.basicConsume(QUEUE_NAME, false, deliverCallback, cancelCallback);
    }
}
