package com.liu.rabbit.chapter3;


import com.liu.rabbit.chapter1.RabbitMQUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author LiuYi
 * @description Description about the file
 * @since 2022/6/21
 */
public class DirectProducer {

    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        // 创建多个bindingKey
        HashMap<String, String> keyMap = new HashMap<>();
        keyMap.put("info", "info信息");
        keyMap.put("warning", "warning警告");
        keyMap.put("error", "error错误");
        // debug不设置消费者接收，查看效果是全部丢失
        keyMap.put("debug", "debug调试");


        for (Map.Entry<String, String> keyEntry : keyMap.entrySet()) {
            String key = keyEntry.getKey();
            String value = keyEntry.getValue();
            channel.basicPublish(EXCHANGE_NAME, key, null, value.getBytes(StandardCharsets.UTF_8));
            System.out.println("生产者发送消息：" + value);
        }
    }
}
