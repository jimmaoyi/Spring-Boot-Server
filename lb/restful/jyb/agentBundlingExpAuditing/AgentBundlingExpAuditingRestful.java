package com.maryun.lb.restful.jyb.agentBundlingExpAuditing;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.maryun.lb.mapper.jyb.agentBundlingExpAuditing.AgentBundlingExpAuditingMapper;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.WebResult;

/**
 * @author: Libra(Hyt)
 * Date: 2017年5月5日 上午9:11:17
 * Version: 1.0
 * @Description: 代理商绑定专家申请审核Restful
 */
@RestController
@RequestMapping(value = "jyb/audit")
public class AgentBundlingExpAuditingRestful extends BaseRestful{

	@Autowired
	private AgentBundlingExpAuditingMapper agentBundlingExpAuditingMapper;
	
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
		lists = agentBundlingExpAuditingMapper.listPage(pd);
		// 分页
		pd = this.getPagingPd(lists);
		// 结果集封装
		return WebResult.requestSuccess(pd);
	}
	
	
	/**
	 * 获取代理商绑定专家申请列表
	 * @param pd
	 * @return
	 */
	@RequestMapping(value = "/viewList")
	public PageData toView(@RequestBody PageData pd){
		List<PageData> lists = null;
		if (pd.getPageNumber() != 0) {
			PageHelper.startPage(pd.getPageNumber(), pd.getPageSize());
		}
		lists = agentBundlingExpAuditingMapper.getBundExpList(pd);
		pd = this.getPagingPd(lists);
		return WebResult.requestSuccess(pd);
	}
	
	/**
	 * 管理员审核
	 * @param pd
	 * @return
	 */
	@RequestMapping(value = "toCheck")
	public PageData toCheck(@RequestBody PageData pd){
		String DEL_BIND = pd.getString("DEL_BIND");
		String RETURN_REASON = pd.getString("ReturnReason");
		pd.put("RETURN_REASON", RETURN_REASON);
		if(DEL_BIND != null && DEL_BIND != ""){
			if(RETURN_REASON != null && !RETURN_REASON.equals("")){
				agentBundlingExpAuditingMapper.toCheck(pd);
				pd.put("msg", "success");
			}else{
				agentBundlingExpAuditingMapper.toCheck(pd);
				pd.put("msg", "success");
			}
		}else{
			pd.put("msg", "failure");
			return WebResult.requestFailed(200, "操作有误", pd);
		}
		return WebResult.requestSuccess(pd);
	}
	
	
}
