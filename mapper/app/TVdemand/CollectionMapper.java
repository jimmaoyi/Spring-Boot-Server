/**
 * 
 */
package com.maryun.mapper.app.TVdemand;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.maryun.model.PageData;

/**
 * @author Administrator
 *
 */
@Mapper
public interface CollectionMapper {
	List<PageData> getCollectonList(PageData pd); // 查询收藏夹列表信息
	
	PageData getCollectionByID(PageData pd);// 查询某节目是否被收藏
	
	void insertCollection(PageData pd);//添加收藏记录
	
	void deleteCollection(PageData pd);//取消收藏
}
