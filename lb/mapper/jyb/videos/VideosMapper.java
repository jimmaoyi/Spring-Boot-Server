package com.maryun.lb.mapper.jyb.videos;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.maryun.model.PageData;

/**
 * 类名称：VideoMapper 创建人：ChuMingZe 创建时间：2017年2月27日
 * 
 * @version
 */
@Mapper
public interface VideosMapper {
	/**
	 * 查询
	 * @param map 查询条件
	 * @return
	 */
	List<PageData> listPage(PageData map);
	/**
	 * 批量添加视频信息
	 * @param maps 集合参数
	 */
	void addVideos(List<PageData> maps);
	/**
	 * 批量添加视频与主键对应关系表
	 * @param maps 集合参数
	 */
	void addVideoRelate(List<PageData> maps);
	/**
	 * 删除视频信息
	 * @param map 集合参数
	 */
	void deleteVideos(PageData map);
	/**
	 * 按类型删除视频信息
	 * @param map 集合参数
	 */
	void deleteVideosByType(PageData map);
	/**
	 * 删除视频与主键对应关系表
	 * @param maps 集合参数
	 */
	void deleteVideoRelate(PageData map);
}
