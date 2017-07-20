package com.maryun.restful.app.sp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.maryun.common.service.PushServiceForCall;
import com.maryun.mapper.app.sp.AppSpMapper;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.UuidUtil;
import com.maryun.utils.WebResult;

/**
 * 类名称：MessageRestful 创建人：MARYUN 创建时间：2016年2月13日
 * 
 * @version
 */
@RestController
@RequestMapping(value = "/app/sp/sp")
public class AppSpRestful extends BaseRestful {

	@Autowired
	private AppSpMapper appSpMapper;

	@Autowired
	PushServiceForCall pushServiceForCall;

	/**
	 * 新单列表（分页）
	 * @return
	 */
	@RequestMapping(value = "/getNewOrderInfoList")
	@ResponseBody
	public PageData getNewOrderInfoList(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("UI_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			List<PageData> list = null;
			list = appSpMapper.getNewOrderInfoList(pd);
			return WebResult.requestSuccess(list);
		}
	}

	// 订单中心的数量显示
	@RequestMapping(value = "/checkOrderCenterNum")
	@ResponseBody
	public PageData checkOrderCenterNum(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("UI_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			PageData pdNum = appSpMapper.checkOrderCenterNum(pd);
			return WebResult.requestSuccess(pdNum);
		}
	}

	/**
	 * 全部订单列表（分页）
	 * @return
	 */
	@RequestMapping(value = "/getAllOrderInfoList")
	@ResponseBody
	public PageData getAllOrderInfoList(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("UI_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			List<PageData> list = null;
			list = appSpMapper.getAllOrderInfoList(pd);
			return WebResult.requestSuccess(list);
		}
	}

	/**
	 * 订单详情
	 * @return
	 */
	@RequestMapping(value = "/getOrderInfoDetail")
	@ResponseBody
	public PageData getOrderInfoDetail(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("O_ID")) || StringUtils.isBlank(pd.getString("UI_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			PageData npd = appSpMapper.getOrderInfoDetail(pd);
			return WebResult.requestSuccess(npd);
		}
	}

	/**
	 * 设置接单/拒绝接单
	 * @return
	 */
	@RequestMapping(value = "/setOrderIfAccept")
	@ResponseBody
	public PageData setOrderIfAccept(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("O_ID")) || StringUtils.isBlank(pd.getString("DEAL_FLAG"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			appSpMapper.setOrderIfAccept(pd);
			return WebResult.requestSuccess();
		}
	}

	/**
	 * 待分配订单列表（分页）
	 * @return
	 */
	@RequestMapping(value = "/getUnAssignOrderInfoList")
	@ResponseBody
	public PageData getUnAssignOrderInfoList(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("SP_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			List<PageData> list = null;
			list = appSpMapper.getUnAssignOrderInfoList(pd);
			return WebResult.requestSuccess(list);
		}

	}

