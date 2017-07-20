package com.maryun.lb.restful.jyb.evaluate;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.maryun.lb.mapper.jyb.evaluate.EvaluateMapper;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.WebResult;

/**
 * 类名称：EvaluateRestful 创建人：ChuMingZe 创建时间：2017年2月20日
 * 
 * @version
 */
@RestController
@RequestMapping(value = "/evaluate")
public class EvaluateRestful extends BaseRestful {
//	//系统服务地址
//	@Value("${const.dic.IMG_SERVER_PREFIX}")
	public String IMG_SERVER_PREFIX;
		
	@Autowired
	private EvaluateMapper evaluateMapper;

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
		PageData pds = evaluateMapper.selectUserArea(pd);
		if(pds != null){
			pd.put("UA_AREA", pds.getString("UA_AREA"));
		}
		if (pd.getPageNumber() != 0) {
			PageHelper.startPage(pd.getPageNumber(), pd.getPageSize());
		}
		lists = evaluateMapper.listPage(pd);
		// 分页
		pd = this.getPagingPd(lists);
		// 结果集封装
		return WebResult.requestSuccess(pd);
	}
	
	/**
	 * 按主键I查询
	 * 
	 * @param pd
	 *            查询条件
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/findById")
	public PageData findById(@RequestBody PageData pd) throws Exception {
		PageData pds = pd;
		pd.put("IMG_SERVER_PREFIX", IMG_SERVER_PREFIX);
		pd = evaluateMapper.findById(pd);
		if(pd != null){
			pd.put("basePaths", pds.getString("basePath"));
			pd.put("EVA_ID", pds.getString("EVA_ID"));
		}
		pds = evaluateMapper.findPicById(pds);
		if(pds != null){
			pd.put("imgPaths", pds.getString("PATH").split(","));
		}
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
		PageData pds = evaluateMapper.selectSpId(pd);
		if(pds != null){
			pd.put("SP_ID", pds.getString("SP_ID"));
		}
		if (pd.getPageNumber() != 0) {
			PageHelper.startPage(pd.getPageNumber(), pd.getPageSize());
		}
		lists = evaluateMapper.listPageBySP(pd);
		// 分页
		pd = this.getPagingPd(lists);
		// 结果集封装
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
			evaluateMapper.delete(pd);
			pd.put("msg", "success");
		} else {
			pd.put("msg", "failed");
		}
		return WebResult.requestSuccess(pd);
	}
	/**
	 * 设置状态为显示
	 * 
	 * @param pd
	 *            参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/changeShow")
	public PageData changeShow(@RequestBody PageData pd) throws Exception {
		evaluateMapper.changeShow(pd);
		pd.put("msg", "success");
		return WebResult.requestSuccess(pd);
	}
	/**
	 * 设置状态为不显示
	 * 
	 * @param pd
	 *            参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/change")
	public PageData change(@RequestBody PageData pd) throws Exception {
		evaluateMapper.change(pd);
		pd.put("msg", "success");
		return WebResult.requestSuccess(pd);
	}
}
