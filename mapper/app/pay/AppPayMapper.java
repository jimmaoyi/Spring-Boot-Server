package com.maryun.mapper.app.pay;

import org.apache.ibatis.annotations.Mapper;

import com.maryun.model.PageData;
@Mapper
public interface AppPayMapper {

	public void insertNode(PageData pd);//将付款状态插入到节点记录表中
	public void updateOrderState(PageData pd);//更新订单状态为已付款
}
