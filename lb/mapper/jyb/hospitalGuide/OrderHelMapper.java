package com.maryun.lb.mapper.jyb.hospitalGuide;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.maryun.model.PageData;

@Mapper
public interface OrderHelMapper {
	void save(PageData pd);//添加分派记录
	
	PageData selectRecording(PageData pd);//查询是否已经有记录
	
	void delrecording(PageData pd);//逻辑删除记录
	
	PageData selectGiId(PageData pd);//查询代理商id
}
