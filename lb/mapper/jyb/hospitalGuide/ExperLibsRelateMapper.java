package com.maryun.lb.mapper.jyb.hospitalGuide;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.maryun.model.PageData;

@Mapper
public interface ExperLibsRelateMapper {
	List<PageData> listPage(PageData pd); // 代理商查询
	void deleteRecording(PageData pd);//删除代理商专家订单记录
	void addRecording(PageData pd);//添加代理商专家订单记录
	void saveKeyNode(PageData pd);//添加keynode表中数据
}
