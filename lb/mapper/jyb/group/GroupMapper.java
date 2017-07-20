package com.maryun.lb.mapper.jyb.group;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.maryun.model.PageData;

/**
 * 类名称：GroupMapper 创建人：ChuMingZe 创建时间：2017年2月26日
 * 
 * @version
 */
@Mapper
public interface GroupMapper {
	/**
	 * 查询
	 * @param map 查询条件
	 * @return
	 */
	List<PageData> listPage(PageData map);
	/**
	 * 删除
	 * @param map 数据集合
	 */
	void delete(PageData map);
}
