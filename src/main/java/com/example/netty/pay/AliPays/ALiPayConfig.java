package com.example.netty.pay.AliPays;

/**
 * 支付宝支付所需的必要参数 理想情况下只需配置此处
 */
public class ALiPayConfig {
	// 合作身份者ID，以2088开头由16位纯数字组成的字符串
	public static String partner = "2088621598179204";

	// appid
	public static String appid = "2019101468355457";

	// 商户支付宝账号
	public static String seller_email = "fuwu@tdzhe.com";

	// 商户真实姓名
	public static String account_name = "杭州凤梨网络科技有限公司";

	// URL
	public static String url = "https://openapi.alipay.com/gateway.do";
	public static String timeoutExpress = "1000";

	// 商户的私钥RSA
	public static String private_key = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCCa9ZvW5Iu2/rRQUB27BLBbXBVkVLoO6gBSvvNHwrKEjAGZgGwDvCLfSagaX86e8t4QFeTBd37QXT3jVDrYOK3gjuV6cxGG1xQGEYNYN3IlpUd45wYf0rF0j60hHx1VvrhhUmD9U0RXttb/WuHCXY2RTo041fkpCM+oi8/64xIoZ2C33r401ZIiOjQ6rQYig3lA/SslQEWbM+FHfQtdpiR3Fc84OdGYQVKD+fNIUn3QHWI0PEM7W3LPAUhsxl1Z02j3l29WOrYgkv3lTXqhnpQtepuK1ruWKol4A0X3wETb5IemKV+zANZcbRKqVbhcRZ84ulUVQeapuaP9eI/v80vAgMBAAECggEANzg5XoTC7BwvohGinbduaX9QpKoXFETN6V46JqsSAmSY+NyPsl9g8W5XwGtqdvSbPZrgeVtulUctovKuZDptQ41cdSi8WKKU/0SHxHpS7O70y+bm3Gho3EEcU4oUuAnPx1SLv9dIfiZPci9mK5KR/oBpGKN5P1FPquE4aDPu/YD1VRoe62GyJYi5nGZ2ys6iwXmqXoVj9vwjyTj+JD+kFWbhvAqN022JjTkUki8fKpiHfpU9O3r/lsj+6w2uzp2M4i3inczw8HRzxuxu3tmbE8L07uxwLpNPtBsXg7LCj1GjH8JRFic06t6qP0ToJUu4X/4JaA2vSiZ80VcDQj2yIQKBgQC5Oq3e5uxsVdRWJIUkxr0Y7UZ42jC8IL9be8WQpsU3Wks5Nk77jQ4j9/c5m7fYZ9zuAkfg6tBGOdtxx1LlO5Qg3hEecVnQwZb2BBjR9njbKo+WYjpi+x2IkAW0cjbKoFBh2XGFhCrO8wGerfiesvaHzkwxlOhZmuVwH72Y2nNZ5QKBgQC0QGASLzKgOMuutbIa82zPB6RHrOZeTxgKxmUgWadzGwzKAZXaLSt+uc7bVTYPtoJl/QalxUMsfxrGdsk24VOtfqm9Gp+k3vXsyB9NmKz0FqGnUhURiIwmf7jN8BIRdAF1ah3tmfCOUKG1m6oxY7HltAc5S/+ivrcaLlk8blTJgwKBgDirtlSTTFRc8ikk5UN6NpKwzbXd87krP8CqnqiCDabAznEDp6iY4HGqxQ3wEZB5PoPnWLx8uxoxS22FMVyHHOvtZUluHq+a+yfr2vgqM5HCdMnq5bgw4jlM7yHlK9RUtiMp1uyloj9M4/r7eSx8zqaAPJmS2H/Jv1bNr0Fb3JztAoGAdPOX4fSRqndvYIovZ2Et5h4VLxQ13FcBSGx1TMoe5YZVg/QHYKoSIIU6fAELtKI8dSP43jHhZcuKed60q1ED0/vs8e+GSZkIg3tNJDG9dhoBBOkzFk88b/ZK2AO+FWjpUV9qz7Ej7ZLPeV/oy2beFCX80eZHlTac4vXTnGwRwzUCgYABCeygikz28GnLNrtmfSNSjT9gB62ev+lv1WKTNbCBxY2TBkxDFDsYCrNOjvVs/Yr+0818bINW8SPsegnOUqUrTgn6oDPXrLAJ2YdAOYvm17FyXIULrdfXoca2/Y9r/iNlTomLNIXFtKzmbZ/mhqTIGcJQANHpQlr8ux9c1HhnMg==";	// 支付宝的公钥 RSA
	//支付宝公钥
    public static String ali_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnoVkXmCCHH2np9E9s/KSQSdEb/dZXCTOsMYLN/ub5QsQ+dM9lSu3oHpeD0/7sKI2vekB/IQggHYOk4bGHAVCEI2PMSHQqxVuyRlPEkWJIyIRJ6124wStFEmFg/DvCP1HKdj2b1iuILa2Klu6RSpPwhXbIrK5dg1G9Ni78MLKntbQE2GNeyIcMidiD2APTnpw9Lr9tQvW7eowkl3fndQLMtfSzmfBqWEteMqC8nEMLLiGwXOXi25qXl6AeDpMLNMFbgy862A7zIKJEpcTAcchzCBFo6qFQmV05hKyTYMOIYsuUnDK9mjPHvQAtNqgsslZmxEpKiHeaXaZJ5VMBSp1swIDAQAB";
	// 签名方式 (支付回调签名方式)
	public static String pay_sign_type = "RSA2";

	/**
	 * 支付宝服务器主动通知商户服务器里指定的页面http/https路径。建议商户使用https
	 * 这里需要测试的话，需要用外网测试。https://www.ngrok.cc/ 这里有免费和付费的，实际上，免费用一下就可以了。
	 */
	public static String notify_url = "http://8fmpiy.natappfree.cc/notify_url";
	// 商品的标题/交易标题/订单标题/订单关键字等。
	public static String subject = "短信费充值";

	// ↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

	// 接口名称 固定为：alipay.trade.app.pay
	public static String method = "alipay.trade.app.pay";

	// 调用的接口版本，固定为：1.0
	public static String version = "1.0";

	// 销售产品码，商家和支付宝签约的产品码，为固定值QUICK_MSECURITY_PAY
	public static String product_code = "QUICK_MSECURITY_PAY";

	// 字符编码格式 目前支持 gbk 或 utf-8
	public static String input_charset = "utf-8";

}
