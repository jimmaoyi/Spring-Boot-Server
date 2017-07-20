package com.maryun.mapper.system.button;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.maryun.model.PageData;
@Mapper
public interface ButtonMapper {
	List<PageData> buttonlistPage(PageData map);
	PageData findById(PageData map);
	void saveButton(PageData map);
	void editButton(PageData map);
	void deleteButton(String[] map);
}
