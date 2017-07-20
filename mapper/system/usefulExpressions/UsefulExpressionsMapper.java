package com.maryun.mapper.system.usefulExpressions;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.maryun.model.PageData;

@Mapper
public interface UsefulExpressionsMapper {
	List<PageData> selectAll(PageData pd);
	void insert(PageData pd);
	void delete(String[] pd);
	void update(PageData pd);
}
