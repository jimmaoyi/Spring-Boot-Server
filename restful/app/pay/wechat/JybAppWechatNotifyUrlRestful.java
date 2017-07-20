package com.maryun.restful.app.pay.wechat;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.maryun.mapper.app.pay.AppPayMapper;
import com.maryun.mapper.app.pay.AppPayWechatPayMapper;
import com.maryun.mapper.app.user.order.JybUserMobilePersonalMyOrderMapper;
import com.maryun.model.PageData;
import com.maryun.restful.app.pay.AppPayComponent;
import com.maryun.restful.app.pay.wechat.wechatUtil.MD5;
import com.maryun.restful.base.BaseRestful;


@RestController
@RequestMapping(value = "/app/user/wechatPay/NotifyUrl")
public class JybAppWechatNotifyUrlRestful extends BaseRestful {
	//商户key用于签名
	@Value("${pay.webchart.app.mchkey}")
	 private String KEY;
	@Autowired
	AppPayWechatPayMapper appPayWechatPayMapper;
	@Autowired
	AppPayMapper appPayMapper;
	@Autowired
	AppPayComponent appPayComponent;
	@Autowired
	private JybUserMobilePersonalMyOrderMapper jybUserMobilePersonalMyOrderMapper;
	
	private static Log log = LogFactory.getLog(JybAppWechatNotifyUrlRestful.class);
	
	@RequestMapping("/receiveInfo")
	@ResponseBody
	public void getWeChatInfor(HttpServletRequest request, HttpServletResponse response){
		/**
		 * 微信公众平台接口配置用
		 */
		/**
		 * 获取从微信端返回的xml文件信息，并用dom4J解析文件
		 * 获取微信用户与微信公众号交互的唯一标识openID
		 */
		InputStream in = null;
		PageData map = new PageData();
		try {
			in = request.getInputStream();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		SAXReader reader = new SAXReader();
		
		
		try {
			Document document = reader.read(in);
			
			Element root = document.getRootElement();
			
			List<Element> elementList = root.elements();
			
			for (Element element : elementList) {
				System.out.println("xml信息------>"+element);
				map.put(element.getName(), element.getText());
			}
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		
//		System.out.println("appid------>"+map.get("appid"));
//		System.out.println("bank_type------>"+map.get("bank_type"));
//		System.out.println("cash_fee------>"+map.get("cash_fee"));
//		System.out.println("fee_type------>"+map.get("fee_type"));
//		System.out.println("is_subscribe------>"+map.get("is_subscribe"));
//		System.out.println("mch_id------>"+map.get("mch_id"));
//		System.out.println("nonce_str------>"+map.get("nonce_str"));
//		System.out.println("openid------>"+map.get("openid"));
//		System.out.println("out_trade_no------>"+map.get("out_trade_no"));
//		System.out.println("result_code------>"+map.get("result_code"));
//		System.out.println("return_code------>"+map.get("return_code"));
//		System.out.println("sign------>"+map.get("sign"));
//		System.out.println("time_end------>"+map.get("time_end"));
//		System.out.println("total_fee------>"+map.get("total_fee"));
//		System.out.println("trade_type------>"+map.get("trade_type"));
//		System.out.println("transaction_id------>"+map.get("transaction_id"));
//		log.info("----------------------");
		log.info(map);
		//验证微信支付回调支付成功的签名,如果签名正确，执行response.getWriter().write(...)返回给微信支付成功的信息
		//如何验证微信支付回调支付成功的签名，就是把返回的xml里的数据（去掉里面的sign）再从新签名生成的签名
		//和xml里的sign进行对比，如果相同给微信返回正确的信息，不相同就返回错误的信息
//		String KEY = "ea69857e11b56a6609b7dbb6edcb17a4";
		
		StringBuilder sbNewSign = new StringBuilder();
		String strNewSign = sbNewSign.append("")
		.append("appid="+map.get("appid"))
		.append("&bank_type="+map.get("bank_type"))
		.append("&cash_fee="+map.get("cash_fee"))
		.append("&fee_type="+map.get("fee_type"))
		.append("&is_subscribe="+map.get("is_subscribe"))
		.append("&mch_id="+map.get("mch_id"))
		.append("&nonce_str="+map.get("nonce_str"))
		.append("&openid="+map.get("openid"))
		.append("&out_trade_no="+map.get("out_trade_no"))
		.append("&result_code="+map.get("result_code"))
		.append("&return_code="+map.get("return_code"))
		.append("&time_end="+map.get("time_end"))
		.append("&total_fee="+map.get("total_fee"))
		.append("&trade_type="+map.get("trade_type"))
		.append("&transaction_id="+map.get("transaction_id"))
		.append("&key="+KEY)
		.append("").toString();
		
		//根据返回的xml文件里的信息生成新的签名
		String newSign = MD5.MD5Encode(strNewSign).toUpperCase();
		System.out.println("newSign--------<<<<"+newSign);
		
		//定义成功失败的参数文件，下面要用
		StringBuilder sbSuccess = new StringBuilder();
		sbSuccess.append("<xml>");
		sbSuccess.append("\r\n");
		sbSuccess.append("<return_code><![CDATA[SUCCESS]]></return_code>");
		sbSuccess.append("\r\n");
		sbSuccess.append("</xml>");
		StringBuilder sbFail = new StringBuilder();
		sbFail.append("<xml>");
		sbFail.append("\r\n");
		sbFail.append("<return_code><![CDATA[Fail]]></return_code>");
		sbFail.append("\r\n");
		sbFail.append("<return_msg><![CDATA[签名验证失败]]></return_msg>");
		sbFail.append("\r\n");
		sbFail.append("</xml>");
		//判读签名的正确与否返回相应的成功还是失败参数给微信，不然微信会一直的给该回调地址发xml文件
		PageData pd = this.getPageData();
		pd.put("O_ID", map.get("out_trade_no"));
		try {
			
			if(map.get("sign").equals(newSign)){
				if("SUCCESS".equals(map.get("return_code"))){
				log.info(sbSuccess.toString());
				response.getWriter().write(sbSuccess.toString());
				/**
				 * 返回给微信SUCCESS后，然后再执行相关的业务操作，去数据库中修改相关的订单信息的订单状态
				 */
				appPayWechatPayMapper.saveNotify(map);
				appPayMapper.updateOrderState(map);
				//支付成功后，修改lb_jyb_pay里的订单支付类型和状态
				PageData payWay = new PageData();
				payWay.put("O_ID",map.get("out_trade_no"));
				payWay.put("PAY_WAY","1");
				payWay.put("PAY_STATUS","1");
				jybUserMobilePersonalMyOrderMapper.updatePayStatus(payWay);
				appPayComponent.saveKeyNode(map.get("out_trade_no").toString());
				System.out.println("修改订单状态成功");
				}else{
					log.info("返回失败！");
				}
			}else{
				log.info("签名不一样！");
				response.getWriter().write(sbFail.toString());
			}	
			
		} catch (IOException e) {
			log.info(e);
			e.printStackTrace();
		} catch (Exception e) {
			log.info(e);
			e.printStackTrace();
		}
	}
}
