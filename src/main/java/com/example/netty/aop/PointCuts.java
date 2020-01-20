package com.example.netty.aop;

import org.aspectj.lang.annotation.Pointcut;

/**
 * @program: netty
 * @description: 标记切点
 * @author: 曹孙翔
 * @create: 2020-01-06 17:29
 **/

public class PointCuts {
    @Pointcut(value = "within(com.example.netty.aop.User)")
    public void aopDemo() {
    }
}
