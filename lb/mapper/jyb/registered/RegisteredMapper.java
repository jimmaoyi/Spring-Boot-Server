package com.maryun.lb.mapper.jyb.registered;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.maryun.model.PageData;

/**
 * 类名称：HospitalExpertLibsMapper 创建人：ChuMingZe 创建时间：2017年2月15日
 * 
 * @version
 */
@Mapper
public interface RegisteredMapper {
	
	/**
	 * 设置专家模板
	 * @param map 数据集合
	 */
	void saveAdd(PageData map);
	/**
	 * 修改专家为模板状态
	 * @param map
	 */
	void saveUser(PageData map);
	/**
	 * 查询手机号是否重复
	 * @param map
	 */
	PageData findByUId(PageData pd);
	/**
	 *  查询手机号是否在sys_user表中有数据 
	 * @param map
	 */
	PageData findBySysUser (PageData pd);
}
