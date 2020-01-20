package com.example.netty.netty.IdleState;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;

/**
 * @program: netty
 * @description: 心跳器处理
 * @author: 曹孙翔
 * @create: 2020-01-08 17:22
 **/
public class HeartbeatServerHandler extends ChannelInboundHandlerAdapter {
    //定义了心跳时，要发送的内容
    private static final ByteBuf HEARTBEAT_SEQUENCE = Unpooled
            .unreleasableBuffer(Unpooled.copiedBuffer("Heartbeat",
                    CharsetUtil.UTF_8));


    //ALT+Ins调用
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        //判断是否是 IdleStateEvent 事件，是则处理
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            String type = "";
            if (event.state() == IdleState.READER_IDLE) {
                type = "read idle";
            } else if (event.state() == IdleState.WRITER_IDLE) {
                type = "write idle";
            } else if (event.state() == IdleState.ALL_IDLE) {
                type = "all idle";
            }
            //将心跳内容发送给客户端
            ctx.writeAndFlush(HEARTBEAT_SEQUENCE.duplicate()).addListener(
                    ChannelFutureListener.CLOSE_ON_FAILURE
            );
            System.out.println(ctx.channel().remoteAddress()+"超时类型：" + type);
        }else {
            super.userEventTriggered(ctx,evt);
        }

    }
}
