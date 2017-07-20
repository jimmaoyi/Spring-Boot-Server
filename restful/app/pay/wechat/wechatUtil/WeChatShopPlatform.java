package com.maryun.restful.app.pay.wechat.wechatUtil;


import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;

public class WeChatShopPlatform {
	@Value("${pay.webchart.app.appid}")	
	private String APPID;//appid
	@Value("${pay.webchart.app.mchid}")
	private String MCH_ID;//商户id
	
	private String OUT_TRADE_NO;//商户订单号
	
	private String NONCE_STR;//随机字符串，长度在32位以内
	
	private String SIGN;//签名
	@Value("${pay.webchart.app.mchkey}")
	private String KEY;//商户key用于签名
	
	private String SHOP_ORDERS_URL = "https://api.mch.weixin.qq.com/pay/orderquery";//商铺接口请求
	
	//初始化数据
	public void initData(String orderNo){
		
		NONCE_STR = UUID.randomUUID().toString().replace("-","").substring(0, 30);
		
		OUT_TRADE_NO = orderNo;
		
		StringBuilder sb = new StringBuilder();
		sb.append("appid=");
		sb.append(APPID);
		sb.append("&mch_id=");
		sb.append(MCH_ID);
		sb.append("&nonce_str=");
		sb.append(NONCE_STR);
		sb.append("&out_trade_no=");
		sb.append(OUT_TRADE_NO);
		sb.append("&key=");
		sb.append(KEY);
		
		SIGN = MD5.MD5Encode(sb.toString());
		
	}
	
	//拼接成xml格式的
	public  String dataToXML(){
		
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");
		sb.append("\r\n");
		sb.append("<appid>"+APPID+"</appid>");
		sb.append("\r\n");
		sb.append("<mch_id>"+MCH_ID+"</mch_id>");
		sb.append("\r\n");
		sb.append("<nonce_str>"+NONCE_STR+"</nonce_str>");
		sb.append("\r\n");
		sb.append("<out_trade_no>"+OUT_TRADE_NO+"</out_trade_no>");
		sb.append("\r\n");
		sb.append("<sign>"+SIGN+"</sign>");
		sb.append("\r\n");
		sb.append("</xml>");
		
		return sb.toString();
		
	}
	
	
	public String postWechat(String orderNo){
		
		initData(orderNo);
		String data = dataToXML();
		String result = WeChatPayRequest.httpsRequest(SHOP_ORDERS_URL, "POST", data);
		return result;
		
	}
	
	
}
