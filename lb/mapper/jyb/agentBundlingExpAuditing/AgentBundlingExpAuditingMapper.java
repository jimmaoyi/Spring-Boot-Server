package com.maryun.lb.mapper.jyb.agentBundlingExpAuditing;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.maryun.model.PageData;

/**
 * @author: Libra(Hty)
 * Date: 2017年5月5日 上午9:15:36
 * Version: 1.0
 * @Description: 
 */
@Mapper
public interface AgentBundlingExpAuditingMapper {

	List<PageData> listPage(PageData pd);

	List<PageData> getBundExpList(PageData pd);

	String[] getIDS(PageData libPd);

	boolean toCheck(PageData pd);

}
