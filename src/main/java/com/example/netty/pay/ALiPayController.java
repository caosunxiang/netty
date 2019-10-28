package com.example.netty.pay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.example.netty.pay.AliPays.ALiPayConfig;
import com.example.netty.util.IDUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


@Slf4j
@RestController
public class ALiPayController {



    @RequestMapping("/payAlis")
    public String doGetCode(HttpServletRequest req, Integer user, Double mo)
            throws Exception {
        String msg = null;// 最后的返回值
        return payIn(msg, user, mo);
    }

    public String payIn(String msg, Integer user, Double mo) {
        try {
            // DecimalFormat dex = new DecimalFormat("#0.00");
            // String payAccount = String.valueOf(dex.format(1));// 测试使用（可删除）

            // 实例化客户端
            AlipayClient alipayClient = new DefaultAlipayClient(
                    "https://openapi.alipay.com/gateway.do",
                    ALiPayConfig.appid, ALiPayConfig.private_key, "json",
                    ALiPayConfig.input_charset, ALiPayConfig.ali_public_key,
                    ALiPayConfig.pay_sign_type);
            // 实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
            AlipayTradeAppPayRequest request1 = new AlipayTradeAppPayRequest();
            // SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
            AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
            model.setBody("凤梨科技果果驿站");
            model.setSubject("用户余额充值:" + mo + "元");
            model.setOutTradeNo(IDUtils.genItemId() + "");
            model.setTimeoutExpress("30m");
            model.setPassbackParams(user + "");
            model.setTotalAmount(mo + "");
            model.setSellerId(ALiPayConfig.partner);
            model.setProductCode("QUICK_MSECURITY_PAY");
            request1.setBizModel(model);
            request1.setNotifyUrl(ALiPayConfig.notify_url);
            try {
                // 这里和普通的接口调用不同，使用的是sdkExecute
                AlipayTradeAppPayResponse response = alipayClient
                        .sdkExecute(request1);
                System.out.println(response.getBody());
                msg = response.getBody();
            } catch (AlipayApiException e) {
                e.getMessage();
                msg = "error";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            msg = "error";
        }
        return msg;
    }

    // 购买商品：支付后异步请求地址
    @RequestMapping("notify_url")
    public void doOrderSecPayNotify(HttpServletRequest request,
                                    HttpServletResponse response) throws Exception {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>进入回调方法");
        try {
            // 获取支付宝POST过来反馈信息
            Map<String, String> params = new HashMap<String, String>();
            @SuppressWarnings("rawtypes")
            Map requestParams = request.getParameterMap();
            for (@SuppressWarnings("rawtypes")
                 Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
                String name = (String) iter.next();
                String[] values = (String[]) requestParams.get(name);
                String valueStr = "";
                for (int i = 0; i < values.length; i++) {
                    valueStr = (i == values.length - 1) ? valueStr + values[i]
                            : valueStr + values[i] + ",";
                    valueStr = new String(valueStr.getBytes("utf-8"), "utf-8");
                    System.out.println(valueStr);
                }
                // 乱码解决，这段代码在出现乱码时使用。
                params.put(name, valueStr.trim());
            }
            /**
             * System.out.println("--->获取支付宝POST过来反馈信息");
             * 切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。 boolean
             * AlipaySignature.rsaCheckV1(Map<String, String> params, String
             * publicKey, String charset, String sign_type)
             */
            try {
                boolean flag = AlipaySignature.rsaCheckV1(params,
                        ALiPayConfig.ali_public_key,
                        ALiPayConfig.input_charset, "RSA2");
                // boolean flag = AlipaySignature.rsaCheckV1(params,
                // ALiPayConfig.ali_public_key,
                // "UTF-8",ALiPayConfig.pay_sign_type);
                // 商户订单号
                String out_trade_no = new String(request.getParameter(
                        "out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
                // 支付宝交易号
                String trade_no = new String(request.getParameter(
                        "trade_no").getBytes("ISO-8859-1"), "UTF-8");
                // 交易状态
                String trade_status = new String(request.getParameter(
                        "trade_status").getBytes("ISO-8859-1"), "UTF-8");
                // 支付金额
                String totalFee = new String(request.getParameter(
                        "buyer_pay_amount").getBytes("ISO-8859-1"), "UTF-8");
                // 买家账号
                String buyerEmail = new String(request.getParameter(
                        "buyer_logon_id").getBytes("ISO-8859-1"), "UTF-8");
                // 通用参数
                String PassbackParams = new String(request.getParameter(
                        "passback_params").getBytes("ISO-8859-1"), "UTF-8");
                // 商户订单号
                String body = new String(request.getParameter(
                        "body").getBytes("ISO-8859-1"), "UTF-8");
                log.info(">>>>>>>>>>>>>>>商户订单号:" + out_trade_no);
                System.out.println(">>>>>>>>>>>>>>>商户订单号:" + out_trade_no);
                System.out.println(">>>>>>>>>>>>>>>>>支付宝交易号" + trade_no);
                System.out.println(">>>>>>>>>>>>>>>>>>>>交易状态" + trade_status);
                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>支付金额" + totalFee);
                System.out.println(">>>>>>>>>>>>>>>>>>>>买家账号" + buyerEmail);
                if (flag) {// 验证
                    if (trade_status.equals("TRADE_FINISHED")
                            || trade_status.equals("TRADE_SUCCESS")) {
                        System.out.println("success");
                        /** 支付成功后的逻辑 */

                        System.out
                                .println(">>>>>>>>>>>>>>>>>>>>>验证确认支付成功！！！！！！！！！！！");
                    } else {
                        System.out.println("fail1");
                    }
                } else {// 验证失败
                    System.out.println("fail1");
                }
            } catch (AlipayApiException e1) {
                System.out.println("fail2");
            }
        } catch (UnsupportedEncodingException e) {
            System.out.println("fail3");
        }
    }
}





// 查询支付宝的支付状态
// @RequestMapping("carts/query")
// public ModelAndView QueryStatus( ){
// System.err.println("进入该方法");
// return new ModelAndView(new RedirectView("http://www.baidu.com"));
// }
//}