package com.maryun.common.service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
/**
 * 推送服务类
 * @author <a href="mailto:shiran@maryun.net">shiran</a>
 * @version 2017年2月26日
 *
 */
@Component
public class PushService{
    /**
     * 
     * @param MSG_CONTENT 消息内容
     * @param ALERT alert
     * @param TITLE 标题 //ios没有title
     * @param extra 附加参数
     * @param regid 用户注册id
     * @return
     */
    public PushPayload buildPushObject_id_alert_title_extra(String ALERT,String TITLE,Map<String, String> extra,String regId) {
    	Audience registrationId;
    	if(regId!=null){
    		registrationId = Audience.registrationId(regId);
    	}else{
    		registrationId = Audience.all();
    	}
    	return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(registrationId)
                .setNotification(Notification.newBuilder()
                		.setAlert(ALERT)
                		.addPlatformNotification(AndroidNotification.newBuilder()
                				.setTitle(TITLE).addExtras(extra).build())
                		.addPlatformNotification(IosNotification.newBuilder()
                				.incrBadge(1)
                				.addExtras(extra).build())
                		.build())
                //.setMessage(Message.content(MSG_CONTENT))
                .build();
    }
}
