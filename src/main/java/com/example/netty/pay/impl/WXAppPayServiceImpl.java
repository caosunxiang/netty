package com.example.netty.pay.impl;

import com.example.netty.pay.commons.WXpay.TenpayHttpClient;
import com.example.netty.pay.commons.WXpay.XMLUtil;
import com.example.netty.pay.service.WXAppPayService;
import com.example.netty.util.IPGet;
import com.example.netty.util.JsonUtils;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConfig;
import com.github.wxpay.sdk.WXPayUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @program: netty
 * @description: 微信支付实现
 * @author: 曹孙翔
 * @create: 2019-12-02 10:10
 **/
@Service
@Slf4j
public class WXAppPayServiceImpl implements WXAppPayService {
    @Autowired
    private WXPayConfig wxPayConfig;

    @Value("${wx.app.key}")
    String key;
    @Value("${wx.app.notifyUrl}")
    String notify_url;
    @Value("${wx.app.appId}")
    String appId;
    @Value("${wx.app.mchId}")
    String mch_id;

    @Override
    public String pay(Integer totalFee, String orderNo, HttpServletRequest request) {
        log.info("微信app支付");
        String ip= IPGet.IPGet(request);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+ip);
        Map<String,String>map= wxPay(orderNo,totalFee,ip);
        return JsonUtils.objectToJson(map);
    }

    @Override
    public Boolean refund(Integer refundFee, Integer totalFee, String orderNo) {
        log.info("微信退款");
        return refund(refundFee, totalFee, orderNo);
    }


    private Map<String,String> wxPay(String orderNO,Integer payAmount,String ip){
        try {
            Map<String ,String >map=new LinkedHashMap<>();
            String body="支付订单";
            String nonce_str= WXPayUtil.generateNonceStr();
            log.info("生成的随机数nonce_str>>>>>>>>>>>>>>>>>>>"+nonce_str);
            String total_fee=String.valueOf(payAmount);
            map.put("body",body);
            map.put("nonce_str", nonce_str);
            map.put("out_trade_no", orderNO);
            map.put("total_fee", total_fee);
            map.put("spbill_create_ip", ip);
            map.put("notify_url", notify_url);
            map.put("trade_type", "APP");
            log.info("给微信发送的订单信息>>>>>>>>>>>>>>>>>"+map.toString());
            WXPay pay=new WXPay(wxPayConfig);
            Map<String,String>returnMap=pay.unifiedOrder(map);
            log.info("给微信返回的拉起服务的参数>>>>>>>>>>>>>>>>>"+map.toString());
            Map<String,String>resultMap=new LinkedHashMap<>();
            resultMap.put("appid",appId);
            log.info("参数中的appid>>>>>>>>>>>>>>>>>"+resultMap.get("appid"));
            resultMap.put("noncestr", nonce_str);
            log.info("参数中的noncestr>>>>>>>>>>>>>>>>>"+ resultMap.get("noncestr"));
            resultMap.put("packages", "Sign=WXPay");
            log.info("参数中的packages>>>>>>>>>>>>>>>>>"+resultMap.get("packages"));
            resultMap.put("partnerid", mch_id);
            log.info("参数中的partnerid>>>>>>>>>>>>>>>>>"+resultMap.get("partnerid"));
            resultMap.put("prepayid", returnMap.get("prepay_id"));
            log.info("参数中的prepayid>>>>>>>>>>>>>>>>>"+resultMap.get("prepayid"));
            resultMap.put("timestamp", System.currentTimeMillis() / 1000 + "");
            log.info("参数中的timestamp>>>>>>>>>>>>>>>>>"+resultMap.get("timestamp"));
            StringBuilder result=new StringBuilder();
            for (Map.Entry<String,String > entry:resultMap.entrySet()){
                result.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            result.append("key=").append(key);
            log.info("加密前的用户信息"+result.toString());
            resultMap.put("sign", DigestUtils.md5Hex(result.toString()).toUpperCase());
            log.info("加密后的用户信息"+resultMap.get("sign"));
            return resultMap;
        }catch (Exception e){
            log.info("微信支付失败{}", Arrays.toString(e.getStackTrace()));
            throw  new ServiceException("微信支付异常");
        }
    }


    private Boolean wxRefund(Integer refundFee,Integer totalFee,String orderNo){
        try {
            String outRequestNo=orderNo+"";
            String refundAmount = String.valueOf(refundFee);
            String totalAmount = String.valueOf(totalFee);
            Map<String, String> map = new LinkedHashMap<>();
            String nonce_str = WXPayUtil.generateNonceStr();
            map.put("nonce_str", nonce_str);
            map.put("out_trade_no", orderNo);
            map.put("out_refund_no", outRequestNo);
            map.put("total_fee", totalAmount);
            map.put("refund_fee", refundAmount);
            WXPay pay = new WXPay(wxPayConfig);
            Map<String, String> resultMap = pay.refund(map);
            String resultCode=resultMap.get("result_code")==null?"FAIL":resultMap.get("result_code");
            String resultRefundFee=resultMap.get("refund_fee")==null?"FAIL":resultMap.get("refund_fee");
            if ("SUCCESS".equals(resultCode)&&resultRefundFee.equals(refundAmount)){
                return  true;
            }
        }catch (Exception e){
            log.info("微信退款失败{}",Arrays.toString(e.getStackTrace()));
            return false;
        }
        return false;
    }
}
