package com.maryun.restful.app.pay.wechat;

import com.maryun.model.PageData;
import com.maryun.restful.app.pay.wechat.wechatUtil.WeChatShopPlatform;
import com.maryun.restful.app.pay.wechat.wechatUtil.WechatPay;
import com.maryun.restful.base.BaseRestful;
import com.maryun.service.app.pay.weixin.PayOrderService;
import com.maryun.utils.WebResult;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/app/user/wechatPay")
public class JybAppWechatPayRestful extends BaseRestful {
	@Autowired
	WechatPay wechatPay;

	@Autowired
	private PayOrderService payOrderService;
	/**
	 * 根据订单id，订单信息，商品价格，商品数量
	 * String O_ID,String body,String price
	 * @param O_ID
	 * @param body
	 * @param price
	 * @param num
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/prepayId")
	@ResponseBody
	public PageData getPrepayId(@RequestBody PageData pd) throws Exception{
		if(StringUtils.isBlank(pd.getString("O_ID"))||StringUtils.isBlank(pd.getString("O_PRICE"))||StringUtils.isBlank(pd.getString("O_CONTENT"))){
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		//根据传过来的O_ID,O_CONTENT,O_PRICE,sum去生成预支付的订单信息并返回给前台
//		pd.put("TRADE_TYPE", "APP");
//		PageData result = wechatPay.postWechat(pd);
//		Document document = DocumentHelper.parseText(result.getString("result"));
//		Element root = document.getRootElement();
//		PageData pdR = new PageData();
//		List<Element> elementList =  root.elements();
//		for (Element element : elementList) {
//			if(element.getName().equals("appid")){
//				System.out.println("appid------>"+element.getText());
//				pdR.put("appid", element.getText());
//			}else if(element.getName().equals("mch_id")){
//				System.out.println("mch_id------>"+element.getText());
//				pdR.put("mch_id", element.getText());
//			}else if(element.getName().equals("nonce_str")){
//				System.out.println("nonce_str------>"+element.getText());
//				pdR.put("nonce_str", element.getText());
//			}else if(element.getName().equals("sign")){
//				System.out.println("sign------>"+element.getText());
//				pdR.put("sign", element.getText());
//			}else if(element.getName().equals("prepay_id")){
//				System.out.println("prepay_id------>"+element.getText());
//				pdR.put("prepay_id", element.getText());
//			}else if(element.getName().equals("trade_type")){
//				System.out.println("trade_type------>"+element.getText());
//				pdR.put("trade_type", element.getText());
//			}else if(element.getName().equals("return_code")){
//				System.out.println("return_code------>"+element.getText());
//				pdR.put("return_code", element.getText());
//			}else if(element.getName().equals("return_msg")){
//				System.out.println("return_msg------>"+element.getText());
//				pdR.put("return_msg", element.getText());
//			}
//		}
//		Date date = new Date();
//		String time = String.valueOf(date.getTime());
//		String timestamp = time.substring(0, 10);
//		System.out.println("timestamp------>"+timestamp);
//		pdR.put("SIGN", result.get("SIGN"));
//		if("SUCCESS".equals(pdR.getString("return_code"))){
//			pdR.put("timestamp",timestamp);
//			return WebResult.requestSuccess(pdR);
//		}else{
//			return WebResult.requestFailed(500, pdR.getString("return_msg"), pdR);
//		}
		payOrderService.setPublicOrder(false);
		return payOrderService.createTrade(pd);
	}
	
	/**
	 * 去商户平台查询该订单的支付情况
	 * String O_ID
	 * @return
	 */
	@RequestMapping("/goWeChatShopPlatform")
	@ResponseBody
	public Object goWeChatShopPlatform(@RequestBody PageData pd){
		WeChatShopPlatform weChatShopPlatForm = new WeChatShopPlatform();
		String O_ID = pd.getString("O_ID");
		String result = weChatShopPlatForm.postWechat(O_ID);
		
		PageData pdR = this.getPageData();
		Document document;
		try {
			document = DocumentHelper.parseText(result);
			Element root = document.getRootElement();
			List<Element> elementList =  root.elements();
			
			for (Element element : elementList) {
				pdR.put(element.getName(),element.getText());
			}
			
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("<return_code------>"+pdR.get("<return_code"));
		System.out.println("return_msg------>"+pdR.get("return_msg"));
		System.out.println("appid------>"+pdR.get("appid"));
		System.out.println("mch_id------>"+pdR.get("mch_id"));
		System.out.println("nonce_str------>"+pdR.get("nonce_str"));
		System.out.println("sign------>"+pdR.get("sign"));
		System.out.println("result_code------>"+pdR.get("result_code"));
		System.out.println("openid------>"+pdR.get("openid"));
		System.out.println("is_subscribe------>"+pdR.get("is_subscribe"));
		System.out.println("trade_type------>"+pdR.get("trade_type"));
		System.out.println("bank_type------>"+pdR.get("bank_type"));
		System.out.println("total_fee------>"+pdR.get("total_fee"));
		System.out.println("fee_type------>"+pdR.get("fee_type"));
		System.out.println("transaction_id------>"+pdR.get("transaction_id"));
		System.out.println("out_trade_no------>"+pdR.get("out_trade_no"));
		System.out.println("attach------>"+pdR.get("attach"));
		System.out.println("time_end------>"+pdR.get("time_end"));
		System.out.println("trade_state------>"+pdR.get("trade_state"));
		System.out.println("cash_fee------>"+pdR.get("cash_fee"));
		return WebResult.requestSuccess(pdR);
	}
	
}
