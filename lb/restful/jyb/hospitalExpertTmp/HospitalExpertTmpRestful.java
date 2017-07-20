package com.maryun.lb.restful.jyb.hospitalExpertTmp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.maryun.common.service.KeywordFilter;
import com.maryun.lb.mapper.jyb.agent.AgentMapper;
import com.maryun.lb.mapper.jyb.hospitalExpertTmp.HospitalExpertTmpMapper;
import com.maryun.mapper.system.user.UserMapper;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.ChineseToEnglish;
import com.maryun.utils.UuidUtil;
import com.maryun.utils.WebResult;

/**
 * 类名称：HospitalExpertTmpRestful 创建人：SunYongLiang 创建时间：2017年2月20日
 * 
 * @version
 */
@RestController
@RequestMapping(value = "/hospitalExpertTmp")
public class HospitalExpertTmpRestful extends BaseRestful {
	@Autowired
	private HospitalExpertTmpMapper hospitalExpertTmpMapper;

	@Autowired
	private AgentMapper agentMapper;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	KeywordFilter keywordFilter;

	/**
	 * 分页查询
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
		lists = hospitalExpertTmpMapper.listPage(pd);
		// 分页
		pd = this.getPagingPd(lists);
		// 结果集封装
		return WebResult.requestSuccess(pd);
	}

	/**
	 * 根据id查询
	 * 
	 * @param pd 查询、分页条件
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/findById")
	public PageData findById(@RequestBody PageData pd) throws Exception {
		pd = hospitalExpertTmpMapper.findById(pd);
		String HE_AREA = pd.getString("HE_AREA");
		String sub = HE_AREA.substring(0, 2);
		pd.put("HE_AREA", sub);
		pd.put("caa_pcode", HE_AREA);
		// 结果集封装x
		return WebResult.requestSuccess(pd);
	}

	/**
	 * 重复专家统计信息
	 * 
	 * @param pd 查询、分布条件
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/mergePageSearch")
	public PageData mergePageSearch(@RequestBody PageData pd) throws Exception {
		List<PageData> lists = null;
		if (pd.getPageNumber() != 0) {
			PageHelper.startPage(pd.getPageNumber(), pd.getPageSize());
		}
		// lists = hospitalExpertLibsMapper.mergeListsPage(pd);
		pd = this.getPagingPd(lists);
		return WebResult.requestSuccess(pd);
	}

	/**
	 * 重复专家详情信息
	 * 
	 * @param pd 查询、分页条件
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/mergeViewPageSearch")
	public PageData mergeViewPageSearch(@RequestBody PageData pd) throws Exception {
		List<PageData> lists = null;
		if (pd.getPageNumber() != 0) {
			PageHelper.startPage(pd.getPageNumber(), pd.getPageSize());
		}
		/* lists = hospitalExpertLibsMapper.mergeViewPageSearch(pd); */
		pd = this.getPagingPd(lists);
		return WebResult.requestSuccess(pd);
	}

	/**
	 * 设置专家模板
	 * 
	 * @param pd 参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/setTemplate")
	public PageData setTemplate(@RequestBody PageData pd) throws Exception {
		pd.put("HEL_ID", pd.getString("IDS"));
		pd.put("HE_ID", UuidUtil.get32UUID());
		pd.put("HE_TEMPLATE", "1");
		pd.put("HE_MERGE", "1");
		/*
		 * hospitalExpertLibsMapper.setExpertTemplateDelete(pd);
		 * hospitalExpertLibsMapper.setOtherExpertTemplate(pd);
		 * hospitalExpertLibsMapper.setExpertTemplate(pd);
		 * hospitalExpertLibsMapper.insert(pd);
		 */
		pd.put("msg", "success");
		return WebResult.requestSuccess(pd);
	}

	/**
	 * 跳转到对比合并页面
	 * 
	 * @param pd 参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/toMerge")
	public PageData toMerge(@RequestBody PageData pd) throws Exception {
		/* List<PageData> lists = hospitalExpertLibsMapper.compareLists(pd); */
		return WebResult.requestSuccess(null);
	}

	/**
	 * 修改专家信息
	 * 
	 * @param pd 参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/saveEdit")
	public PageData saveEdit(@RequestBody PageData pd) throws Exception {
		pd.put("PHONE", pd.getString("PHONENUM"));
		PageData pds = userMapper.selectByPhone(pd);
		String NAME = pd.getString("HEL_NAME");
		NAME = ChineseToEnglish.getPinYinHeadChar(NAME);
		NAME = NAME.toUpperCase();
		pd.put("HEL_INITIALS", NAME);
		if (pds != null) {
			pd.put("USER_ID", pds.getString("USER_ID"));
		}
		else {
			hospitalExpertTmpMapper.saveUser(pd);
		}
		hospitalExpertTmpMapper.saveEdit(pd);
		pd.put("msg", "success");
		return WebResult.requestSuccess(pd);
	}

	/**
	 * 添加信息
	 * 
	 * @param pd 参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/saveAdd")
	public PageData saveAdd(@RequestBody PageData pd) throws Exception {
		PageData pds = pd;
		pd.put("PHONE", pd.getString("PHONENUM"));
		PageData pdes = userMapper.selectByPhone(pd);
		pds = agentMapper.selectID(pds);
		String NAME = pd.getString("HEL_NAME");
		NAME = ChineseToEnglish.getPinYinHeadChar(NAME);
		NAME = NAME.toUpperCase();
		pd.put("HEL_INITIALS", NAME);
		if (pds != null) {
			pd.put("HE_BELONGAGENT", pds.getString("SP_ID"));
			if (pdes != null) {
				pd.put("USER_ID", pdes.getString("USER_ID"));
				hospitalExpertTmpMapper.saveAdd(pd);
			}
			else {
				hospitalExpertTmpMapper.saveAdd(pd);
				hospitalExpertTmpMapper.saveUser(pd);
			}
			pd.put("msg", "success");
		}
		else {
			pd.put("msg", "failure");
		}
		return WebResult.requestSuccess(pd);
	}

	/**
	 * 敏感词过滤
	 * 
	 * @param pd 参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/checkKeyWords")
	public PageData checkKeyWords(@RequestBody PageData pd) throws Exception {
		PageData returnState = null;
		pd.put("msg", "success");
		pd.put("words", pd.getString("HEL_GOODPRO"));
		returnState = keywordFilter.checkWords(pd);
		if ("400".equals(returnState.getString("state"))) {
			pd.put("msg", "error");
			pd.put("error_tip", "擅长专业含有敏感词，请修改后保存"); // 擅长专业错误
		}
		pd.put("words", pd.getString("HEL_HONOR"));
		returnState = keywordFilter.checkWords(pd);
		if ("400".equals(returnState.getString("state"))) {
			pd.put("msg", "error");
			pd.put("error_tip", "相关荣誉含有敏感词，请修改后保存"); // 相关荣誉错误
		}
		pd.put("words", pd.getString("HE_HOSPITAL"));
		returnState = keywordFilter.checkWords(pd);
		if ("400".equals(returnState.getString("state"))) {
			pd.put("msg", "error");
			pd.put("error_tip", "所属医院含有敏感词，请修改后保存"); // 所属医院错误
		}
		pd.put("words", pd.getString("HEL_EDU"));
		returnState = keywordFilter.checkWords(pd);
		if ("400".equals(returnState.getString("state"))) {
			pd.put("msg", "error");
			pd.put("error_tip", "教学背景含有敏感词，请修改后保存"); // 名称错误
		}
		pd.put("words", pd.getString("HEL_DUTY"));
		returnState = keywordFilter.checkWords(pd);
		if ("400".equals(returnState.getString("state"))) {
			pd.put("msg", "error");
			pd.put("error_tip", "职务含有敏感词，请修改后保存"); // 职务错误
		}
		pd.put("words", pd.getString("HEL_NAME"));
		returnState = keywordFilter.checkWords(pd);
		if ("400".equals(returnState.getString("state"))) {
			pd.put("msg", "error");
			pd.put("error_tip", "姓名含有敏感词，请修改后保存"); // 姓名含敏感
		}

		return WebResult.requestSuccess(pd);
	}

	/**
	 * 审核
	 * 
	 * @param pd 参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/toCheck")
	public PageData toCheck(@RequestBody PageData pd) throws Exception {
		List<PageData> lists = null;
		String s_IDS = pd.getString("IDS");
		if (null != s_IDS && !"".equals(s_IDS)) {
			String[] sa_IDS = s_IDS.split(",");
			pd.put("IDSs", sa_IDS);
			if (sa_IDS != null && sa_IDS.length > 0) {
				/* hospitalExpertLibsMapper.check(pd); */
				lists = new ArrayList<PageData>();
				for (String s_tmp : sa_IDS) {
					PageData pd_tmp = new PageData();
					pd_tmp.put("CL_ID", UuidUtil.get32UUID());
					pd_tmp.put("CL_TYPE", "3");
					pd_tmp.put("KEY_ID", s_tmp);
					pd_tmp.put("CL_USERID", pd.getString("CHANGE_UID"));
					pd_tmp.put("CL_LEVEL", pd.getString("HEL_AUDIT"));
					pd_tmp.put("CL_REASON", pd.getString("Res"));
					lists.add(pd_tmp);
				}
				/* hospitalExpertLibsMapper.checkLog(lists); */
			}
			pd.put("msg", "success");
		}
		else {
			pd.put("msg", "failed");
		}
		return WebResult.requestSuccess(pd);
	}

	/**
	 * 审核退回原因
	 * 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/toBackReason")
	public PageData toBackReason(@RequestBody PageData pd) throws Exception {
		/* pd = hospitalExpertLibsMapper.findCheckLogById(pd); */
		return WebResult.requestSuccess(pd);
	}

	/**
	 * 批量删除
	 * 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/toDelete")
	public PageData toDelete(@RequestBody PageData pd) throws Exception {
		/* pd = hospitalExpertLibsMapper.findCheckLogById(pd); */
		String s_IDS = pd.getString("IDS");
		if (null != s_IDS && !"".equals(s_IDS)) {
			pd.put("IDSs", s_IDS.split(","));
			hospitalExpertTmpMapper.toDelete(pd);
			pd.put("msg", "success");
		}
		else {
			pd.put("msg", "failed");
		}
		return WebResult.requestSuccess(pd);
	}

	/**
	 * 批量更改提交状态
	 * 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/toSubmission")
	public PageData toSubmission(@RequestBody PageData pd) throws Exception {
		/* pd = hospitalExpertLibsMapper.findCheckLogById(pd); */
		String s_IDS = pd.getString("IDS");
		if (null != s_IDS && !"".equals(s_IDS)) {
			pd.put("IDSs", s_IDS.split(","));
			hospitalExpertTmpMapper.toSubmission(pd);
			pd.put("msg", "success");
		}
		else {
			pd.put("msg", "failed");
		}
		return WebResult.requestSuccess(pd);
	}

	/**
	 * 批量提交
	 * 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/toSubmit")
	public PageData toSubmit(@RequestBody PageData pd) throws Exception {
		String s_IDS = pd.getString("IDS");
		if (null != s_IDS && !"".equals(s_IDS)) {
			pd.put("IDSs", s_IDS.split(","));
			hospitalExpertTmpMapper.toSubmit(pd);
			pd.put("msg", "success");
		}
		else {
			pd.put("msg", "failed");
		}
		return WebResult.requestSuccess(pd);
	}

	/**
	 * 查询手机号是否已被占用
	 * 
	 * @param pd 参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/selectPhone")
	public Object selectPhone(@RequestBody PageData pd) throws Exception {
		PageData map = new PageData();
		String errInfo = "true";// success
		if (hospitalExpertTmpMapper.findByUId(pd) != null) {
			errInfo = "false";// error
		}
		map.put("result", errInfo); // 返回结果
		// 结果集封装
		return WebResult.requestSuccess(map);
	}
	/**
	 * 查询提交状态
	 * 
	 * @param pd
	 *            参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/selectStatus")
	public Object selectStatus(@RequestBody PageData pd) throws Exception {
		pd.put("HEL_ID", pd.getString("HEL_ID[]"));
		pd = hospitalExpertTmpMapper.selectStatus(pd);
		PageData pdResult = new PageData();
		if(pd != null){
			pdResult.put("msg", "failure");
		}else{
			pdResult.put("msg", "success");
		}
		return WebResult.requestSuccess(pdResult);
	}
}
