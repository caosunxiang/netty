package com.example.netty.service.impl;

import com.example.netty.mapper.UserMapper;
import com.example.netty.netty.chat.ChatServerApplication;
import com.example.netty.pojo.User;
import com.example.netty.service.UserService;
import com.example.netty.util.json.Body;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

/**
 * @program: netty
 * @description: 用户
 * @author: 曹孙翔
 * @create: 2019-10-10 17:39
 **/
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public String login(String name, String pwd) {
        User user = userMapper.login(name, pwd);
        if (user==null){
            return "login";
        }
            return "index";
        }
}
