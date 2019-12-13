package com.example.netty.netty.discard;

/**
 * @program: netty
 * @description: 丢弃数据（服务器端）
 * @author: 曹孙翔
 * @create: 2019-12-06 20:38
 **/


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * 丢弃数据
 * */
public class DiscardServer {
    private int port;
    public  DiscardServer(int port){
        this.port=port;
    }

    public void run(){
        EventLoopGroup bossGroup=new NioEventLoopGroup();//老板
        EventLoopGroup workerGroup=new NioEventLoopGroup();//工人


        try {
            ServerBootstrap b=new ServerBootstrap();//这是一个服务器的辅助类
            b.group(bossGroup,workerGroup).
                    channel(NioServerSocketChannel.class)//NioServerSocketChannel是用来实例化channel通道的
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new DiscardServerHandler());
                        }
                    }).option(ChannelOption.SO_BACKLOG,128)
                    .childOption(ChannelOption.SO_KEEPALIVE,true);
            //绑定并开始接受传入的连接。
            ChannelFuture f=b.bind(port).sync();
            System.out.println("DiscardServer已启动，端口："+port);
            //一直等到服务器套接字关闭。
            //在本例中，这种情况不会发生，但是您可以优雅地这样做
            //关闭服务器。
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        int port=8080;
        if (args.length>0){
            port=Integer.parseInt(args[0]);
        }
        new DiscardServer(port).run();
    }
}
