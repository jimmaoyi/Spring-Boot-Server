package com.maryun.lb.mapper.jyb.keysteptemp;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.maryun.model.PageData;

@Mapper
public interface KeystepTemplateMapper {
	List<PageData> findKeystepTemp(PageData pd); // 查询关键节点模版
	void delete(PageData pd); // 查询关键节点模版
	PageData findById(PageData map);
	/**
	 * 添加
	 * @param map 数据集合
	 */
	void save(PageData map);
	/**
	 * 修改
	 * @param map 数据集合
	 */
	void edit(PageData map);
}
