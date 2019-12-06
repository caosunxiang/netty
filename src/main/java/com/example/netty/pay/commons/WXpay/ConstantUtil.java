package com.example.netty.pay.commons.WXpay;

public class ConstantUtil {
	/**
	 * 微信开发平台应用ID
	 */
	public static final String APP_ID = "wxa0278095a91a6a42";
	/**
	 * 应用对应的AppSecret
	 */
	public static final String APP_SECRET = "02c8a53cf76748ecbe841f24774f92f0";
	/**
	 * APP_KEY 商户平台---api安全---密钥
	 */
	public static final String APP_KEY = "828guoguoyizhangguoguo0123456789";
	/**
	 * 微信支付商户号
	 */
	public static final String MCH_ID = "1516171911";
	/**
	 * 商品描述
	 */
	public static final String BODY = "商品支付";
	/**
	 * 商户号对应的密钥 也是 商户平台---api安全---密钥 同上面那个APP_KEY
	 */
	public static final String PARTNER_key = "828guoguoyizhangguoguo0123456789";

	/**
	 * 商户id 同微信支付商户号
	 */
	public static final String PARTNER_ID = "1516171911";
	/**
	 * 常量固定值
	 */
	public static final String GRANT_TYPE = "client_credential";
	/**
	 * 获取预支付id的接口url
	 */
	public static String GATEURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	/**
	 * 微信服务器回调通知url
	 */
	//测试
	//public static String NOTIFY_URL = "192.168.31.88:8080/works/order/wx/notify_url";
	//服务器
	public static String NOTIFY_URL = "http://7vtz88.natappfree.cc/weixin/wx/notify_url";

	//public static String NOTIFY_RECHARGE_URL = "http://47.100.110.119:8089/works/order/wx/notify_recharge_url";
}