package com.example.netty.pay.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeAppMergePayResponse;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.example.netty.pay.commons.config.ALiPayConfig;
import com.example.netty.pay.service.ALiPayService;
import com.example.netty.util.json.Body;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @program: netty
 * @description:
 * @author: 曹孙翔
 * @create: 2019-11-30 11:01
 **/
@Service
public class AliPayServiceImpl implements ALiPayService {
    private final Logger logger = LoggerFactory.getLogger(AliPayServiceImpl.class);

    @Autowired
    private ALiPayConfig aLiPayConfig;
    @Autowired
    private AlipayClient alipayClient;
    @Override
    public String createOrder(String orderNo, Double amount, String body) throws AlipayApiException {
        //SDK已经封装掉了公众参数，这里只需要传入业务参数，以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model =new AlipayTradeAppPayModel();
        model.setSubject(body);
        model.setOutTradeNo(orderNo);
        model.setTotalAmount(String.valueOf(amount));
        model.setProductCode("QUICK_MSECURITY_PAY");
        model.setPassbackParams("");
        //实例化具体API对应的request类，类名称和接口名称对应，当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest ali_request=new AlipayTradeAppPayRequest();
        ali_request.setBizModel(model);
        ali_request.setNotifyUrl(aLiPayConfig.getNotifyUrl());//回调地址
        AlipayTradeAppPayResponse ali_response=alipayClient.sdkExecute(ali_request);
        //就是orderString 可以直接给客户端请求，无需再做处理。
        System.out.println(ali_response.getBody());
        return ali_response.getBody();
    }

    @Override
    public boolean notify(String tradeStatus, String orderNo, String tradeNo) {
        if ("TRADE_FINISHED".equals(tradeStatus)||"TRADE_SUCCESS".equals(tradeStatus)){
            //支付成功，根据业务逻辑修改相应的数据的状态
            // boolean state = orderPaymentService.updatePaymentState(orderNo, tradeNo);
            //if (state){
            //    return true;
            //}
        }
        return false;
    }

    @Override
    public boolean rsaCheckV1(HttpServletRequest request) {
        try {
        Map<String,String>params=new HashMap<>();
        Map<String,String[]>requestParams=request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }

        boolean verifyResult = AlipaySignature.rsaCheckV1(params, aLiPayConfig.getAlipayPublicKey(), aLiPayConfig.getCharset(), aLiPayConfig.getSignType());
        return verifyResult;
    } catch (AlipayApiException e) {
        logger.debug("verify sigin error, exception is:{}", e);
        return false;
    }
    }

    @Override
    public Body refund(String orderNo, Double amount, String refundReason) {
        if(StringUtils.isBlank(orderNo)){
            return Body.newInstance("订单编号不能为空");
        }
        if(amount <= 0){
            return Body.newInstance("退款金额必须大于0");
        }

        AlipayTradeRefundModel model=new AlipayTradeRefundModel();
        // 商户订单号
        model.setOutTradeNo(orderNo);
        // 退款金额
        model.setRefundAmount(String.valueOf(amount));
        // 退款原因
        model.setRefundReason(refundReason);
        // 退款订单号(同一个订单可以分多次部分退款，当分多次时必传)
        // model.setOutRequestNo(UUID.randomUUID().toString());
        AlipayTradeRefundRequest alipayRequest = new AlipayTradeRefundRequest();
        alipayRequest.setBizModel(model);
        AlipayTradeRefundResponse alipayResponse = null;
        try {
            alipayResponse = alipayClient.execute(alipayRequest);
        } catch (AlipayApiException e) {
            logger.error("订单退款失败，异常原因:{}", e);
        }
        if(alipayResponse != null){
            String code = alipayResponse.getCode();
            String subCode = alipayResponse.getSubCode();
            String subMsg = alipayResponse.getSubMsg();
            if("10000".equals(code)
                    && StringUtils.isBlank(subCode)
                    && StringUtils.isBlank(subMsg)){
                // 表示退款申请接受成功，结果通过退款查询接口查询
                // 修改用户订单状态为退款
                return Body.newInstance("订单退款成功");
            }
            return Body.newInstance(subCode + ":" + subMsg);
        }
        return Body.newInstance("订单退款失败");
    }
}
