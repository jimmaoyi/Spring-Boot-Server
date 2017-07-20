package com.maryun.mapper.app.jyb.message;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.maryun.model.PageData;

/**
 * @author: Libra
 * Date: 2017年4月18日 上午9:16:31
 * Version: 1.0
 * @Description:  消息推送的mapper映射接口
 */
@Mapper
public interface JybAppMessageMapper {

	/**
	 * 获取消息推送消息的分页列表
	 * @param pd
	 * @return
	 */
	List<PageData> messageList();

	
	
	
}
