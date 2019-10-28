package com.example.netty.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: netty
 * @description: 用户表
 * @author: 曹孙翔
 * @create: 2019-10-10 17:11
 **/
@Data
public class User implements Serializable {
    private Integer id;
    private String name;
    private String pwd;
    private Role role;
}
