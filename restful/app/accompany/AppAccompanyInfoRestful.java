package com.maryun.restful.app.accompany;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.maryun.mapper.app.accompany.AppAccompanyInfoMapper;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.WebResult;

/**
 * 类名称：MessageRestful 创建人：MARYUN 创建时间：2016年2月13日
 * 
 * @version
 */
@RestController
@RequestMapping(value = "/app/accompany/info")
public class AppAccompanyInfoRestful extends BaseRestful {
	@Autowired
	AppAccompanyInfoMapper accompanyInfoMapper;

	/**
	 * 查看陪诊人员个人信息列表
	 * @return
	 */
	@RequestMapping(value = "/get")
	@ResponseBody
	public PageData getAccompanyInfo(@RequestBody PageData pd) throws Exception {
		PageData accInfo = accompanyInfoMapper.getAccompanyInfo(pd);
		return WebResult.requestSuccess(accInfo);
	}
}
