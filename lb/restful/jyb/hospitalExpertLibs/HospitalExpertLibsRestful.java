package com.maryun.lb.restful.jyb.hospitalExpertLibs;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.maryun.lb.mapper.jyb.hospitalExpertLibs.HospitalExpertLibsMapper;
import com.maryun.mapper.system.user.UserMapper;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.ChineseToEnglish;
import com.maryun.utils.UuidUtil;
import com.maryun.utils.WebResult;

/**
 * 类名称：HospitalExpertLibsRestful 创建人：ChuMingZe 创建时间：2017年2月15日
 * 
 * @version
 */
@RestController
@RequestMapping(value = "/hospExpLib")
public class HospitalExpertLibsRestful extends BaseRestful {
	//专家角色ID
	@Value("${system.role.expert}")
	public String ROLE_ID;
		
	@Autowired
	private HospitalExpertLibsMapper hospitalExpertLibsMapper;
	
	@Autowired
	private UserMapper userMapper;

	/**
	 * 分页查询
	 * 
	 * @param pd
	 *            查询、分页条件
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/pageSearch")
	public PageData pageSearch(@RequestBody PageData pd) throws Exception {
		List<PageData> lists = null;
		if (pd.getPageNumber() != 0) {
			PageHelper.startPage(pd.getPageNumber(), pd.getPageSize());
		}
		String s_IDS = pd.getString("IDS");
		if(null != s_IDS && !"".equals(s_IDS)){
			//选择同步专家
			pd.put("HEL_IDS", s_IDS.split(","));
		}
		lists = hospitalExpertLibsMapper.listPage(pd);
		// 分页
		pd = this.getPagingPd(lists);
		// 结果集封装
		return WebResult.requestSuccess(pd);
	}
	
	/**
	 * 代理商分页查询
	 * 
	 * @param pd
	 *            查询、分页条件
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/pageSearchBySP")
	public PageData pageSearchBySP(@RequestBody PageData pd) throws Exception {
		List<PageData> lists = null;
		if (pd.getPageNumber() != 0) {
			PageHelper.startPage(pd.getPageNumber(), pd.getPageSize());
		}
		lists = hospitalExpertLibsMapper.listPageBySP(pd);
		// 分页
		pd = this.getPagingPd(lists);
		// 结果集封装
		return WebResult.requestSuccess(pd);
	}

	/**
	 * 重复专家统计信息
	 * 
	 * @param pd
	 *            查询、分布条件
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/mergePageSearch")
	public PageData mergePageSearch(@RequestBody PageData pd) throws Exception {
		List<PageData> lists = null;
		if (pd.getPageNumber() != 0) {
			PageHelper.startPage(pd.getPageNumber(), pd.getPageSize());
		}
		lists = hospitalExpertLibsMapper.mergeListsPage(pd);
		pd = this.getPagingPd(lists);
		return WebResult.requestSuccess(pd);
	}

	/**
	 * 重复专家详情信息
	 * 
	 * @param pd
	 *            查询、分页条件
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/mergeViewPageSearch")
	public PageData mergeViewPageSearch(@RequestBody PageData pd) throws Exception {
		List<PageData> lists = null;
		if (pd.getPageNumber() != 0) {
			PageHelper.startPage(pd.getPageNumber(), pd.getPageSize());
		}
		lists = hospitalExpertLibsMapper.mergeViewPageSearch(pd);
		pd = this.getPagingPd(lists);
		return WebResult.requestSuccess(pd);
	}

	/**
	 * 设置专家模板
	 * 
	 * @param pd
	 *            参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/setTemplate")
	public PageData setTemplate(@RequestBody PageData pd) throws Exception {
		pd.put("HEL_ID", pd.getString("IDS"));
		pd.put("HE_ID", UuidUtil.get32UUID());
		pd.put("HE_TEMPLATE", "1");
		pd.put("HE_MERGE", "1");
		if(!"1".equals(pd.getString("HEL_MERGE"))){
			pd.put("HEL_MERGE", "0");
		}
		pd.put("OMS_ID", ChineseToEnglish.getPingYin(pd.getString("HEL_NAME")));
		//PageData pd_tmp = hospitalExpertLibsMapper.getMaxRepeatOMSNo(pd);
		String s_maxNo = hospitalExpertLibsMapper.getMaxRepeatOMSNo(pd);
		double d_maxNo = Double.parseDouble(s_maxNo);
 		//double d_maxNo = Double.parseDouble(pd_tmp.getString("num"));
		if(d_maxNo > 1){
			pd.put("OMS_ID", pd.getString("OMS_ID") + "_" + (int)d_maxNo);
		}
		hospitalExpertLibsMapper.setExpertTemplateDelete(pd);
		hospitalExpertLibsMapper.setOtherExpertTemplate(pd);
		hospitalExpertLibsMapper.setExpertTemplate(pd);
		hospitalExpertLibsMapper.insert(pd);
		pd.put("msg", "success");
		return WebResult.requestSuccess(pd);
	}

	/**
	 * 跳转到对比合并页面
	 * 
	 * @param pd
	 *            参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/toMerge")
	public PageData toMerge(@RequestBody PageData pd) throws Exception {
		List<PageData> lists = hospitalExpertLibsMapper.compareLists(pd);
		return WebResult.requestSuccess(lists);
	}

	/**
	 * 修改保存对比合并数据
	 * 
	 * @param pd
	 *            参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/saveMergeEdit")
	public PageData saveMergeEdit(@RequestBody PageData pd) throws Exception {
		hospitalExpertLibsMapper.setExpertMerge(pd);
		hospitalExpertLibsMapper.saveMergeEdit(pd);
		pd.put("msg", "success");
		return WebResult.requestSuccess(pd);
	}
	
	/**
	 * 修改排序数据
	 * 
	 * @param pd
	 *            参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/saveSetOrder")
	public PageData saveSetOrder(@RequestBody PageData pd) throws Exception {
		hospitalExpertLibsMapper.saveSetOtherOrder(pd);
		hospitalExpertLibsMapper.saveSetOrder(pd);
		pd.put("msg", "success");
		return WebResult.requestSuccess(pd);
	}

	/**
	 * 审核
	 * 
	 * @param pd
	 *            参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/toCheck")
	public PageData toCheck(@RequestBody PageData pd) throws Exception {
		List<PageData> lists = null;
		if(!"".equals(pd.getString("lists"))){
			List<PageData> lists_user = JSONObject.parseArray(JSON.parseObject(pd.getString("lists")).getString("rows"), PageData.class);
			if(null != lists_user && lists_user.size() > 0){
				for (PageData pd_tmp : lists_user) {
					PageData pd_sysUser = new PageData();
					pd_sysUser.put("USERNAME", pd_tmp.getString("PHONENUM"));
					PageData pdTemp = hospitalExpertLibsMapper.selectUserName(pd_sysUser);
					if(pdTemp != null){
						pd_sysUser.put("USER_ID",pdTemp.getString("USER_ID"));
					}else{
						pd_sysUser.put("USER_ID", UuidUtil.get32UUID());
						pd_sysUser.put("PASSWORD", pd.getString("PASSWORD"));
						pd_sysUser.put("NAME", pd_tmp.getString("HEL_NAME"));
						pd_sysUser.put("STATUS", "0");
						pd_sysUser.put("SKIN", "default");
						pd_sysUser.put("PHONE", pd_tmp.getString("PHONENUM"));
						pd_sysUser.put("TYPE", "5");
						pd_sysUser.put("CREATE_UID", pd.getString("CREATE_UID"));
						pd_sysUser.put("CHANGE_UID", pd.getString("CHANGE_UID"));
						pd_sysUser.put("DEL_STATE", pd.getString("DEL_STATE"));
						userMapper.saveAddByOtherType(pd_sysUser);
					}
					PageData pd_sysUserRole = new PageData();
					pd_sysUserRole.put("UR_ID", UuidUtil.get32UUID());
					pd_sysUserRole.put("USER_ID", pd_sysUser.getString("USER_ID"));
					pd_sysUserRole.put("ROLE_ID", ROLE_ID);
					
					pd.put("HEL_ID", pd_tmp.getString("HEL_ID"));
					pd.put("SYS_UI_ID", pd_sysUser.getString("USER_ID"));
					
					
					userMapper.saveUserRole(pd_sysUserRole);
					hospitalExpertLibsMapper.saveSysUserID(pd);
				}
				pd.put("msg", "success");
			}else{
				pd.put("msg", "failed");
			}
		}/*else{
			pd.put("msg", "failed");
		}*/
		String s_IDS = pd.getString("IDS");
		if (null != s_IDS && !"".equals(s_IDS)) {
			String[] sa_IDS = s_IDS.split(",");
			pd.put("IDSs", sa_IDS);
			if (sa_IDS != null && sa_IDS.length > 0) {
				hospitalExpertLibsMapper.check(pd);
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
				hospitalExpertLibsMapper.checkLog(lists);
			}
			pd.put("msg", "success");
		} else {
			pd.put("msg", "failed");
		}
		return WebResult.requestSuccess(pd);
	}

	/**
	 * 审核退回原因
	 * 
	 * @param pd
	 *            参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/toBackReason")
	public PageData toBackReason(@RequestBody PageData pd) throws Exception {
		pd = hospitalExpertLibsMapper.findCheckLogById(pd);
		return WebResult.requestSuccess(pd);
	}
	/**
	 * 查询排序
	 * 
	 * @param pd
	 *            参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/selectSort")
	public PageData selectSort(@RequestBody PageData pd) throws Exception {
		pd = hospitalExpertLibsMapper.selectSort(pd);
		return WebResult.requestSuccess(pd);
	}

	/**
	 * 按主键Id查询
	 * 
	 * @param pd
	 *            参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/findById")
	public PageData findById(@RequestBody PageData pd) throws Exception {
		PageData pds = pd;
		String O_ID = pd.getString("O_ID");
		pd = hospitalExpertLibsMapper.findById(pd);
		if(O_ID != null && O_ID != ""){
			pds = hospitalExpertLibsMapper.selectREJECT_REASON(pds);
			pd.put("REJECT_REASON", pds.getString("REJECT_REASON"));
			pd.put("OGS_STATUS", pds.getString("OGS_STATUS"));
		}
		return WebResult.requestSuccess(pd);
	}
	
	/**
	 * 合并状态
	 * 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/toMergeEnd")
	public PageData toMergeEnd(@RequestBody PageData pd) throws Exception {
		String s_IDS = pd.getString("IDS");
		String S_IDS = pd.getString("IDES");
		if (null != s_IDS && !"".equals(s_IDS)) {
			pd.put("IDSs", s_IDS.split(","));
			hospitalExpertLibsMapper.toMergeEnd(pd);
			pd.put("IDES", S_IDS.split(","));
			hospitalExpertLibsMapper.toHeMergeEnd(pd);
			pd.put("msg", "success");
		}else{
			pd.put("msg", "failed");
		}
		return WebResult.requestSuccess(pd);
	}
	
	/**
	 * 显示状态
	 * 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/toShow")
	public PageData toShow(@RequestBody PageData pd) throws Exception {
		String s_IDS = pd.getString("IDS");
		if (null != s_IDS && !"".equals(s_IDS)) {
			pd.put("IDSs", s_IDS.split(","));
			hospitalExpertLibsMapper.toShow(pd);
			pd.put("msg", "success");
		}else{
			pd.put("msg", "failed");
		}
		return WebResult.requestSuccess(pd);
	}
	/**
	 * 查询专家库专家绑定的代理商
	 * 
	 * @param pd
	 *            参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/selectSP")
	public PageData selectSP(@RequestBody PageData pd) throws Exception {
		List<PageData> list= hospitalExpertLibsMapper.selectBindSp(pd);
		pd = this.getPagingPd(list);
		return WebResult.requestSuccess(pd);
	}
	/**
	 * 查询专家库专家绑定的代理商
	 * 
	 * @param pd
	 *            参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/findByIdForEdit")
	public PageData findByIdForEdit(@RequestBody PageData pd) throws Exception {
		pd = hospitalExpertLibsMapper.findByIdForEdit(pd);
		return WebResult.requestSuccess(pd);
	}
	/**
	 * 修改专家信息
	 * 
	 * @param pd 参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/saveEditForEdit")
	public PageData saveEditForEdit(@RequestBody PageData pd) throws Exception {
		if("4".equals(pd.getString("HEL_AUDIT"))){
			pd.put("PHONE", pd.getString("PHONENUM"));
			PageData pds = userMapper.selectByPhone(pd);
			if (pds != null) {
				pd.put("USER_ID", pds.getString("USER_ID"));
			}
			else {
				hospitalExpertLibsMapper.saveUser(pd);
			}
		}
		hospitalExpertLibsMapper.saveEditForEdit(pd);
		pd.put("msg", "success");
		return WebResult.requestSuccess(pd);
	}
	/**
	 * 查询专家库专家绑定的代理商
	 * 
	 * @param pd
	 *            参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/findByIdForPhone")
	public PageData findByIdForPhone(@RequestBody PageData pd) throws Exception {
		PageData map = new PageData();
		String errInfo = "true";// success
		if (hospitalExpertLibsMapper.findByUId(pd) != null) {
			errInfo = "false";// error
		}
		map.put("result", errInfo); // 返回结果
		// 结果集封装
		return WebResult.requestSuccess(map);
	}

		
}
