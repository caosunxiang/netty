package com.example.netty.aop;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @program: netty
 * @description: 用户
 * @author: 曹孙翔
 * @create: 2020-01-06 16:44
 **/
@Component
public class User {
    public void AopTest() {
        System.out.println("我是曹孙翔。");
    }
}
