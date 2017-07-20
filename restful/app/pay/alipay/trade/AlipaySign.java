package com.maryun.restful.app.pay.alipay.trade;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
@Component
public class AlipaySign {
	@Value("${pay.alipay.app.prikey}")
	private String prikey;
	static {
        /** 一定要在创建AlipayTradeService之前调用Configs.init()设置默认参数
         *  Configs会读取classpath下的zfbinfo.properties文件配置信息，如果找不到该文件则确认该文件是否在classpath目录
         */
        Configs.init("zfbinfo.properties");
	}
//	 /**
//	    *支付宝支付
//	    * @param orderId 订单编号
//	    * @param actualPay 实际支付金额
//	    * @return
//	    */
//	 private String getOrderInfoByAliPay(String orderId,float actualPay) {
//	      //回调页面
//	      String ali_call_back_url = propertiesService.ALI_CALL_BACK_URL;
//	      String seller_id = propertiesService.SELLER_ID;//商户编号
//	      String[] parameters={
//	            "service=\"mobile.securitypay.pay\"",//固定值（手机快捷支付）
//	            "partner=\"2088421544444\"",//合作身份者ID（16位）
//	            "_input_charset=\"utf-8\"",
//	            "notify_url=\""+ali_call_back_url+"\"",//通知地址
//	            "out_trade_no=\""+orderId+"\"",//商户内部订单号
//	            "subject=\"测试\"",//测试
//	            "payment_type=\"1\"",//固定值
//	            "seller_id=\""+seller_id+"\"",//账户邮箱
//	           "total_fee=\""+"0.01"+"\"",//支付金额（元）
//
//	            "body=\"订单说明\"",//订单说明            
//	            "it_b_pay=\"30m\""//（订单过期时间 30分钟过期无效）
//	      };
//	      String signOrderUrl = signAllString(parameters);
//	      return signOrderUrl;
//	   }
	 
	/**
	 * 支付宝签名
	 * @param array
	 * @return
	 * @throws AlipayApiException 
	 */
	public String signAllString(String [] array) throws AlipayApiException{
	   StringBuffer sb = new StringBuffer("");
	   for (int i = 0; i < array.length; i++) {
	      if(i==(array.length-1)){
	         sb.append(array[i]);
	      }else{
	         sb.append(array[i]+"&");
	      }
	   }
	   System.out.println(sb.toString());
	   String sign = AlipaySignature.rsaSign(sb.toString(), prikey, "utf-8");
//	   try {
////	      sign = URLEncoder.encode(AlipayRSA.sign(sb.toString(), Configs.getPrivateKey(), "utf-8"), "utf-8");//private_key私钥
//	   } catch (UnsupportedEncodingException e) {
//	      e.printStackTrace();
//	   }
	   sb.append("&sign=\""+sign+"\"&");
	   sb.append("sign_type=\"RSA\"");
	   return sb.toString();
	}
}
