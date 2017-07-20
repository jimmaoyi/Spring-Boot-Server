package com.maryun.restful.app.common;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maryun.common.service.SendMsgService;
/**
 * 
 * @ClassName: SmsRestful 
 * @Description: 短信发送接口
 * @author SR
 * @date 2017年3月2日
 */
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.WebResult;
@RestController
@RequestMapping("sms")
public class SmsRestful extends BaseRestful{
	@Resource
	SendMsgService sendMsgService;
	
	@RequestMapping("smsPush")

	public PageData smsPush(@RequestBody PageData pd){
		/*PageData pd = this.getPageData();*/

		PageData sendMsg=new PageData();
		try {
			sendMsg = sendMsgService.sendMsg(pd);
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return sendMsg;
	}
	
}
