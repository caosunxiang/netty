package com.example.netty.pojo;

import lombok.Data;

/**
 * @program: netty
 * @description: 消息结构
 * @author: 曹孙翔
 * @create: 2019-10-31 10:58
 **/
@Data
public class MessageInfo {
    //源客户端id
    private String sourceClientId;
    //目标客户端id
    private String targetClientId;
    //消息类型
    private String msgType;
    //消息内容
    private String msgContent;
}
