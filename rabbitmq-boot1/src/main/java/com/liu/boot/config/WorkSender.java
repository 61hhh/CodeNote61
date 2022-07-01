package com.liu.boot.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Description:
 *
 * @author LiuYi
 * @since 2022/6/30
 */
public class WorkSender {

    public static final Logger LOGGER = LoggerFactory.getLogger(WorkSender.class);
    public static final String QUEUE_NAME = "work";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(int index){
        StringBuilder builder = new StringBuilder("Hello");
        int limitIndex = index % 3+1;
        for (int i = 0; i < limitIndex; i++) {
            builder.append('.');
        }
        builder.append(index+1);
        String message = builder.toString();
        rabbitTemplate.convertAndSend(QUEUE_NAME, message);
        LOGGER.info(" [x] Sent '{}'", message);
    }


}
