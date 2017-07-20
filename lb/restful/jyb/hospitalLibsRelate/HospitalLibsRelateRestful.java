package com.maryun.lb.restful.jyb.hospitalLibsRelate;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.maryun.lb.mapper.jyb.hospitalLibsRelate.HospitalLibsRelateMapper;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.UuidUtil;
import com.maryun.utils.WebResult;

@RestController
@RequestMapping(value = "/hospExpLibRela")
public class HospitalLibsRelateRestful extends BaseRestful {
	@Autowired
	private HospitalLibsRelateMapper hospitalLibsRelateMapper;

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
		lists = hospitalLibsRelateMapper.listPage(pd);
		// 分页
		pd = this.getPagingPd(lists);
		// 结果集封装
		return WebResult.requestSuccess(pd);
	}

	/**
	 * 分页查询专家库审核通过数据
	 * 
	 * @param pd
	 *            查询、分页查询
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/expertsPageSearch")
	public PageData expertsPageSearch(@RequestBody PageData pd) throws Exception {
		String s_bingState = pd.getString("bingState");
		List<PageData> lists = null;
		//获取代理商绑定专家数量
		int i_agentBingExpSize = Integer.parseInt(hospitalLibsRelateMapper.getAgentBingExpSize(pd).getString("agentBingExpertSize"));
		if("1".equals(s_bingState)){//绑定
			if(i_agentBingExpSize == 0){//第一次绑定专家，默认选取代理商提交的专家
				if (pd.getPageNumber() != 0) {
					PageHelper.startPage(pd.getPageNumber(), pd.getPageSize());
				}
				lists = hospitalLibsRelateMapper.getAgentSubmitExpert(pd);
			}else{//第一次以后，默认选中之前选取绑定专家信息
				if (pd.getPageNumber() != 0) {
					PageHelper.startPage(pd.getPageNumber(), pd.getPageSize());
				}
				lists = hospitalLibsRelateMapper.getAgentBingedExpert(pd);
			}
		}else if("2".equals(s_bingState)){//解绑
			if (pd.getPageNumber() != 0) {
				PageHelper.startPage(pd.getPageNumber(), pd.getPageSize());
			}
			//查询已绑定的专家库审核通过数据
			lists = hospitalLibsRelateMapper.expertsBingListPage(pd);
		}
		// 分页
		pd = this.getPagingPd(lists);
		// 结果集封装
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
		if (null != s_IDS && !"".equals(s_IDS)) {
			List<PageData> lists = new ArrayList<PageData>();
			String[] sa_IDSs = s_IDS.split(",");
			pd.put("HEL_IDs", sa_IDSs);
			//获取需绑定专家是否重新
			List<PageData> lists_tmp = hospitalLibsRelateMapper.getAgentBingExpInfo(pd);
			for (String s_tmp : sa_IDSs) {
				PageData pd_tmp = new PageData();
				pd_tmp.put("ELR_ID", UuidUtil.get32UUID());
				pd_tmp.put("SP_ID", pd.getString("SP_ID"));
				pd_tmp.put("HEL_ID", s_tmp);
				if(null != lists_tmp && lists_tmp.size() > 0){
					boolean isFlag = false;
					for (PageData pd_temp : lists_tmp) {
						if(s_tmp.equals(pd_temp.getString("HEL_ID"))){
							isFlag = true;
							break;
						}
					}
					if(!isFlag){
						lists.add(pd_tmp);
					}
				}else{
					lists.add(pd_tmp);
				}
			}
			if(null != lists && lists.size() > 0){
				hospitalLibsRelateMapper.bing(lists);
				pd.put("msg", "success");
			}else{
				pd.put("msg", "error");
			}
		}else{
			pd.put("msg", "failed");
		}
		return WebResult.requestSuccess(pd);
	}
	
	/**
	 * 取消绑定
	 * 
	 * @param pd
	 *            参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/saveCancelBing")
	public PageData saveCancelBing(@RequestBody PageData pd) throws Exception {
		String s_IDS = pd.getString("IDS");
		if (null != s_IDS && !"".equals(s_IDS)) {
			pd.put("ELR_IDs", s_IDS.split(","));
			hospitalLibsRelateMapper.cancelBing(pd);
			pd.put("msg", "success");
		}else{
			pd.put("msg", "failed");
		}
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
	public PageData delete(@RequestBody PageData pd) throws Exception {
		String s_IDS = pd.getString("IDS");
		if (null != s_IDS && !"".equals(s_IDS)) {
			pd.put("IDSs", s_IDS.split(","));
			hospitalLibsRelateMapper.delete(pd);
			pd.put("msg", "success");
		} else {
			pd.put("msg", "failed");
		}
		return WebResult.requestSuccess(pd);
	}
}
