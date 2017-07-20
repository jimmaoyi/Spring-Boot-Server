package com.maryun.restful.app.pay.wechat.wechatUtil;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.maryun.model.PageData;
@Component("wechatPay")
public class WechatPay {
	//appid 
		@Value("${pay.webchart.app.appid}")
		private String APPID;
		//商户id
		 @Value("${pay.webchart.app.mchid}")
		private String MCH_ID;
		//商户key用于签名
		 @Value("${pay.webchart.app.mchkey}")
		 private String KEY;
		 //统一下单地址
		 @Value("${pay.webchart.app.uniformOrdersUrl}")
		 private String UNIFORM_ORDERS_URL;
		 //通知回调地址
		 @Value("${pay.webchart.app.notifyurl}")
		 private String NOTIFY_URL;
		//就医宝定金类型
			private String product_id = "0";
			
//	private String NONCE_STR;//随机字符串，长度在32位以内
//	
//	private String SIGN;//签名
//	
//	private String BODY;//商品描述
//	
//	private String OUT_TRADE_NO;//商户订单号
//	
//	private long TOTAL_FEE;//金额
//	
//	private String SPBILL_CREATE_IP;//用户端ip
//	
//	private String TRADE_TYPE = "APP";//交易类型

	/**
	 * 获取签名
	 * @param pd
	 * @return
	 */
	public String getSign(PageData pd){
		StringBuilder sb = new StringBuilder();
		sb.append("appid=");
		sb.append(APPID);
		sb.append("&body=");
		sb.append(pd.getString("BODY"));
		sb.append("&mch_id=");
		sb.append(MCH_ID);
		sb.append("&nonce_str=");
		sb.append(pd.getString("NONCE_STR"));
		sb.append("&notify_url=");
		sb.append(NOTIFY_URL);
		sb.append("&out_trade_no=");
		sb.append(pd.getString("OUT_TRADE_NO"));
//		sb.append("&product_id=");
//		sb.append(product_id);
		sb.append("&spbill_create_ip=");
		sb.append(pd.getString("SPBILL_CREATE_IP"));
		sb.append("&total_fee=");
		sb.append(pd.getString("TOTAL_FEE"));
		sb.append("&trade_type=");
		sb.append(pd.getString("TRADE_TYPE"));
		sb.append("&key=");
		sb.append(KEY);
		System.out.println("生成预支付订单的排序"+sb.toString());
		return MD5.MD5Encode(sb.toString());
	}
	
	
	/**
	 * 获取IP地址
	 * @return
	 */
	public static String getIPAddr(){
		InetAddress addr = null;
		try {
			addr = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return addr.getHostAddress().toString();
			
	}
	
	/**
	 * 拼接成xml格式的,发送请求
	 * @param pd
	 * @return
	 */
	public String dataToXML(PageData pd){
		//随机数
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");
		sb.append("\r\n");
		sb.append("<appid>"+APPID+"</appid>");
		sb.append("\r\n");
		sb.append("<body>"+pd.getString("BODY")+"</body>");
		sb.append("\r\n");
		sb.append("<mch_id>"+MCH_ID+"</mch_id>");
		sb.append("\r\n");
		sb.append("<nonce_str>"+pd.getString("NONCE_STR")+"</nonce_str>");
		sb.append("\r\n");
		sb.append("<notify_url>"+NOTIFY_URL+"</notify_url>");
		sb.append("\r\n");
		sb.append("<out_trade_no>"+pd.getString("OUT_TRADE_NO")+"</out_trade_no>");
		sb.append("\r\n");
		sb.append("<spbill_create_ip>"+pd.getString("SPBILL_CREATE_IP")+"</spbill_create_ip>");
		sb.append("\r\n");
		sb.append("<total_fee>"+pd.getString("TOTAL_FEE")+"</total_fee>");
		sb.append("\r\n");
		sb.append("<trade_type>"+pd.getString("TRADE_TYPE")+"</trade_type>");
		sb.append("\r\n");
		sb.append("<sign>"+pd.getString("SIGN")+"</sign>");
		sb.append("\r\n");
		sb.append("</xml>");
		
		return sb.toString();
		
	}
	
	/**
	 * 发送支付请求								
	 * @param pd
	 * @return
	 */
	public PageData postWechat(PageData pd){
		//随机数
		String NONCE_STR = UUID.randomUUID().toString().replace("-","").substring(0, 30);
				//订单号
		String OUT_TRADE_NO = pd.getString("O_ID");
				//获取IP地址
		String SPBILL_CREATE_IP = getIPAddr();
				//商品描述
		String BODY = pd.getString("O_CONTENT");
		BigDecimal bt = new BigDecimal(pd.getString("O_PRICE"));
		long price =  bt.multiply(new BigDecimal(100)).longValue();
		long TOTAL_FEE = price;
		//订单号
		String TRADE_TYPE = pd.getString("TRADE_TYPE");
		pd.put("NONCE_STR", NONCE_STR);
		pd.put("SPBILL_CREATE_IP", SPBILL_CREATE_IP);
		pd.put("BODY", BODY);
		pd.put("TOTAL_FEE", TOTAL_FEE);
		pd.put("TRADE_TYPE", TRADE_TYPE);
		pd.put("OUT_TRADE_NO", OUT_TRADE_NO);
		String sign = getSign(pd);
		System.out.println("SIGN----->"+sign.toUpperCase());
		pd.put("SIGN", sign.toUpperCase());
		String data = dataToXML(pd);
		String result = WeChatPayRequest.httpsRequest(UNIFORM_ORDERS_URL, "POST", data);
		System.out.println("result----->"+result);
		PageData rePd = new PageData();
		rePd.put("SIGN", sign.toUpperCase());
		rePd.put("result", result);
		return rePd;
	}
	
//	public static void main(String[] args) {
//	
//		WechatPay pay=new WechatPay();
//		String result=pay.postWechat("11111111","body","1");
//		System.out.println(result);
//		
//		try {
//			Document document = DocumentHelper.parseText(result);
//			Element root = document.getRootElement();
//			List<Element> elementList =  root.elements();
//			
//			for (Element element : elementList) {
//				if(element.getName().equals("appid")){
//					System.out.println("appid------>"+element.getText());
//				}else if(element.getName().equals("mch_id")){
//					System.out.println("mch_id------>"+element.getText());
//				}else if(element.getName().equals("nonce_str")){
//					System.out.println("nonce_str------>"+element.getText());
//				}else if(element.getName().equals("sign")){
//					System.out.println("sign------>"+element.getText());
//				}else if(element.getName().equals("prepay_id")){
//					System.out.println("prepay_id------>"+element.getText());
//				}else if(element.getName().equals("trade_type")){
//					System.out.println("trade_type------>"+element.getText());
//				}
//			}
//			
//		} catch (DocumentException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		
//	}
	
	
}

















