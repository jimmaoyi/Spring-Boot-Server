package com.maryun.mapper.system.constant;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.maryun.mapper.BaseMapper;
import com.maryun.model.PageData;

@Mapper
public interface ConstantMapper extends BaseMapper{
	@Override
	//添加缓存
	List<PageData> list(PageData map);
	@Override
	//添加缓存
	PageData findById(PageData map);
	@Override
	//添加缓存
	void save(PageData map);
	@Override
	//添加缓存
	void edit(PageData map);
	@Override
	//添加缓存
	void delete(String[] map);
}
