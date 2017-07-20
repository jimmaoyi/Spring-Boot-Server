package com.maryun.restful.app.doctor;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.maryun.mapper.app.doctor.AppDoctorMapper;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.WebResult;

/**
 * 类名称：MessageRestful 创建人：MARYUN 创建时间：2016年2月13日
 * 
 * @version
 */
@RestController
@RequestMapping(value = "/app/doctor/doctor")
public class AppDoctorRestful extends BaseRestful {

	@Autowired
	private AppDoctorMapper appDoctorMapper;
	
	
	/**
	 * 获得所有有专家的城市
	 * @return
	 */
	@RequestMapping(value = "/getTheExpertCitys")
	@ResponseBody
	public PageData getTheExpertCitys(@RequestBody PageData pd) throws Exception {
		PageData pdResult = new PageData();
		List<PageData> pdCitys = appDoctorMapper.getTheExpertCitys(pd);
		pdResult.put("result", pdCitys);
		return WebResult.requestSuccess(pdResult);
	}
	
	/**
	 * 新单提醒列表（分页）
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
			list = appDoctorMapper.getNewOrderInfoList(pd);
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
			pd = appDoctorMapper.getNewOrderInfoDetail(pd);
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
			appDoctorMapper.setOrderIfAccept(pd);
			return WebResult.requestSuccess();
		}
	}

	/**
	 * 今日接诊列表（分页）
	 * @return
	 */
	@RequestMapping(value = "/getTodayTreatList")
	@ResponseBody
	public PageData getTodayTreatList(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("UI_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			List<PageData> list = null;
			list = appDoctorMapper.getTodayTreatList(pd);
			return WebResult.requestSuccess(list);
		}
	}

	/**
	 * 今日接诊详情
	 * @return
	 */
	@RequestMapping(value = "/getTodayTreatDetail")
	@ResponseBody
	public PageData getTodayAccompanyDetail(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("O_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			pd = appDoctorMapper.getTodayTreatDetail(pd);
			return WebResult.requestSuccess(pd);
		}
	}

	/**
	 * 根据lb_jyb_order_hel来获取意向专家，进而获得科室和省份
	 * @return
	 */
	@RequestMapping(value = "/getAllByOID")
	@ResponseBody
	public PageData getAllByOID(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("O_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			pd = appDoctorMapper.getAllByOID(pd);
			return WebResult.requestSuccess(pd);
		}
	}
}
