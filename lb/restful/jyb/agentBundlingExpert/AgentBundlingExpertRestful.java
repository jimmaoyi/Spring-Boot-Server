package com.maryun.lb.restful.jyb.agentBundlingExpert;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.maryun.lb.mapper.jyb.agentBundlingExpert.AgentBundlingExpertMapper;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.UuidUtil;
import com.maryun.utils.WebResult;

/**
 * @author: Libra(Hyt)
 * Date: 2017年5月3日 上午10:41:22
 * Version: 1.0
 * @Description:  代理商绑定专家申请管理Restful
 */
@RestController
@RequestMapping(value = "jyb/bundling")
public class AgentBundlingExpertRestful extends BaseRestful{

	@Autowired
	private AgentBundlingExpertMapper agentBundlingExpertMapper;
	
	/**
	 * 分页查询
	 * 
	 * @param pd
	 *            查询、分页条件
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/search")
	public PageData pageSearch(@RequestBody PageData pd) throws Exception{
		List<PageData> lists = null;
		if (pd.getPageNumber() != 0) {
			PageHelper.startPage(pd.getPageNumber(), pd.getPageSize());
		}
		String SP_ID = agentBundlingExpertMapper.getUserSp(pd);
		pd.put("SP_ID", SP_ID);
		String[] HEL_IDS = agentBundlingExpertMapper.findBySP(pd);
		if(HEL_IDS != null && !HEL_IDS.equals("")){
			pd.put("HEL_IDS", HEL_IDS);
		}
		lists = agentBundlingExpertMapper.listPage(pd);
		//与代理商相关的专家
		List<PageData> explists = agentBundlingExpertMapper.expListPage(pd);
		//去重
		for(PageData data : lists){
			data.put("DEL_BIND", "1");
			explists.add(data);
		}
		// 分页
		pd = this.getPagingPd(explists);
		// 结果集封装
		return WebResult.requestSuccess(pd);
	}
	
	/**
	 * 获取专家个人详情
	 * @param pd
	 * @return
	 */
	@RequestMapping(value = "/findById")
	public PageData findById(@RequestBody PageData pd){
		PageData pds = pd;
		String HEL_ID = pd.getString("HEL_ID");
		pd = agentBundlingExpertMapper.findById(pd);
		if(HEL_ID != null && HEL_ID != ""){
			pds = agentBundlingExpertMapper.getRETURN_REASON(pds);
			if(pds != null){
				pd.put("RETURN_REASON", pds.getString("RETURN_REASON"));
			}
		}
		return WebResult.requestSuccess(pd);
	}
	
	/**
	 * 绑定
	 * 
	 * @param pd
	 *            参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/saveBing")
	public PageData saveBing(@RequestBody PageData pd) throws Exception {
		String s_IDS = pd.getString("IDS");
		String SP_ID = agentBundlingExpertMapper.getUserSp(pd);
		pd.put("SP_ID", SP_ID);
		if (null != s_IDS && !"".equals(s_IDS)) {
			String[] sa_IDSs = s_IDS.split(",");
			pd.put("HEL_IDs", sa_IDSs);
			for (String s_tmp : sa_IDSs) {
				PageData pd_tmp = new PageData();
				pd_tmp.put("ELR_ID", UuidUtil.get32UUID());
				pd_tmp.put("SP_ID", SP_ID);
				pd_tmp.put("DEL_BIND", "2");
				pd_tmp.put("HEL_ID", s_tmp);
				agentBundlingExpertMapper.bing(pd_tmp);
			}
			pd.put("msg", "success");
		}else{
			pd.put("msg", "failure");
		}
		return WebResult.requestSuccess(pd);
	}
	
	
}
