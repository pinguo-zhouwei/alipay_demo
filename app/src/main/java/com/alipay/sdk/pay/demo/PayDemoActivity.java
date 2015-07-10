package com.alipay.sdk.pay.demo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;

public class PayDemoActivity extends FragmentActivity {

	//商户PID
	public static final String PARTNER = "2088901408530365";
	//商户收款账号
	public static final String SELLER = "2088901408530365";
	//商户私钥，pkcs8格式
	public static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKyzAlEDI/KACtXQ" +
			"H6F5/zR/L6IFh0REDtmWQ5+nXS1m57q6aBEkhbi0kM3iJnwdfoHNcbAyaj9a7G2X" +
			"OBsSVfBOpYMBcrf/uyvpJEKr7W2b06DxyJZOvZrCa+e2iEHK7qFW/wGpqdpnzXS6" +
			"ZfqRZyZDVCnG8mfQDM92+pobkzxdAgMBAAECgYBGQhVBLAvlzh2aWq9jnCQtEUkp" +
			"myIqrZ10++LLAuWvYNwJZWiNANS+DgeTGtwZRk5/NDt6OShsWesxsoGK0ezPyTZ6" +
			"tFlG5WhUBkURpzoHaPjtaZfxTH7z8ccTD6ENJuQfU1vknzAGAhPgog3FtmQ47397" +
			"1XdCrtfXxhYb72xiKQJBAOMv2nxZdFptpc+x6oZPaIwpJZ6tsfIdbgiMtKCu8LtT" +
			"OoH16/r/GQpOcxMnI3fj9//9As0jeexY4XfVCHUrAcsCQQDCmhZ9Vzi2Yc7a7eFa" +
			"JDnXdFgBMJBmiD47he8s7iURDePZ3O+09aMPtFPOUgbRhQfb6DiJq1z622fmeLgj" +
			"V1V3AkBzhOWnzSSoxpBFZ8ZEvSQAAPwsD04oOma02jcObWXjGpLpek6ftg7ZesJw" +
			"vrn+QGDePZyrCpr4Xslm0WAXk+K5AkEAgaaFW0aUGza4yTV5UUQXYUs7IrfHTPhy" +
			"dde6zLVw7pbReiHvdiG9XMYom6ocrtSxsrpZiKcp0FwDMhK1o54Y6wJAcWra/CHr" +
			"J+Pe/qT0K6vg+6lHGEkLs4lsm3zO2VIV6X+QrYEoJEYFUSiX6aFOGqNXkmK2DZ6S" +
			"TRhvoHhr8GH7Ag==";
	//支付宝公钥
	//public static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAM0dmJUMRhjRFtBZyTyt4vNDn/oAvO4IgZK5FpBocYqWKQtin++YeFxwIx5nBfK7RIqJTENWB8Hpg/4bObcAe5LgCLYsGGzUscsmxR6nZde+Ahwr4aUd7vV5xUsL1q9EC+531KElareq84SkOpTDTBsBimb4JfnJhMNliK6rFzIDAgMBAAECgYEAlr39eOyAR5TCk3So9DNG60jztSP9tWIC9/WWo6Jeb19T4s4khE0lh6nR5XIJaQ4/nSCyL3p2UQhwvLr9e4SbS1D4cv/mP+M2wkgBk5sEEY0UYUJqqeLGi5k/KNhl6HCa9yVolBVPYPrW8RSnkEZ7T1h8UDw/2hoKkSJNzKxztuECQQDqx2D8GpORuE5ghndnN8xpNSUaJAUh+dEODTWyqK/6uxbVV9Olq8EzXIHVMQ5Ca4D6cPTQW894pXe38JiEtEt1AkEA36fUqiuPb8yekG9HUb4wUZntUSlqQDO+DTjORrX0c4rth/+G3Gck2J/FUzC0M/rI/mJjNMePGlBSZFBsEt3wlwJAW5DXKnvTAgo3xz2qY/9HIeEwTzmtJssf1bMhNkTEHa3KAfr2+K4786QontU7EnyLGSHk2T51dEBUqNCVK2yF4QJARLNF9PuqdLoS3uT/sFFOgFJa/npNQ+lg8KjJXBkaPL+pFQRdxaf9PnldYJ9uSyU2xkUsmhLOl1Oi62N0vxrjgwJAJIL2eukSPbeXUw1VELqia9c6F6jiZja7I8W/VOQi3lC40Scir07s/P/tcH9kSuWwuX3XQAJcn8lVxJHxYlGLaQ==";


	private static final int SDK_PAY_FLAG = 1;

	private static final int SDK_CHECK_FLAG = 2;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);
				
				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
				String resultInfo = payResult.getResult();
				
				String resultStatus = payResult.getResultStatus();

				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					Toast.makeText(PayDemoActivity.this, "支付成功",
							Toast.LENGTH_SHORT).show();
				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(PayDemoActivity.this, "支付结果确认中",
								Toast.LENGTH_SHORT).show();

					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						Toast.makeText(PayDemoActivity.this, "支付失败",
								Toast.LENGTH_SHORT).show();

					}
				}
				break;
			}
			case SDK_CHECK_FLAG: {
				Toast.makeText(PayDemoActivity.this, "检查结果为：" + msg.obj,
						Toast.LENGTH_SHORT).show();
				break;
			}
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay_main);
	}

	/**
	 * call alipay sdk pay. 调用SDK支付
	 * 
	 */
	public void pay(View v) {
		// 订单
		String orderInfo = getOrderInfo("测试的商品", "该测试商品的详细描述", "0.01");

		// 对订单做RSA 签名
		String sign = sign(orderInfo);
		try {
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// 完整的符合支付宝参数规范的订单信息
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
				+ getSignType();

		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask(PayDemoActivity.this);
				// 调用支付接口，获取支付结果
				String result = alipay.pay(payInfo);

				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	/**
	 * check whether the device has authentication alipay account.
	 * 查询终端设备是否存在支付宝认证账户
	 * 
	 */
	public void check(View v) {
		Runnable checkRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask payTask = new PayTask(PayDemoActivity.this);
				// 调用查询接口，获取查询结果
				boolean isExist = payTask.checkAccountIfExist();

				Message msg = new Message();
				msg.what = SDK_CHECK_FLAG;
				msg.obj = isExist;
				mHandler.sendMessage(msg);
			}
		};

		Thread checkThread = new Thread(checkRunnable);
		checkThread.start();

	}

	/**
	 * get the sdk version. 获取SDK版本号
	 * 
	 */
	public void getSDKVersion() {
		PayTask payTask = new PayTask(this);
		String version = payTask.getVersion();
		Toast.makeText(this, version, Toast.LENGTH_SHORT).show();
	}

	/**
	 * create the order info. 创建订单信息
	 * 
	 */
	public String getOrderInfo(String subject, String body, String price) {
		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + PARTNER + "\"";

		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm"
				+ "\"";

		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";
		return orderInfo;
	}

	/**
	 * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
	 * 
	 */
	public String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
				Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);

		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 15);
		return key;
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	public String sign(String content) {
		return SignUtils.sign(content, RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	public String getSignType() {
		return "sign_type=\"RSA\"";
	}

}
