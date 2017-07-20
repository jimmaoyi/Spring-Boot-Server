package com.maryun.lb.mapper.jyb.userShare;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.maryun.model.PageData;

/**
 * 类名称：UserShareMapper 创建人：ChuMingZe 创建时间：2017年3月6日
 * 
 * @version
 */
@Mapper
public interface UserShareMapper {
	/**
	 * 查询
	 * @param map 查询条件
	 * @return
	 */
	List<PageData> listPage(PageData map);
	/**
	 * 发布
	 * @param map
	 */
	void updateRecommendState(PageData map);
}
