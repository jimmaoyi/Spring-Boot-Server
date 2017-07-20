package com.maryun.lb.restful.jyb.agency;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.maryun.common.service.PushServiceForCall;
import com.maryun.lb.mapper.jyb.agency.AgencyOrderManagementMapper;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.UuidUtil;
import com.maryun.utils.WebResult;

/**
 * 
 * @version
 */
@RestController
@RequestMapping(value = "/agency")
public class AgencyOrderManagementRestful extends BaseRestful {

	@Autowired
	private AgencyOrderManagementMapper orderManagementMapper;
	@Autowired
	PushServiceForCall pushServiceForCall;

	/**
	 * 订单查询(未处理订单)
	 * 
	 * @param pd 查询、分页条件
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/pageSearch")
	public PageData pageSearch(@RequestBody PageData pd) throws Exception {
		List<PageData> lists = null;
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
	 * 订单查询(全部订单)
	 * 
	 * @param pd 查询、分页条件
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/pageSearch_all")
	public PageData pageSearch_all(@RequestBody PageData pd) throws Exception {
		List<PageData> lists = null;
		if (pd.getPageNumber() != 0) {
			PageHelper.startPage(pd.getPageNumber(), pd.getPageSize());
		}
		lists = orderManagementMapper.listPageAll(pd);
		// 分页
		pd = this.getPagingPd(lists);
		// 结果集封装
		return WebResult.requestSuccess(pd);
	}

	/**
	 * 全部专家(代理商分派)
	 * 
	 * @param pd 查询、分页条件
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/pageSearchAssign_expert")
	public PageData pageSearchAssign_expert(@RequestBody PageData pd) throws Exception {
		List<PageData> lists = null;
		if (pd.getPageNumber() != 0) {
			PageHelper.startPage(pd.getPageNumber(), pd.getPageSize());
		}
		lists = orderManagementMapper.listPageAll_expert(pd);
		// 分页
		pd = this.getPagingPd(lists);
		// 结果集封装
		return WebResult.requestSuccess(pd);
	}
	
	/**
	 * 查询专家是否已接单
	 * 
	 * @param pd 查询、分页条件
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/selectExpertOrders")
	public PageData selectExpertOrders(@RequestBody PageData pd) throws Exception {
		
		PageData pds = orderManagementMapper.selectExpertOrders(pd);
		if(pds != null){
			pd.put("msg", "fielt");
		}else{
			pd.put("msg", "success");
		}
		// 结果集封装
		return WebResult.requestSuccess(pd);
	}
	/**
	 * 查询专家是否已接单
	 * 
	 * @param pd 查询、分页条件
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/selectAccompantOrders")
	public PageData selectAccompantOrders(@RequestBody PageData pd) throws Exception {
		
		PageData pds = orderManagementMapper.selectAccompantOrders(pd);
		if(pds != null){
			pd.put("msg", "fielt");
		}else{
			pd.put("msg", "success");
		}
		// 结果集封装
		return WebResult.requestSuccess(pd);
	}

	/**
	 * 全部陪诊人员(代理商分派)
	 * 
	 * @param pd 查询、分页条件
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/pageSearchAssign_accompany")
	public PageData pageSearchAssign_accompany(@RequestBody PageData pd) throws Exception {
		List<PageData> lists = null;
		if (pd.getPageNumber() != 0) {
			PageHelper.startPage(pd.getPageNumber(), pd.getPageSize());
		}
		lists = orderManagementMapper.listPageAll_accompany(pd);
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
	 * 代理商未分派给专家
	 * 
	 * @param pd 查询、分页条件
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/assignToExpert")
	public PageData assignToExpert(@RequestBody PageData pd) throws Exception {
		List<PageData> lists = null;
		if (pd.getPageNumber() != 0) {
			PageHelper.startPage(pd.getPageNumber(), pd.getPageSize());
		}
		lists = orderManagementMapper.assignToExpert(pd);
		// 分页
		pd = this.getPagingPd(lists);
		// 结果集封装
		return WebResult.requestSuccess(pd);
	}

	/**
	 * 代理商未分派给陪诊人员
	 * 
	 * @param pd 查询、分页条件
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/assignToAccompany")
	public PageData assignToAccompany(@RequestBody PageData pd) throws Exception {
		List<PageData> lists = null;
		if (pd.getPageNumber() != 0) {
			PageHelper.startPage(pd.getPageNumber(), pd.getPageSize());
		}
		lists = orderManagementMapper.assignToAccompany(pd);
		// 分页
		pd = this.getPagingPd(lists);
		// 结果集封装
		return WebResult.requestSuccess(pd);
	}

	/**
	 * 代理商未分派给陪诊人员,也没有分派给专家
	 * 
	 * @param pd 查询、分页条件
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/assignToExpertAndAccompany")
	public PageData assignToExpertAndAccompany(@RequestBody PageData pd) throws Exception {
		List<PageData> lists = null;
		if (pd.getPageNumber() != 0) {
			PageHelper.startPage(pd.getPageNumber(), pd.getPageSize());
		}
		lists = orderManagementMapper.assignToExpertAndAccompany(pd);
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
	 * 修改接单状态
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/changeSpStatus")
	public PageData changeSpStatus(@RequestBody PageData pd) throws Exception {
		// 修改
		orderManagementMapper.changeSpStatus(pd);
		pd.put("OSHA_STATUS", "0");
		orderManagementMapper.updateoshaid(pd);
		orderManagementMapper.updateOrderHel(pd);
		
		pd.put("msg", "success");
		return WebResult.requestSuccess(pd);
	}
	/**
	 * 修改接单状态
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/notPass")
	public PageData notPass(@RequestBody PageData pd) throws Exception {
		// 修改
		pd.put("OGS_STATUS", "2");
		orderManagementMapper.deleteOrderRecoding(pd);
		orderManagementMapper.updateOrderRecoding(pd);
		pd.put("msg", "success");
		return WebResult.requestSuccess(pd);
	}

	/**
	 * 向专家、代理商、订单表中添加一条数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/assign_expertSure")
	public PageData assign_expertSure(@RequestBody PageData pd) throws Exception {
		// 先从数据库中根据订单ID从关联表中查找，如果没有，则添加，如果有，先修改再添加新的
		PageData pageData = orderManagementMapper.findAssignExpertById(pd);
		if (pageData != null) {
			// 先更新
			orderManagementMapper.changeAssignExpertDelStatus(pd);
		}

		pd.put("OSHA_ID", UuidUtil.get32UUID());
		pd.put("DEL_STATE", "1");// 是否删除，默认为1，未删除
		pd.put("OSHA_STATUS", "1");// 接单状态
		// TODO 从session中取代理商ID
		//pd.put("SP_ID", "1");// 写死了，，后面要修改
		PageData pd_id = orderManagementMapper.findID(pd);
		pd.put("SP_ID", pd_id.getString("SP_ID"));
		// TODO create_UID
		// 添加
		orderManagementMapper.addAssignExpert(pd);
		
		//修改orderkey_id
		orderManagementMapper.updateExpert(pd);
		
		//向专家推送一条记录
		
		Map<String, String> extra = new HashMap<String, String>();
		Map<String, Object> extraContent = new HashMap<String, Object>();
		extra.put("KEY_PARAM", JSON.toJSONString(extraContent));
		 
		 //推送接收者的id
		List<String> userList = new ArrayList<String>();
		userList.add(pd.getString("HEL_ID"));
		 
		 //推送类型
		pushServiceForCall.setAPPKEYANDSecret("4");
		 
		pushServiceForCall.alertTitleMsgExtraCallback("您有一条新单提醒", "您有一条新单提醒", extra, userList, "102", pd.getString("SP_ID"), pd.getString("O_ID"), "4");
		pd.put("msg", "success");
		return WebResult.requestSuccess(pd);
	}

	/**
	 * 向陪诊人员、代理商、订单表中添加一条数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/assign_accompanySure")
	public PageData assign_accompanySure(@RequestBody PageData pd) throws Exception {
		// 先从数据库中根据订单ID从关联表中查找，如果没有，则添加，如果有，先修改再添加新的
		PageData pageData = orderManagementMapper.findAssignAccompanyById(pd);
		if (pageData != null) {
			orderManagementMapper.changeAssignAccompanyDelStatus(pd);
		}

		// 先从数据库中根据订单ID从关联表中查找，如果没有，则添加，如果有，则修改

		pd.put("OSHA_ID", UuidUtil.get32UUID());
		pd.put("DEL_STATE", "1");// 是否删除，默认为1，未删除
		pd.put("OSHA_STATUS", "0");// 接单状态
		// TODO 从session中取代理商ID
		//pd.put("SP_ID", "1");// 写死了，后面要修改
		PageData pd_id = orderManagementMapper.findID(pd);
		pd.put("SP_ID", pd_id.getString("SP_ID"));
		// TODO create_UID
		// 添加
		orderManagementMapper.addAssignAccompany(pd);
		orderManagementMapper.updateOrderAssign(pd);
		
		//下面是给陪诊推送一条信息
		 Map<String, String> extra = new HashMap<String, String>();
		 Map<String, Object> extraContent = new HashMap<String, Object>();
		 extra.put("KEY_PARAM", JSON.toJSONString(extraContent));
		 
		 //推送接收者的id
		 List<String> userList = new ArrayList<String>();
		 userList.add(pd.getString("AI_ID"));
		 
		 //推送类型
		 pushServiceForCall.setAPPKEYANDSecret("2");
		 
		 pushServiceForCall.alertTitleMsgExtraCallback("您有一条新单提醒", "您有一条新单提醒", extra, userList, "102", pd.getString("SP_ID"), pd.getString("O_ID"), "2");
		 
		 pd.put("msg", "success");
		 return WebResult.requestSuccess(pd);
	}
	/**
	 * 给朱娜家推送一条消息
	 */
	private void pushExpert(PageData pd)throws Exception{
		
		 //推送时所带的内容
		 Map<String, String> extra = new HashMap<String, String>();
		 Map<String, Object> extraContent = new HashMap<String, Object>();
		 extra.put("KEY_PARAM", JSON.toJSONString(extraContent));
		 
		 //推送接收者的id
		 List<String> userList = new ArrayList<String>();
		 userList.add(pd.getString("HEL_ID"));
		 
		 //推送类型
		 pushServiceForCall.setAPPKEYANDSecret("4");
		 
		 pushServiceForCall.alertTitleMsgExtraCallback("您有一条新单提醒", "您有一条新单提醒", extra, userList, "101", pd.getString("GI_ID"), pd.getString("O_ID"), "4");
		 
	}

