package com.example.netty.netty.IdleState;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @program: netty
 * @description: 服务器
 * @author: 曹孙翔
 * @create: 2020-01-08 17:44
 **/
public class HeartbeatServer {
    static final int PORT=8082;

    public static void main(String[] args) throws  Exception{
        //配置服务器
        EventLoopGroup bossGroup=new NioEventLoopGroup(1);
        EventLoopGroup workerGroup=new NioEventLoopGroup();
        try {
            ServerBootstrap b=new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new HeartbeatHandlerInitializer());
            // 启动服务器
            ChannelFuture f=b.bind(PORT).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            //关闭所有事件循环以终止所有线程。
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
