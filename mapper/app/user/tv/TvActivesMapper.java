package com.maryun.mapper.app.user.tv;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.maryun.model.PageData;

@Mapper
public interface TvActivesMapper {
	
	List<PageData> getActivesList(PageData pd);//查看活动列表
	List<PageData> getMyActivesList(PageData pd);//查看活动列表
	PageData getActivesDetail(PageData pd);//查看活动详情列表
	List<PageData> getActivesVideoList(PageData pd);//查看活动视频列表
	List<PageData> getActivesOfficialVideoList(PageData pd);//查看活动官方视频列表
}
