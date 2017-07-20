/**
 * 
 */
package com.maryun.mapper.app.accompany;

import org.apache.ibatis.annotations.Mapper;

import com.maryun.model.PageData;

/**
 * @author Administrator
 *
 */
@Mapper
public interface AppAccompanyInfoMapper {
	PageData getAccompanyInfo(PageData pd); // 查询陪审人员个人信息
}
