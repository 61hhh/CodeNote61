package com.liu.boot;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * @author LiuYi
 * @description Description about the file
 * @since 2022/6/23
 */
@SpringBootTest
public class RabbitTemplateTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testTemplate() {
        // 创建消息 可指定具体参数
        MessageProperties properties = new MessageProperties();
        properties.getHeaders().put("desc", "请求头desc信息");
        properties.getHeaders().put("type", "请求头type信息");
        properties.setContentType("application/json");
        properties.setContentEncoding("UTF-8");

        // 创建消息
        Message message = new Message("01--这是rabbitTemplate的消息".getBytes(StandardCharsets.UTF_8),properties);
        /*
         * MessagePostProcessor 发送消息前的拦截器，可以修改要发送的消息参数，如优先级、请求头等
         */
        rabbitTemplate.convertAndSend("TemplateDirectEx", "WeiXin", message, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                System.out.println("拦截需要发送的消息，并二次设置");
                message.getMessageProperties().getHeaders().put("desc", "请求头desc信息修改");
                message.getMessageProperties().getHeaders().put("attr", "请求头新增attr");
                return message;
            }
        });

        // 链式调用创建消息
        Message message2 = MessageBuilder.withBody("02--这是rabbitTemplate的消息".getBytes(StandardCharsets.UTF_8))
                .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                .setMessageId("消息ID：" + UUID.randomUUID())
                .setContentEncoding("UTF-8")
                .setHeader("desc", "消息2的描述")
                .build();
        rabbitTemplate.convertAndSend("TemplateDirectEx", "user.student", message2);

        // 最基础的调用方式
        rabbitTemplate.convertAndSend("TemplateDirectEx", "user.student", "基础消息");
        rabbitTemplate.send("TemplateDirectEx", "user.student",message);

    }

}
