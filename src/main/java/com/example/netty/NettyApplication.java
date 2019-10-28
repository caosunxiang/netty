package com.example.netty;

import com.example.netty.netty.chat.ChatServerApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@MapperScan("com.example.netty.mapper")
public class NettyApplication {

    public static void main(String[] args) throws Exception {
        //SpringApplication.run(NettyApplication.class, args);
        ConfigurableApplicationContext context =SpringApplication.run(NettyApplication.class, args);
        ChatServerApplication chatServerApplication=context.getBean(ChatServerApplication.class);
        chatServerApplication.start();
    }

}
