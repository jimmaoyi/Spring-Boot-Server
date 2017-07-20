package com.maryun.lb.mapper.platform.bankaccount;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.maryun.model.PageData;

/**
 * 类名称：BankAccountMapper 创建人：SunYongLiang 创建时间：2017年2月14日
 * 
 * @version
 */
@Mapper
public interface BankAccountMapper {
	/**
	 * 查询
	 * @param map 查询条件
	 * @return
	 */
	PageData find();
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

}
