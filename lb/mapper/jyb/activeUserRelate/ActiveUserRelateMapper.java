package com.maryun.lb.mapper.jyb.activeUserRelate;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.maryun.model.PageData;

/**
 * 类名称：ActiveUserRelateMapper 创建人：ChuMingZe 创建时间：2017年2月26日
 * 
 * @version
 */
@Mapper
public interface ActiveUserRelateMapper {
	/**
	 * 查询
	 * @param map 查询条件
	 * @return
	 */
	List<PageData> listPage(PageData map);
	/**
	 * 查询个人参与历史活动
	 * @param map 查询条件
	 * @return
	 */
	List<PageData> listPageByActHis(PageData map);
	/**
	 * 修改选取状态
	 * @param map 参数
	 */
	void updateState(PageData map);
	/**
	 * 按活动主键Id获取活动信息及报名数量
	 * @param map 参数
	 * @return
	 */
	PageData getActiveAndAppleInfo(PageData map);
}
