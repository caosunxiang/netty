package com.example.netty.pay.controller;

import com.example.netty.pay.commons.WXpay.*;
import com.example.netty.pay.service.WXAppPayService;
import com.example.netty.util.Hutool;
import com.github.wxpay.sdk.WXPayUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.jdom2.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/***
 * @Author: 曹孙翔
 * @Description:微信支付
 * @Date: 13:44 2019/12/2
 * @Param:
 * @return:
 **/
@RestController
@Slf4j
@Api(tags = "微信支付")
public class WXpayController {

    private static final Logger logger = LoggerFactory
            .getLogger(WXpayController.class);

    @Autowired
    private WXAppPayService wxAppPayService;

    private ExecutorService executorService = Executors.newFixedThreadPool(20);


    /***
     * @Author: 曹孙翔
     * @Description:支付接口
     * @Date: 18:50 2019/12/5
     * @Param: [request, orderId, money]
     * @return: java.lang.String
     **/
    @RequestMapping("/WXpay")
    public String pay(HttpServletRequest request, String orderId, Integer money) {
        return wxAppPayService.pay(money, orderId, request);
    }

    /***
     * @Author: 曹孙翔
     * @Description:支付回调
     * @Date: 15:09 2019/12/2
     * @Param: [request, response, orderId, money]
     * @return: java.lang.String
     **/
    @RequestMapping("/wxPayCallback")
    public String initWx(HttpServletRequest request) {
        try {
            log.info("微信支付回调");
            //读取参数
            //解析xml成map
            Map<String, String> map = WXPayUtil.xmlToMap(getParam(request));
            log.info("微信支付回调返回消息{}", map);
            check(map);//该处读者自行校验（验证订单号，付款金额等是否正确）
            String orderNo = map.get("out_trade_no");
            String resultCode = map.get("result_code");
            //另起线程处理业务
            log.info("另起线程处理业务");
            executorService.execute(() -> {
                //支付成功
                if (resultCode.equals("SUCCESS")) {
                    log.info("支付成功");
                    // TODO 自己的业务逻辑
                } else {
                    log.info("支付失败");
                    // TODO 自己的业务逻辑
                }
            });
            if (resultCode.equals("SUCCESS")) {
                return XMLUtil.setXML("SUCCESS", "OK");
            } else {
                return XMLUtil.setXML("fail", "付款失败");
            }
        } catch (ServiceException e) {
            log.info("微信支付回调发生异常{}", e.getMessage());
            return XMLUtil.setXML("fail", "付款失败");
        } catch (Exception e) {
            log.info("微信支付回调发生异常{}", e.getLocalizedMessage());
            return XMLUtil.setXML("fail", "付款失败");
        }
    }


    /**
     * 接收微信支付回调通知
     *
     * @param request
     * @param response
     * @throws Exception
     */

    @RequestMapping("/wx/notify_url")
    public String getTenPayNotif(HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>进入微信回调方法");
        PrintWriter writer = response.getWriter();
        InputStream inStream = request.getInputStream();
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        outSteam.close();
        inStream.close();
        String result = new String(outSteam.toByteArray(), "utf-8");
        logger.info("微信回调支付通知结果：" + result);
        Map<String, String> map = null;
        try {
            /**
             * 解析微信通知返回的信息
             */
            map = XMLUtil.doXMLParse(result);
        } catch (JDOMException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        logger.info("- - - - - - - - -");
        logger.info("= = = = = = = = =:" + map);
        // 若支付成功，则告知微信服务器收到通知
        if (map.get("return_code").equals("SUCCESS")) {
            if (map.get("result_code").equals("SUCCESS")) {
                logger.info("微信充值成功！");

                if (1 == 1) {

                    return "SUCCESS";
                }

            } else {

                logger.error("微信支付回调失败");
                return "fail";
            }
            return "fail";
        }
        return "fail";
    }

    public static void main(String args[]) {
        String notifyStr = XMLUtil.setXML("SUCCESS", "OK");
        System.out.println(notifyStr);
    }

    public String getParam(HttpServletRequest request) throws IOException {
        //读取参数
        InputStream inputStream;
        StringBuilder sb = new StringBuilder();
        inputStream = request.getInputStream();
        String s;
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        while ((s = in.readLine()) != null) {
            sb.append(s);
        }
        in.close();
        inputStream.close();
        return sb.toString();
    }

    private void check(Map<String, String> params) throws ServiceException {
        String outTradeNo = params.get("out_trade_no");
        // 1、商户需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
        //OrderInfo order = orderService.selectOrderByOrderNo(outTradeNo);
        //if (order == null) {
        //    throw new ServiceException("out_trade_no错误");
        //}
        //
        // 2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
        //Integer totalAmount = Integer.valueOf(params.get("total_fee"));
        //log.info("totalAmount{}", totalAmount);
        //if (!totalAmount.equals(order.getSnapshotTotalFee())) {
        //    throw new ServiceException("total_amount错误");
        //}
    }
}
