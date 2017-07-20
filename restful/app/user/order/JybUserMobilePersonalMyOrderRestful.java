package com.maryun.restful.app.user.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.maryun.common.service.PushServiceForCall;
import com.maryun.mapper.app.service.AppServiceMapper;
import com.maryun.mapper.app.user.order.JybUserMobilePersonalMyOrderMapper;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.CommUtil;
import com.maryun.utils.OrderNumService;
import com.maryun.utils.UuidUtil;
import com.maryun.utils.WebResult;

/**
 * 就医宝用户端我的订单，包括订单显示，订单的查询，订单的删除等
 * @author 小帅
 *
 */
@RestController
@RequestMapping(value = "/app/user/order")
public class JybUserMobilePersonalMyOrderRestful extends BaseRestful {

	@Autowired
	private JybUserMobilePersonalMyOrderMapper jybUserMobilePersonalMyOrderMapper;

	@Autowired
	PushServiceForCall pushServiceForCall;

	@Autowired
	AppServiceMapper appServiceMapper;

	// 因为数据库表中新添加字段O_PRICE所以需要从数据库字典中查出该字段金额
	// 然后插入订单中来
	// 所以下面代码需要插入新增字段O_PRICE

	/**
	 * 用户下单生成新的订单，需要的参数：UI_ID用户,HEL_ID医生，O_CONTENT订单内容,HEL_NAME医生名字,REMIT_ID
	 * 汇付识别码
	 * @param UI_ID
	 * @param HEL_ID
	 * @param O_CONTENT
	 * @return
	 * @throws Exception
	 */
	// insert lb_jyb_order (O_ID,O_NUM,O_CONTENT,UI_ID,O_TYPE,KEY_ID) VALUES
	// (5,5,555,5,0,55)
	@RequestMapping("/createNewOrder")
	@ResponseBody
	@Transactional
	public PageData createNewOrder(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("UI_ID")) || StringUtils.isBlank(pd.getString("HEL_ID")) || StringUtils.isBlank(pd.getString("HEL_NAME"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}

		// 订单编号
		String O_NUM = OrderNumService.getOrderNum("");
		// 支付流水号,在用户确认线下支付发送给用户后会去修改汇付识别码
		// String REMIT_ID = OrderNumService.getOfflineNum();

		String O_ID = UuidUtil.get32UUID();
		pd.put("O_ID", O_ID);
		pd.put("O_NUM", O_NUM);
		// pd.put("O_CONTENT", O_CONTENT);
		// pd.put("UI_ID", UI_ID);
		pd.put("O_TYPE", 0);
		pd.put("KEY_ID", pd.getString("HEL_ID"));
		// pd.put("REMIT_ID", REMIT_ID);
		// 订单金额因为甲方规定是固定的，所以在sys_dictionaries数据库字典表中去查出订单金额
		PageData suggest = appServiceMapper.getEarestMoney();
		if (suggest != null) {
			pd.put("O_PRICE", suggest.getString("CODE"));
		}
		else {
			return WebResult.requestFailed(10001, "订单金额参数缺失！", null);
		}

		// 生成新的订单
		jybUserMobilePersonalMyOrderMapper.createNewOrder(pd);

		pd.put("PAY_ID", UuidUtil.get32UUID());
		pd.put("PAY_STATUS", "0");
		pd.put("PAY_MONEY", suggest.getString("CODE"));
		// 生成新的订单之后，去LB_JYB_PAY里插入该条订单数据
		jybUserMobilePersonalMyOrderMapper.insertOderPayWay(pd);

		return WebResult.requestSuccess(O_ID);
		/**
		 * 以下代码为PC端操作
		 */
		/*
		 * // 生成订单马上去lb_jyb_node_mode表查出用户下单所对应的导医接单节点插入陪诊的模板节点表lb_jyb_key_node中
		 * //注意只有一条数据，即导医接单的数据 List<PageData> pdModelList = null; pdModelList =
		 * jybUserMobilePersonalMyOrderMapper
		 * .checkModelInforByLB_JYB_NODE_MODE();
		 * 
		 * //查出所有的模板和订单id一起插入陪诊的模板节点表lb_jyb_key_node //insert lb_jyb_key_node
		 * (KN_ID,O_ID,NM_ID,NM_NAME) VALUES (66,66,66,'6666')
		 * if(pdModelList.size() == 1 && pdModelList!=null){
		 * 
		 * for (PageData pageData : pdModelList) { pageData.put("KN_ID",
		 * UuidUtil.get32UUID()); pageData.put("O_ID", O_ID);
		 * jybUserMobilePersonalMyOrderMapper
		 * .insertModelInforIntoLb_JYB_KEY_NODE(pageData); }
		 * 
		 * //用户下单，模板创建完成之后，修改lb_jyb_key_node的陪诊状态中的导医接单内容 //update
		 * lb_jyb_key_node set KN_TIME=NOW(),KN_CONTENT=#{KN_CONTENT} where
		 * (NM_NAME='导医接单' or NM_ID='1') and O_ID=#{O_ID} and DEL_STATE='1'
		 * pd.put("KN_CONTENT", pd.getString("HEL_NAME")); //pd.put("NM_ID",
		 * NM_ID);
		 * jybUserMobilePersonalMyOrderMapper.customerServiceReceiverOrder(pd);
		 * return WebResult.requestSuccess(); }else{ return
		 * WebResult.requestFailed(100021, "未获取到模板信息,或模板获取数多！", null); }
		 */

	}

