package com.maryun.lb.mapper.jyb.image;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.maryun.model.PageData;

@Mapper
public interface ImageMapper {
	void delete(PageData pd);
	void deleteOne(PageData pd);
	void saveAdd(PageData pd);
	List<PageData> findAll(PageData pd);
	PageData selectOne(PageData pd);
}
