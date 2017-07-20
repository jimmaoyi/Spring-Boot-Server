package com.maryun.restful.app.pay.alipay.trade;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayResponse;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.ExtendParams;
import com.alipay.demo.trade.model.GoodsDetail;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import com.maryun.common.service.Qrcode;
import com.maryun.mapper.app.pay.AppPayAlipayMapper;
import com.maryun.mapper.app.pay.AppPayMapper;
import com.maryun.mapper.app.user.order.JybUserMobilePersonalMyOrderMapper;
import com.maryun.model.PageData;
import com.maryun.restful.app.pay.AppPayComponent;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.WebResult;

/**
 * 类名称：AliTradePrecreateRestful 创建人：MARYUN 创建时间：2016年2月13日
 * 
 * @version
 */
@RestController
@RequestMapping(value = "/app/pay/alipay")
public class AliTradePrecreateRestful extends BaseRestful{
	
	@Autowired
	Qrcode qrcode;
	@Value("${pay.alipay.app.notifyurl}")
	private String notifyurl;
	@Value("${pay.alipay.app.seller_id}")
	private String seller_id;
	//app支付私钥key
	@Value("${pay.alipay.app.prikey}")
	private String prikey;
	//app支付公钥key
	@Value("${pay.alipay.app.pubkey}")
	private String pubkey;
	//app支付阿里公钥key
	@Value("${pay.alipay.app.alipubkey}")
	private String alipubkey;
	@Autowired
	AppPayAlipayMapper appPayAlipayMapper;
	@Autowired
	AlipaySign alipaySign;
	@Autowired
	AppPayMapper appPayMapper;
	@Autowired
	AppPayComponent appPayComponent;
	@Autowired
	private JybUserMobilePersonalMyOrderMapper jybUserMobilePersonalMyOrderMapper;
	
