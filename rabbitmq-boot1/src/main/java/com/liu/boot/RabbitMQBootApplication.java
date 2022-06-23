package com.liu.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author LiuYi
 * @description Description about the file
 * @since 2022/6/23
 */
@SpringBootApplication
public class RabbitMQBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(RabbitMQBootApplication.class,args);
    }
}
