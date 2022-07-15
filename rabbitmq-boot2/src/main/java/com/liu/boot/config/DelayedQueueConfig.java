package com.liu.boot.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

/**
 * Description:
 *
 * @author LiuYi
 * @since 2022/7/15
 */
@Configuration
public class DelayedQueueConfig {

    public static final String DELAYED_QUEUE = "delayed.queue";
    public static final String DELAYED_EXCHANGE = "delayed.exchange";
    public static final String DELAYED_ROUTING_KEY = "delayed.routingKey";

    /*
     * 创建插件版本的交换器，通过自定义方法创建
     * 插件版本非死信队列，不需要路由到不同的交换器指定过期时间，所以可以固定为direct类型
     */
    @Bean
    public CustomExchange delayedExchange() {
        HashMap<String, Object> map = new HashMap<>(1);
        map.put("x-delayed-type", "direct"); // 自定义交换机类型，固定为direct
        return new CustomExchange(DELAYED_EXCHANGE, "x-delayed-message", true, false, map);
    }

    @Bean
    public Queue delayedQueue() {
        return QueueBuilder.durable(DELAYED_QUEUE).build();
    }

    @Bean
    public Binding delayedBinding(@Qualifier("delayedQueue") Queue delayedQueue,
                                  @Qualifier("delayedExchange") CustomExchange delayedExchange) {
        return BindingBuilder.bind(delayedQueue).to(delayedExchange)
                .with(DELAYED_ROUTING_KEY)
                .noargs();
    }
}
