package com.maryun.restful.app.pay.wechat;

import com.maryun.common.service.Qrcode;
import com.maryun.model.PageData;
import com.maryun.restful.app.pay.wechat.wechatUtil.MD5;
import com.maryun.restful.app.pay.wechat.wechatUtil.WeChatPayRequest;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.WebResult;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.net.UnknownHostException;
import java.util.List;
import java.util.UUID;
@RestController
@RequestMapping(value = "/app/pay/wechatPay/QRcode")
public class JybAppWechatQRcodeRestful extends BaseRestful{
	@Autowired
	Qrcode qrcode;
	//appid 
	@Value("${pay.webchart.qrcode.appid}")
	private String APPID;
	//商户id
	 @Value("${pay.webchart.qrcode.mchid}")
	private String MCH_ID;
	//商户key用于签名
	 @Value("${pay.webchart.qrcode.mchkey}")
	 private String KEY;
	 //统一下单地址
	 @Value("${pay.webchart.app.uniformOrdersUrl}")
	 private String UNIFORM_ORDERS_URL;
	 //通知回调地址
	 @Value("${pay.webchart.app.notifyurl}")
	 private String NOTIFY_URL;
	 
	private String NONCE_STR;//随机字符串，长度在32位以内
	
	private String SIGN;//签名
	
	private String BODY;//商品描述
	
	private String OUT_TRADE_NO;//商户订单号
	
	private long TOTAL_FEE;//金额
	
	private String SPBILL_CREATE_IP;//用户端ip
	
	//交易类型
	private String TRADE_TYPE = "NATIVE";//原生扫码支付
	//就医宝定金类型
	private String product_id = "0";
	
	/**
	 * 初始化数据获取签名
	 * @param pd
	 * @return
	 * @throws UnknownHostException 
	 */
	public String getSign(PageData pd) throws UnknownHostException{		
	    //随机数
		NONCE_STR = UUID.randomUUID().toString().replace("-","").substring(0, 30);
		//订单号
		OUT_TRADE_NO = pd.getString("O_ID");
		//获取IP地址
		SPBILL_CREATE_IP = getIPAddr();
		//商品描述
		BODY = pd.getString("O_CONTENT");
		BigDecimal bt = new BigDecimal(pd.getString("O_PRICE"));
	    long price =  bt.multiply(new BigDecimal(100)).longValue();
		TOTAL_FEE = price;
		StringBuilder sb = new StringBuilder();
		sb.append("appid=");
		sb.append(APPID);
		sb.append("&body=");
		sb.append(BODY);
		sb.append("&mch_id=");
		sb.append(MCH_ID);
		sb.append("&nonce_str=");
		sb.append(NONCE_STR);
		sb.append("&notify_url=");
		sb.append(NOTIFY_URL);		
		sb.append("&out_trade_no=");
		sb.append(OUT_TRADE_NO);
		sb.append("&product_id=");
		sb.append(product_id);
		sb.append("&spbill_create_ip=");
		sb.append(SPBILL_CREATE_IP);
		sb.append("&total_fee=");
		sb.append(TOTAL_FEE);
		sb.append("&trade_type=");
		sb.append(TRADE_TYPE);
		sb.append("&key=");
		sb.append(KEY);
		String signTemp = sb.toString(); 
//		System.out.println("加密码："+SIGN);
//		System.out.println("加密码长度："+SIGN.length());
		return MD5.MD5Encode(signTemp);
	}
	
	/**
	 * 获取本机IP地址
	 * @return
	 * @throws UnknownHostException 
	 */
	public String getIPAddr() throws UnknownHostException{
		//InetAddress addr= InetAddress.getLocalHost();
		//return addr.getHostAddress().toString();
		return "60.205.223.167";
	}
	
	//拼接成xml格式的
	public  String dataToXML(){		
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");
		sb.append("\r\n");
		sb.append("<appid>"+APPID+"</appid>");
		sb.append("\r\n");
		sb.append("<body>"+BODY+"</body>");
		sb.append("\r\n");
		sb.append("<mch_id>"+MCH_ID+"</mch_id>");
		sb.append("\r\n");
		sb.append("<nonce_str>"+NONCE_STR+"</nonce_str>");
		sb.append("\r\n");
		sb.append("<notify_url>"+NOTIFY_URL+"</notify_url>");
		sb.append("\r\n");
		sb.append("<out_trade_no>"+OUT_TRADE_NO+"</out_trade_no>");
		sb.append("\r\n");
		sb.append("<product_id>"+product_id+"</product_id>");
		sb.append("\r\n");
		sb.append("<spbill_create_ip>"+SPBILL_CREATE_IP+"</spbill_create_ip>");
		sb.append("\r\n");
		sb.append("<total_fee>"+TOTAL_FEE+"</total_fee>");
		sb.append("\r\n");
		sb.append("<trade_type>"+TRADE_TYPE+"</trade_type>");
		sb.append("\r\n");
		sb.append("<sign>"+SIGN+"</sign>");
		sb.append("\r\n");
		sb.append("</xml>");
		System.out.println(sb.toString());
		return sb.toString();
		
	}
	/**
	 * 创建二维码
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/preOrders")
	@ResponseBody
	public PageData createTradeQRCode(@RequestBody PageData pd){
		//获取签名
		try {
			SIGN = getSign(pd);
		//xml格式参数
		String xmlParams = dataToXML();
		String result =  WeChatPayRequest.httpsRequest(UNIFORM_ORDERS_URL, "POST", xmlParams);
		Document document = DocumentHelper.parseText(result);
		Element root = document.getRootElement();
		List<Element> elementList =  root.elements();
		String code_url = "";
		String return_msg = "";
		String return_code = "";
		for (Element element : elementList) {
			if(element.getName().equals("appid")){
				System.out.println("appid------>"+element.getText());
			}else if(element.getName().equals("mch_id")){
				System.out.println("mch_id------>"+element.getText());
			}else if(element.getName().equals("nonce_str")){
				System.out.println("nonce_str------>"+element.getText());
			}else if(element.getName().equals("sign")){
				System.out.println("sign------>"+element.getText());
			}else if(element.getName().equals("prepay_id")){
				System.out.println("prepay_id------>"+element.getText());
			}else if(element.getName().equals("trade_type")){
				System.out.println("trade_type------>"+element.getText());
			}else if(element.getName().equals("code_url")){
				System.out.println("code_url------>"+element.getText());
				code_url = element.getText();
			}else if(element.getName().equals("return_code")){
				System.out.println("return_code------>"+element.getText());
				return_code = element.getText();
			}else if(element.getName().equals("return_msg")){
				System.out.println("return_msg------>"+element.getText());
				return_msg = element.getText();
			}
		}
		if("SUCCESS".equals(return_code)){
			return  WebResult.requestSuccess(qrcode.createQrcode(code_url));
		}else{
			return  WebResult.requestFailed(701, return_msg, null);
		}
		
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return  WebResult.requestFailed(701, "获取二维码失败！", null);
		} catch (DocumentException e) {
			e.printStackTrace();
			return  WebResult.requestFailed(701, "获取二维码失败！！！", null);
		}
	}
}
