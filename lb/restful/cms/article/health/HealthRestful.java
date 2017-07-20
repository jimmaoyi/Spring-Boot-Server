package com.maryun.lb.restful.cms.article.health;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.maryun.common.service.KeywordFilter;
import com.maryun.lb.mapper.cms.article.health.HealthMapper;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.UuidUtil;
import com.maryun.utils.WebResult;

/**
 * 类名称：HealthRestful 创建人：ChuMingZe 创建时间：2017年2月13日
 * 
 * @version
 */
@RestController
@RequestMapping(value = "/articleHealth")
public class HealthRestful extends BaseRestful {
	@Autowired
	private HealthMapper healthMapper;

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
		lists = healthMapper.listPage(pd);
		// 分页
		pd = this.getPagingPd(lists);
		// 结果集封装
		return WebResult.requestSuccess(pd);
	}

	/**
	 * 按主键Id查询
	 * 
	 * @param pd 查询条件
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/findById")
	public PageData findById(@RequestBody PageData pd) throws Exception {
		pd = healthMapper.findById(pd);
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
		pd.put("ARTICLE_ID", UuidUtil.get32UUID());
		healthMapper.save(pd);
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
		healthMapper.edit(pd);
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
		pd.put("words", pd.getString("ARTICLE_CONTENTS"));
		returnState = keywordFilter.checkWords(pd);
		if ("400".equals(returnState.getString("state"))) {
			pd.put("msg", "error");
			pd.put("error_tip", "内容含有敏感词，请修改后保存"); // 内容含敏感
		}
		pd.put("words", pd.getString("PUBLISH_DEPT"));
		returnState = keywordFilter.checkWords(pd);
		if ("400".equals(returnState.getString("state"))) {
			pd.put("msg", "error");
			pd.put("error_tip", "发表单位含有敏感词，请修改后保存"); // 标题错误
		}
		pd.put("words", pd.getString("PUBLISH_NAME"));
		returnState = keywordFilter.checkWords(pd);
		if ("400".equals(returnState.getString("state"))) {
			pd.put("msg", "error");
			pd.put("error_tip", "发表人含有敏感词，请修改后保存"); // 标题错误
		}
		pd.put("words", pd.getString("EXPERT_HOSP"));
		returnState = keywordFilter.checkWords(pd);
		if ("400".equals(returnState.getString("state"))) {
			pd.put("msg", "error");
			pd.put("error_tip", "所属医院含有敏感词，请修改后保存"); // 标题错误
		}
		pd.put("words", pd.getString("EXPERT_DUTY"));
		returnState = keywordFilter.checkWords(pd);
		if ("400".equals(returnState.getString("state"))) {
			pd.put("msg", "error");
			pd.put("error_tip", "职称含有敏感词，请修改后保存"); // 标题错误
		}
		pd.put("words", pd.getString("EXPERT_NAME"));
		returnState = keywordFilter.checkWords(pd);
		if ("400".equals(returnState.getString("state"))) {
			pd.put("msg", "error");
			pd.put("error_tip", "专家含有敏感词，请修改后保存"); // 标题错误
		}
		pd.put("words", pd.getString("ARTICLE_TITLE"));
		returnState = keywordFilter.checkWords(pd);
		if ("400".equals(returnState.getString("state"))) {
			pd.put("msg", "error");
			pd.put("error_tip", "标题含有敏感词，请修改后保存"); // 标题错误
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
	public PageData delete(@RequestBody PageData pd) throws Exception {
		String s_IDS = pd.getString("IDS");
		if (null != s_IDS && !"".equals(s_IDS)) {
			pd.put("IDSs", s_IDS.split(","));
			healthMapper.delete(pd);
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
				healthMapper.check(pd);
				lists = new ArrayList<PageData>();
				for (String s_tmp : sa_IDS) {
					PageData pd_tmp = new PageData();
					pd_tmp.put("CL_ID", UuidUtil.get32UUID());
					pd_tmp.put("CL_TYPE", "1");
					pd_tmp.put("KEY_ID", s_tmp);
					pd_tmp.put("CL_USERID", pd.getString("CHANGE_UID"));
					pd_tmp.put("CL_LEVEL", pd.getString("CHECK_STATE"));
					pd_tmp.put("CL_REASON", pd.getString("Res"));
					lists.add(pd_tmp);
				}
				healthMapper.checkLog(lists);
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
		pd = healthMapper.findCheckLogById(pd);
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
		healthMapper.updatePublishState(pd);
		pd.put("msg", "success");
		return WebResult.requestSuccess(pd);
	}

}
