package com.maryun.lb.restful.jyb.activeGroupUserRelate;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.maryun.lb.mapper.jyb.activeGroupUserRelate.ActiveGroupUserRelateMapper;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.WebResult;

/**
 * 类名称：ActiveGroupUserRelateRestful 创建人：ChuMingZe 创建时间：2017年2月26日
 * 
 * @version
 */
@RestController
@RequestMapping(value = "/actGropUserRela")
public class ActiveGroupUserRelateRestful extends BaseRestful {
	@Autowired
	private ActiveGroupUserRelateMapper activeGroupUserRelateMapper;

	/**
	 * 分页查询
	 * 
	 * @param pd
	 *            查询、分页条件
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/pageSearch")
	public PageData pageSearch(@RequestBody PageData pd) throws Exception {
		List<PageData> lists = null;
		if (pd.getPageNumber() != 0) {
			PageHelper.startPage(pd.getPageNumber(), pd.getPageSize());
		}
		lists = activeGroupUserRelateMapper.listPage(pd);
		// 分页
		pd = this.getPagingPd(lists);
		// 结果集封装
		return WebResult.requestSuccess(pd);
	}
	
	/**
	 * 按活动主键Id获取活动信息及报名数量
	 * 
	 * @param pd
	 *            参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getActiveAndAppleInfo")
	public PageData getActiveAndAppleInfo(@RequestBody PageData pd) throws Exception {
		pd = activeGroupUserRelateMapper.getActiveAndAppleInfo(pd);
		return WebResult.requestSuccess(pd);
	}
}
