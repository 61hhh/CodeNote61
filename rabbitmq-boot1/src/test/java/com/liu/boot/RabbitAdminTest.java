package com.liu.boot;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author LiuYi
 * @description Description about the file
 * @since 2022/6/23
 */
@SpringBootTest
public class RabbitAdminTest {

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Test
    public void declareExchange() {
        rabbitAdmin.declareExchange(new DirectExchange("AdminDirect", false, false, null));
        rabbitAdmin.declareExchange(new FanoutExchange("AdminFanout", false, false, null));
        rabbitAdmin.declareExchange(new TopicExchange("AdminTopic", false, false, null));

    }

    @Test
    public void declareQueue() {
        rabbitAdmin.declareQueue(new Queue("directAdmin", false, false, false));
        rabbitAdmin.declareQueue(QueueBuilder.durable("directAdmin2").build());

        rabbitAdmin.declareQueue(new Queue("fanoutAdmin", false, false, false));
        rabbitAdmin.declareQueue(QueueBuilder.durable("fanoutAdmin2").build());

        rabbitAdmin.declareQueue(new Queue("topicAdmin", false, false, false));
        rabbitAdmin.declareQueue(QueueBuilder.durable("topicAdmin2").build());
    }

    @Test
    public void declareBind() {
        rabbitAdmin.declareBinding(
                new Binding("directAdmin", Binding.DestinationType.QUEUE, "AdminDirect", "info", null));

        rabbitAdmin.declareBinding(BindingBuilder.
                bind(new Queue("directAdmin2", false)).
                to(new DirectExchange("AdminDirect")).
                with("info"));
    }

    @Test
    public void otherApi() {
        /*
         * 清空队列内的消息
         * 参数一：队列名
         * 参数二：是否等待
         */
        rabbitAdmin.purgeQueue("directAdmin", false);
    }
}
