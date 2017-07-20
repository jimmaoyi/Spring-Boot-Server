package com.maryun.mapper.app.user.health;

import org.apache.ibatis.annotations.Mapper;

import com.maryun.model.PageData;

@Mapper
public interface AppUserHealthMapper {
	PageData getUserHealthData(PageData pd); // 查询用户健康信息
}
