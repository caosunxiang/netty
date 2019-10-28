package com.example.netty.pay.weixin;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wxpay.sdk.WXPayConfig;

public class EduGZHConfig implements WXPayConfig {

	protected Logger log = LoggerFactory.getLogger(EduGZHConfig.class);

	@Override
	public String getAppID() {
		return "wxe6121400f18ba921";
	}

	@Override
	public InputStream getCertStream() {
		return EduGZHConfig.class
				.getResourceAsStream("/wx_client_cert/apiclient_cert.p12");
	}

	@Override
	public int getHttpConnectTimeoutMs() {
		return 8000;
	}

	@Override
	public int getHttpReadTimeoutMs() {
		return 10000;
	}

	@Override
	public String getKey() {
		return "hongdongxinxi12345yichuangjiaoyu";
	}

	@Override
	public String getMchID() {
		return "1508524861";
	}

	public static void main(String[] args) throws Exception {
		EduGZHConfig config = new EduGZHConfig();
		InputStream is = config.getCertStream();
		System.out.println(is.toString());
		System.out.println(config.getAppID());
	}

}
