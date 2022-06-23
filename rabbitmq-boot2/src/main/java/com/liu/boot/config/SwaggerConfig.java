package com.liu.boot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author LiuYi
 * @description Description about the file
 * @since 2022/6/23
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    /**
     * 配置基本信息
     */
    private ApiInfo webApiInfo() {
        return new ApiInfoBuilder()
                .title("rabbitmq接口文档")
                .description("关于rabbitmq服务接口的定义文档")
                .version("1.0")
                .contact(new Contact("Leslie", "www.baidu.com", "1587403870@qq.com"))
                .build();
    }

    /**
     * 配置swagger文档生成最佳实践
     */
    @Bean
    public Docket webApiConfig() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("webApi")
                .apiInfo(webApiInfo())
                .select()
                .build();
    }

}
