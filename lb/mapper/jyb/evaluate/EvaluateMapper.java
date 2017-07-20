package com.maryun.lb.mapper.jyb.evaluate;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.maryun.model.PageData;

/**
 * 类名称：EvaluateMapper 创建人：ChuMingZe 创建时间：2017年2月20日
 * 
 * @version
 */
@Mapper
public interface EvaluateMapper {
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
	 * 代理商查询
	 * @param map 查询条件
	 * @return
	 */
	List<PageData> listPageBySP(PageData map);
	/**
	 * 删除
	 * @param map 数据集合
	 */
	void delete(PageData map);
	/**
	 * 改变状态为显示
	 * @param map 数据集合
	 */
	void changeShow(PageData map);
	/**
	 * 改变状态为不显示
	 * @param map 数据集合
	 */
	void change(PageData map);
	/**
	 * 改变状态为不显示
	 * @param map 数据集合
	 */
	PageData findPicById(PageData pd);
	/**
	 * 查询运营人员区域
	 * @param map 数据集合
	 */
	PageData selectUserArea(PageData pd);
	/**
	 * 查询代理商id
	 * @param map 数据集合
	 */
	PageData selectSpId(PageData pd);
}
