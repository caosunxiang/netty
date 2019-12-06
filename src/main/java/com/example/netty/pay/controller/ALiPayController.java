package com.example.netty.pay.controller;

import com.example.netty.pay.service.ALiPayService;
import com.example.netty.util.json.Body;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jdk.nashorn.internal.runtime.logging.Logger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(tags = "支付宝支付接口管理")
@Slf4j
@Logger
@RestController
public class ALiPayController {

    @Autowired
    private ALiPayService aLiPayService;

/***
 * @Author: 曹孙翔
 * @Description:创建订单
 * @Date: 14:17 2019/11/30
 * @Param: [req, order, mo, body]
 * @return: java.lang.String
 **/
    @RequestMapping("/payAlis")
    @ApiOperation(value  ="创建订单",notes ="支付宝创建订单" )
    public String doGetCode(HttpServletRequest req,
                            @ApiParam(value = "订单号") @RequestParam String order,
                            @ApiParam(value = "订单金额") @RequestParam Double mo,
                            @ApiParam(value = "商品名称")@RequestParam String body) {
        try{
            //1.验证订单是否存在

            //2.创建支付宝订单
            String orderStr= aLiPayService.createOrder(order,mo,body);
            return orderStr;
        }catch (Exception e){
            log.error(e.getMessage());
            return "订单生成失败";
        }
    }


    /***
     * @Author: 曹孙翔
     * @Description:支付异步通知
     *      接收到异步通知并验签通过后，一定要检查通知内容，
     *      包括通知中的app_id、out_trade_no、total_amount是否与请求中的一致，并根据trade_status进行后续业务处理。
     *      https://docs.open.alipay.com/194/103296
     * @Date: 14:28 2019/11/30
     * @Param: [request, response]
     * @return: java.lang.String
     **/
    @RequestMapping("notify_url")
    public String  doOrderSecPayNotify(HttpServletRequest request,
                                    HttpServletResponse response) throws Exception {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>进入回调方法");
        //验证签名
        boolean flag=aLiPayService.rsaCheckV1(request);
        if (flag){
            String tradeStatus = request.getParameter("trade_status"); // 交易状态
            String outTradeNo = request.getParameter("out_trade_no"); // 商户订单号
            String tradeNo = request.getParameter("trade_no"); // 支付宝订单号
            /**
             * 还可以从request中获取更多有用的参数，自己尝试
             */
            boolean notify=aLiPayService.notify(tradeStatus,outTradeNo,tradeNo);
            if (notify){
                return "success";
            }
        }
        return "fail";
    }

    @ApiOperation(value = "退款", notes = "退款")
    @PostMapping("/refund")
    public Body refund(@ApiParam(value = "订单号") @RequestParam String orderNo,
                       @ApiParam(value = "退款金额") @RequestParam double amount,
                       @ApiParam(value = "退款原因") @RequestParam(required = false) String refundReason) {
        return aLiPayService.refund(orderNo, amount, refundReason);
    }
}