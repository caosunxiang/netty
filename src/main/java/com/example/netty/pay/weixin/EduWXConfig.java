package com.example.netty.pay.weixin;

import com.github.wxpay.sdk.WXPayConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

public class EduWXConfig implements WXPayConfig {

	protected Logger log = LoggerFactory.getLogger(EduWXConfig.class);

	@Override
	public String getAppID() {
		return "wx42a748e3ef3dda08";
	}

	@Override
	public InputStream getCertStream() {
		return EduWXConfig.class.getResourceAsStream("/wx_client_cert/apiclient_cert.p12");
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
		return "1496848922";
	}

	public static void main(String[] args) throws Exception {
		EduWXConfig config = new EduWXConfig();
		InputStream is = config.getCertStream();
		System.out.println(is.toString());
		System.out.println(config.getAppID());
	}

}
