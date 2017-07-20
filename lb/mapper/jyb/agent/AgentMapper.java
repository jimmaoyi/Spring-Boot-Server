package com.maryun.lb.mapper.jyb.agent;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.maryun.model.PageData;

/**
 * 类名称：AgentMapper 创建人：SunYongLiang 创建时间：2017年2月21日
 * 
 * @version
 */
@Mapper
public interface AgentMapper {
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
	 * 删除
	 * @param map 数据集合
	 */
	PageData selectID(PageData map);
	/**
	 * 添加sys_user用户
	 * @param map 数据集合
	 */
	void saveUser(PageData map);
	/**
	 * 查询代理商下拉列表
	 * @param map 数据集合
	 */
	List<PageData> getAllAgent(PageData pd);
	/**
	 * 修改用户表信息
	 * @param map 数据集合
	 */
	void editUser(PageData pd);
	/**
	 * 查询手机号是否被占用
	 * @param map 数据集合
	 */
	PageData findByUId(PageData pd);
	/**
	 * 添加用户关系表
	 * @param map 数据集合
	 */
	void saveUserRole(PageData pd);
	/**
	 * 查询代理商导医退回的记录
	 * @param map 数据集合
	 */
	PageData findReturnRecord(PageData pd);
	/**
	 * 查询运营人员区域
	 * @param map 数据集合
	 */
	PageData selectUserArea(PageData pd);
	
}