	/**
	 * 退款
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/prrefund")
	public PageData prrefund(@RequestBody PageData pd) throws Exception {
		// 修改
		String s_IDS = pd.getString("IDS");
		if (null != s_IDS && !"".equals(s_IDS)) {
			boolean flag = true;
			String[] IDSs = s_IDS.split(",");
			pd.put("IDSs", IDSs);
			for (int i = 0; i < IDSs.length; i++) {
				pd.put("O_ID", IDSs[i]);
				// 只查询订单状态是申请退单的
				PageData pdStatus = orderManagementMapper.findOrderStatusById(pd);
				if (!(pdStatus.get("O_STATUS").equals("4"))) {// 订单状态不是4的，即不是为申请退单的
					flag = false;
				}
			}

			if (flag) {
				// 选中的订单类型全是申请退单的
				// orderManagementMapper.changerefund(pd);
				// pd.put("msg", "success");
			}
			else {
				pd.put("msg", "error");
			}

		}
		else {
			pd.put("msg", "failed");
		}
		return WebResult.requestSuccess(pd);
	}

	/**
	 * 退款
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/refund")
	public PageData refund(@RequestBody PageData pd) throws Exception {
		// 修改
		String s_IDS = pd.getString("IDS");
		if (null != s_IDS && !"".equals(s_IDS)) {
			boolean flag = true;
			String[] IDSs = s_IDS.split(",");
			pd.put("IDSs", IDSs);
			for (int i = 0; i < IDSs.length; i++) {
				pd.put("O_ID", IDSs[i]);
				// 只查询订单状态是申请退单的
				PageData pdStatus = orderManagementMapper.findOrderStatusById(pd);
				if (!(pdStatus.get("O_STATUS").equals("4"))) {// 订单状态不是4的，即不是为申请退单的
					flag = false;
				}
			}

			if (flag) {
				// 选中的订单类型全是申请退单的
				orderManagementMapper.changerefund(pd);
				pd.put("msg", "success");
				PageData pdTmp =  orderManagementMapper.selectSPID(pd);
				List<PageData> lists = orderManagementMapper.selectUserId(pd);
				for (PageData pageData : lists) {
					Map<String, String> extra = new HashMap<String, String>();
				    Map<String, Object> extraContent = new HashMap<String, Object>();
				    extra.put("KEY_PARAM", JSON.toJSONString(extraContent));

				    // 推送接受者的id
				    List<String> userList = new ArrayList<String>();
				    userList.add(pageData.getString("UI_ID"));

				    // 推送类型，如用户退给代理，这里就是3表示推给代理
				    pushServiceForCall.setAPPKEYANDSecret("1");

				    pushServiceForCall.alertTitleMsgExtraCallback("您有一条退单提醒", "您有一条退单提醒", extra, userList, "103",pdTmp.getString("SP_ID"), pageData.getString("O_ID"), "1");
				}
				  	
			}
			else {
				pd.put("msg", "error");
			}

		}
		else {
			pd.put("msg", "failed");
		}
		return WebResult.requestSuccess(pd);
	}
}
