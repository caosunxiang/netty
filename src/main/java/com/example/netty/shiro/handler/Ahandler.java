package com.example.netty.shiro.handler;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @program: netty
 * @description: 用户
 * @author: 曹孙翔
 * @create: 2019-10-10 17:38
 **/
@Controller
@RequestMapping("/a")
public class Ahandler {

    //登入的url
    @RequestMapping("/login")
    public String indexHtml(){
        return "/index";
    }
    @RequestMapping("/index")
    public  String index(){
        return  "login";
    }
    //属于user角色@RequiresRoles("user")
    //必须同时属于user和admin @RequiresRoles({"user","admin"})
    //属于user或者admin之一；修改logical为OR 即可@RequiresRoles(value = { "user", "admin"},
    // logical = Logical.OR

    @RequestMapping("/showUserHtml")
    @RequiresRoles(value={"admin","user"},logical = Logical.OR)
    //@RequiresPermissions("user:query")
    public String userHtml(){
        return "/user";
    }

    @RequestMapping("/showAdminHtml")
    @RequiresRoles("admin")
    @RequiresPermissions("admin:query")
    public String adminHtml() {
        return "/admin";
    }

    @RequestMapping("/unauthorized")
    public String unauthorized() {
        return "/abc";
    }

    @RequestMapping("/LoginSuccess")
    public String listHtml() {
        return "/list";
    }


    @RequestMapping("/error")
    public String error() {
        int a=1/0;
        return "/abc";
    }

    @RequestMapping("/chat")
    @RequiresRoles(value={"admin","user"},logical = Logical.OR)
    public String chat(){
        return "/chat";
    }

    @RequestMapping("/private")
    public String ccc() {
        return "/private1";
    }
    @RequestMapping("/private1")
    public String cc() {
        return "/private";
    }
}
