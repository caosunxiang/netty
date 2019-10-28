package com.example.netty.datasource.aop;

import com.example.netty.datasource.config.DBContextHolder;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @program: netty
 * @description: 数据库          切面处理选择数据库
 * @author: 曹孙翔
 * @create: 2019-10-23 15:50
 **/
@Aspect
@Component
public class DataSourceAop {
    @Pointcut("!@annotation(com.example.netty.datasource.annotation.Master) " +
            "&& (execution(* com.example.netty.service..*.select*(..)) " +
            "|| execution(* com.example.netty.service..*.get*(..))" +
            "|| execution(* com.example.netty.service..*.login*(..)))")
    public void readPointcut() {

    }

    @Pointcut("@annotation(com.example.netty.datasource.annotation.Master) " +
            "|| execution(* com.example.netty.service..*.insert*(..)) " +
            "|| execution(* com.example.netty.service..*.add*(..)) " +
            "|| execution(* com.example.netty.service..*.update*(..)) " +
            "|| execution(* com.example.netty.service..*.edit*(..)) " +
            "|| execution(* com.example.netty.service..*.delete*(..)) " +
            "|| execution(* com.example.netty.service..*.remove*(..))")
    public void writePointcut() {

    }

    @Before("readPointcut()")
    public void read() {
        DBContextHolder.slave();
    }

    @Before("writePointcut()")
    public void write() {
        DBContextHolder.master();
    }


    /**
     * 另一种写法：if...else...  判断哪些需要读从数据库，其余的走主数据库
     */
//    @Before("execution(* com.cjs.example.service.impl.*.*(..))")
//    public void before(JoinPoint jp) {
//        String methodName = jp.getSignature().getName();
//
//        if (StringUtils.startsWithAny(methodName, "get", "select", "find")) {
//            DBContextHolder.slave();
//        }else {
//            DBContextHolder.master();
//        }
//    }
}
