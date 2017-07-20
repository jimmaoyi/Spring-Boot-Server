package com.maryun.mapper.system.dept;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.maryun.mapper.BaseMapper;
import com.maryun.model.PageData;

@Mapper
public interface DeptMapper extends BaseMapper{
	List<PageData> getMaxDeptID();
	List<PageData> findByIds(String[] args);
}
