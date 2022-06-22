package com.liu.rabbit.chapter3;


import com.liu.rabbit.chapter1.RabbitMQUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author LiuYi
 * @description 相当于Direct模式添加了模糊匹配，可以使用通配符绑定RoutingKey
 * @since 2022/6/21
 */
public class TopicProducer {

    private static final String EXCHANGE_NAME = "topic_exchange";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

        /*
         * Q1绑定：中间带orange的3个单词字符串—> *.orange.*
         * Q2绑定：最后一个单词为rabbit的3个单词字符串—> *.*.rabbit 或 首单词为lazy的多单词-> lazy.#
         */
        HashMap<String, String> keyMap = new HashMap<>();
        keyMap.put("quick.orange.rabbit", "被队列 Q1Q2 接收到");
        keyMap.put("lazy.orange.elephant", "被队列 Q1Q2 接收到");
        keyMap.put("quick.orange.fox", "被队列 Q1 接收到");
        keyMap.put("lazy.brown.fox", "被队列 Q2 接收到");
        keyMap.put("lazy.pink.rabbit", "虽然满足 Q2 的两个绑定,但只被队列 Q2 接收一次");
        keyMap.put("quick.brown.fox", "不匹配任何绑定不会被任何队列接收到 会被丢弃");
        keyMap.put("quick.orange.male.rabbit", "是四个单词不匹配任何绑定 会被丢弃");
        keyMap.put("lazy.orange.male.rabbit", "是四个单词但匹配 Q2");


        for (Map.Entry<String, String> keyEntry : keyMap.entrySet()) {
            String key = keyEntry.getKey();
            String value = keyEntry.getValue();
            channel.basicPublish(EXCHANGE_NAME, key, null, (key + value).getBytes(StandardCharsets.UTF_8));
            System.out.println("生产者发送消息：" + value);
        }
    }
}
