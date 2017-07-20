package com.maryun.restful.app.doctor.order;

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
import com.maryun.mapper.app.doctor.AppDoctorMapper;
import com.maryun.mapper.app.doctor.order.JybAPPDoctorMobileMyOrderMapper;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.WebResult;

@RestController
@RequestMapping(value = "/app/doctor/order")
public class JybAPPDoctorMobileMyOrderRestful extends BaseRestful {

	@Autowired
	private JybAPPDoctorMobileMyOrderMapper jybAPPDoctorMobileMyOrderMapper;

	@Autowired
	private AppDoctorMapper appDoctorMapper;

	@Autowired
	PushServiceForCall pushServiceForCall;

	/**
	 * 根据医生的hel_id和接单状态osha_status查询对应的所有订单
	 * @param HEL_ID
	 * @param OSHA_STATUS
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/orderList")
	@ResponseBody
	public PageData orderList(@RequestBody PageData pd) throws Exception {

		PageData pdMyOrderInfor = new PageData();
		List<PageData> list = null;

		if (StringUtils.isBlank(pd.getString("UI_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		// 因为数据库的陪诊订单状态只有0未处理 1接收 2拒绝
		// 所以只要大于2的userId去数据库查询的时候为空即可查询全部的订单显示
		if (StringUtils.isBlank(pd.getString("OSHA_STATUS"))) {
			pd.put("OSHA_STATUS", null);
		}
		else if (!StringUtils.isBlank(pd.getString("OSHA_STATUS")) && Integer.parseInt(pd.getString("OSHA_STATUS")) > 2) {
			pd.put("OSHA_STATUS", null);
		}
		// pd.put("HEL_ID", hel_id);
		// pd.put("OSHA_STATUS", osha_status);
		list = jybAPPDoctorMobileMyOrderMapper.orderList(pd);

		pdMyOrderInfor.put("falge", pd.getString("OSHA_STATUS") + "");
		pdMyOrderInfor.put("result", list);
		return WebResult.requestSuccess(pdMyOrderInfor);

	}

	// checkAIOrderCenterNum订单中心数量显示查询专家的已接单的订单为正在服务中状态的数量
	@RequestMapping(value = "/checkAIOrderCenterNum")
	@ResponseBody
	public PageData checkAIOrderCenterNum(@RequestBody PageData pd) throws Exception {

		if (StringUtils.isBlank(pd.getString("UI_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}

		PageData pdOCNum = jybAPPDoctorMobileMyOrderMapper.checkAIOrderCenterNum(pd);

		return WebResult.requestSuccess(pdOCNum);

	}

	/**
	 * 医生根据hel_id和订单的o_id接受用户订单
	 * @param order_id
	 * @param hel_id
	 * @return
	 */
	@RequestMapping("/receiveOrder")
	@ResponseBody
	public PageData receiveOrder(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("HEL_ID")) || StringUtils.isBlank(pd.getString("O_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		jybAPPDoctorMobileMyOrderMapper.receiveOrder(pd);
		// 添加推送.
		PageData orderDetail = appDoctorMapper.getNewOrderInfoDetail(pd);
		Map<String, String> extra = new HashMap<String, String>();
		List<String> userList = new ArrayList<String>();
		userList.add(orderDetail.getString("SP_ID"));
		pushServiceForCall.setAPPKEYANDSecret("3");
		pushServiceForCall.alertTitleMsgExtraCallback("老伴儿", "订单：" + orderDetail.getString("O_NUM") + "已被专家接单", extra, userList, "102", pd.getString("HEL_ID"),
				pd.getString("O_ID"), "3");
		userList.clear();
		userList.add(orderDetail.getString("UI_ID"));
		pushServiceForCall.setAPPKEYANDSecret("1");
		pushServiceForCall.alertTitleMsgExtraCallback("老伴儿", "订单：" + orderDetail.getString("O_NUM") + "已被专家接单", extra, userList, "102", pd.getString("HEL_ID"),
				pd.getString("O_ID"), "1");
		return WebResult.requestSuccess();

	}

	/**
	 * 医生根据hel_id和订单的o_id拒绝用户订单
	 * @param order_id
	 * @param hel_id
	 * @return
	 */
	@RequestMapping("/refuseOrder")
	@ResponseBody
	public PageData refuseOrder(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("HEL_ID")) || StringUtils.isBlank(pd.getString("O_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		jybAPPDoctorMobileMyOrderMapper.refuseOrder(pd);
		List<PageData> list = jybAPPDoctorMobileMyOrderMapper.getGuideSpAssign(pd);
		if (list.size() > 0) {
			String status = list.get(0).getString("OGS_ASSIGN");
			if ("3".equals(status)) {
				pd.put("OGS_ASSIGN", "2");
			}
			else if ("1".equals(status)) {
				pd.put("OGS_ASSIGN", "0");
			}
			jybAPPDoctorMobileMyOrderMapper.updateGuideSpAssign(pd);
		}
		// 添加推送.
		PageData orderDetail = appDoctorMapper.getNewOrderInfoDetail(pd);
		Map<String, String> extra = new HashMap<String, String>();

		List<String> userList = new ArrayList<String>();
		userList.add(orderDetail.getString("SP_ID"));
		pushServiceForCall.setAPPKEYANDSecret("3");
		pushServiceForCall.alertTitleMsgExtraCallback("老伴儿", "订单：" + orderDetail.getString("O_NUM") + "已被专家拒绝", extra, userList, "102", pd.getString("HEL_ID"),
				pd.getString("O_ID"), "3");
		return WebResult.requestSuccess();
	}
}
