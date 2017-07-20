package com.maryun.common.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSON;
import com.maryun.model.PageData;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;

/**
 * Created by lh on 2017/2/16.
 */
@Component("sendMsgUtil")
public class SendMsgUtils {

    @Value("${alibaba.project.appKey}")
    private  String appkey;
    @Value("${alibaba.project.appSecret}")
    private  String secret;
    @Value("${alibaba.project.url}")
    private  String url;
    /**
     * 发送短信
     */
    public Map send(PageData pageData){
        Map<String,String> map=new HashMap<>();
    	try {
        	String recNum=pageData.getString("recNum");
        	String msgCode=pageData.getString("msgCode");
            TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
            AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
            req.setExtend(pageData.getString("extend"));
            req.setSmsType("normal");
            req.setSmsFreeSignName(pageData.getString("smsFreeSignName"));
            req.setSmsParamString(pageData.getString("smdParam"));
            req.setRecNum(recNum);
            req.setSmsTemplateCode(pageData.getString("smsTemplateCode"));
            AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);
            String res=rsp.getBody();
            PageData retData=JSON.parseObject(res, PageData.class);
            
            if(retData.containsKey("error_response")){
            	map.put("state", "0");
            	map.put("msg", res);
            	return map;
            }else{
            	map.put("state", "1");
            	map.put("msg", res);
            	return map;
            }
        }catch(Exception e){
        	map.put("state", "0");
        	map.put("msg", "程序错误");
        	return map;
        }
    }
}
