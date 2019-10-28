package com.example.netty.service;

import com.example.netty.util.json.Body;

import javax.servlet.http.HttpSession;

/**
 * @program: netty
 * @description: 用户
 * @author: 曹孙翔
 * @create: 2019-10-10 17:36
 **/
public interface UserService {
    public String login(String name, String pwd);
}
