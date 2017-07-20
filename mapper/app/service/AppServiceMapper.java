package com.maryun.mapper.app.service;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.maryun.model.PageData;

@Mapper
public interface AppServiceMapper {
	PageData getServiceCenterTel();//获取客服电话
	PageData getEarestMoney();//获取定金金额
	List<PageData> getOrderCancelReason();//获取取消原因
}
