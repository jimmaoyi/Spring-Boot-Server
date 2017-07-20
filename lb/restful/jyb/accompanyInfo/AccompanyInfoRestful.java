package com.maryun.lb.restful.jyb.accompanyInfo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.maryun.lb.mapper.jyb.accompanyInfo.AccompanyInfoMapper;
import com.maryun.lb.mapper.jyb.agent.AgentMapper;
import com.maryun.mapper.system.user.UserMapper;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.UuidUtil;
import com.maryun.utils.WebResult;
/**
 * 类名称：AccompanyInfoRestful 创建人：SunYongLiang 创建时间：2017年2月22日
 * 
 * @version
 */
@RestController
@RequestMapping(value = "/accompanyInfo")
public class AccompanyInfoRestful extends BaseRestful{
	@Autowired
	private AccompanyInfoMapper accompanyInfoMapper;
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
		if (pd.getPageNumber() != 0) {
			PageHelper.startPage(pd.getPageNumber(), pd.getPageSize());
		}
		PageData pds = accompanyInfoMapper.selectUserArea(pd);
		if(pds != null){
			pd.put("UA_AREA", pds.getString("UA_AREA"));
		}
		lists = accompanyInfoMapper.listPage(pd);
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
		String O_ID =pd.getString("O_ID");
		PageData pds = pd;
		pd = accompanyInfoMapper.findById(pd);
		if(O_ID != null && O_ID != ""){
			pds = accompanyInfoMapper.selectReturnReason(pds);
			pd.put("REJECT_REASON", pds.getString("REJECT_REASON"));
			pd.put("OSHA_STATUS", pds.getString("OSHA_STATUS"));
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
	@RequestMapping(value = "/saveAdd")
	public PageData saveAdd(@RequestBody PageData pd) throws Exception {
		pd.put("AI_ID", UuidUtil.get32UUID());
		PageData pds = pd;
		pds = agentMapper.selectID(pds);
		pd.put("SP_ID", pds.getString("SP_ID"));
		pd.put("PHONE", pd.getString("AI_PHONE"));
		PageData pdes = userMapper.selectByPhone(pd);
		if(pdes != null){
			pd.put("USER_ID", pdes.getString("USER_ID"));
			accompanyInfoMapper.save(pd);
		}else{
			accompanyInfoMapper.saveUser(pd);
			accompanyInfoMapper.save(pd);
		}
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
	@RequestMapping(value = "/saveEdit")
	public PageData saveEdit(@RequestBody PageData pd) throws Exception {
		pd.put("PHONE", pd.getString("AI_PHONE"));
		PageData pds = userMapper.selectByPhone(pd);
		if(pds != null){
			pd.put("USER_ID", pds.getString("USER_ID"));
		}else{
			accompanyInfoMapper.saveUser(pd);
		}
		accompanyInfoMapper.edit(pd);
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
			accompanyInfoMapper.delete(pd);
			pd.put("msg", "success");
		}else{
			pd.put("msg", "failed");
		}
		return WebResult.requestSuccess(pd);
	}
	/**
	 * 运营商查询用户信息
	 * 
	 * @param pd
	 *            参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/findList")
	public PageData findList(@RequestBody PageData pd) throws Exception {
		List<PageData> lists = null;
		if (pd.getPageNumber() != 0) {
			PageHelper.startPage(pd.getPageNumber(), pd.getPageSize());
		}
		PageData pds = agentMapper.selectID(pd);
		pd.put("SP_ID", pd.getString(""));
		lists = accompanyInfoMapper.listsPage(pd);
		pd = this.getPagingPd(lists);
		return WebResult.requestSuccess(pd);
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
			if (accompanyInfoMapper.findByUId(pd) != null) {
				errInfo = "false";//error
			}
			map.put("result", errInfo); // 返回结果
			// 结果集封装
			return WebResult.requestSuccess(map);
	}
}
