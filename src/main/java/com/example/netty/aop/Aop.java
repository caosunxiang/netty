package com.example.netty.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * @program: netty
 * @description: aop测试
 * @author: 曹孙翔
 * @create: 2020-01-06 16:40
 **/
@Aspect
@Component
public class Aop {
    @Around(value = "com.example.netty.aop.PointCuts.aopDemo()")
    public void Respects(ProceedingJoinPoint pjp) throws Throwable {
        System.out.println("你好，");
        pjp.proceed();
    }
}
