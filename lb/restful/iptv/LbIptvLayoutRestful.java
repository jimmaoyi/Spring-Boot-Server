package com.maryun.lb.restful.iptv;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.maryun.lb.mapper.iptv.LbIptvLayoutMapper;
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
@RequestMapping(value = "/lbIptvLayout")
public class LbIptvLayoutRestful extends BaseRestful {
	@Autowired
	private AgentMapper agentMapper;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private LbIptvLayoutMapper lbIptvLayoutMapper;
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
		lists = lbIptvLayoutMapper.listPage(pd);
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
		
		pd = lbIptvLayoutMapper.findById(pd);
		return WebResult.requestSuccess(pd);
	}

	/**
	 * 修改查询状态
	 * 
	 * @param pd
	 *            查询条件
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/updateState")
	public PageData updateState(@RequestBody PageData pd) throws Exception {
		lbIptvLayoutMapper.updateState(pd);
		pd.put("msg", "success");
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
		pd.put("IL_ID", UuidUtil.get32UUID());
		pd.put("IL_STATUS", '2');
		lbIptvLayoutMapper.save(pd);
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
		lbIptvLayoutMapper.edit(pd);
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
			lbIptvLayoutMapper.delete(pd);
			pd.put("msg", "success");
		}else{
			pd.put("msg", "failed");
		}
		return WebResult.requestSuccess(pd);
	}
	
}
