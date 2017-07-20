package com.maryun.lb.mapper.jyb.agentBundlingExpert;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.maryun.model.PageData;

/**
 * @author: Libra(Hyt)
 * Date: 2017年5月3日 下午3:42:25
 * Version: 1.0
 * @Description:  代理商绑定专家申请管理Mapper
 */
@Mapper
public interface AgentBundlingExpertMapper {

	List<PageData> listPage(PageData pd);

	PageData findById(PageData pd);

	PageData getRETURN_REASON(PageData pds);

	String getUserSp(PageData pd);

	void bing(PageData pd);

	List<PageData> expListPage(PageData pd);

	String[] findBySP(PageData pd);


}
