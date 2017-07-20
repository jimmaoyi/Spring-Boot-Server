/**
 * 
 */
package com.maryun.restful.app.pay;

import java.util.Date;

import org.apache.commons.httpclient.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.maryun.mapper.app.pay.AppPayMapper;
import com.maryun.model.PageData;

/**
 * @author Administrator
 *
 */
@Component
public class AppPayComponent {
	@Autowired
	AppPayMapper appPayMapper;

	public void saveKeyNode(String orderId){
		PageData pd = new PageData();
		pd.put("O_ID", orderId);
		pd.put("NM_ID", "");
		pd.put("NM_NAME", "已付款");
		pd.put("KN_TIME", DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
		pd.put("KN_DAY", 0);
		pd.put("KN_CONTENT", "请等待服务人员联系");
		pd.put("IS_SCHEDULE", "0");
		pd.put("EDIT_STATE", "1");
		appPayMapper.insertNode(pd);
	}
}
