package com.example.netty.shiro.exception;

import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @program: netty
 * @description: 全局异常捕捉
 * @author: 曹孙翔
 * @create: 2019-10-25 16:03
 **/
@ControllerAdvice
public class AllException {


    /// 角色权限异常捕捉
    @ExceptionHandler(value = UnauthorizedException.class)
    @ResponseBody // 在返回自定义相应类的情况下必须有，这是@ControllerAdvice注解的规定
    public String roleException(UnauthorizedException e) {
        System.out.println("---------------------->" + e);
        return "角色权限不够！！！";
        // return "/abc";
    }

    // 其它异常异常捕捉
    @ExceptionHandler(value = Exception.class)
    @ResponseBody // 在返回自定义相应类的情况下必须有，这是@ControllerAdvice注解的规定
    public String allException(Exception e) {
        System.out.println("---------------------->" + e);
        return "系統出现异常！！！";
    }
}
