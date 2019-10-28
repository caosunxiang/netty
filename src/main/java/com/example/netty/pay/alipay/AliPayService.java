package com.example.netty.pay.alipay;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayFundTransToaccountTransferModel;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayFundTransToaccountTransferRequest;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.mj.courier_station.utils.Hutool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AliPayService {
	private static final Logger log = LoggerFactory
			.getLogger(AliPayService.class);

	public static void main(String[] args) throws Exception {
		// prepay(UUID.getUUID(), "我是测试数据", "App支付测试Java", "0.01");

		System.out.println(payToAccount("15190740025", "1", Hutool.getId()));
	}

	/**
	 * 预下单
	 * 
	 * @param orderId
	 * @param body
	 * @param subject
	 * @param money
	 * @return
	 * @throws Exception
	 */
	public static String prepay(String orderId, String body, String subject,
			String money, String bizType) throws Exception {

		Configs.init(AliPayService.class.getClassLoader().getResource("")
				.toString()
				+ "zfbinfo.properties");

		AlipayClient alipayClient = new DefaultAlipayClient(
				Configs.getOpenApiDomain(), Configs.getAppid(),
				Configs.getPrivateKey(), "json", "utf-8",
				Configs.getAlipayPublicKey(), Configs.getSignType());
		// ,"9OZJ1rtpC9TmfPikzCXdVQ==", "AES");

		AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
		AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
		model.setOutTradeNo(orderId);
		model.setBody(body);
		model.setSubject(subject);
		model.setTimeoutExpress("30m");
		model.setTotalAmount(money);
		model.setPassbackParams(bizType);
		model.setProductCode("QUICK_MSECURITY_PAY");
		request.setNotifyUrl("http://39.104.73.196/edu/PayAction_alipayNotify.xhtml");
		request.setBizModel(model);
		// request.setNeedEncrypt(true);
		AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
		log.info(response.getBody());
		return response.getBody();

	}

	/**
	 * 机构从平台提现，付款到机构账户
	 * 
	 * @param deliverAccount
	 * @param money
	 * @param orderNo
	 * @return
	 * @throws Exception
	 */
	public static String payToAccount(String account, String money,
			String orderNo) throws Exception {
		Configs.init(AliPayService.class.getClassLoader().getResource("")
				.toString()
				+ "zfbinfo.properties");

		AlipayClient alipayClient = new DefaultAlipayClient(
				Configs.getOpenApiDomain(), Configs.getAppid(),
				Configs.getPrivateKey(), "json", "utf-8",
				Configs.getAlipayPublicKey(), Configs.getSignType());

		AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();

		AlipayFundTransToaccountTransferModel model = new AlipayFundTransToaccountTransferModel();
		model.setAmount(money);// 单位（元）
		model.setOutBizNo(orderNo);// 订单号
		model.setPayeeAccount(account);// 账号
		// model.setPayeeRealName("");
		model.setPayeeType("ALIPAY_LOGONID");
		model.setPayerShowName("蚁创教育");
		model.setRemark("蚁创教育云平台提现");
		request.setBizModel(model);
		AlipayFundTransToaccountTransferResponse response = alipayClient
				.execute(request);
		if (response == null || !response.isSuccess()) {
			log.info(orderNo + "提现失败");
			return null;
		}
		log.info(response.getBody());
		log.info(orderNo + "提现成功");
		log.info(response.getOrderId());
		return response.getOrderId();
	}

}
