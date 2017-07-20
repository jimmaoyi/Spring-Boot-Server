package com.maryun.lb.mapper.jyb.operator;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.maryun.model.PageData;

@Mapper
public interface AccompanyStaffManagementMapper {
	List<PageData> listPage(PageData pd); // 陪诊人员信息查询

}
