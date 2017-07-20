package com.maryun.mapper.app.user.userinfo;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.maryun.model.PageData;

@Mapper
public interface AppUserScanMapper {

	List<PageData> getScanList(PageData pd);// 用户浏览列表(专家)
	void addScan(PageData pd);// 添加浏览
	void updateScan(PageData pd);// 更新浏览
	PageData getScan(PageData pd);// 获取浏览
	void delScan(PageData pd);//删除单个浏览
	void delScanAll(PageData pd);//删除我的浏览
}
