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
public interface IptvLayoutMapper {
	PageData getLayoutList(PageData pd);//查询layout表中数据
	
	List<PageData> getTabList(PageData pd); // 查询tab表中数据
	
	List<PageData> getLayoutDetailList(PageData pd); //查询layout_detail表中数据
}
