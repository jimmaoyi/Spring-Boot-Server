package com.maryun.mapper.system.dictionaries;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.maryun.mapper.BaseMapper;
import com.maryun.model.PageData;

@Mapper
public interface DictionariesMapper extends BaseMapper{
	List<PageData> getSubDicList(PageData pd);
	List<PageData> getSubDicListByParentCode(PageData pd);
	/** 查询省区字典 */
	List<PageData> getSubDicAreaListByParentCode(PageData pd);
	/** 查询省极下市区字典 */
	List<PageData> getSubDicAreaListByProvinceCode(PageData pd);
	List<PageData> findSameCode(PageData pd);
	List<PageData> getInfoByCode(PageData pd);
}
