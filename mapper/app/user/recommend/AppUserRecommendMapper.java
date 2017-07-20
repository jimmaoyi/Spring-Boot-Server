package com.maryun.mapper.app.user.recommend;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.maryun.model.PageData;

@Mapper
public interface AppUserRecommendMapper {
	
	List<PageData> getActiveRotationList(PageData pd); // 获取轮播列表
	
	List<PageData> getRotationList(PageData pd); // 获取轮播列表

	PageData getRotationDetail(PageData pd); // 轮播信息详情

	List<PageData> getHealthArticleList(PageData pd); // 获取健康教育列表

	PageData getHealthArticleDetail(PageData pd); // 获取健康教育详情

}
