package com.liu.rabbit.chapter2;

import com.liu.rabbit.chapter1.RabbitMQUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;

import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author LiuYi
 * @description Description about the file
 * @since 2022/6/22
 */
public class MessageConfirm {

    private static final int MSG_COUNT = 10000;
    private static final String QUEUE_NAME = UUID.randomUUID().toString();


    /**
     * 单个confirm
     */
    public static void singleConfirm() throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        // 开启发布确认
        channel.confirmSelect();

        long start = System.currentTimeMillis();
        for (int i = 0; i < MSG_COUNT; i++) {
            String msg = "消息_" + i;
            System.out.println("生产了：" + msg);
            channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
            // 服务端返回 false 或超时时间内未返回，生产者可以消息重发
            boolean flag = channel.waitForConfirms();
            if (flag) {
                System.out.println(msg + "已发送到队列中");
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("发布了 " + MSG_COUNT + " 个单独确认消息，耗时：" + (end - start) + " ms");
    }

    /**
     * 批量confirm
     */
    public static void batchConfirm() throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        channel.confirmSelect();
        // 批量确认size
        int batchSize = 100;
        // 待确认的消息个数
        int size4Confirm = 0;

        long start = System.currentTimeMillis();
        for (int i = 0; i < MSG_COUNT; i++) {
            String msg = "消息_" + i;
            channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
            size4Confirm++;
            if (size4Confirm == batchSize) {
                channel.waitForConfirms();
                size4Confirm = 0;
                System.out.println("批量确认，最新的消息是：" + msg);
            }
        }
        //为了确保还有剩余没有确认消息 再次确认
        if (size4Confirm > 0) {
            System.out.println("处理剩余的未确认消息");
            channel.waitForConfirms();
        }
        long end = System.currentTimeMillis();
        System.out.println("发布了 " + MSG_COUNT + " 个批量确认消息，耗时：" + (end - start) + " ms");
    }

    /**
     * 异步confirm1
     */
    public static void asyConfirm1() throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        channel.confirmSelect();

        /*
         * 消息确认成功的回调函数
         * 参数一：消息的标记
         * 参数二：是否批量确认
         */
        ConfirmCallback successCall = (deliveryTag, multiple) -> {
            System.out.println("确认的消息：" + deliveryTag);
        };

        /*
         * 消息确认失败的回调函数
         * 参数一：消息的标记
         * 参数二：是否批量确认
         */
        ConfirmCallback failedCall = (deliveryTag, multiple) -> {
            System.out.println("未能确认的消息：" + deliveryTag);
        };

        /*
         * 设置异步确认模式的消息确认监听器
         * 参数一：成功的回调
         * 参数二：失败的回调
         */
        channel.addConfirmListener(successCall, failedCall);

        long start = System.currentTimeMillis();
        for (int i = 0; i < MSG_COUNT; i++) {
            String msg = "消息_" + i;
            channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
        }
        long end = System.currentTimeMillis();
        System.out.println("发布了 " + MSG_COUNT + " 个异步确认消息，耗时：" + (end - start) + " ms");
    }

    /**
     * 异步confirm2
     */
    public static void asyConfirm2() throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        channel.confirmSelect();

        /*
         * 用于存储消息的线程安全队列
         * concurrentSkipListMap比concurrentHashMap有更好的并发支持，是一个有序map容器，原理为跳表
         */
        ConcurrentSkipListMap<Long, Object> infoMap = new ConcurrentSkipListMap<>();

        /*
         * 消息确认成功的回调函数：删除已确认的消息
         */
        ConfirmCallback successCall = (deliveryTag, multiple) -> {
            if (multiple) {
                // 从key=null到截止key，作为指定key合集
                ConcurrentNavigableMap<Long, Object> confirmed = infoMap.headMap(deliveryTag);
                confirmed.clear();
            } else {
                infoMap.remove(deliveryTag);
            }
            System.out.println("确认的消息：" + deliveryTag);
        };

        /*
         * 消息确认失败的回调函数
         */
        ConfirmCallback failedCall = (deliveryTag, multiple) -> {
            String info = String.valueOf(infoMap.get(deliveryTag));
            System.out.println("未能确认的消息：" + deliveryTag + " ，消息是：" + info);
        };

        /*
         * 设置异步确认模式的消息确认监听器
         */
        channel.addConfirmListener(successCall, failedCall);

        long start = System.currentTimeMillis();
        for (int i = 0; i < MSG_COUNT; i++) {
            String msg = "消息_" + i;
            channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
            // 记录所有要打的消息，key为下一次发布信息的序号
            infoMap.putIfAbsent(channel.getNextPublishSeqNo(), msg);
        }
        long end = System.currentTimeMillis();
        System.out.println("发布了 " + MSG_COUNT + " 个异步确认消息，耗时：" + (end - start) + " ms");
    }


    public static void main(String[] args) throws Exception {
        // 单独确认的消息
//        singleConfirm();
        // 批量确认的消息
//        batchConfirm();
        // 异步确认的消息
        asyConfirm1();

        System.exit(0);
    }
}
