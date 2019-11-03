package com.example.netty.netty.nettysocket;

import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @program: netty
 * @description:
 * @author: 曹孙翔
 * @create: 2019-10-31 11:46
 **/
@Component
public class ServerRunner implements CommandLineRunner {
    private final SocketIOServer server;
    @Autowired
    public ServerRunner(SocketIOServer server){
        this.server=server;
    }
    @Override
    public void run(String... args) throws Exception {
        server.start();
    }
}
