package com.maryun.restful.app.doctor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.maryun.mapper.app.doctor.AppDepartmentMapper;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.WebResult;

/**
 * 类名称：AppDoctorInfoRestful 创建人：MARYUN 创建时间：2016年2月13日
 * 
 * @version
 */
@RestController
@RequestMapping(value = "/app/doctor/dept")
public class AppDepartmentRestful extends BaseRestful {
	@Autowired
	AppDepartmentMapper appDepartmentMapper;

	/**
	 * 科室信息
	 * @return
	 */
	@RequestMapping(value = "/list")
	@ResponseBody
	public PageData getDepartmentlist() throws Exception {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String HE_AREA = request.getParameter("HE_AREA");
		PageData pd = this.getPageData();
		pd.put("HE_AREA", HE_AREA);
		List<PageData> accInfo = appDepartmentMapper.getDeptList(pd);
		return WebResult.requestSuccess(accInfo);
	}
	/**
	 * 科室信息
	 * @return
	 */
	@RequestMapping(value = "/APPlist")
	@ResponseBody
	public PageData getDepartmentApplist() throws Exception {
		List<PageData> accInfo = appDepartmentMapper.getDeptAPPList();
		return WebResult.requestSuccess(accInfo);
	}
	/**
	 * 科室列表第一个科室的ID
	 * @return
	 */
	@RequestMapping(value = "/listByFirstID")
	@ResponseBody
	public PageData getDeptListByFirstId() throws Exception {
		PageData accInfoByFirstID = appDepartmentMapper.getDeptListByFirstId();
		return WebResult.requestSuccess(accInfoByFirstID);
	}

	/**
	 * 根据科室名字获取唯一的一个科室
	 * @return
	 */
	@RequestMapping(value = "/deptByDeptName")
	@ResponseBody
	public PageData getDeptByDeptName(PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("HD_NAME"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			PageData accInfoByDeptName = appDepartmentMapper.getDeptByDeptName(pd);
			return WebResult.requestSuccess(accInfoByDeptName);
		}
	}

	/**
	 * 根据科室ID获取唯一的一个科室信息
	 * @return
	 */
	@RequestMapping(value = "/getDeptByDeptID")
	@ResponseBody
	public PageData getDeptByDeptID(PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("HD_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			PageData accInfo = appDepartmentMapper.getDeptByDeptID(pd);
			return WebResult.requestSuccess(accInfo);
		}
	}
}
