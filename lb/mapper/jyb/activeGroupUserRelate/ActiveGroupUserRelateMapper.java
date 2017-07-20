package com.maryun.lb.mapper.jyb.activeGroupUserRelate;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.maryun.model.PageData;

/**
 * 类名称：ActiveGroupUserRelateMapper 创建人：ChuMingZe 创建时间：2017年2月26日
 * 
 * @version
 */
@Mapper
public interface ActiveGroupUserRelateMapper {
	/**
	 * 查询
	 * @param map 查询条件
	 * @return
	 */
	List<PageData> listPage(PageData map);
	/**
	 * 按活动主键Id获取活动信息及报名数量
	 * @param map 参数
	 * @return
	 */
	PageData getActiveAndAppleInfo(PageData map);
}
