package com.liu.boot.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description:
 * @author LiuYi
 * @since 2022/6/23
 */
@Configuration
public class RabbitAdminConfig {

    @Autowired(required = false)
    private RabbitProperties properties;

    /**
     * RabbitMQ连接池，从配置文件获取参数
     */
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setHost(properties.getHost());
        factory.setPort(properties.getPort());
        factory.setUsername(properties.getUsername());
        factory.setPassword(properties.getPassword());
        factory.setVirtualHost(properties.getVirtualHost());
        return factory;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(@Autowired(required = false) CachingConnectionFactory factory) {
        return new RabbitAdmin(factory);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(CachingConnectionFactory factory) {
        return new RabbitTemplate(factory);
    }

    /*
     * RabbitTemplate使用配置文件形式创建
     */
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange("TemplateDirectEx", false, false);
    }

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange("TemplateFanoutEx", false, false);
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("TemplateTopicEx", false, false);
    }

    /*创建队列*/
    @Bean
    public Queue directQueue1() {
        return new Queue("directQueue1", true);
    }

    @Bean
    public Queue directQueue2() {
        return new Queue("directQueue2", true);
    }

    @Bean
    public Queue topicQueue1() {
        return QueueBuilder.durable("topicQueue1").build();
    }

    @Bean
    public Queue topicQueue2() {
        return QueueBuilder.durable("topicQueue2").build();
    }

    /**
     * 创建绑定关系方法一
     */
    @Bean
    public Binding directBind1() {
        return new Binding("directQueue1", Binding.DestinationType.QUEUE,
                "TemplateDirectEx", "WeiXin", null);
    }

    @Bean
    public Binding directBind2() {
        return BindingBuilder.bind(new Queue("directQueue2", false))
                .to(new DirectExchange("TemplateDirectEx"))
                .with("WeiXin");
    }


    /**
     * 创建绑定关系方法二
     * 将Bean方法名称作为参数代入
     */
    @Bean
    public Binding topicBind1(@Qualifier("topicQueue1") Queue queue,
                              @Qualifier("topicExchange") TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("user.#");
    }

    @Bean
    public Binding topicBind2(@Qualifier("topicQueue2") Queue queue,
                              @Qualifier("topicExchange") TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("vip.*");
    }
}