	private static Log log = LogFactory.getLog(AliTradePrecreateRestful.class);
	 private static AlipayTradeService   tradeService;
	 @Value("${pay.alipay.app.storeId}")
	private String storeId;
	static {
        /** 一定要在创建AlipayTradeService之前调用Configs.init()设置默认参数
         *  Configs会读取classpath下的zfbinfo.properties文件配置信息，如果找不到该文件则确认该文件是否在classpath目录
         */
        Configs.init("zfbinfo.properties");
        tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();
        System.out.println(Configs.getAppid());
	}
	  // 简单打印应答
    private void dumpResponse(AlipayResponse response) {
        if (response != null) {
            log.info(String.format("code:%s, msg:%s", response.getCode(), response.getMsg()));
            if (StringUtils.isNotEmpty(response.getSubCode())) {
                log.info(String.format("subCode:%s, subMsg:%s", response.getSubCode(),
                    response.getSubMsg()));
            }
            log.info("body:" + response.getBody());
        }
    }
	/**
	 * 创建二维码
	 * @return
	 */
	@RequestMapping(value = "/preOrders")
	@ResponseBody
	public PageData createTradeQRCode(@RequestBody PageData pd) {
		System.out.println("=============="+notifyurl);
		// (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
        //订单编号
        String outTradeNo = pd.getString("O_ID");
        // (必填) 订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店当面付扫码消费”
        String oTitle = "老伴儿就医宝定金扫码支付";//pd.getString("O_TTILE")
        String subject = oTitle;
        // (必填) 订单总金额，单位为元，不能超过1亿元
        // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
        String totalAmount = pd.getString("O_PRICE");

        // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
        // 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
        String undiscountableAmount = "0";

        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
        String sellerId = pd.getString("SELLERID");

        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
        String body = pd.getString("O_CONTENT");

        // 商户操作员编号，添加此参数可以为商户操作员做销售统计
        String operatorId = "";

        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
//        String storeId = storeId;

        // 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
        ExtendParams extendParams = new ExtendParams();
//        extendParams.setSysServiceProviderId("2088100200300400500");

        // 支付超时，定义为120分钟
        String timeoutExpress = "120m";

        // 商品明细列表，需填写购买商品详细信息，
        List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();
        // 创建一个商品信息，参数含义分别为商品id（使用国标）、名称、单价（单位为分）、数量，如果需要添加商品类别，详见GoodsDetail
        BigDecimal bt = new BigDecimal(pd.getString("O_PRICE"));
        long price =  bt.multiply(new BigDecimal(100)).longValue();
        GoodsDetail goods1 = GoodsDetail.newInstance("0", "就诊服务", price, 1);
        // 创建好一个商品后添加至商品明细列表
        goodsDetailList.add(goods1);

        // 创建扫码支付请求builder，设置请求参数
        AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder()
            .setSubject(subject).setTotalAmount(totalAmount).setOutTradeNo(outTradeNo)
            .setUndiscountableAmount(undiscountableAmount).setSellerId(sellerId).setBody(body)
            .setOperatorId(operatorId).setStoreId(storeId).setExtendParams(extendParams)
            .setTimeoutExpress(timeoutExpress)
                            .setNotifyUrl(notifyurl)//支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
            .setGoodsDetailList(goodsDetailList);

        AlipayF2FPrecreateResult result = tradeService.tradePrecreate(builder);
        switch (result.getTradeStatus()) {
            case SUCCESS:
                log.info("支付宝预下单成功: ");
                AlipayTradePrecreateResponse response = result.getResponse();
//                dumpResponse(response);
                System.out.println(response.getQrCode());
                System.out.println(qrcode.createQrcode(response.getQrCode()));
                return  WebResult.requestSuccess(qrcode.createQrcode(response.getQrCode()));
               
                // 需要修改为运行机器上的路径
//                String filePath = String.format("/Users/sudo/Desktop/qr-%s.png",
//                    response.getOutTradeNo());
//                log.info("filePath:" + filePath);
                //ZxingUtils.getQRCodeImge(response.getQrCode(), 256, filePath);
//                break;

            case FAILED:
//                log.error("支付宝预下单失败!!!");
//                break;
            	return WebResult.requestFailed(701, "支付宝预下单失败!", null);
            case UNKNOWN:
//                log.error("系统异常，预下单状态未知!!!");
//                break;
            	return WebResult.requestFailed(701, "系统异常，预下单状态未知!", null);
            default:
//                log.error("不支持的交易状态，交易返回异常!!!");
//                break;
            	return WebResult.requestFailed(701, "不支持的交易状态，交易返回异常!", null);
        }
    }
	/**
	 * 根据订单参数进行签名认证，将签名后的信息返回值app端 进行支付
	 * @return
	 * @throws AlipayApiException 
	 */
	@RequestMapping(value = "/sign/orderInfo")
	@ResponseBody
	public PageData getOrderInfo(@RequestBody PageData pd) throws AlipayApiException{
		String out_trade_no = pd.getString("O_ID");
		String body = pd.getString("BODY");
		String subject = pd.getString("SUBJECT");
		String partner = Configs.getPid();
		String total_fee = pd.getString("PRICE");
//		String service="mobile.securitypay.pay";
//		String payment_type="1";
//		String _input_charset="utf-8";
//		String it_b_pay="30m" ;
//		partner="2088101568358171"&seller_id="xxx@alipay.com"&out_trade_no="0819145412-6177"&subject="测试"&body="测试测试"&total_fee="0.01"&notify_url="http://notify.msp.hk/notify.htm"&service="mobile.securitypay.pay"&payment_type="1"&_input_charset="utf-8"&it_b_pay="30m"&sign="lBBK%2F0w5LOajrMrji7DUgEqNjIhQbidR13GovA5r3TgIbNqv231yC1NksLdw%2Ba3JnfHXoXuet6XNNHtn7VE%2BeCoRO1O%2BR1KugLrQEZMtG5jmJIe2pbjm%2F3kb%2FuGkpG%2BwYQYI51%2BhA3YBbvZHVQBYveBqK%2Bh8mUyb7GM1HxWs9k4%3D"&sign_type="RSA"
//最新格式
//		app_id=2015052600090779&biz_content={"timeout_express":"30m","seller_id":"","product_code":"QUICK_MSECURITY_PAY","total_amount":"0.01","subject":"1","body":"我是测试数据","out_trade_no":"IQJZSRC1YMQB5HU"}
		//&charset=utf-8&format=json&method=alipay.trade.app.pay&notify_url=http://domain.merchant.com/payment_notify&sign_type=RSA2&timestamp=2016-08-25 20:26:31&version=1.0
		String[] parameters={
				"partner=\""+partner+"\"",//合作身份者ID（16位）
				"seller_id=\""+seller_id+"\"",//账户邮箱
				"out_trade_no=\""+out_trade_no+"\"",//商户内部订单号
				"subject=\""+subject+"\"",//测试
				"body=\""+body+"\"",//订单说明           
				"total_fee=\""+total_fee+"\"",//支付金额（元）
				"notify_url=\""+notifyurl+"\"",//通知地址
				"service=\"mobile.securitypay.pay\"",//固定值（手机快捷支付
				"payment_type=\"1\"",//固定值
				"_input_charset=\"utf-8\"",
				"it_b_pay=\"30m\""//（订单过期时间 30分钟过期无效）
		};
//		String timestamp = DateUtils.formatDate(new Date(), "yyyy-MM-dd hh24:mm:ss");
//		String biz_content = "{\"timeout_express\":\"30m\",\"seller_id\":\""+seller_id+"\",\"product_code\":\"QUICK_MSECURITY_PAY\",\"total_amount\":\""+total_fee+"\",\"subject\":\""+subject+"\",\"body\":\""+body+"\",\"out_trade_no\":\""+out_trade_no+"\"}";
//		String[] neP = {
//				"app_id=\""+Configs.getAppid()+"\"",
//				"biz_content="+biz_content,
//				"charset=\"utf-8\"",
//				"format=\"json-8\"",
//				"method=\"alipay.trade.app.pay\"",
//				"notify_url=\""+notifyurl+"\"",//通知地址
//				"sign_type=\"RSA\"",
//				"timestamp=\""+timestamp+"\"",
//				"version=\"1.0\""
//		};
		String signinfo = alipaySign.signAllString(parameters);
//		System.out.println(signinfo);
		return WebResult.requestSuccess(signinfo);
	}
	/**
	 * 将支付账号信息发送到
	 * @return
	 */
	@RequestMapping(value = "/order/paymeg")
	@ResponseBody
	public PageData getPayMeg(@RequestBody PageData pd) {
		pd.put("partner", Configs.getPid());
		pd.put("rsa_private", prikey);
		pd.put("rsa_public", pubkey);
		pd.put("seller", seller_id);
		pd.put("notifyUrl", notifyurl);
		return WebResult.requestSuccess(pd);
		//
	}
	/**
	   * 支付宝回调
	   * 
	   * @param map
	   */
	@RequestMapping(value = "/recevice/notify")
	@ResponseBody
	  public void backRcvResponseAlipay(HttpServletRequest request,HttpServletResponse response) {
	    // 获取支付宝POST过来反馈信息
	    Map<String, String> map = new HashMap<String, String>();
	    Map requestParams = request.getParameterMap();
	    PageData pd = new PageData();
	    for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
	      String name = (String) iter.next();
	      String[] values = (String[]) requestParams.get(name);
	      String valueStr = "";
	      for (int i = 0; i < values.length; i++) {
	        valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
	      }
	      map.put(name, valueStr);
	      pd.put(name, valueStr);
	    }
//	    System.out.println("-------------------------------");
//	    System.out.println(pd);
	    log.info("alipay返回的数据：" + JSON.toJSONString(map));
	    // 交易状态
	    String trade_status = null;
	    String sign_type = null;
	    String ordernumber = null;
	    String body = null;
	    String paymentType = null;
	    try {
	      trade_status = new String(map.get("trade_status").getBytes("ISO-8859-1"), "UTF-8");
	      sign_type = new String(map.get("sign_type").getBytes("ISO-8859-1"), "UTF-8");
	      ordernumber = new String(map.get("out_trade_no").getBytes("ISO-8859-1"), "UTF-8"); // 订单号
	      body = new String(map.get("body").getBytes("ISO-8859-1"), "UTF-8"); // 交易详情
	      if(null!=map.get("payment_type")){
	    	  paymentType = new String(map.get("payment_type").getBytes("ISO-8859-1"), "UTF-8"); // 交易详情
	      }
	      log.info("支付类型[" + paymentType + "] ");
	      System.out.println("支付类型[" + paymentType + "] ");
//	      String paydate = new String(map.get("gmt_payment").getBytes("ISO-8859-1"), "UTF-8"); // 充值时间
//	      String trade_no = new String(map.get("trade_no").getBytes("ISO-8859-1"), "UTF-8"); // 支付宝流水号
//	      String strfee = new String(map.get("total_fee").getBytes("ISO-8859-1"), "UTF-8"); // 充值金额
//	      Double fee = Double.valueOf(strfee);
//	      System.out.println("--------------2-----------------");
	      log.info("交易状态[" + trade_status + "]  签名方式[" + sign_type + "]   交易单号[" + ordernumber + "]");
	      boolean signVerified = false;
          if(paymentType!=null && "1".equals(paymentType)){
        	  System.out.println("验签[------------------] ");
        	  signVerified = AlipaySignature.rsaCheckV1(map,alipubkey,"UTF-8"); //调用SDK验证签名
        	  System.out.println("验签1[---------"+signVerified+"---------] ");
          }else{
        	  //将异步通知中收到的待验证所有参数都存放到map中
    	      signVerified = AlipaySignature.rsaCheckV1(map, Configs.getAlipayPublicKey(),"UTF-8"); //调用SDK验证签名
    	      System.out.println("验签2[---------"+signVerified+"---------] ");
          }
	    
	      System.out.println("--------------3-----------------");
	      if(signVerified){
	    	  System.out.println("--------------4-----------------");
	        // TODO 验签成功后
	        //按照支付结果异步通知中的描述，对支付结果中的业务内容进行1\2\3\4二次校验，校验成功后在response中返回success，校验失败返回failure
	        System.out.println("ordernumber==="+ordernumber);
	        System.out.println("trade_status+++++++++"+trade_status);
	        if (trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS")) { // 交易成功
	          try {
	            System.out.println("trade_status+++++++++"+trade_status);
	           //获取接收到的通知信息并保存到数据库
				appPayAlipayMapper.saveNotify(pd);		
				PageData payState = new PageData();
				payState.put("out_trade_no", ordernumber);
				//更新订单支付状态为已付款
				appPayMapper.updateOrderState(payState);
				//支付成功后，修改lb_jyb_pay里的订单支付类型和状态
				PageData payWay = new PageData();
				payWay.put("O_ID",ordernumber);
				payWay.put("PAY_WAY","0");
				payWay.put("PAY_STATUS","1");
				jybUserMobilePersonalMyOrderMapper.updatePayStatus(payWay);
				appPayComponent.saveKeyNode(ordernumber);
				response.getWriter().write("success");
	            //return "SUCCESS";
	          } catch (Exception e) {
	            e.printStackTrace();
	          }
	        } else if (trade_status.equals("WAIT_BUYER_PAY")) {
	          // 轮询查询
//	        	System.out.println("--------------5-----------------");
	        	log.info("交易创建，等待买家付款...");
	        } else if (trade_status.equals("TRADE_PENDING")) {
//	        	System.out.println("--------------6-----------------");
	        	log.info("等待卖家收款(买家付款后，如果卖家账号被冻结)");
	        } else if (trade_status.equals("TRADE_CLOSED")) {
//	        	System.out.println("--------------7-----------------");
	        	log.info("在指定时间段内未支付时关闭的交易；在交易完成全额退款成功时关闭的交易");
	        } else { // 交易失败
//	        	System.out.println("--------------8-----------------");
	        	log.info("交易失败");
	        }
	        
	      }else{
	        // TODO 验签失败则记录异常日志，并在response中返回failure.
	    	  System.out.println("--------------9-----------------");
	    	  log.error("验签失败");
	      }
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	  }
}
