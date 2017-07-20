package com.maryun.mapper;

import java.util.List;

import com.maryun.model.PageData;

public interface BaseMapper {
	List<PageData> list(PageData map);
	PageData findById(PageData map);
	void save(PageData map);
	void edit(PageData map);
	void delete(String[] map);
}
