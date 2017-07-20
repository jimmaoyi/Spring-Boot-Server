package com.maryun.mapper.app.sp;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.maryun.model.PageData;

@Mapper
public interface AppSpMapper {

	List<PageData> getNewOrderInfoList(PageData pd); // 新单列表

	PageData checkOrderCenterNum(PageData pd);// 订单中心数量显示

	List<PageData> getAllOrderInfoList(PageData pd); // 全部订单列表

	PageData getOrderInfoDetail(PageData pd); // 订单详细信息

	void setOrderIfAccept(PageData pd); // 设置接诊/拒绝接诊

	List<PageData> getUnAssignOrderInfoList(PageData pd); // 待分配订单列表

	void assignExpert(PageData map); // 分配专家

	void delAssignedExpert(PageData map); // 删除分配专家

	void assignAccompany(PageData map); // 分配陪诊

	void delAssignedAccompany(PageData map); // 删除分配陪诊

	List<PageData> getAccompanyCalendarPlan(PageData pd); // 获取陪诊日程安排

	void setAssignStatus(PageData pd); // 设置分配状态 1专家已分配 2陪诊已分配 3专家陪诊已分配

	List<PageData> getAssignedOrderInfoList(PageData pd); // 已分配订单列表

	List<PageData> getOrderEvaluatedInfoList(PageData pd); // 已评价订单列表

	List<PageData> getDoctorList(PageData pd); // 专家列表

	List<PageData> getAccompanyList(PageData pd); // 陪诊列表

	PageData getDoctorDetail(PageData pd); // 专家详情

	PageData getAccompanyDetail(PageData pd); // 陪诊详情

	List<PageData> getEvaluateOfSpList(PageData pd); // sp评价列表

	PageData getEvaluateOfSpOrder(PageData pd); // 订单评价

	PageData getRankLast(PageData pd);// 代理商接单排名和累计接单数量

	PageData getRankMonth(PageData pd);// 代理商每日接单数量

	PageData getRankLast_expert(PageData pd);// 专家接单排名和累计接单数量

	PageData getRankMonth_expert(PageData pd);// 专家每日接单数量

	void receiveOrder(PageData map); // 确认接单

	void updateOrderHelStatus(PageData map); // 更改专家订单管联表状态由-1 更新为0
												// 表明sp已接单，专家才可接单

	void refuseOrder(PageData map); // 拒绝接单

	void delOrderHel(PageData map); // 删除专家订单关联表该记录

	void setRefundStatus(PageData map); // 设置退款

}
