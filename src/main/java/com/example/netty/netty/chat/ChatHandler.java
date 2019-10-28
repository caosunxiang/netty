package com.example.netty.netty.chat;

import com.example.netty.mapper.UserMapper;
import com.example.netty.pojo.User;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;

/**
 * @program: netty
 * @description: 聊天逻辑
 * @author: 曹孙翔
 * @create: 2019-10-10 11:03
 **/
@Component
//以多线程的方式处理
@ChannelHandler.Sharable
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame/*以哪一种类型处理数据*/> {
    //创建通道组    将进入连接的用户添加进去
    private static ChannelGroup channels= new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    //广播
    @Override
    protected void channelRead0(ChannelHandlerContext ctx/*通道对象*/, TextWebSocketFrame msg/*携带的消息内容*/) throws Exception {

        String content =msg.text();
        Channel iChannel =ctx.channel();
        for (Channel channel : channels){
            if (channel != iChannel){
                channel.writeAndFlush(new TextWebSocketFrame(ctx.channel().remoteAddress()+":"+content));
            }else{
                channel.writeAndFlush(new TextWebSocketFrame("我说："+content));
            }
        }
    }
    //退出

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        channels.remove(ctx.channel());
        for (Channel channel : channels){
            channel.writeAndFlush(new TextWebSocketFrame(ctx.channel().remoteAddress()+"退出聊天室"));
        }
        channels.add(ctx.channel());
    }

    //进入

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {

      for (Channel channel : channels){
          channel.writeAndFlush(new TextWebSocketFrame(ctx.channel().remoteAddress()+"进入聊天室"));
      }
      channels.add(ctx.channel());
    }
}
