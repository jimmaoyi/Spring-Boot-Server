package com.maryun.restful.app.accompany.order;

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
import com.maryun.mapper.app.accompany.AppAccompanyMapper;
import com.maryun.mapper.app.accompany.order.JybAPPAccompanyMobileMyOrderMapper;
import com.maryun.mapper.app.user.order.JybUserMobilePersonalMyOrderMapper;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.WebResult;

@RestController
@RequestMapping(value = "/app/accompany/order")
public class JybAPPAccompanyMobileMyOrderRestful extends BaseRestful {

	@Autowired
	private JybAPPAccompanyMobileMyOrderMapper jybAPPAccompanyMobileMyOrderMapper;

	@Autowired
	private JybUserMobilePersonalMyOrderMapper jybUserMobilePersonalMyOrderMapper;

	@Autowired
	PushServiceForCall pushServiceForCall;

	@Autowired
	private AppAccompanyMapper appAccompanyMapper;

	/**
	 * 根据陪诊人员的ai_id和osha_status接单状态查询所有的订单信息
	 * @param AI_ID
	 * @param OSHA_STATUS
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/orderList")
	@ResponseBody
	public PageData orderList(@RequestBody PageData pd) throws Exception {

		PageData pdMyOrderInfor = new PageData();

		if (StringUtils.isBlank(pd.getString("UI_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}

		List<PageData> list = null;

		// 因为数据库的陪诊订单状态只有0未处理 1接收 2拒绝
		// 所以只要大于2的userId去数据库查询的时候为空即可查询全部的订单显示
		if (StringUtils.isBlank(pd.getString("OSHA_STATUS"))) {
			pd.put("OSHA_STATUS", null);
		}
		else if (!StringUtils.isBlank(pd.getString("OSHA_STATUS")) && Integer.parseInt(pd.getString("OSHA_STATUS")) > 2) {
			pd.put("OSHA_STATUS", null);
		}
		// pd.put("AI_ID", ai_id);
		// pd.put("OSHA_STATUS", osha_status);
		list = jybAPPAccompanyMobileMyOrderMapper.orderList(pd);

		pdMyOrderInfor.put("falge", pd.getString("OSHA_STATUS") + "");
		pdMyOrderInfor.put("result", list);
		return WebResult.requestSuccess(pdMyOrderInfor);

	}

	/**
	 * 根据订单的order_id查询该订单的详细信息
	 * @param order_id
	 * @return
	 */
	// 说明：和用户的订单详细信息查询一致，所以注释不用
	/*
	 * @RequestMapping("orderInfoByOrderId")
	 * 
	 * @ResponseBody public PageData orderInfoByOrderId(Integer order_id){
	 * PageData pdResult = new PageData(); PageData pd = this.getPageData();
	 * if(order_id != null){ pd.put("O_ID", order_id); PageData orderInfo =
	 * jybAPPAccompanyMobileMyOrderMapper.orderInfoByOrderId(pd);
	 * System.out.println("订单信息-----------------"+orderInfo); if(orderInfo !=
	 * null){ pdResult.put("errorCode", "0"); pdResult.put("message",
	 * "查询订单信息成功"); pdResult.put("result", orderInfo); }else{
	 * pdResult.put("errorCode", "1"); pdResult.put("message", "查询订单信息失败");
	 * pdResult.put("result", null); }
	 * 
	 * }else{ pdResult.put("errorCode", "error"); pdResult.put("message",
	 * "请求参数丢失"); pdResult.put("result", null); } return pdResult; }
	 */

