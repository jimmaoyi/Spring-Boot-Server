package com.maryun.restful.app.pay;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.maryun.mapper.app.pay.AppBankAccountMapper;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.WebResult;
/**
 * 类名称：AppDoctorInfoRestful 创建人：MARYUN 创建时间：2016年2月13日
 * 
 * @version
 */
@RestController
@RequestMapping(value = "/app/pay/bankaccount")
public class AppBankAccountRestful  extends BaseRestful{
	@Autowired
	AppBankAccountMapper appBankAccountMapper;
	/**
	 * 线下汇款账号信息
	 * @return
	 */
	@RequestMapping(value = "/info")
	@ResponseBody
	public PageData getBankAccontInfo() throws Exception {
		PageData accInfo = appBankAccountMapper.getBankAccontInfo();
		return WebResult.requestSuccess(accInfo);
	}
}
