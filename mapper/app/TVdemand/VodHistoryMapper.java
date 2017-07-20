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
public interface VodHistoryMapper {
	List<PageData> getVodHistoryListByToday(PageData pd); // 查询今天历史记录信息
	
	List<PageData> getVodHistoryListByWeek(PageData pd);//查看一周历史记录信息
	
	List<PageData> getVodHistoryListByAgo(PageData pd);//查看更早之前历史记录信息
	
	void deleteRecoding(PageData pd);//删除之前历史记录
	
	PageData getVodHistoryByID(PageData pd);// 查询某节目是否被收藏
	
	void insertVodHistory(PageData pd);//添加历史记录
}
