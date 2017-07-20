/**
 * 
 */
package com.maryun.restful.app.common;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.maryun.mapper.app.common.AppAreaMapper;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.WebResult;

/**
 * 类名称：AppAreaRestful 创建人：MARYUN 创建时间：2016年2月13日
 * 
 * @version
 */
@RestController
@RequestMapping(value = "/app/area")
public class AppAreaRestful extends BaseRestful {
	@Autowired
	private AppAreaMapper appAreaMapper;

	/**
	 * 省一级的数据列表
	 * @return
	 */
	@RequestMapping(value = "/provinceArea")
	@ResponseBody
	public PageData getProvinceArea(@RequestBody PageData pd) throws Exception {
		List<PageData> accInfo = appAreaMapper.getProvinceAreaExist(pd);
		return WebResult.requestSuccess(accInfo);
	}
	/**
	 * 市级的数据列表
	 * @return
	 */
	@RequestMapping(value = "/cityArea")
	@ResponseBody
	public PageData getCityArea(@RequestBody PageData pd) throws Exception {
		List<PageData> accInfo = appAreaMapper.getCityArea(pd);
		return WebResult.requestSuccess(accInfo);
	}

	/**
	 * 获取城市列表
	 * @return
	 */
	@RequestMapping(value = "/getCityList")
	@ResponseBody
	public PageData getCityList() throws Exception {
		List<PageData> list = appAreaMapper.getCityList();
		return WebResult.requestSuccess(list);
	}

}
