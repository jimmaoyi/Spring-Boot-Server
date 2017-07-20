package com.maryun.lb.mapper.cms.article.rotation;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.maryun.model.PageData;

/**
 * 类名称：RotationMapper 创建人：ChuMingZe 创建时间：2017年2月13日
 * 
 * @version
 */
@Mapper
public interface RotationMapper {
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
	 * 添加
	 * @param map 数据集合
	 */
	void save(PageData map);
	/**
	 * 修改
	 * @param map 数据集合
	 */
	void edit(PageData map);
	/**
	 * 删除
	 * @param map 数据集合
	 */
	void delete(PageData map);
	/**
	 * 发布
	 * @param map
	 */
	void updatePublishState(PageData map);
	/**
	 * 审核
	 * @param map 数据集合
	 */
	void check(PageData map);
	/**
	 * 按主键获取审核日志的退回原因
	 * @param map 数据集合
	 * @return
	 */
	PageData findCheckLogById(PageData map);
	/**
	 * 记录审核日志
	 * @param map 数据集合
	 */
	void checkLog(List<PageData> map);
}
