/**
 * 
 */
package com.maryun.mapper.app.sp;

import org.apache.ibatis.annotations.Mapper;

import com.maryun.model.PageData;

/**
 * @author Administrator
 *
 */
@Mapper
public interface AppSPInfoMapper {
	PageData getSpInfo(PageData pd); // 查询机构信息
}
