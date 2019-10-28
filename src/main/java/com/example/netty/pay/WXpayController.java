package com.example.netty.pay;

import com.example.netty.pay.WXpay.*;
import com.example.netty.util.Hutool;
import org.jdom2.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Map;


@RestController
@RequestMapping("/weixin")
public class WXpayController {

	private static final Logger logger = LoggerFactory
			.getLogger(WXpayController.class);

	/**
	 * 初始化微信支付
	 *
	 * // * @param request // * @param response // * @param orderId // * @return
	 */
	 @RequestMapping("/pay/wx")
	public WxNotifyParam initWx(HttpServletRequest request,
								HttpServletResponse response, Integer uid, Double money) {
		// Map<String, Object> map = new HashMap<String, Object>();
		// 获取生成预支付订单的请求类
		PrepayIdRequestHandler prepayReqHandler = new PrepayIdRequestHandler(
				request, response);
		String totalFee = String.valueOf((int) (money * 100));// 微信支付是
																			// 分--
		// 上线后，将此代码放开
		prepayReqHandler.setParameter("appid", ConstantUtil.APP_ID);
		prepayReqHandler.setParameter("body", ConstantUtil.BODY);
		prepayReqHandler.setParameter("mch_id", ConstantUtil.MCH_ID);
		String nonce_str = WXUtil.getNonceStr();
		prepayReqHandler.setParameter("nonce_str", nonce_str);
		prepayReqHandler.setParameter("notify_url", ConstantUtil.NOTIFY_URL);
		// String out_trade_no = userOrder.getOrderNo();
		String out_trade_no = Hutool.getId();
		prepayReqHandler.setParameter("out_trade_no", out_trade_no);
		// request.getRemoteAddr()
		// "14.23.150.211"
		prepayReqHandler.setParameter("spbill_create_ip", "192.168.0.88");
		String timestamp = WXUtil.getTimeStamp();
		prepayReqHandler.setParameter("time_start", timestamp);
		// logger.info("金额" + totalFee);
		prepayReqHandler.setParameter("total_fee", totalFee);
		prepayReqHandler.setParameter("trade_type", "APP");
		prepayReqHandler.setParameter("attach",uid+"");
		/**
		 * 注意签名（sign）的生成方式，具体见官方文档（传参都要参与生成签名，且参数名按照字典序排序，最后接上APP_KEY,转化成大写）
		 */
		prepayReqHandler.setParameter("sign", prepayReqHandler.createMD5Sign());
		prepayReqHandler.setGateUrl(ConstantUtil.GATEURL);
		String prepayid;
		WxNotifyParam param = new WxNotifyParam();
		try {
			prepayid = prepayReqHandler.sendPrepay();
			// 若获取prepayid成功，将相关信息返回客户端
			if (prepayid != null && !prepayid.equals("")) {
				String signs = "appid=" + ConstantUtil.APP_ID + "&noncestr="
						+ nonce_str + "&package=Sign=WXPay&partnerid="
						+ ConstantUtil.PARTNER_ID + "&prepayid=" + prepayid
						+ "&timestamp=" + timestamp + "&key="
						+ ConstantUtil.APP_KEY;
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
				logger.info("-----------》创建微信订单成功: " + param);
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
	 * 初始化微信支付
	 *
	 * // * @param request // * @param response // * @param orderId // * @return
	 */
 //@RequestMapping("/pay/wx")
	//public WxNotifyParam initWxgwc(HttpServletRequest request,
	//		HttpServletResponse response, Object pe) {
	//	// Map<String, Object> map = new HashMap<String, Object>();
	//	// 获取生成预支付订单的请求类
	//	PrepayIdRequestHandler prepayReqHandler = new PrepayIdRequestHandler(
	//			request, response);
	//	String totalFee = String.valueOf((int) (1 * 100));// 微信支付是
	//	// 分--
	//	// 上线后，将此代码放开
	//	prepayReqHandler.setParameter("appid", ConstantUtil.APP_ID);
	//	prepayReqHandler.setParameter("body", ConstantUtil.BODY);
	//	prepayReqHandler.setParameter("mch_id", ConstantUtil.MCH_ID);
	//	String nonce_str = WXUtil.getNonceStr();
	//	prepayReqHandler.setParameter("nonce_str", nonce_str);
	//	prepayReqHandler.setParameter("notify_url", ConstantUtil.NOTIFY_URL);
	//	// String out_trade_no = userOrder.getOrderNo();
	//	String out_trade_no = IDUtils.Random() + "";
	//	prepayReqHandler.setParameter("out_trade_no", out_trade_no);
	//	// request.getRemoteAddr()
	//	// "14.23.150.211"
	//	prepayReqHandler.setParameter("spbill_create_ip", "196.168.0.88");
	//	String timestamp = WXUtil.getTimeStamp();
	//	prepayReqHandler.setParameter("time_start", timestamp);
	//	// logger.info("金额" + totalFee);
	//	prepayReqHandler.setParameter("total_fee", totalFee);
	//	prepayReqHandler.setParameter("trade_type", "APP");
	//	/**
	//	 * 注意签名（sign）的生成方式，具体见官方文档（传参都要参与生成签名，且参数名按照字典序排序，最后接上APP_KEY,转化成大写）
	//	 */
	//	prepayReqHandler.setParameter("sign", prepayReqHandler.createMD5Sign());
	//	prepayReqHandler.setGateUrl(ConstantUtil.GATEURL);
	//	String prepayid;
	//	WxNotifyParam param = new WxNotifyParam();
	//	try {
	//		prepayid = prepayReqHandler.sendPrepay();
	//		// 若获取prepayid成功，将相关信息返回客户端
	//		if (prepayid != null && !prepayid.equals("")) {
	//			String signs = "appid=" + ConstantUtil.APP_ID + "&noncestr="
	//					+ nonce_str + "&package=Sign=WXPay&partnerid="
	//					+ ConstantUtil.PARTNER_ID + "&prepayid=" + prepayid
	//					+ "&timestamp=" + timestamp + "&key="
	//					+ ConstantUtil.APP_KEY;
	//			/**
	//			 * 签名方式与上面类似
	//			 */
	//			param.setPrepayId(prepayid);
	//			param.setSign(Md5Util.MD5Encode(signs, "utf8").toUpperCase());
	//			param.setAppId(ConstantUtil.APP_ID);
	//			// 等于请求prepayId时的time_start
	//			param.setTimeStamp(timestamp);
	//			// 与请求prepayId时值一致
	//			param.setNonceStr(nonce_str);
	//			// 固定常量
	//			param.setPackages("Sign=WXPay");
	//			param.setPartnerId(ConstantUtil.PARTNER_ID);
	//			logger.info("-----------》创建微信订单成功: " + param);
	//		} else {
	//			return null;
	//		}
	//	} catch (Exception e) {
	//		// TODO Auto-generated catch block
	//		return null;
	//	}
	//	return param;
	//}

	/**
	 * 打赏
	 *
	 * // * @param request // * @param response // * @param orderId // * @return
	 */

	public WxNotifyParam initWxdashang(HttpServletRequest request,
			HttpServletResponse response, String logid, String money) {
		// Map<String, Object> map = new HashMap<String, Object>();
		// 获取生成预支付订单的请求类
		PrepayIdRequestHandler prepayReqHandler = new PrepayIdRequestHandler(
				request, response);
		String totalFee = String
				.valueOf((int) (Double.parseDouble(money) * 100));// 微信支付是
		// 分--
		// 上线后，将此代码放开
		prepayReqHandler.setParameter("appid", ConstantUtil.APP_ID);
		prepayReqHandler.setParameter("body", ConstantUtil.BODY);
		prepayReqHandler.setParameter("mch_id", ConstantUtil.MCH_ID);
		String nonce_str = WXUtil.getNonceStr();
		prepayReqHandler.setParameter("nonce_str", nonce_str);
		prepayReqHandler.setParameter("notify_url", ConstantUtil.NOTIFY_URL);
		// String out_trade_no = userOrder.getOrderNo();
		String out_trade_no = logid + "";
		prepayReqHandler.setParameter("out_trade_no", out_trade_no);
		// request.getRemoteAddr()
		// "14.23.150.211"
		prepayReqHandler.setParameter("spbill_create_ip", "47.100.110.119");
		String timestamp = WXUtil.getTimeStamp();
		prepayReqHandler.setParameter("time_start", timestamp);
		// logger.info("金额" + totalFee);
		prepayReqHandler.setParameter("total_fee", totalFee);
		prepayReqHandler.setParameter("trade_type", "APP");
		/**
		 * 注意签名（sign）的生成方式，具体见官方文档（传参都要参与生成签名，且参数名按照字典序排序，最后接上APP_KEY,转化成大写）
		 */
		prepayReqHandler.setParameter("sign", prepayReqHandler.createMD5Sign());
		prepayReqHandler.setGateUrl(ConstantUtil.GATEURL);
		String prepayid;
		WxNotifyParam param = new WxNotifyParam();
		try {
			prepayid = prepayReqHandler.sendPrepay();
			// 若获取prepayid成功，将相关信息返回客户端
			if (prepayid != null && !prepayid.equals("")) {
				String signs = "appid=" + ConstantUtil.APP_ID + "&noncestr="
						+ nonce_str + "&package=Sign=WXPay&partnerid="
						+ ConstantUtil.PARTNER_ID + "&prepayid=" + prepayid
						+ "&timestamp=" + timestamp + "&key="
						+ ConstantUtil.APP_KEY;
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
				logger.info("-----------》创建微信订单成功: " + param);
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

				if (1== 1) {

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
}
