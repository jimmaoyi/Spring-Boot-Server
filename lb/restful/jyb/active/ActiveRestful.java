package com.maryun.lb.restful.jyb.active;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.maryun.common.service.KeywordFilter;
import com.maryun.lb.mapper.jyb.active.ActiveMapper;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.UuidUtil;
import com.maryun.utils.WebResult;

/**
 * 类名称：ActiveRestful 创建人：ChuMingZe 创建时间：2017年2月23日
 * 
 * @version
 */
@RestController
@RequestMapping(value = "/active")
public class ActiveRestful extends BaseRestful {
	@Autowired
	private ActiveMapper activeMapper;

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
		lists = activeMapper.listPage(pd);
		// 分页
		pd = this.getPagingPd(lists);
		// 结果集封装
		return WebResult.requestSuccess(pd);
	}

	/**
	 * 按主键I查询
	 * 
	 * @param pd 查询条件
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/findById")
	public PageData findById(@RequestBody PageData pd) throws Exception {
		pd = activeMapper.findById(pd);
		return WebResult.requestSuccess(pd);
	}

	/**
	 * 添加
	 * 
	 * @param pd 参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/saveAdd")
	public PageData saveAdd(@RequestBody PageData pd) throws Exception {
		pd.put("A_ID", UuidUtil.get32UUID());
		pd.put("words", pd.get("A_CONTENT"));
		PageData pd_re = keywordFilter.checkWords(pd);

		activeMapper.save(pd);
		pd.put("msg", "success");
		return WebResult.requestSuccess(pd);
	}

	/**
	 * 修改
	 * 
	 * @param pd 参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/saveEdit")
	public PageData saveEdit(@RequestBody PageData pd) throws Exception {
		String s_order = pd.getString("A_ORDER");
		if (null != s_order && !"".equals(s_order)) {
			activeMapper.saveSetOtherOrder(pd);
		}
		activeMapper.edit(pd);
		pd.put("msg", "success");
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
		pd.put("words", pd.getString("A_CONTENT"));
		returnState = keywordFilter.checkWords(pd);
		if ("400".equals(returnState.getString("state"))) {
			pd.put("msg", "error");
			pd.put("error_tip", "活动内容含有敏感词，请修改后保存"); // 内容含敏感
		}
		pd.put("words", pd.getString("A_ADDR"));
		returnState = keywordFilter.checkWords(pd);
		if ("400".equals(returnState.getString("state"))) {
			pd.put("msg", "error");
			pd.put("error_tip", "活动地点含有敏感词，请修改后保存"); // 地点错误
		}
		pd.put("words", pd.getString("A_NAME"));
		returnState = keywordFilter.checkWords(pd);
		if ("400".equals(returnState.getString("state"))) {
			pd.put("msg", "error");
			pd.put("error_tip", "活动名称含有敏感词，请修改后保存"); // 名称错误
		}
		return WebResult.requestSuccess(pd);
	}

	/**
	 * 删除
	 * 
	 * @param pd 参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/toDelete")
	public PageData toDelete(@RequestBody PageData pd) throws Exception {
		String s_IDS = pd.getString("IDS");
		if (null != s_IDS && !"".equals(s_IDS)) {
			pd.put("IDSs", s_IDS.split(","));
			activeMapper.delete(pd);
			pd.put("msg", "success");
		}
		else {
			pd.put("msg", "failed");
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
				activeMapper.check(pd);
				lists = new ArrayList<PageData>();
				for (String s_tmp : sa_IDS) {
					PageData pd_tmp = new PageData();
					pd_tmp.put("CL_ID", UuidUtil.get32UUID());
					pd_tmp.put("CL_TYPE", "4");
					pd_tmp.put("KEY_ID", s_tmp);
					pd_tmp.put("CL_USERID", pd.getString("CHANGE_UID"));
					pd_tmp.put("CL_LEVEL", pd.getString("A_CHECK_STATE"));
					pd_tmp.put("CL_REASON", pd.getString("Res"));
					lists.add(pd_tmp);
				}
				activeMapper.checkLog(lists);
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
	 * @param pd 参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/toBackReason")
	public PageData toBackReason(@RequestBody PageData pd) throws Exception {
		pd = activeMapper.findCheckLogById(pd);
		return WebResult.requestSuccess(pd);
	}

	/**
	 * 发布、取消发布
	 * 
	 * @param pd 参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/toPublish")
	public PageData toPublish(@RequestBody PageData pd) throws Exception {
		activeMapper.updatePublishState(pd);
		pd.put("msg", "success");
		return WebResult.requestSuccess(pd);
	}
}
