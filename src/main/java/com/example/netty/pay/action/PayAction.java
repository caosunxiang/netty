package com.example.netty.pay.action;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alipay.api.internal.util.AlipaySignature;
import com.github.wxpay.sdk.WXPayUtil;
import com.mj.courier_station.pay.alipay.Configs;
import com.mj.courier_station.pay.weixin.EduGZHConfig;
import com.mj.courier_station.pay.weixin.EduWXConfig;
import org.springframework.web.bind.annotation.RestController;

/**
 * 支付后的回调业务控制
 *
 * @author think
 *
 */
@RestController
@RequestMapping("/PayAction")
public class PayAction {
	protected Logger log = LoggerFactory.getLogger(PayAction.class);

	/**
	 * 支付宝回调
	 */

	public void alipayNotify(HttpServletRequest request, HttpServletResponse response) {
		try {
			String result = "";
			log.debug("支付宝回调数据---------------------");

			Enumeration<?> paramNames = request.getParameterNames();

			Map<String, String> params = new HashMap<String, String>();

			while (paramNames.hasMoreElements()) {
				String paramName = (String) paramNames.nextElement();
				String[] paramValues = request.getParameterValues(paramName);
				if (paramValues.length == 1) {
					String paramValue = paramValues[0];
					if (paramValue.length() != 0) {
						log.debug(String.format("%s:%s", paramName, paramValue));
						params.put(paramName, paramValue);
					}
				}
			}
			log.debug("支付宝回调数据----end-----------------");
			// 异常请求
			if (params.size() <= 0) {
				result = "fail";
			}
			Configs.init(PayAction.class.getClassLoader().getResource("/").toString() + "zfbinfo.properties");

			boolean flag = AlipaySignature.rsaCheckV1(params, Configs.getAlipayPublicKey(), "utf-8",
					Configs.getSignType());

			if (flag) {
				String orderId = params.get("out_trade_no");// 订单号
				String trade_no = params.get("trade_no");// 支付宝流水号
				// String total_amount = params.get("total_amount");// 订单金额\
				String buyer_logon_id = params.get("buyer_logon_id");// 买家支付宝账号
				String trade_status = params.get("trade_status");// 交易状态
																	// TRADE_SUCCESS
																	// 支付成功
																	// TRADE_FINISHED
																	// 交易完成
				if (trade_status.equals("TRADE_SUCCESS")) {

				}
				result = "success";
			} else {
				log.debug("签名验证失败");
				result = "fail";
			}
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().print(result);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 微信支付回调
	 * <p>
	 * <xml> <appid><![CDATA[wx42a748e3ef3dda08]]></appid>
	 * <attach><![CDATA[APP_PAY]]></attach> <bank_type><![CDATA[CFT]]></bank_type>
	 * <cash_fee><![CDATA[1]]></cash_fee>
	 * <device_info><![CDATA[公羊篮球[篮球基础]-篮球20课时]]></device_info>
	 * <fee_type><![CDATA[CNY]]></fee_type>
	 * <is_subscribe><![CDATA[N]]></is_subscribe>
	 * <mch_id><![CDATA[1496848922]]></mch_id>
	 * <nonce_str><![CDATA[c2af478a877d4983878aaa4cbe064433]]></nonce_str>
	 * <openid><![CDATA[oRa9J1bMlmX6GanAn_JMppSHsMyQ]]></openid>
	 * <out_trade_no><![CDATA[61d517f159184f14b1929d35e05a02e8]]></out_trade_no>
	 * <result_code><![CDATA[SUCCESS]]></result_code>
	 * <return_code><![CDATA[SUCCESS]]></return_code>
	 * <sign><![CDATA[BA59A44AAD1008D0AB82E77B6B85B3B5]]></sign>
	 * <time_end><![CDATA[20180118151638]]></time_end> <total_fee>1</total_fee>
	 * <trade_type><![CDATA[APP]]></trade_type>
	 * <transaction_id><![CDATA[4200000090201801175824651938]]></transaction_id>
	 * </xml>
	 */
	@GetMapping("wxpayNotify")
	public void wxpayNotify(HttpServletRequest request, HttpServletResponse response) {
		BufferedReader bis = null;
		try {
			String return_code = "";
			String return_msg = "";

			log.debug("微信回调数据---------------------");
			bis = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
			String line = null;
			String result = "";
			while ((line = bis.readLine()) != null) {
				result += line + "\r\n";
			}
			log.debug(result);
			Map<String, String> params = WXPayUtil.xmlToMap(result);
			log.debug(params.toString());
			log.debug("微信回调数据----end-----------------");
			EduWXConfig config = new EduWXConfig();
			boolean signFlag = WXPayUtil.isSignatureValid(params, config.getKey());
			if (signFlag) {
				// 异常请求
				if (params.size() <= 0) {
					return_code = "FAIL";
					return_msg = "没有接收到数据";
				} else {
					String orderId = params.get("out_trade_no");// 订单号
					String trade_no = params.get("transaction_id");// 微信支付订单号
					// String total_amount = params.get("total_fee");// 订单金额\(分)
					String buyer_logon_id = params.get("openid");// 用户在商户appid下的唯一标识
					String trade_status = params.get("result_code");// 交易状态
					// TRADE_SUCCESS 支付成功 TRADE_FINISHED 交易完成

					if (trade_status.equals("SUCCESS")) {
						return_code = "SUCCESS";
						return_msg = "成功";
					} else {
						return_code = "FAIL";
						return_msg = "交易状态失败";
					}
				}
			} else {
				return_code = "FAIL";
				return_msg = "签名验证失败";
			}
			log.debug(return_msg);
			BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
			Map<String, String> map = new HashMap<String, String>();
			map.put("return_code", return_code);
			map.put("return_msg", return_msg);
			String resultStr = WXPayUtil.mapToXml(map);
			out.write(resultStr.getBytes());
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getLocalizedMessage(), e);
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
					log.error(e.getLocalizedMessage(), e);
				}
			}
		}
	}

	/**
	 * 微信公众号支付回调
	 * <p>
	 * <xml> <appid><![CDATA[wx42a748e3ef3dda08]]></appid>
	 * <attach><![CDATA[APP_PAY]]></attach> <bank_type><![CDATA[CFT]]></bank_type>
	 * <cash_fee><![CDATA[1]]></cash_fee>
	 * <device_info><![CDATA[公羊篮球[篮球基础]-篮球20课时]]></device_info>
	 * <fee_type><![CDATA[CNY]]></fee_type>
	 * <is_subscribe><![CDATA[N]]></is_subscribe>
	 * <mch_id><![CDATA[1496848922]]></mch_id>
	 * <nonce_str><![CDATA[c2af478a877d4983878aaa4cbe064433]]></nonce_str>
	 * <openid><![CDATA[oRa9J1bMlmX6GanAn_JMppSHsMyQ]]></openid>
	 * <out_trade_no><![CDATA[61d517f159184f14b1929d35e05a02e8]]></out_trade_no>
	 * <result_code><![CDATA[SUCCESS]]></result_code>
	 * <return_code><![CDATA[SUCCESS]]></return_code>
	 * <sign><![CDATA[BA59A44AAD1008D0AB82E77B6B85B3B5]]></sign>
	 * <time_end><![CDATA[20180118151638]]></time_end> <total_fee>1</total_fee>
	 * <trade_type><![CDATA[APP]]></trade_type>
	 * <transaction_id><![CDATA[4200000090201801175824651938]]></transaction_id>
	 * </xml>
	 */
	public void gzhpayNotify(HttpServletRequest request, HttpServletResponse response) {
		BufferedReader bis = null;
		try {
			String return_code = "";
			String return_msg = "";

			log.debug("微信回调数据---------------------");
			bis = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
			String line = null;
			String result = "";
			while ((line = bis.readLine()) != null) {
				result += line + "\r\n";
			}
			log.debug(result);
			Map<String, String> params = WXPayUtil.xmlToMap(result);
			log.debug(params.toString());
			log.debug("微信回调数据----end-----------------");
			EduGZHConfig config = new EduGZHConfig();
			boolean signFlag = WXPayUtil.isSignatureValid(params, config.getKey());
			if (signFlag) {
				// 异常请求
				if (params.size() <= 0) {
					return_code = "FAIL";
					return_msg = "没有接收到数据";
				} else {
					String orderId = params.get("out_trade_no");// 订单号
					String trade_no = params.get("transaction_id");// 微信支付订单号
					// String total_amount = params.get("total_fee");// 订单金额\(分)
					String buyer_logon_id = params.get("openid");// 用户在商户appid下的唯一标识
					String trade_status = params.get("result_code");// 交易状态
					// TRADE_SUCCESS 支付成功 TRADE_FINISHED 交易完成

					if (trade_status.equals("SUCCESS")) {

						return_code = "SUCCESS";
						return_msg = "成功";
					} else {
						return_code = "FAIL";
						return_msg = "交易状态失败";
					}
				}
			} else {
				return_code = "FAIL";
				return_msg = "签名验证失败";
			}
			log.debug(return_msg);
			BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
			Map<String, String> map = new HashMap<String, String>();
			map.put("return_code", return_code);
			map.put("return_msg", return_msg);
			String resultStr = WXPayUtil.mapToXml(map);
			out.write(resultStr.getBytes());
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getLocalizedMessage(), e);
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
					log.error(e.getLocalizedMessage(), e);
				}
			}
		}
	}

	public static void main(String[] args) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("return_code", "1");
		map.put("return_msg", "2");
		System.out.println(WXPayUtil.mapToXml(map));

	}

}