	/**
	 * 分配专家
	 * @return
	 */
	@RequestMapping(value = "/assignExpert")
	@ResponseBody
	public PageData assignExpert(@RequestBody PageData pd) throws Exception {

		if (StringUtils.isBlank(pd.getString("O_ID")) || StringUtils.isBlank(pd.getString("UI_ID")) || StringUtils.isBlank(pd.getString("HEL_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			pd.put("OSHA_ID", UuidUtil.get32UUID());
			pd.put("OSHA_STATUS", "0"); // 订单状态默认为0 未处理
			pd.put("CREATE_UID", pd.getString("UI_ID"));
			pd.put("CHANGE_UID", pd.getString("UI_ID"));
			appSpMapper.delAssignedExpert(pd);
			appSpMapper.assignExpert(pd); // 分配专家
			if ("0".equals(pd.getString("OGS_ASSIGN"))) {
				pd.put("OGS_ASSIGN", "1");
				appSpMapper.setAssignStatus(pd);
			}
			else if ("2".equals(pd.getString("OGS_ASSIGN"))) {
				pd.put("OGS_ASSIGN", "3");
				appSpMapper.setAssignStatus(pd);
			}
			// 添加推送.
			PageData orderDetail = appSpMapper.getOrderInfoDetail(pd);
			Map<String, String> extra = new HashMap<String, String>();
			List<String> userList = new ArrayList<String>();
			userList.add(orderDetail.getString("HEL_ID"));
			pushServiceForCall.setAPPKEYANDSecret("4");
			pushServiceForCall.alertTitleMsgExtraCallback("老伴儿", "您有一个新单待查看", extra, userList, "102", pd.getString("UI_ID"), pd.getString("O_ID"), "4");
			return WebResult.requestSuccess();
		}
	}

	/**
	 * 分配陪诊
	 * @return
	 */
	@RequestMapping(value = "/assignAccompany")
	@ResponseBody
	public PageData assignAccompany(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("O_ID")) || StringUtils.isBlank(pd.getString("UI_ID")) || StringUtils.isBlank(pd.getString("AI_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			pd.put("OSHA_ID", UuidUtil.get32UUID());
			pd.put("OSHA_STATUS", "0"); // 订单状态默认为0 未处理
			pd.put("CREATE_UID", pd.getString("UI_ID"));
			pd.put("CHANGE_UID", pd.getString("UI_ID"));
			appSpMapper.delAssignedAccompany(pd);
			appSpMapper.assignAccompany(pd); // 分配陪诊
			if ("0".equals(pd.getString("OGS_ASSIGN"))) {
				pd.put("OGS_ASSIGN", "2");
				appSpMapper.setAssignStatus(pd);
			}
			else if ("1".equals(pd.getString("OGS_ASSIGN"))) {
				pd.put("OGS_ASSIGN", "3");
				appSpMapper.setAssignStatus(pd);
			}
			// 添加推送.
			PageData orderDetail = appSpMapper.getOrderInfoDetail(pd);
			Map<String, String> extra = new HashMap<String, String>();
			List<String> userList = new ArrayList<String>();
			userList.add(orderDetail.getString("AI_ID"));
			pushServiceForCall.setAPPKEYANDSecret("2");
			pushServiceForCall.alertTitleMsgExtraCallback("老伴儿", "您有一个新单待查看", extra, userList, "102", pd.getString("UI_ID"), pd.getString("O_ID"), "2");
			return WebResult.requestSuccess();
		}
	}

	/**
	 * 查看陪诊工作日程
	 * @return
	 */
	@RequestMapping(value = "/getAccompanyCalendarPlan")
	@ResponseBody
	public PageData getAccompanyCalendarPlan(@RequestBody PageData pd) throws Exception {

		if (StringUtils.isBlank(pd.getString("UI_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			List<PageData> res = appSpMapper.getAccompanyCalendarPlan(pd);
			return WebResult.requestSuccess(res);
		}
	}

	/**
	 * 已分配订单列表（分页）
	 * @return
	 */
	@RequestMapping(value = "/getAssignedOrderInfoList")
	@ResponseBody
	public PageData getAssignedOrderInfoList(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("SP_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			List<PageData> list = null;
			list = appSpMapper.getAssignedOrderInfoList(pd);
			return WebResult.requestSuccess(list);
		}
	}

	/**
	 * 已评价订单列表（分页）
	 * @return
	 */
	@RequestMapping(value = "/getOrderEvaluatedInfoList")
	@ResponseBody
	public PageData getOrderEvaluatedInfoList(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("SP_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			List<PageData> list = null;
			list = appSpMapper.getOrderEvaluatedInfoList(pd);
			return WebResult.requestSuccess(list);
		}
	}

	/**
	 * sp评价（分页）
	 * @return
	 */
	@RequestMapping(value = "/getEvaluateOfSpList")
	@ResponseBody
	public PageData getEvaluateOfSpList(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("UI_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			List<PageData> list = null;
			list = appSpMapper.getEvaluateOfSpList(pd);
			return WebResult.requestSuccess(list);
		}
	}

	/**
	 * 订单评价
	 * @return
	 */
	@RequestMapping(value = "/getEvaluateOfSpOrder")
	@ResponseBody
	public PageData getEvaluateOfSpOrder(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("O_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			PageData npd = appSpMapper.getEvaluateOfSpOrder(pd);
			return WebResult.requestSuccess(npd);
		}
	}

	/**
	 * 代理商接单排名和累计接单数量 后台传过来的实际上是SP_ID
	 * @param 消息id
	 */
	@RequestMapping(value = "/getRankLast")
	@ResponseBody
	public PageData getRankLast(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("UI_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			PageData accinfo = appSpMapper.getRankLast(pd);
			return WebResult.requestSuccess(accinfo);
		}
	}

	/**
	 * 代理商每日接单数量 后台传过来的实际上是SP_ID
	 * @param 消息id
	 */
	@RequestMapping(value = "/getRankMonth")
	@ResponseBody
	public PageData getRankMonth(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("UI_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			PageData accinfo = appSpMapper.getRankMonth(pd);
			return WebResult.requestSuccess(accinfo);
		}
	}

	/**
	 * 陪诊列表
	 * @return
	 */
	@RequestMapping(value = "/getAccompanyList")
	@ResponseBody
	public PageData getAccompanyList(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("AI_BELONGAGENT"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			List<PageData> list = null;
			list = appSpMapper.getAccompanyList(pd);
			return WebResult.requestSuccess(list);
		}
	}

	/**
	 * 专家列表
	 * @return
	 */
	@RequestMapping(value = "/getDoctorList")
	@ResponseBody
	public PageData getDoctorList(@RequestBody PageData pd) throws Exception {
		List<PageData> accInfo = appSpMapper.getDoctorList(pd);
		return WebResult.requestSuccess(accInfo);
	}

	/**
	 * 专家接单排名和累计接单数量
	 * @param 消息id
	 */
	@RequestMapping(value = "/getRankLast_expert")
	@ResponseBody
	public PageData getRankLast_expert(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("HEL_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			PageData accinfo = appSpMapper.getRankLast_expert(pd);
			return WebResult.requestSuccess(accinfo);
		}
	}

	/**
	 * 专家每月接单数量
	 * @param 消息id
	 */
	@RequestMapping(value = "/getRankMonth_expert")
	@ResponseBody
	public PageData getRankMonth_expert(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("HEL_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			PageData accinfo = appSpMapper.getRankMonth_expert(pd);
			return WebResult.requestSuccess(accinfo);
		}
	}

	/**
	 * 接单
	 * @return
	 */
	@RequestMapping(value = "/receiveOrder")
	@ResponseBody
	public PageData receiveOrder(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("O_ID")) || StringUtils.isBlank(pd.getString("UI_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			appSpMapper.receiveOrder(pd);
			appSpMapper.updateOrderHelStatus(pd); // 更改专家订单表的状态 由-1--->0

			PageData orderDetail = appSpMapper.getOrderInfoDetail(pd);
			Map<String, String> extra = new HashMap<String, String>();
			// 添加成功后进行推送.
			List<String> userList = new ArrayList<String>();
			userList.add(orderDetail.getString("HEL_ID"));
			pushServiceForCall.setAPPKEYANDSecret("4");
			pushServiceForCall.alertTitleMsgExtraCallback("老伴儿", "您有一个新单待查看", extra, userList, "102", pd.getString("UI_ID"), pd.getString("O_ID"), "4");
			return WebResult.requestSuccess();
		}
	}

	/**
	 * 拒绝接单
	 * @return
	 */
	@RequestMapping(value = "/refuseOrder")
	@ResponseBody
	public PageData refuseOrder(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("O_ID")) || StringUtils.isBlank(pd.getString("UI_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			appSpMapper.refuseOrder(pd);
//			appSpMapper.delOrderHel(pd); // 删除事先预分配的专家
			pd.put("OGS_ASSIGN", "0"); // 分配改为未分配
			appSpMapper.setAssignStatus(pd);
			return WebResult.requestSuccess();
		}
	}

	/**
	 * 设置退款
	 * @return
	 */
	@RequestMapping(value = "/setRefundStatus")
	@ResponseBody
	public PageData setRefundStatus(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("O_ID")) || StringUtils.isBlank(pd.getString("UI_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			appSpMapper.setRefundStatus(pd);
			PageData orderDetail = appSpMapper.getOrderInfoDetail(pd);
			Map<String, String> extra = new HashMap<String, String>();
			// 添加成功后进行推送.
			List<String> userList = new ArrayList<String>();
			userList.add(orderDetail.getString("UI_ID"));
			pushServiceForCall.setAPPKEYANDSecret("1");
			pushServiceForCall.alertTitleMsgExtraCallback("老伴儿", "您申请退单成功，订单号" + orderDetail.getString("O_NUM"), extra, userList, "103", pd.getString("UI_ID"),
					pd.getString("O_ID"), "1");
			return WebResult.requestSuccess();
		}
	}
}
