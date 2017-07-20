package com.maryun.lb.mapper.jyb.feedBack;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.maryun.model.PageData;

/**
 * 类名称：GuideMapper 创建人：ChuMingZe 创建时间：2017年2月21日
 * 
 * @version
 */
@Mapper
public interface FeedBackMapper {
	/**
	 * 查询
	 * @param map 查询条件
	 * @return
	 */
	List<PageData> listPage(PageData map);
	/**
	 * 按主键Id查询
	 * @param map 主键Id
	 * @return
	 */
	PageData findById(PageData map);
	
	/**
	 * 删除
	 * @param map 数据集合
	 */
	void delete(PageData map);
	/**
	 * 查看图片信息
	 * @param map 数据集合
	 */
	List<PageData> selectPic(PageData pd);
	
}
