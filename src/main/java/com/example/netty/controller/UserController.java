package com.example.netty.controller;

import com.example.netty.pojo.User;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

/**
 * @program: netty
 * @description: 控制器
 * @author: 曹孙翔
 * @create: 2020-01-07 11:30
 **/
@RestController
@RequestMapping("/user")
public class UserController {
    @PostMapping("/add")
    @ApiOperation("新增用户接口")
    public boolean addUser(@RequestBody User user) {
        return false;
    }
    @GetMapping("/find/{id}")
    public User findById(@PathVariable("id") Integer id) {
        return new User();
    }
    @PutMapping("/update")
    public boolean update(@RequestBody User user) {
        return true;
    }
    @DeleteMapping("/delete/{id}")
    public boolean delete(@PathVariable("id") Integer id) {
        return true;
    }
}
