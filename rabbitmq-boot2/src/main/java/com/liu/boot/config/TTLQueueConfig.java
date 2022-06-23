package com.liu.boot.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author LiuYi
 * @description Description about the file
 * @since 2022/6/23
 */
@Configuration
public class TTLQueueConfig {

    public static final String X_EXCHANGE = "ex_x";
    public static final String QUEUE_A = "QA";
    public static final String QUEUE_B = "QB";
    public static final String QUEUE_C = "QC";
    // 死信交换机和队列
    public static final String Y_EXCHANGE = "ex_y";
    public static final String QUEUE_D = "QD";

    @Bean("xExchange")
    public DirectExchange xExchange() {
        return new DirectExchange(X_EXCHANGE);
    }

    @Bean("yExchange")
    public DirectExchange yExchange() {
        return new DirectExchange(Y_EXCHANGE);
    }

    @Bean("queueA")
    public Queue queueA() {
        Map<String, Object> map = new HashMap<>(3);
        // 声明死信队列
        map.put("x-dead-letter-exchange", Y_EXCHANGE);
        map.put("x-dead-letter-routing-key", "RK_Y");
        map.put("x-message-ttl", 10000);
        return QueueBuilder.durable(QUEUE_A).withArguments(map).build();
    }

    @Bean
    public Binding queueBindingA(@Qualifier("queueA") Queue queueA,
                                 @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(queueA).to(xExchange).with("RK_XA");
    }

    @Bean("queueB")
    public Queue queueB() {
        Map<String, Object> map = new HashMap<>(3);
        // 声明死信队列
        map.put("x-dead-letter-exchange", Y_EXCHANGE);
        map.put("x-dead-letter-routing-key", "RK_Y");
        map.put("x-message-ttl", 40000);
        return QueueBuilder.durable(QUEUE_B).withArguments(map).build();
    }

    @Bean
    public Binding queueBindingB(@Qualifier("queueB") Queue queueB,
                                 @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(queueB).to(xExchange).with("RK_XB");
    }

    @Bean("queueC")
    public Queue queueC() {
        Map<String, Object> map = new HashMap<>(3);
        // 声明死信队列
        map.put("x-dead-letter-exchange", Y_EXCHANGE);
        map.put("x-dead-letter-routing-key", "RK_Y");
        return QueueBuilder.durable(QUEUE_C).withArguments(map).build();
    }

    @Bean
    public Binding queueBindingC(@Qualifier("queueC") Queue queueC,
                                 @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(queueC).to(xExchange).with("RK_XC");
    }

    @Bean("queueD")
    public Queue queueD() {
        return new Queue(QUEUE_D);
    }

    @Bean
    public Binding queueBindingD(@Qualifier("queueD") Queue queueD,
                                 @Qualifier("yExchange") DirectExchange yExchange) {
        return BindingBuilder.bind(queueD).to(yExchange).with("RK_Y");
    }
}
