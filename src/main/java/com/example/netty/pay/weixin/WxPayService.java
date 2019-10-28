package com.example.netty.pay.weixin;

import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayUtil;
import com.mj.courier_station.utils.Hutool;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class WxPayService {
	private static final Logger log = LoggerFactory
			.getLogger(WxPayService.class);

	public static void main(String[] args) throws Exception {
		prepay(Hutool.getId(), "商城购买", "商品", "1", "172.16.1.164", "good");
		// String openid = "oRa9J1bMlmX6GanAn_JMppSHsMyQ";
		// System.out.println(transferPay(Hutool.getId(), openid, "1",
		// "172.16.1.164"));
	}

	/**
	 * app支付，预下单
	 *
	 * @param orderId
	 * @param body
	 * @param subject
	 * @param money
	 * @param ip
	 * @return
	 * @throws Exception
	 */
	public static String prepay(String orderId, String body, String subject,
			String money, String ip, String bizType) throws Exception {

		EduWXConfig config = new EduWXConfig();
		WXPay wxpay = new WXPay(config);
		Map<String, String> data = new HashMap<String, String>();
		data.put("body", body);
		data.put("out_trade_no", orderId);
		data.put("device_info", subject);
		data.put("fee_type", "CNY");
		data.put("total_fee", money);// 分
		data.put("spbill_create_ip", ip);
		data.put("notify_url",
				"http://172.16.1.164:8082/airtime/PayAction/wxpayNotify");
		data.put("trade_type", "APP"); // JSAPI--公众号支付、NATIVE--原生扫码支付、APP--app支付
		data.put("attach", bizType);// 顾客在app中支付

		Map<String, String> resData = wxpay.unifiedOrder(data);
		log.info(resData.toString());
		String result = "";
		Map<String, Object> res = new LinkedHashMap<String, Object>();
		if ("SUCCESS".equals(resData.get("return_code"))
				&& "SUCCESS".equals(resData.get("result_code"))) {
			long timestamp = System.currentTimeMillis();
			String ts = String.valueOf(timestamp).substring(0, 10);

			String str = "appid=" + resData.get("appid") + "&noncestr="
					+ resData.get("nonce_str") + "&package=Sign=WXPay"
					+ "&partnerid=" + resData.get("mch_id") + "&prepayid="
					+ resData.get("prepay_id") + "&timestamp=" + ts + "&key="
					+ config.getKey();
			String newSign = MD5(str);

			res.put("appid", resData.get("appid"));
			res.put("noncestr", resData.get("nonce_str"));
			res.put("package", "Sign=WXPay");
			res.put("partnerid", resData.get("mch_id"));
			res.put("prepayid", resData.get("prepay_id"));
			res.put("timestamp", Long.valueOf(ts));
			res.put("sign", newSign);
			ObjectMapper json = new ObjectMapper();
			result = json.writeValueAsString(res);
		}
		log.info(resData.get("return_code"));
		log.info(result);
		return result;
	}

	/**
	 * 公众号支付，预下单
	 *
	 * @param orderId
	 * @param body
	 * @param subject
	 * @param money
	 * @param ip
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> gzhPrepay(String openId, String orderId,
			String body, String subject, String money, String ip, String bizType)
			throws Exception {

		EduGZHConfig config = new EduGZHConfig();
		WXPay wxpay = new WXPay(config);
		Map<String, String> data = new HashMap<String, String>();
		data.put("body", body);
		data.put("out_trade_no", orderId);
		data.put("device_info", subject);
		data.put("fee_type", "CNY");
		data.put("total_fee", money);// 分
		data.put("spbill_create_ip", ip);
		data.put("notify_url",
				"http://39.104.73.196/edu/PayAction_gzhpayNotify.xhtml");
		data.put("attach", bizType);// 顾客在app中支付
		data.put("openid", openId);

		data.put("trade_type", "JSAPI"); // JSAPI--公众号支付、NATIVE--原生扫码支付、APP--app支付

		Map<String, String> resData = wxpay.unifiedOrder(data);
		log.info(resData.toString());
		if ("SUCCESS".equals(resData.get("return_code"))
				&& "SUCCESS".equals(resData.get("result_code"))) {
			String prepay_id = resData.get("prepay_id");
			Map<String, String> cfg = new HashMap<String, String>();

			String nonceStr = WXPayUtil.generateNonceStr();
			String ts = Long.toString(System.currentTimeMillis() / 1000);
			String str = "appId=" + resData.get("appid") + "&nonceStr="
					+ nonceStr + "&package=prepay_id=" + prepay_id
					+ "&signType=MD5" + "&timeStamp=" + ts + "&key="
					+ config.getKey();
			String newSign = MD5(str);
			cfg.put("appId", resData.get("appid"));
			cfg.put("timeStamp", ts);
			cfg.put("nonceStr", nonceStr);
			cfg.put("package", "prepay_id=" + prepay_id);
			cfg.put("signType", "MD5");
			cfg.put("paySign", newSign);
			log.info(cfg.toString());
			return cfg;
		}
		return null;
	}

	// 企业付款
	public static final String TRANSFERS_PAY = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";
	// 企业付款查询
	public static final String TRANSFERS_PAY_QUERY = "https://api.mch.weixin.qq.com/mmpaymkttransfers/gettransferinfo";

	/**
	 * 企业付款到微信openId
	 *
	 * @param orderId
	 * @param openId
	 * @param amout
	 * @param ip
	 * @return
	 * @throws Exception
	 */
	public static String transferPay(String orderId, String openId,
			String amout, String ip) throws Exception {
		String restxml = null;
		EduWXConfig config = new EduWXConfig();

		Map<String, String> parm = new HashMap<String, String>();
		parm.put("mch_appid", config.getAppID()); // 公众账号appid
		parm.put("mchid", config.getMchID()); // 商户号
		parm.put("nonce_str", WXPayUtil.generateNonceStr()); // 随机字符串
		parm.put("partner_trade_no", orderId); // 商户订单号
		parm.put("openid", openId); // 用户openid
		parm.put("check_name", "NO_CHECK"); // 校验用户姓名选项 OPTION_CHECK
		// parm.put("re_user_name", "yy");
		// //check_name设置为FORCE_CHECK或OPTION_CHECK，则必填
		parm.put("amount", amout); // 转账金额
		parm.put("desc", "蚁创教育云平台提现"); // 企业付款描述信息
		parm.put("spbill_create_ip", ip); // Ip地址
		parm.put("sign", WXPayUtil.generateSignature(parm, config.getKey()));
		String params = WXPayUtil.mapToXml(parm);
		restxml = HttpUtils.posts(TRANSFERS_PAY, params);
		Map<String, String> res = WXPayUtil.xmlToMap(restxml);
		log.info(restxml);
		if ("SUCCESS".equals(res.get("result_code"))
				&& "SUCCESS".equals(res.get("return_code"))) {
			return res.get("payment_no");
		}
		return null;
	}

	private static String MD5(String data) throws Exception {
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] array = md.digest(data.getBytes("UTF-8"));
		StringBuilder sb = new StringBuilder();
		for (byte item : array) {
			sb.append(Integer.toHexString((item & 0xFF) | 0x100)
					.substring(1, 3));
		}
		return sb.toString().toUpperCase();
	}

}
