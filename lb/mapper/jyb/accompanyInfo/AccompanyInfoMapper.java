package com.maryun.lb.mapper.jyb.accompanyInfo;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.maryun.model.PageData;

/**
 * 类名称：AccompanyInfoMapper 创建人：SunYongLiang 创建时间：2017年2月22日
 * 
 * @version
 */
@Mapper
public interface AccompanyInfoMapper {
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
	 * 添加sys_user用户
	 * @param map 数据集合
	 */
	void saveUser(PageData map);
	/**
	 * 代理商查询用户
	 * @param map 数据集合
	 */
	List<PageData> listsPage(PageData map);
	/**
	 * 查询手机号是否被占用
	 * @param map 数据集合
	 */
	PageData findByUId(PageData pd);
	/**
	 * 查询退回理由
	 * @param map 数据集合
	 */
	PageData selectReturnReason(PageData map);
	/**
	 * 查询地区
	 * @param map 数据集合
	 */
	PageData selectUserArea(PageData pd);
}
