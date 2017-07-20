package com.maryun.lb.mapper.jyb.commMemo;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.maryun.model.PageData;

/**
 * 类名称：CommunicateMemoMapper 创建人：ChuMingZe 创建时间：2017年2月22日
 * 
 * @version
 */
@Mapper
public interface CommunicateMemoMapper {
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
	 * 按主键Uid查询
	 * @param map 主键Id
	 * @return
	 */
	PageData findByUid(PageData map);
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
}
