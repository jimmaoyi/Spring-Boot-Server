package com.maryun.restful.app.accompany;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.maryun.mapper.app.accompany.AppAccompanyMapper;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.WebResult;

/**
 * 类名称：MessageRestful 创建人：MARYUN 创建时间：2016年2月13日
 * 
 * @version
 */
@RestController
@RequestMapping(value = "/app/accompany/accompany")
public class AppAccompanyRestful extends BaseRestful {

	@Autowired
	private AppAccompanyMapper appAccompanyMapper;

	// /**
	// * 消息列表（分页）
	// * @return
	// */
	// @RequestMapping(value = "/messagePageSearch")
	// @ResponseBody
	// public PageData messagePageSearch(@RequestBody PageData pd) throws
	// Exception {
	// List<PageData> list = null;
	// list = appAccompanyMapper.messagePageSearch(pd);
	// PageData res = new PageData();
	// res.put("result", list);
	// res.put("errorCode", "0");
	// // 结果集封装
	// return res;
	// }

	// /**
	// * 查看某条消息详情
	// * @param 消息id
	// */
	// @RequestMapping(value = "/getMessageById")
	// @ResponseBody
	// public PageData getMessageById(String M_ID) throws Exception {
	// PageData pd = new PageData();
	// pd.put("M_ID", M_ID);
	// pd = appAccompanyMapper.getMessageById(pd);
	// // 结果集封装
	// return pd;
	// }

	/**
	 * 查看陪诊评价（分页）
	 * @return
	 */
	@RequestMapping(value = "/getEvaluateOfAccompanyList")
	@ResponseBody
	public PageData getEvaluateOfAccompanyList(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("UI_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			List<PageData> list = null;
			list = appAccompanyMapper.getEvaluateOfAccompanyList(pd);
			return WebResult.requestSuccess(list);
		}
	}

	/**
	 * 新单提醒列表（分页）
	 * @return
	 */
	@RequestMapping(value = "/getNewOrderInfoList")
	@ResponseBody
	public PageData getNewOrderInfoList(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("AI_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			List<PageData> list = null;
			list = appAccompanyMapper.getNewOrderInfoList(pd);
			return WebResult.requestSuccess(list);
		}
	}

	/**
	 * 新单详情
	 * @return
	 */
	@RequestMapping(value = "/getNewOrderInfoDetail")
	@ResponseBody
	public PageData getNewOrderInfoDetail(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("O_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			pd = appAccompanyMapper.getNewOrderInfoDetail(pd);
			return WebResult.requestSuccess(pd);
		}
	}

	/**
	 * 设置是接诊/拒绝接诊
	 * @return
	 */
	@RequestMapping(value = "/setOrderIfAccept")
	@ResponseBody
	public PageData setOrderIfAccept(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("O_ID")) || StringUtils.isBlank(pd.getString("DEAL_FLAG"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			appAccompanyMapper.setOrderIfAccept(pd);
			return WebResult.requestSuccess();
		}
	}

	/**
	 * 今日陪诊列表（分页）
	 * @return
	 */
	@RequestMapping(value = "/getTodayAccompanyList")
	@ResponseBody
	public PageData getTodayAccompanyList(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("UI_ID"))) {// UI_ID为陪诊的AI_ID
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			List<PageData> list = null;
			list = appAccompanyMapper.getTodayAccompanyList(pd);
			return WebResult.requestSuccess(list);
		}
	}

	/**
	 * 今日陪诊详情
	 * @return
	 */
	@RequestMapping(value = "/getTodayAccompanyDetail")
	@ResponseBody
	public PageData getTodayAccompanyDetail(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("O_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			PageData pdData = new PageData();
			List<PageData> keyNodeList = null;
			pdData = appAccompanyMapper.getTodayAccompanyDetail(pd);
			keyNodeList = appAccompanyMapper.getTodayAccompanyKeyNodeList(pd); // 关键节点
			pdData.put("NODE_LIST", keyNodeList);
			return WebResult.requestSuccess(pdData);
		}
	}

	/**
	 * 接单排名和累计接单数量 后台传过来的实际上是AI_ID
	 * @param 消息id
	 */
	@RequestMapping(value = "/getRankLast")
	@ResponseBody
	public PageData getRankLast(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("UI_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			PageData accinfo = appAccompanyMapper.getRankLast(pd);
			return WebResult.requestSuccess(accinfo);
		}
	}

	/**
	 * 每月接单数量 后台传过来的实际上是AI_ID
	 * @param 消息id
	 */
	@RequestMapping(value = "/getRankMonth")
	@ResponseBody
	public PageData getRankMonth(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("UI_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			PageData accinfo = appAccompanyMapper.getRankMonth(pd);
			return WebResult.requestSuccess(accinfo);
		}
	}

	/**
	 * 根据陪诊名字获取陪诊列表
	 * @return
	 */
	@RequestMapping(value = "/getAccompanyListByName")
	@ResponseBody
	public PageData getAccompanyListByName(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("AI_NAME"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			List<PageData> accInfo = appAccompanyMapper.getAccompanyListByName(pd);
			return WebResult.requestSuccess(accInfo);
		}
	}

}
