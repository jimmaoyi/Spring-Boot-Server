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
public interface AppVersionMapper {
	/**
	 * 获取最新版本号
	 */
	PageData getVersion(PageData pd);

}