package com.maryun.mapper.app.accompany;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.maryun.model.PageData;

@Mapper
public interface AppAccompanyMapper {
	List<PageData> messagePageSearch(PageData pd); // dd 所有消息

	PageData getMessageById(PageData pd); // 查看某条消息详情

	List<PageData> getEvaluateOfAccompanyList(PageData pd); // 查看陪诊评价

	List<PageData> getNewOrderInfoList(PageData pd); // 新单提醒

	PageData getNewOrderInfoDetail(PageData pd); // 新单详情

	void setOrderIfAccept(PageData pd); // 设置接诊/拒绝接诊

	List<PageData> getTodayAccompanyList(PageData pd); // 今日陪诊提醒

	PageData getTodayAccompanyDetail(PageData pd); // 今日陪诊详情

	List<PageData> getTodayAccompanyKeyNodeList(PageData pd); // 订单里程碑

	PageData getRankLast(PageData pd);// 接单排名和累计接单数量

	PageData getRankMonth(PageData pd);// 每月接单数量

	List<PageData> getAccompanyListByName(PageData pd);// 根据陪诊人员名字来获取陪诊人员(模糊查询)

}
