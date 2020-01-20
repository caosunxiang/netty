package com.example.netty.commons.swagger.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

/**
 * @program: netty
 * @description: swagger配置
 * @author: 曹孙翔
 * @create: 2020-01-07 11:22
 **/
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    /***
     * @Author: 曹孙翔
     * @Description:
     * @Configuration 是告诉 Spring Boot 需要加载这个配置类，
     * @EnableSwagger2 是启用 Swagger2，
     * 如果没加的话自然而然也就看不到后面的验证效果了。
     * @Date: 11:28 2020/1/7
     * @Param: []
     * @return: springfox.documentation.spring.web.plugins.Docket
     * @see: https://www.ibm.com/developerworks/cn/java/j-using-swagger-in-a-spring-boot-project/index.html
     **/
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }
    private ApiInfo apiInfo(){
        return  new ApiInfoBuilder()
                .title("netty")
                .description("曹孙翔-api模块接口文档")
                .termsOfServiceUrl("127.0.0.1:9999")
                .version("1.0")
                .build();
    }
}
