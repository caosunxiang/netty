package com.example.netty.pay.service;

import com.alipay.api.AlipayApiException;
import com.example.netty.util.json.Body;

import javax.servlet.http.HttpServletRequest;

/**
 * @program: netty
 * @description:
 * @author: 曹孙翔
 * @create: 2019-11-30 10:52
 **/
public interface ALiPayService {
    /***
     * @Author: 曹孙翔
     * @Description:创建制度订单
     * @Date: 10:55 2019/11/30
     * @Param: [orderNo 订单号, amount 金额, body 订单描述]
     * @return: java.lang.String
     **/
    public String createOrder(String orderNo,Double amount,String body) throws AlipayApiException;
/***
 * @Author: 曹孙翔
 * @Description:
 * @Date: 10:57 2019/11/30
 * @Param: [tradeStatus 支付宝交易状态, orderNo 订单编号, tradeNo 支付宝订单号]
 * @return: boolean
 **/
    public boolean notify(String tradeStatus,String orderNo ,String tradeNo);
/***
 * @Author: 曹孙翔
 * @Description:校验签名
 * @Date: 10:58 2019/11/30
 * @Param: [request]
 * @return: boolean
 **/
    public boolean rsaCheckV1(HttpServletRequest request);
/***
 * @Author: 曹孙翔
 * @Description:退款
 * @Date: 11:00 2019/11/30
 * @Param: [orderNo 订单号, amount 实际支付金额, refundReason 退款原因]
 * @return: com.example.netty.util.json.Body
 **/
    public Body refund(String orderNo,Double amount,String refundReason);
}
