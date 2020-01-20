package com.example.netty.pay.controller;

import com.example.netty.pay.commons.WXpay.*;
import com.example.netty.util.Hutool;
import com.example.netty.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jdom2.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
@Api("微信支付")
@Controller
@CrossOrigin
@RequestMapping("/weixin")
public class WXpayController {

	private static final Logger logger = LoggerFactory.getLogger(WXpayController.class);
     @RequestMapping("/pay/wx")
	 @ResponseBody
	 public WxNotifyParam pay(HttpServletRequest request, HttpServletResponse response, String id, Double money){
     	return initWx(request, response, id, money);
	 }
	/**
	 * 初始化微信支付
	 *
	 * // * @param request // * @param response // * @param orderId // * @return
	 */
	// @RequestMapping("/pay/wx")
	// @ResponseBody
	public WxNotifyParam initWx(HttpServletRequest request, HttpServletResponse response, String id, Double money) {
		// Map<String, Object> map = new HashMap<String, Object>();
		// 获取生成预支付订单的请求类
		PrepayIdRequestHandler prepayReqHandler = new PrepayIdRequestHandler(request, response);
//		Integer sum = Integer.parseInt(orders.getTotalMoney()) - Integer.parseInt(orders.getCouponMoney());
		new DecimalFormat("#.00").format(money);
		String totalFee = String.valueOf((int) (money * 100));// 微信支付是
		// 分--* 100
		// 上线后，将此代码放开
		prepayReqHandler.setParameter("appid", ConstantUtil.APP_ID);
		prepayReqHandler.setParameter("body", ConstantUtil.BODY);
		prepayReqHandler.setParameter("mch_id", ConstantUtil.MCH_ID);
		String nonce_str = WXUtil.getNonceStr();
		prepayReqHandler.setParameter("nonce_str", nonce_str);
		prepayReqHandler.setParameter("notify_url", ConstantUtil.NOTIFY_URL);
		String out_trade_no = Hutool.getOrderIdByTime();
		prepayReqHandler.setParameter("out_trade_no", out_trade_no);
		prepayReqHandler.setParameter("spbill_create_ip", "192.168.0.88");
		String timestamp = WXUtil.getTimeStamp();
		prepayReqHandler.setParameter("time_start", timestamp);
		prepayReqHandler.setParameter("total_fee", totalFee);
		prepayReqHandler.setParameter("trade_type", "APP");
		prepayReqHandler.setParameter("attach", id);
		/**
		 * 注意签名（sign）的生成方式，具体见官方文档（传参都要参与生成签名，且参数名按照字典序排序，最后接上APP_KEY,转化成大写）
		 */
		prepayReqHandler.setParameter("sign", prepayReqHandler.createMD5Sign());
		prepayReqHandler.setGateUrl(ConstantUtil.GATEURL);
		String prepayid;
		WxNotifyParam param = new WxNotifyParam();
		Map<Object, Object> map = new HashMap<>();
		try {
			prepayid = prepayReqHandler.sendPrepay();
			// 若获取prepayid成功，将相关信息返回客户端
			if (prepayid != null && !prepayid.equals("")) {
				String signs = "appid=" + ConstantUtil.APP_ID + "&noncestr=" + nonce_str
						+ "&package=Sign=WXPay&partnerid=" + ConstantUtil.PARTNER_ID + "&prepayid=" + prepayid
						+ "&timestamp=" + timestamp + "&key=" + ConstantUtil.APP_KEY;
				/**
				 * 签名方式与上面类似
				 */
				param.setPrepayId(prepayid);
				param.setSign(Md5Util.MD5Encode(signs, "utf8").toUpperCase());
				param.setAppId(ConstantUtil.APP_ID);
				// 等于请求prepayId时的time_start
				param.setTimeStamp(timestamp);
				// 与请求prepayId时值一致
				param.setNonceStr(nonce_str);
				// 固定常量
				param.setPackages("Sign=WXPay");
				param.setPartnerId(ConstantUtil.PARTNER_ID);

//				map.put("appid", ConstantUtil.APP_ID);
//				map.put("noncestr", nonce_str);
//				map.put("package", "Sign=WXPay");
//				map.put("partnerid", ConstantUtil.PARTNER_ID);
//				map.put("prepayid", prepayid);
//				map.put("timestamp", timestamp);
//				map.put("sign", Md5Util.MD5Encode(signs, "utf8").toUpperCase());
				logger.info("-----------》创建微信支付成功: " + param);
				logger.info("-----------》获取微信订单Id成功: " + out_trade_no);
			} else {
				return null;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return null;
		}
		return param;
	}

	/**
	 * 接收微信支付回调通知
	 *
	 * @param request
	 * @param response
	 * @throws Exception
	 */

	@RequestMapping("/wx/notify_url")
	public String notifyWeiXinPay(HttpServletRequest request, HttpServletResponse response) throws IOException, JDOMException {
		System.out.println("微信支付回调");
		InputStream inStream = request.getInputStream();
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outSteam.write(buffer, 0, len);
		}
		String resultxml = new String(outSteam.toByteArray(), "utf-8");
		Map<String, String> params = XMLUtil.doXMLParse(resultxml);
		outSteam.close();
		inStream.close();


		Map<String, String> return_data = new HashMap<String, String>();
		if (!params.get("return_code").equals("SUCCESS")) {
			// 支付失败
			return_data.put("return_code", "FAIL");
			return_data.put("return_msg", "return_code不正确");
			return StringUtil.GetMapToXML(return_data);
		} else {
			System.out.println("===============付款成功==============");
			// ------------------------------
			// 处理业务开始
			// ------------------------------
			// 此处处理订单状态，结合自己的订单数据完成订单状态的更新
			// ------------------------------


				if (true){

				return_data.put("return_code", "SUCCESS");
				return_data.put("return_msg", "OK");
				return StringUtil.GetMapToXML(return_data);
			}
			return_data.put("return_code", "fail");
			return_data.put("return_msg", "fail");
			return StringUtil.GetMapToXML(return_data);
		}
	}
	public static void main(String args[]) {
		String notifyStr = XMLUtil.setXML("SUCCESS", "");
		System.out.println(notifyStr);
	}
}
