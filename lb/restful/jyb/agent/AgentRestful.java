package com.maryun.lb.restful.jyb.agent;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.maryun.lb.mapper.jyb.agent.AgentMapper;
import com.maryun.mapper.system.user.UserMapper;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.UuidUtil;
import com.maryun.utils.WebResult;
/**
 * 类名称：AgentRestful 创建人：SunYongLiang 创建时间：2017年2月21日
 * 
 * @version
 */
@RestController
@RequestMapping(value = "/agent")
public class AgentRestful extends BaseRestful {
	@Autowired
	private AgentMapper agentMapper;
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
		PageData pds = agentMapper.selectUserArea(pd);
		if(pds != null){
			pd.put("UA_AREA", pds.getString("UA_AREA"));
		}
		if (pd.getPageNumber() != 0) {
			PageHelper.startPage(pd.getPageNumber(), pd.getPageSize());
		}
		lists = agentMapper.listPage(pd);
		pd = this.getPagingPd(lists);
		return WebResult.requestSuccess(pd);
	}
	
	/**
	 * 按主键Id查询
	 * 
	 * @param pd
	 *            查询条件
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/findById")
	public PageData findById(@RequestBody PageData pd) throws Exception {
		String oid = pd.getString("O_ID");
		PageData pds = pd;
		pd = agentMapper.findById(pd);
		if(pd == null){
			PageData pdTemp = agentMapper.selectUserArea(pds);
			pd.put("SP_REGISTER_ADDR", pdTemp.getString("UA_AREA"));
		}
		if(oid != null && oid != ""){
			pds = agentMapper.findReturnRecord(pds);
			pd.put("REJECT_REASON", pds.getString("REJECT_REASON"));
			pd.put("OGS_STATUS", pds.getString("OGS_STATUS"));
		}
		return WebResult.requestSuccess(pd);
	}
	
	/**
	 * 按主键Id查询
	 * 
	 * @param pd
	 *            查询条件
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/findByArea")
	public PageData findByArea(@RequestBody PageData pd) throws Exception {
		pd = agentMapper.selectUserArea(pd);
		if(pd != null){
			pd.put("SP_REGISTER_ADDR", pd.getString("UA_AREA"));
		}
		return WebResult.requestSuccess(pd);
	}
	
	/**
	 * 添加
	 * 
	 * @param pd
	 *            参数
	 * @return
	 * @throws Exception
	 */
	@Value("${system.role.sp}")
	public String ROLE_ID;
	@RequestMapping(value = "/saveAdd")
	public PageData saveAdd(@RequestBody PageData pd) throws Exception {
		if(pd.getString("SP_REGISTER_ADDR") == null || pd.getString("SP_REGISTER_ADDR").equals("")){
			pd.put("SP_REGISTER_ADDR", "country");
		}
		pd.put("SP_ID", UuidUtil.get32UUID());
		pd.put("ROLE_ID", ROLE_ID);
		pd.put("UR_ID", UuidUtil.get32UUID());
		pd.put("PHONE", pd.getString("SP_PHONE"));
		PageData pds = userMapper.selectByPhone(pd);
		if(pds != null){
			pd.put("USER_ID", pds.getString("USER_ID"));
		}else{
			agentMapper.saveUser(pd);
		}
		agentMapper.save(pd);
		userMapper.deleteUserRole(pd);
		userMapper.saveUserRole(pd);
		pd.put("msg", "success");
		return WebResult.requestSuccess(pd);
	}
	
	/**
	 * 修改
	 * 
	 * @param pd
	 *            参数
	 * @return
	 * @throws Exception
	 */
	@Value("${system.role.sp}")
	public String ROLE_IDS;
	@RequestMapping(value = "/saveEdit")
	public PageData saveEdit(@RequestBody PageData pd) throws Exception {
		pd.put("ROLE_ID", ROLE_IDS);
		pd.put("UR_ID", UuidUtil.get32UUID());
		pd.put("PHONE", pd.getString("SP_PHONE"));
		PageData pds = userMapper.selectByPhone(pd);
		if(pds != null){
			pd.put("SYS_UI_ID", pds.getString("USER_ID"));
			pd.put("USER_ID", pds.getString("USER_ID"));
		}else{
			pd.put("SYS_UI_ID", pd.getString("USER_ID"));
			agentMapper.saveUser(pd);
		}
		userMapper.deleteUserRole(pd);
		userMapper.saveUserRole(pd);
		agentMapper.edit(pd);
		pd.put("msg", "successEdit");
		return WebResult.requestSuccess(pd);
	}
	
	/**
	 * 删除
	 * 
	 * @param pd
	 *            参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/toDelete")
	public PageData toDelete(@RequestBody PageData pd) throws Exception {
		String s_IDS = pd.getString("IDS");
		if(null != s_IDS && !"".equals(s_IDS)){
			pd.put("IDSs", s_IDS.split(","));
			agentMapper.delete(pd);
			pd.put("msg", "success");
		}else{
			pd.put("msg", "failed");
		}
		return WebResult.requestSuccess(pd);
	}
	/**
	 * 查询下拉列表代理商
	 * 
	 * @param pd
	 *            参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getAllAgent")
	public Object getAllAgent(@RequestBody PageData pd) throws Exception {
		PageData pds = agentMapper.selectUserArea(pd);
		if(pds != null){
			pd.put("UA_AREA", pds.getString("UA_AREA"));
		}
		List lists = agentMapper.getAllAgent(pd);
		return WebResult.requestSuccess(lists);
	}
	/**
	 * 查询手机号是否已被占用
	 * 
	 * @param pd
	 *            参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/selectPhone")
	public Object selectPhone(@RequestBody PageData pd) throws Exception {
		PageData map = new PageData();
		String errInfo = "true";//success
			if (agentMapper.findByUId(pd) != null) {
				errInfo = "false";//error
			}
			map.put("result", errInfo); // 返回结果
			// 结果集封装
			return WebResult.requestSuccess(map);
	}
}