	/**
	 * 陪诊人员根据ai_id和订单的o_id接受用户订单
	 * @param O_ID
	 * @param AI_ID
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/receiveOrder")
	@ResponseBody
	public PageData receiveOrder(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("AI_ID")) || StringUtils.isBlank(pd.getString("O_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		jybAPPAccompanyMobileMyOrderMapper.receiveOrder(pd);
		jybAPPAccompanyMobileMyOrderMapper.updateOrderStatus(pd); // 更新订单状态为服务中
		// 添加推送.
		PageData orderDetail = appAccompanyMapper.getNewOrderInfoDetail(pd);
		Map<String, String> extra = new HashMap<String, String>();
		List<String> userList = new ArrayList<String>();
		userList.add(orderDetail.getString("SP_ID"));
		pushServiceForCall.setAPPKEYANDSecret("3");
		pushServiceForCall.alertTitleMsgExtraCallback("老伴儿", "订单：" + orderDetail.getString("O_NUM") + "已被陪诊接单", extra, userList, "102", pd.getString("AI_ID"),
				pd.getString("O_ID"), "3");
		userList.clear();
		userList.add(orderDetail.getString("UI_ID"));
		pushServiceForCall.setAPPKEYANDSecret("1");
		pushServiceForCall.alertTitleMsgExtraCallback("老伴儿", "订单：" + orderDetail.getString("O_NUM") + "已被陪诊接单", extra, userList, "102", pd.getString("AI_ID"),
				pd.getString("O_ID"), "1");
		return WebResult.requestSuccess();
	}

	/**
	 * 陪诊人员根据ai_id和订单的o_id拒绝用户订单
	 * @param order_id
	 * @param ai_id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/refuseOrder")
	@ResponseBody
	public PageData refuseOrder(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("AI_ID")) || StringUtils.isBlank(pd.getString("O_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		jybAPPAccompanyMobileMyOrderMapper.refuseOrder(pd);
		List<PageData> list = jybAPPAccompanyMobileMyOrderMapper.getGuideSpAssign(pd);
		if (list.size() > 0) {
			String status = list.get(0).getString("OGS_ASSIGN");
			if ("3".equals(status)) {
				pd.put("OGS_ASSIGN", "1");
			}
			else if ("2".equals(status)) {
				pd.put("OGS_ASSIGN", "0");
			}
			jybAPPAccompanyMobileMyOrderMapper.updateGuideSpAssign(pd);
		}
		// 添加推送.
		PageData orderDetail = appAccompanyMapper.getNewOrderInfoDetail(pd);
		Map<String, String> extra = new HashMap<String, String>();
		List<String> userList = new ArrayList<String>();
		userList.add(orderDetail.getString("SP_ID"));
		pushServiceForCall.setAPPKEYANDSecret("3");
		pushServiceForCall.alertTitleMsgExtraCallback("老伴儿", "订单：" + orderDetail.getString("O_NUM") + "已被陪诊拒绝", extra, userList, "102", pd.getString("AI_ID"),
				pd.getString("O_ID"), "3");
		return WebResult.requestSuccess();
	}

	/**
	 * 根据订单id获取订单里程碑节点
	 * @param O_ID
	 * @param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getOrderNodeList")
	@ResponseBody
	public PageData getOrderNodeList(@RequestBody PageData pd) throws Exception {
		List<PageData> list = null;
		if (StringUtils.isBlank(pd.getString("O_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			list = jybAPPAccompanyMobileMyOrderMapper.getOrderNodeList(pd);
		}
		return WebResult.requestSuccess(list);

	}

	// checkAIOrderCenterNum订单中心数量显示查询陪诊的已接单的订单为正在服务中状态的数量
	@RequestMapping(value = "/checkAIOrderCenterNum")
	@ResponseBody
	public PageData checkAIOrderCenterNum(@RequestBody PageData pd) throws Exception {

		if (StringUtils.isBlank(pd.getString("UI_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}

		PageData pdOCNum = jybAPPAccompanyMobileMyOrderMapper.checkAIOrderCenterNum(pd);

		return WebResult.requestSuccess(pdOCNum);

	}

	/**
	 * 申请订单完结
	 * @param order_id
	 * @param ai_id
	 * @return
	 */
	@RequestMapping("/applyOrderEnd")
	@ResponseBody
	public PageData applyOrderEnd(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("O_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		jybAPPAccompanyMobileMyOrderMapper.applyOrderEnd(pd);

		// 申请完结后通知用户
		PageData pdMessage = jybUserMobilePersonalMyOrderMapper.orderInfoByOrderId(pd);

		// 推送时带的内容
		Map<String, String> extra = new HashMap<String, String>();
		extra.put("KEY_PARAM", "");

		// 推送接受者的id
		List<String> userList = new ArrayList<String>();
		userList.add(pdMessage.getString("uiid"));

		// 推送类型，如用户退给代理，这里就是3表示推给代理
		pushServiceForCall.setAPPKEYANDSecret("1");
		pushServiceForCall.alertTitleMsgExtraCallback("您有一条订单完结提醒", "您有一条订单完结提醒", extra, userList, "104", pdMessage.getString("AI_ID"), pd.getString("O_ID"),
				"1");

		return WebResult.requestSuccess();
	}

	/**
	 * 节点详情
	 * @param
	 * @param
	 * @return
	 */
	@RequestMapping("/getNodeDetail")
	@ResponseBody
	public PageData getNodeDetail(@RequestBody PageData pd) {
		if (StringUtils.isBlank(pd.getString("KN_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		PageData res = jybAPPAccompanyMobileMyOrderMapper.getNodeDetail(pd);
		return WebResult.requestSuccess(res);
	}

	/**
	 * 插入节点
	 * @param
	 * @param
	 * @return
	 */
	@RequestMapping("/insertNode")
	@ResponseBody
	public PageData insertNode(@RequestBody PageData pd) {
		if (StringUtils.isBlank(pd.getString("NM_NAME")) || StringUtils.isBlank(pd.getString("O_ID")) || StringUtils.isBlank(pd.getString("KN_TIME"))
				|| StringUtils.isBlank(pd.getString("KN_DAY")) || StringUtils.isBlank(pd.getString("IS_SCHEDULE"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		jybAPPAccompanyMobileMyOrderMapper.insertNode(pd);
		return WebResult.requestSuccess();
	}

	/**
	 * 插入节点
	 * @param
	 * @param
	 * @return
	 */
	@RequestMapping("/updateNode")
	@ResponseBody
	public PageData updateNode(@RequestBody PageData pd) {
		if (StringUtils.isBlank(pd.getString("KN_ID")) || StringUtils.isBlank(pd.getString("KN_TIME")) || StringUtils.isBlank(pd.getString("KN_DAY"))
				|| StringUtils.isBlank(pd.getString("IS_SCHEDULE"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		jybAPPAccompanyMobileMyOrderMapper.updateNode(pd);
		return WebResult.requestSuccess();
	}

	/**
	 * 删除节点
	 * @param
	 * @param
	 * @return
	 */
	@RequestMapping("/deleteNodeById")
	@ResponseBody
	public PageData deleteNodeById(@RequestBody PageData pd) {
		if (StringUtils.isBlank(pd.getString("KN_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		jybAPPAccompanyMobileMyOrderMapper.deleteNodeById(pd);
		return WebResult.requestSuccess();
	}

	/**
	 * 得到模板节点集合
	 * @param
	 * @param
	 * @return
	 */
	@RequestMapping("/getNodeModel")
	@ResponseBody
	public PageData getNodeModel(@RequestBody PageData pd) {
		if (StringUtils.isBlank(pd.getString("O_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		List<PageData> res = null;
		res = jybAPPAccompanyMobileMyOrderMapper.getNodeModel(pd);
		return WebResult.requestSuccess(res);
	}

}
