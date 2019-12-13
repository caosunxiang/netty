package com.example.netty.netty.chat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


/**
 * @program: netty
 * @description: 聊天服务器启动
 * @author: 曹孙翔
 * @create: 2019-10-10 10:43
 **/
@Component
public class ChatServerApplication {
    @Autowired
    @Qualifier("bootstrap")
    private ServerBootstrap serverBootstrap;
    @Autowired
    ChatHandler chatHandler;

    private Channel channel;

    public void start() throws Exception {
        System.out.println("netty启动");
        channel = serverBootstrap.bind(8787).sync().channel().closeFuture().sync().channel();
    }
    public void close(){
        channel.close();
        channel.parent().close();
    }
}
