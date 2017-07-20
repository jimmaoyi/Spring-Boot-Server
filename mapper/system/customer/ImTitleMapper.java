package com.maryun.mapper.system.customer;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.maryun.mapper.BaseMapper;
import com.maryun.model.PageData;

@Mapper
public interface ImTitleMapper extends BaseMapper{
	public List<PageData> notReportList();
}
