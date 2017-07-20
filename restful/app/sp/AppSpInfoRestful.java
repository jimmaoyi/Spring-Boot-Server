package com.maryun.restful.app.sp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.maryun.mapper.app.sp.AppSPInfoMapper;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.WebResult;
/**
 * 类名称：AppSpInfoRestful 创建人：MARYUN 创建时间：2016年2月13日
 * 
 * @version
 */
@RestController
@RequestMapping(value = "/app/sp/info")
public class AppSpInfoRestful  extends BaseRestful{
	@Autowired
	AppSPInfoMapper appSpInfoMapper;
	/**
	 * 机构信息
	 * @return
	 */
	@RequestMapping(value = "/get")
	@ResponseBody
	public PageData getUserInfo(@RequestBody PageData pd) throws Exception {
		PageData accInfo = appSpInfoMapper.getSpInfo(pd);
		return WebResult.requestSuccess(accInfo);
	}
}
