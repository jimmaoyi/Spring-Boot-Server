package com.maryun.mapper.app.pay;

import org.apache.ibatis.annotations.Mapper;

import com.maryun.model.PageData;
@Mapper
public interface AppPayAlipayMapper {

	public void saveNotify(PageData pd);//保存通知信息
}
