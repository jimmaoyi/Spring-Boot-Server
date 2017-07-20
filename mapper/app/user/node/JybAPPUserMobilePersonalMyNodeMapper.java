package com.maryun.mapper.app.user.node;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.maryun.model.PageData;

@Mapper
public interface JybAPPUserMobilePersonalMyNodeMapper {

	/**
	 * 用户服务状态节点表查询
	 * @param pd
	 * @return
	 */
	List<PageData> nodeList(PageData pd);
	
	/**
	 * 查询用户最新的订单的订单节点状态中的状态更新时间最新的内容显示
	 * @param pd
	 * @return
	 */
	PageData theNewServiceNodeStateByOrder(PageData pd);
	
	/**
	 * 修改订单不同服务状态节点下的内容
	 * @param pd
	 */
	void updateNodeContent(PageData pd);
	
	/**
	 * 陪诊人员更新不同服务节点的地点信息
	 * @param pd
	 */
	void updateNodeAddress(PageData pd);
	/**
	 * 陪诊人员更新不同服务节点的照片信息
	 * @param pd
	 */
	void updateNodePhoto(PageData pd);
	
	/**
	 * 查询节点模板表中的所有信息
	 * @return
	 */
	List<PageData> nodeModelLists();
	
	/**
	 * 根据选择的节点信息插入到陪诊节点流程中取
	 * @param pd
	 */
	void accompanyInsertKeyNodeInfor(PageData pd);
	
	//可以在确定保存的时候执行该方法
	//插入数据之后紧接着修改confimStatus状态为1
	void updateKeyNodeConfimStatus(PageData pd);
	
}
