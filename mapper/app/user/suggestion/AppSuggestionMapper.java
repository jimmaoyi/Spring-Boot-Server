package com.maryun.mapper.app.user.suggestion;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.maryun.model.PageData;

@Mapper
public interface AppSuggestionMapper {
	PageData getSuggestion(PageData pd);// 后去意见

	List<PageData> getSuggestionPic(PageData pd);// 后去意见图片

	void addSuggestionPic(PageData pd);// 添加意见对应图片

	void addSuggestion(PageData pd);// 添加意见

	void updateMasterId(PageData pd); // 更新fileupload MasterID
}
