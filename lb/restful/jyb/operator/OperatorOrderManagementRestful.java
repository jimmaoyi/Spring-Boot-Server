package com.maryun.lb.restful.jyb.operator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.maryun.lb.mapper.jyb.operator.OperatorOrderManagementMapper;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.WebResult;

/**
 * 类名称：AuthController 创建人：MARYUN 创建时间：2017年1月18日
 * 
 * @version
 */
@RestController
@RequestMapping(value = "/operator")
public class OperatorOrderManagementRestful extends BaseRestful {

	@Autowired
	private OperatorOrderManagementMapper orderManagementMapper;

	/**
	 * 订单查询
	 * 
	 * @param pd 查询、分页条件
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/pageSearch")
	public PageData pageSearch(@RequestBody PageData pd) throws Exception {
		List<PageData> lists = null;
		PageData pds = orderManagementMapper.selectUserArea(pd);
		if(pds != null){
			pd.put("UA_AREA", pds.getString("UA_AREA"));
		}
		if (pd.getPageNumber() != 0) {
			PageHelper.startPage(pd.getPageNumber(), pd.getPageSize());
		}
		
		lists = orderManagementMapper.listPage(pd);
		// 分页
		pd = this.getPagingPd(lists);
		// 结果集封装
		return WebResult.requestSuccess(pd);
	}

	/**
	 * 用户信息沟通备忘录
	 * 
	 * @param pd 查询、分页条件
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/pageSearch_MEMO")
	public PageData pageSearch_MEMO(@RequestBody PageData pd) throws Exception {
		List<PageData> lists = null;
		if (pd.getPageNumber() != 0) {
			PageHelper.startPage(pd.getPageNumber(), pd.getPageSize());
		}
		lists = orderManagementMapper.listPageMEMO(pd);
		// 分页
		pd = this.getPagingPd(lists);
		// 结果集封装
		return WebResult.requestSuccess(pd);
	}

	/**
	 * 订单查询(点击页面中的用户订单)
	 * 
	 * @param pd 查询、分页条件
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/pageSearch_historyorder")
	public PageData pageSearch_historyorder(@RequestBody PageData pd) throws Exception {
		List<PageData> lists = null;
		if (pd.getPageNumber() != 0) {
			PageHelper.startPage(pd.getPageNumber(), pd.getPageSize());
		}
		lists = orderManagementMapper.listPagehistoryorder(pd);
		// 分页
		pd = this.getPagingPd(lists);
		// 结果集封装
		return WebResult.requestSuccess(pd);
	}

	/**
	 * 订单查询中的用户历史订单信息
	 * 
	 * @param pd 查询、分页条件
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/uinameDesc")
	public PageData uinameDesc(@RequestBody PageData pd) throws Exception {
		List<PageData> lists = null;
		if (pd.getPageNumber() != 0) {
			PageHelper.startPage(pd.getPageNumber(), pd.getPageSize());
		}
		lists = orderManagementMapper.uinameDesc(pd);
		// 分页
		pd = this.getPagingPd(lists);
		// 结果集封装
		return WebResult.requestSuccess(pd);
	}

	/**
	 * 订单查询中的订单详细信息
	 * 
	 * @param pd 查询、分页条件
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/onumDesc")
	public PageData onumDesc(@RequestBody PageData pd) throws Exception {
		List<PageData> lists = null;
		if (pd.getPageNumber() != 0) {
			PageHelper.startPage(pd.getPageNumber(), pd.getPageSize());
		}
		lists = orderManagementMapper.onumDesc(pd);
		// 分页
		pd = this.getPagingPd(lists);
		// 结果集封装
		return WebResult.requestSuccess(pd);
	}

	/**
	 * 订单查询中的专家库信息
	 * 
	 * @param pd 查询、分页条件
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/expertLibsDesc")
	public PageData expertLibsDesc(@RequestBody PageData pd) throws Exception {
		List<PageData> lists = null;
		if (pd.getPageNumber() != 0) {
			PageHelper.startPage(pd.getPageNumber(), pd.getPageSize());
		}
		lists = orderManagementMapper.expertLibsDesc(pd);
		// 分页
		pd = this.getPagingPd(lists);
		// 结果集封装
		return WebResult.requestSuccess(pd);
	}

	/**
	 * 订单查询中的代理商信息
	 * 
	 * @param pd 查询、分页条件
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/spDesc")
	public PageData spDesc(@RequestBody PageData pd) throws Exception {
		List<PageData> lists = null;
		if (pd.getPageNumber() != 0) {
			PageHelper.startPage(pd.getPageNumber(), pd.getPageSize());
		}
		lists = orderManagementMapper.spDesc(pd);
		// 分页
		pd = this.getPagingPd(lists);
		// 结果集封装
		return WebResult.requestSuccess(pd);
	}

	/**
	 * 订单查询中的陪诊人员列表信息
	 * 
	 * @param pd 查询、分页条件
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/accompanyDesc")
	public PageData accompanyDesc(@RequestBody PageData pd) throws Exception {
		List<PageData> lists = null;
		if (pd.getPageNumber() != 0) {
			PageHelper.startPage(pd.getPageNumber(), pd.getPageSize());
		}
		lists = orderManagementMapper.accompanyDesc(pd);
		// 分页
		pd = this.getPagingPd(lists);
		// 结果集封装
		return WebResult.requestSuccess(pd);
	}

	/**
	 * 订单查询中的就诊过程记录列表信息
	 * 
	 * @param pd 查询、分页条件
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/keyNodeDesc")
	public PageData keyNodeDesc(@RequestBody PageData pd) throws Exception {
		List<PageData> lists = null;
		if (pd.getPageNumber() != 0) {
			PageHelper.startPage(pd.getPageNumber(), pd.getPageSize());
		}
		lists = orderManagementMapper.keyNodeDesc(pd);
		// 分页
		pd = this.getPagingPd(lists);
		// 结果集封装
		return WebResult.requestSuccess(pd);
	}

	/**
	 * 订单查询中的评价内容信息
	 * 
	 * @param pd 查询、分页条件
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/evalDesc")
	public PageData evalDesc(@RequestBody PageData pd) throws Exception {
		List<PageData> lists = null;
		if (pd.getPageNumber() != 0) {
			PageHelper.startPage(pd.getPageNumber(), pd.getPageSize());
		}
		lists = orderManagementMapper.evalDesc(pd);
		// 分页
		pd = this.getPagingPd(lists);
		// 结果集封装
		return WebResult.requestSuccess(pd);
	}

	/**
	 * 订单查询中的评价星级信息
	 * 
	 * @param pd 查询、分页条件
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/evallevelDesc")
	public PageData evallevelDesc(@RequestBody PageData pd) throws Exception {
		List<PageData> lists = null;
		if (pd.getPageNumber() != 0) {
			PageHelper.startPage(pd.getPageNumber(), pd.getPageSize());
		}
		lists = orderManagementMapper.evallevelDesc(pd);
		// 分页
		pd = this.getPagingPd(lists);
		// 结果集封装
		return WebResult.requestSuccess(pd);
	}

	/**
	 * 修改订单状态
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/changeOrderStatus")
	public PageData changeOrderStatus(@RequestBody PageData pd) throws Exception {
		// 修改
		orderManagementMapper.changeOrderStatus(pd);
		pd.put("msg", "success");
		return WebResult.requestSuccess(pd);
	}
}
