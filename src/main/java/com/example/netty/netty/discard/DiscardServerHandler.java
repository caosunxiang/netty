package com.example.netty.netty.discard;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * @program: netty
 * @description: netty入门     （丢弃）
 * 要实现丢弃协议，您只需忽略所有接收到的数据。
 * 让我们直接从处理程序实现开始，它处理由Netty生成的I/O事件。
 * @author: 曹孙翔
 * @create: 2019-12-06 20:24
 **/

/**
 * 处理服务器端通道。
 * */
public class DiscardServerHandler extends ChannelInboundHandlerAdapter {

    //@Override
    /***
     * @Author: 曹孙翔
     * @Description:
     * 我们在channelRead()这里重写事件处理程序方法。
     * 每当从客户端接收到新数据时，就使用接收到的消息调用此方法。
     * 在此示例中，接收到的消息的类型为ByteBuf。
     * @Date: 21:33 2019/12/6
     * @Param: [ctx, msg]
     * @return: void
     **/
    public void channelRead(ChannelHandlerContext ctx, Object msg)  {
        //try {
        //    //以静默方式丢弃接收到的数据。
        //    ByteBuf in=((ByteBuf)msg);
        //    while(in.isReadable()){
        //        System.out.println(">>>>>>>>>>>>>>>>"+(char)in.readByte());
        //        System.out.flush();
        //    }
        //} catch (Exception e) {
        //    e.printStackTrace();
        //}finally {
        //    ReferenceCountUtil.release(msg);
        //}
        System.out.println(ctx.channel().remoteAddress()+"->Server:"+msg);
        ctx.write(msg);
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
       //在引发异常时关闭连接。
        cause.printStackTrace();
        ctx.close();
    }

    //@Override
    //public void channelRead(ChannelHandlerContext ctx, Object msg)  {
    //   try {
    //       //业务逻辑
    //       ctx.write("我想服务器您发送了一条 消息");
    //       ctx.flush();
    //   }finally {
    //       ReferenceCountUtil.release(msg);
    //   }
    //}
}
