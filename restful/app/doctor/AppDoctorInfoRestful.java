package com.maryun.restful.app.doctor;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.maryun.mapper.app.doctor.AppDoctorInfoMapper;
import com.maryun.mapper.app.service.AppServiceMapper;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.ChineseToEnglish;
import com.maryun.utils.WebResult;

/**
 * 类名称：AppDoctorInfoRestful 创建人：MARYUN 创建时间：2016年2月13日
 * 
 * @version
 */
@RestController
@RequestMapping(value = "/app/doctor/info")
public class AppDoctorInfoRestful extends BaseRestful {
	@Autowired
	AppDoctorInfoMapper appDoctorInfoMapper;

	@Autowired
	AppServiceMapper appServiceMapper;

	/**
	 * 医生信息
	 * @return
	 */
	@RequestMapping(value = "/get")
	@ResponseBody
	public PageData getDoctorInfo(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("HEL_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			PageData earnestMoney = appServiceMapper.getEarestMoney();
			String searnestMoney = earnestMoney.getString("CODE");
			float f = Float.valueOf("500");
			if (StringUtils.isNotBlank(searnestMoney)) {
				f = Float.valueOf(searnestMoney);
			}
			PageData accInfo = appDoctorInfoMapper.getDoctorInfo(pd);
			accInfo.put("EARNEST_MONEY", f);
			List<PageData> videoList = appDoctorInfoMapper.getDoctorVideoList(pd);
			accInfo.put("videoList", videoList);
			List<PageData> deptVideoList = appDoctorInfoMapper.getDeptVideoList(accInfo);
			accInfo.put("deptVideoList", deptVideoList);
			return WebResult.requestSuccess(accInfo);
		}
	}

	/**
	 * 医生信息
	 * @return
	 */
	@RequestMapping(value = "/list")
	@ResponseBody
	public PageData getDoctorList(@RequestBody PageData pd) throws Exception {
		List<PageData> accInfo = appDoctorInfoMapper.getDoctorList(pd);
		return WebResult.requestSuccess(accInfo);
	}

	/**
	 * 医生信息
	 * @return
	 */
	@RequestMapping(value = "/TVlist")
	@ResponseBody
	public PageData getTVDoctorList(@RequestBody PageData pd) throws Exception {
		String sHelName = pd.getString("HEL_NAME");
		if (!"".equals(sHelName)) {
			String sHelNameChinese = ChineseToEnglish.getPingYin(sHelName).toUpperCase();
			pd.put("OMS_ID", sHelNameChinese);
			pd.remove("HEL_NAME");
		}
		List<PageData> accInfo = appDoctorInfoMapper.getTVDoctorList(pd);
		return WebResult.requestSuccess(accInfo);
	}

	/**
	 * 医生信息
	 * @return
	 */
	@RequestMapping(value = "/getDoctorSimpleList")
	@ResponseBody
	public PageData getDoctorSimpleList() throws Exception {
		List<PageData> accInfo = appDoctorInfoMapper.getDoctorSimpleList();
		return WebResult.requestSuccess(accInfo);
	}

	/**
	 * 名医堂信息
	 * @return
	 */
	@RequestMapping(value = "/doctorsChurchList")
	@ResponseBody
	public PageData getDoctorsChurchList(@RequestBody PageData pd) throws Exception {
		List<PageData> accInfo = appDoctorInfoMapper.getDoctorsChurchList(pd);
		return WebResult.requestSuccess(accInfo);
	}

	/**
	 * 根据医生名字获取医生列表 具体代理商下面的专家
	 * @return
	 */
	@RequestMapping(value = "/getDoctorListByName")
	@ResponseBody
	public PageData getDoctorListByName(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("HEL_NAME"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			List<PageData> accInfo = appDoctorInfoMapper.getDoctorListByName(pd);
			return WebResult.requestSuccess(accInfo);
		}
	}

	/**
	 * 根据医生名字获取医生列表 所有的专家
	 * @return
	 */
	@RequestMapping(value = "/getDoctorListByName_user")
	@ResponseBody
	public PageData getDoctorListByName_user(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("HEL_NAME"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			List<PageData> accInfo = appDoctorInfoMapper.getDoctorListByName_user(pd);
			return WebResult.requestSuccess(accInfo);
		}
	}

	/**
	 * 接单排名和累计接单数量
	 * @param 消息id
	 */
	@RequestMapping(value = "/getRankLast")
	@ResponseBody
	public PageData getRankLast(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("HEL_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			PageData accinfo = appDoctorInfoMapper.getRankLast(pd);
			return WebResult.requestSuccess(accinfo);
		}
	}

	/**
	 * 每月接单数量
	 * @param 消息id
	 */
	@RequestMapping(value = "/getRankMonth")
	@ResponseBody
	public PageData getRankMonth(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("HEL_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			PageData accinfo = appDoctorInfoMapper.getRankMonth(pd);
			return WebResult.requestSuccess(accinfo);
		}
	}

	/**
	 * 专家评价（分页）
	 * @return
	 */
	@RequestMapping(value = "/getEvaluateOfDoctorList")
	@ResponseBody
	public PageData getEvaluateOfDoctorList(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("UI_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			List<PageData> list = null;
			list = appDoctorInfoMapper.getEvaluateOfDoctorList(pd);
			return WebResult.requestSuccess(list);
		}
	}
}
