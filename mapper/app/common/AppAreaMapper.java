/**
 * 
 */
package com.maryun.mapper.app.common;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.maryun.model.PageData;

/**
 * @author Administrator
 *
 */
@Mapper
public interface AppAreaMapper {
	/**
	 * 获取省一级的数据
	 */
	List<PageData> getProvinceArea(PageData pd);
	/**
	 * 获取市级的数据
	 */
	List<PageData> getCityArea(PageData pd);
	/**
	 * 获取存在专家的省一级的数据
	 */
	List<PageData> getProvinceAreaExist(PageData pd);
	/**
	 * 更新拼音字段
	 * @param pd
	 */
	void updateChinese(PageData pd);

	/**
	 * 获取城市列表
	 */
	List<PageData> getCityList();

}