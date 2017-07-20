package com.maryun.restful.app.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.maryun.mapper.app.service.AppServiceMapper;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.WebResult;

/**
 * 类名称：AppUserInfoRestful 创建人：MARYUN 创建时间：2016年2月13日
 * 
 * @version
 */
@RestController
@RequestMapping(value = "/app/service")
public class AppServiceRestful extends BaseRestful{
	@Autowired
	AppServiceMapper appServiceMapper;
	/**
	 * 获取客服电话
	 * @return
	 */
	@RequestMapping(value = "/tel")
	@ResponseBody
	public PageData getServiceCenterTel() throws Exception {
		PageData suggest = appServiceMapper.getServiceCenterTel();
		return WebResult.requestSuccess(suggest);
	}
	/**
	 * 获取定金金额
	 * @return
	 */
	@RequestMapping(value = "/EarestMoney")
	@ResponseBody
	public PageData getEarestMoney() throws Exception {
		PageData suggest = appServiceMapper.getEarestMoney();
		return WebResult.requestSuccess(suggest);
	}
	/**
	 * 获取取消订单原因
	 * @return
	 */
	@RequestMapping(value = "/orderCancelReason")
	@ResponseBody
	public PageData getOrderCancelReason() throws Exception {
		List<PageData> suggest = appServiceMapper.getOrderCancelReason();
		return WebResult.requestSuccess(suggest);
	}
}
