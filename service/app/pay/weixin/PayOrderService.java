package com.maryun.service.app.pay.weixin;

import com.maryun.common.service.Qrcode;
import com.maryun.model.PageData;
import com.maryun.restful.app.pay.wechat.wechatUtil.MD5;
import com.maryun.restful.app.pay.wechat.wechatUtil.WeChatPayRequest;
import com.maryun.utils.WebResult;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @ClassName： com.maryun.service.app.pay.weixin
 * @Description：
 * @Author: lgq
 * @Data: create in 2017/3/22
 * @Version: 1.0
 */
@Service
public class PayOrderService {

    private boolean isPublicOrder=true;//public 公众号APPID  open开放APPId
    public void setPublicOrder(boolean publicOrder){
        this.isPublicOrder=publicOrder;
        if(publicOrder){
            APPID=publicAppid;
            MCH_ID=publicMchid;
            KEY=publicKey;
            TRADE_TYPE=publicTradeType;
        }else{
            APPID=openAppid;
            MCH_ID=openMchid;
            KEY=publicKey;
            TRADE_TYPE=openTradeType;
        }
    }

    //APPID
    private String APPID="";
    @Autowired
    Qrcode qrcode;
    @Value("${pay.webchart.qrcode.appid}")
    private String publicAppid;//公众号APPId

    @Value("${pay.webchart.app.appid}")
    private String openAppid;//开放APPId

    //商户id
    private String MCH_ID;

    @Value("${pay.webchart.qrcode.mchid}")
    private String publicMchid;//公众号商户id

    @Value("${pay.webchart.app.mchid}")
    private String openMchid;//开放平台商户id


    private String KEY;//KEY用户签名
    @Value("${pay.webchart.qrcode.mchkey}")
    private String publicKey;//公账号全名key

    @Value("${pay.webchart.app.mchkey}")
    private String openKey;//公账号全名key


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
    private String TRADE_TYPE = "";

    private String publicTradeType = "NATIVE";//原生扫码支付
    private String openTradeType = "APP";//APP支付


    //就医宝定金类型
    private String product_id = "0";

    /**
     * 初始化数据获取签名
     * @param pd
     * @return
     * @throws UnknownHostException
     */
    private String getSign(PageData pd) throws UnknownHostException{
        //订单号
        OUT_TRADE_NO = pd.getString("O_ID");
        //获取IP地址
        SPBILL_CREATE_IP = getIPAddr();
        //商品描述
        BODY = pd.getString("O_CONTENT");
        BigDecimal bt = new BigDecimal(pd.getString("O_PRICE"));
        long price =  bt.multiply(new BigDecimal(100)).longValue();
        TOTAL_FEE = price;

        //随机数
        NONCE_STR = UUID.randomUUID().toString().replace("-","").substring(0, 30);

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
        return MD5.MD5Encode(signTemp);
    }

    /**
     * 获取本机IP地址
     * @return
     * @throws UnknownHostException
     */
    private String getIPAddr() throws UnknownHostException{
        return "60.205.223.167";
    }

    //拼接成xml格式的
    private  String dataToXML(){
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
        return sb.toString();
    }
    /**
     * 预下单
     * @return
     * @throws Exception
     */
    public PageData createTrade(PageData pd){
        //获取签名
        try {
            SIGN = getSign(pd);
            //xml格式参数
            String xmlParams = dataToXML();
            String result =  WeChatPayRequest.httpsRequest(UNIFORM_ORDERS_URL, "POST", xmlParams);
            Document document = DocumentHelper.parseText(result);
            Element root = document.getRootElement();
            List<Element> elementList =  root.elements();

            //处理数据
            PageData retpd=new PageData();
            for (Element element : elementList) {
               String name=element.getName();
               String text=element.getText();
                retpd.put(name,text);
            }
            if(isPublicOrder){
                return dealpublic(retpd);
            }else{
                return dealOpen(retpd);
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return  WebResult.requestFailed(701, "请求失败！", null);
        } catch (DocumentException e) {
            e.printStackTrace();
            return  WebResult.requestFailed(701, "请求失败", null);
        }
    }


    /**
     * 扫码支付处理
     * @param pd
     * @return
     */
    private PageData dealpublic(PageData pd){
        String return_code=pd.getString("return_code");
        String return_msg=pd.getString("return_msg");
        String code_url=pd.getString("code_url");
        if("SUCCESS".equals(return_code)){
            return  WebResult.requestSuccess(qrcode.createQrcode(code_url));
        }else{
            return  WebResult.requestFailed(701, return_msg, null);
        }
    }


    /**
     * app支付处理
     * @param pd
     * @return
     */
    private PageData dealOpen(PageData pd){
        String return_code=pd.getString("return_code");
        String return_msg=pd.getString("return_msg");
        String prepay_id=pd.getString("prepay_id");
        if("SUCCESS".equals(return_code)){
            PageData singPd=new PageData();
            singPd.put("noncestr",UUID.randomUUID().toString().replace("-","").substring(0, 30));

            Date date = new Date();
            String time = String.valueOf(date.getTime());
    		String timestamp = time.substring(0, 10);

            singPd.put("timestamp",timestamp);
            singPd.put("partnerid",MCH_ID);
            singPd.put("prepayid",prepay_id);
            singPd.put("package","Sign=WXPay");
            singPd.put("sign",getAPPSign(singPd));
            return  WebResult.requestSuccess(singPd);
        }else{
            return  WebResult.requestFailed(701, return_msg, null);
        }
    }




    /**
     * APP
     * @param pd
     * @return
     * @throws UnknownHostException
     */
    private String getAPPSign(PageData pd){
        StringBuilder sb = new StringBuilder();
        sb.append("appid=");
        sb.append(APPID);

        sb.append("&noncestr=");
        sb.append(pd.getString("noncestr"));


        sb.append("&package=");
        sb.append(pd.getString("package"));

        sb.append("&partnerid=");
        sb.append(MCH_ID);


        sb.append("&prepayid=");
        sb.append(pd.getString("prepayid"));


        sb.append("&timestamp=");
        sb.append(pd.getString("timestamp"));

        sb.append("&key=");
        sb.append(KEY);

        String signTemp = sb.toString();
        return MD5.MD5Encode(signTemp);
    }


}
