package com.maryun.lb.restful.jyb.commMemo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.maryun.lb.mapper.jyb.commMemo.CommunicateMemoMapper;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.UuidUtil;
import com.maryun.utils.WebResult;

/**
 * 类名称：CaseHistoryControlller 创建人：ChuMingZe 创建时间：2017年2月22日
 * 
 * @version
 */
@RestController
@RequestMapping(value = "/commMemo")
public class CommunicateMemoRestful extends BaseRestful {
	@Autowired
	private CommunicateMemoMapper communicateMemoMapper;

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
		lists = communicateMemoMapper.listPage(pd);
		// 分页
		pd = this.getPagingPd(lists);
		// 结果集封装
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
		PageData pd_tmp = communicateMemoMapper.findById(pd);
		if(null == pd_tmp){
			pd_tmp = pd;
		}
		return WebResult.requestSuccess(pd_tmp);
	}
	
	/**
	 * 按用户Uid查询 
	 * 
	 * @param pd
	 *            查询条件
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/findByUid")
	public PageData findByUid(@RequestBody PageData pd) throws Exception {
		PageData pd_tmp = communicateMemoMapper.findByUid(pd);
		if(null == pd_tmp){
			pd_tmp = pd;
		}
		return WebResult.requestSuccess(pd_tmp);
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
		pd.put("CH_ID", UuidUtil.get32UUID());
		communicateMemoMapper.save(pd);
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
		communicateMemoMapper.edit(pd);
		pd.put("msg", "success");
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
		if (null != s_IDS && !"".equals(s_IDS)) {
			pd.put("IDSs", s_IDS.split(","));
			communicateMemoMapper.delete(pd);
			pd.put("msg", "success");
		} else {
			pd.put("msg", "failed");
		}
		return WebResult.requestSuccess(pd);
	}
}
