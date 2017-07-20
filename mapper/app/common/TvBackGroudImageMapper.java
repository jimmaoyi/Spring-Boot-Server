/**
 * 
 */
package com.maryun.mapper.app.common;

import org.apache.ibatis.annotations.Mapper;

import com.maryun.model.PageData;

/**
 * @author Administrator
 *
 */
@Mapper
public interface TvBackGroudImageMapper {
    PageData getTvBackGroudImage(PageData pd);//获取电视背景图
}
