package com.liu.rabbit.chapter3;

import com.liu.rabbit.chapter1.RabbitMQUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ReturnListener;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author LiuYi
 * @description Description about the file
 * @since 2022/6/21
 */
public class ReturnProducer {

    private static final String EXCHANGE_NAME = "return_exchange";
    private static final String NORMAL_KEY = "normal";
    private static final String VIP_KEY = "vip";


    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        channel.addReturnListener(new ReturnListener() {
            /**
             * replyCode：路由是否成功的响应码
             * replyText：文本说明
             * exchange：具体路由到的交换机
             * routingKey：路由键
             * properties：消息配置
             */
            @Override
            public void handleReturn(int replyCode, String replyText, String exchange, String routingKey, AMQP.BasicProperties basicProperties, byte[] bytes) throws IOException {
                System.out.println("Listener获取到无法路由的消息：\n" +
                        "replyCode：" + replyCode + "\n" +
                        "replyText：" + replyText + "\n" +
                        "exchange：" + exchange + "\n" +
                        "routingKey：" + routingKey + "\n" +
                        "properties：" + basicProperties + "\n" +
                        "body：" + new String(bytes));
            }
        });

        /*
         * 指定mandatory为true，会将不可路由的消息返回给生产者，设置为false时broker会直接丢弃不可路由的消息
         * 指定queue为vip而消费端为normal，无法路由就会触发回调函数
         */
        channel.basicPublish(EXCHANGE_NAME, NORMAL_KEY, true, null, "测试不可路由的消息文本".getBytes(StandardCharsets.UTF_8));
    }
}