	// 用户下单产生新的订单前，需要判断用户下单的该医生的订单表里有没有未完结的订单，如果有问他是否需要继续下单
	// checkThisDorcorIsNotFinishOrders
	@RequestMapping("/checkThisDorcorIsNotFinishOrders")
	@ResponseBody
	public PageData checkThisDorcorIsNotFinishOrders(@RequestBody PageData pd) {
		PageData pdResult = new PageData();
		if (StringUtils.isBlank(pd.getString("UI_ID")) || StringUtils.isBlank(pd.getString("HEL_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			List<PageData> pdList = jybUserMobilePersonalMyOrderMapper.checkThisDorcorIsNotFinishOrders(pd);
			pdResult.put("pdList", pdList);
			return WebResult.requestSuccess(pdResult);
		}
	}

	// checkThisOrderStatus
	@RequestMapping("/checkThisOrderStatus")
	@ResponseBody
	public PageData checkThisOrderStatus(@RequestBody PageData pd) {

		if (StringUtils.isBlank(pd.getString("UI_ID")) || StringUtils.isBlank(pd.getString("O_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			PageData pdO_STATUS = jybUserMobilePersonalMyOrderMapper.checkThisOrderStatus(pd);
			return WebResult.requestSuccess(pdO_STATUS);
		}
	}

	/**
	 * 根据用户id和订单状态查询订单信息 String UI_ID,Integer O_STATUS
	 * @param user_id
	 * @param order_status
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/orderList")
	@ResponseBody
	public PageData OrderList(@RequestBody PageData pd) throws Exception {
		PageData pdMyOrderInfor = new PageData();
		if (StringUtils.isBlank(pd.getString("UI_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		List<PageData> list = null;
		// 0代付款、1待就诊、2待评价、3已评价、4申请退款、5已退款、6已付款,7取消订单
		if (StringUtils.isBlank(pd.getString("O_STATUS"))) {
			pd.put("O_STATUS", null);
		}
		System.out.println("---------------------------" + pd.getString("O_STATUS"));
		// pd.put("UI_ID", user_id);
		// pd.put("O_STATUS", order_status);

		PageData pdMyOrderStateNum = jybUserMobilePersonalMyOrderMapper.checkMyOrderStateNum(pd);

		list = jybUserMobilePersonalMyOrderMapper.jybUserMobilePersonalMyOrderListByUserId(pd);
		for (PageData pageData : list) {
			System.out.println("就医宝app用户订单信息" + pageData);
		}
		if (pdMyOrderStateNum != null) {
			pdMyOrderInfor.put("orderStateNum", pdMyOrderStateNum.getString("ROWNUMBER"));
		}
		else {
			pdMyOrderInfor.put("orderStateNum", "0");
		}
		pdMyOrderInfor.put("flage", pd.getString("O_STATUS"));
		pdMyOrderInfor.put("result", list);
		return WebResult.requestSuccess(pdMyOrderInfor);
	}

	/**
	 * 就医宝app中的我的订单根据订单Id取消订单 String O_ID
	 * @param order_id
	 * @return
	 */
	@RequestMapping("/cancelOrder")
	@ResponseBody
	public PageData cancelOrder(@RequestBody PageData pd) {
		if (StringUtils.isBlank(pd.getString("O_ID")) || StringUtils.isBlank(pd.getString("CANCEL_ID")) || StringUtils.isBlank(pd.getString("CANCEL_REASON"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			jybUserMobilePersonalMyOrderMapper.cancelOrder(pd);
			return WebResult.requestSuccess();
		}
	}

	/**
	 * 根据订单Id去评价该订单并修改订单状态为已评价状态 String O_ID String EVA_CONTENT
	 * @param order_id
	 * @param content
	 * @return
	 */
	@RequestMapping("/insertOrderEvalContent")
	@ResponseBody
	public PageData insertOrderEvalContent(@RequestBody PageData pd) {

		if (StringUtils.isBlank(pd.getString("O_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}

		if (pd.get("EVA_CONTENT") == null)
			pd.put("EVA_CONTENT", "");
		String EVA_ID = UuidUtil.get32UUID();
		// pd.put("O_ID", order_id);
		pd.put("EVA_ID", EVA_ID);
		// pd.put("EVA_CONTENT",content);
		jybUserMobilePersonalMyOrderMapper.insertOrderEvalContent(pd);
		// 修改订单状态为已评价状态
		jybUserMobilePersonalMyOrderMapper.updateOrderStatusIsEval(pd);

		// 遍历图片并保存,用户提交建议的情况下
		if (!StringUtils.isBlank(pd.getString("FILE_UPLOAD_ID_LIST"))) {
			String ids = pd.get("FILE_UPLOAD_ID_LIST").toString().replace("[", "").replace("]", "").trim();
			if (StringUtils.isNotBlank(ids)) {
				PageData temp = new PageData();
				temp.put("MASTER_ID", EVA_ID);
				temp.put("IDS", CommUtil.arr2InStr(ids));
				jybUserMobilePersonalMyOrderMapper.updateMasterId(temp);
			}
		}

		return WebResult.requestSuccess();
	}

	/**
	 * 根据订单id去查询该订单的详细信息 String O_ID
	 * @param order_id
	 * @return
	 */
	@RequestMapping("/orderInfoByOrderId")
	@ResponseBody
	public PageData orderInfoByOrderId(@RequestBody PageData pd) {
		if (StringUtils.isBlank(pd.getString("O_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}

		// pd.put("O_ID", order_id);
		PageData orderInfo = jybUserMobilePersonalMyOrderMapper.orderInfoByOrderId(pd);
		System.out.println("订单信息-----------------" + orderInfo);
		return WebResult.requestSuccess(orderInfo);
	}

	/**
	 * 根据订单id和订单状态值o_statu去修改订单的状态
	 * @param O_ID
	 * @param O_STATUS
	 * @return
	 */
	@RequestMapping("/updateOrderStatus")
	@ResponseBody

	public PageData updateOrderStatus(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("O_ID")) || StringUtils.isBlank(pd.getString("O_STATUS"))) {

			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		jybUserMobilePersonalMyOrderMapper.updateOrderStatus(pd);

		// 确认完结的订单状态判断，确认完结后推送给代理，医生和陪诊
		if ("2".equals(pd.getString("O_STATUS"))) {

			// 获得推送的数据
			PageData pdMessage = jybUserMobilePersonalMyOrderMapper.orderInfoByOrderId(pd);

			// 推送时带的内容
			Map<String, String> extra = new HashMap<String, String>();
			extra.put("KEY_PARAM", "");

			// 推送接受者的id
			List<String> userList = new ArrayList<String>();

			// 推送给代理
			userList.add(pdMessage.getString("spid"));// 3
			String Type = "3";
			push(pd, pdMessage, extra, userList, Type);

			// 推送给医生
			userList.add(pdMessage.getString("HEL_ID"));// 4
			Type = "4";
			push(pd, pdMessage, extra, userList, Type);

			// 推送给陪诊
			userList.add(pdMessage.getString("AI_ID"));// 2
			Type = "2";
			push(pd, pdMessage, extra, userList, Type);

		}
		return WebResult.requestSuccess();
	}

	private void push(PageData pd, PageData pdMessage, Map<String, String> extra, List<String> userList, String Type) throws Exception {
		// 推送类型，如用户退给代理，这里就是3表示推给代理
		pushServiceForCall.setAPPKEYANDSecret(Type);
		pushServiceForCall.alertTitleMsgExtraCallback("您有一条订单用户已确认完结", "您有一条订单用户已确认完结", extra, userList, "104", pdMessage.getString("uiid"),
				pd.getString("O_ID"), Type);

	}

	/**
	 * 根据订单id去申请退单
	 * @param O_ID
	 * @param O_STATUS
	 * @return
	 */
	@RequestMapping("/chargebackOrder")
	@ResponseBody
	public PageData chargebackOrder(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("O_ID")) || StringUtils.isBlank(pd.getString("REFUND_REASON"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}

		jybUserMobilePersonalMyOrderMapper.chargebackOrder(pd);

		// 推送处理
		// 获得推送的数据
		PageData pdMessage = jybUserMobilePersonalMyOrderMapper.orderInfoByOrderId(pd);

		// 推送时带的内容
		Map<String, String> extra = new HashMap<String, String>();
		Map<String, Object> extraContent = new HashMap<String, Object>();
		extraContent.put("REFUND_REASON", pd.getString("REFUND_REASON"));
		extra.put("KEY_PARAM", JSON.toJSONString(extraContent));

		// 推送接受者的id
		List<String> userList = new ArrayList<String>();
		userList.add(pdMessage.getString("spid"));

		// 推送类型，如用户退给代理，这里就是3表示推给代理
		pushServiceForCall.setAPPKEYANDSecret("3");

		pushServiceForCall.alertTitleMsgExtraCallback("您有一条退单提醒", "您有一条退单提醒", extra, userList, "103", pdMessage.getString("uiid"), pd.getString("O_ID"), "3");

		return WebResult.requestSuccess();
	}

	/**
	 * 根据订单id去lb_jyb_pay支付方法表中去修改为已支付
	 * @param O_ID
	 * @return
	 */
	@RequestMapping("/updatePayWay")
	@ResponseBody
	public PageData updatePayWay(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("O_ID")) || StringUtils.isBlank(pd.getString("PAY_WAY"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		jybUserMobilePersonalMyOrderMapper.updatePayWay(pd);
		return WebResult.requestSuccess();
	}

	/**
	 * 根据订单id去lb_jyb_pay支付方法表中去修改为已支付
	 * @param O_ID
	 * @return
	 */
	@RequestMapping("/updatePayStatus")
	@ResponseBody
	public PageData updatePayStatus(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("O_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		jybUserMobilePersonalMyOrderMapper.updatePayStatus(pd);
		return WebResult.requestSuccess();
	}

	/**
	 * 根据订单id去lb_jyb_pay支付方法表中支付状态和订单表中订单状态
	 * @param O_ID
	 * @return
	 */
	@RequestMapping("/getOrderPayStatus")
	@ResponseBody
	public PageData getOrderPayStatus(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("O_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		PageData orderInfo = jybUserMobilePersonalMyOrderMapper.getOrderInfoByOID(pd);
		PageData orderPayInfo = jybUserMobilePersonalMyOrderMapper.getOrderPayINfoByOID(pd);
		if (null != orderInfo && null != orderPayInfo) {
			orderInfo.put("PAY_WAY", orderPayInfo.get("PAY_WAY"));
			orderInfo.put("PAY_STATUS", orderPayInfo.get("PAY_STATUS"));
			orderInfo.put("PAY_MONEY", orderPayInfo.get("PAY_MONEY"));
		}
		return WebResult.requestSuccess(orderInfo);
	}

	// 用户线下支付确认支付后根据手机发送的汇付识别码，修改订单表里的汇付识别码
	@RequestMapping("/updateREMIT_ID")
	@ResponseBody
	public PageData updateREMIT_ID(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("O_ID")) || StringUtils.isBlank(pd.getString("REMIT_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		jybUserMobilePersonalMyOrderMapper.updateREMIT_ID(pd);

		return WebResult.requestSuccess();
	}
}
