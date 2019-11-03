package com.example.netty.netty.nettysocket;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.example.netty.mapper.UserMapper;
import com.example.netty.pojo.MessageInfo;
import com.example.netty.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @program: netty
 * @description: 消息处理
 * @author: 曹孙翔
 * @create: 2019-10-31 11:09
 **/
@Component
public class MessageEventHandler {
    private final SocketIOServer server;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    public MessageEventHandler(SocketIOServer server){
        this.server=server;
    }
//添加connect时间，当客户端发起链接时调用本文中将clientid与sessionid存入数据库
    //方便后面发送消息时查找到对应的目标client,
    @OnConnect
    public void onConnect(SocketIOClient client){
        String clientId = client.getHandshakeData().getSingleUrlParam("clientid");
        User User = userMapper.getUser(clientId);
        if (User != null)
        {
            User.setConnected((short)1);
            User.setMostsignbits(client.getSessionId().getMostSignificantBits());
            User.setLeastsignbits(client.getSessionId().getLeastSignificantBits());
            userMapper.save(User);

        }

    }
    //添加@OnDisconnect事件，客户端断开连接时调用，刷新客户端信息
    @OnDisconnect
    public void onDisconnect(SocketIOClient client)
    {
        String clientId = client.getHandshakeData().getSingleUrlParam("clientid");
        User User = userMapper.getUser(clientId);
        if (User != null)
        {
            User.setConnected((short)0);
            User.setMostsignbits(null);
            User.setLeastsignbits(null);
            userMapper.save(User);
        }
    }
    //消息接收入口，当接收到消息后，查找发送目标客户端，并且向该客户端发送消息，且给自己发送消息
    @OnEvent(value = "messageevent")
    public void onEvent(SocketIOClient client, AckRequest request, MessageInfo data){
        String targetClientId = data.getTargetClientId();
        User user = userMapper.getUser(targetClientId);
        if (user != null && user.getConnected()!= 0)
        {
            UUID uuid = new UUID(user.getMostsignbits(), user.getLeastsignbits());
            System.out.println(uuid.toString());
            MessageInfo sendData = new MessageInfo();
            sendData.setSourceClientId(data.getSourceClientId());
            sendData.setTargetClientId(data.getTargetClientId());
            sendData.setMsgType("chat");
            sendData.setMsgContent(data.getMsgContent());
            client.sendEvent("messageevent", sendData);
            server.getClient(uuid).sendEvent("messageevent", sendData);
        }
    }
}
