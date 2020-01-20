package com.example.netty.aop;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: netty
 * @description: 测试
 * @author: 曹孙翔
 * @create: 2020-01-06 17:14
 **/
@RestController
@RequestMapping("aop")
@Api("切面")
public class AopController {
    @Autowired
    private User user;

    @RequestMapping("test")
    @ApiOperation("测试")
    public void AopTest() {
        user.AopTest();
    }
}
