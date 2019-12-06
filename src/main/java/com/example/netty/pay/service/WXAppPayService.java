package com.example.netty.pay.service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * @program: netty
 * @description: 微信支付
 * @author: 曹孙翔
 * @create: 2019-12-02 10:16
 **/
public interface WXAppPayService {
    public String pay(Integer totalFee, String orderNo, HttpServletRequest request);//微信app支付

    public Boolean refund(Integer refundFee, Integer totalFee, String orderNo);//微信退款
}
