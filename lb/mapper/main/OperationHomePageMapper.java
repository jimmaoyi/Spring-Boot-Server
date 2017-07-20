package com.maryun.lb.mapper.main;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.maryun.model.PageData;

/**
 * 类名称：OperationHomePageMapper 创建人：ChuMingZe 创建时间：2017年3月1日
 * 
 * @version
 */
@Mapper
public interface OperationHomePageMapper {
	/**
	 * 获取总收入
	 * @param pd 参数
	 * @return
	 */
	PageData getTotalIncome(PageData pd);
	/**
	 * 获取本月收入
	 * @param pd 参数
	 * @return
	 */
	PageData getTotalMonthlyIncome(PageData pd);
	/**
	 * 获取本日收入
	 * @param pd 参数
	 * @return
	 */
	PageData getTotalDailyIncome(PageData pd);
	/**
	 * 获取本日订单
	 * @param pd 参数
	 * @return
	 */
	PageData getTotalDailyOrder(PageData pd);
	/**
	 * 获取最近一个月订单
	 * @param pd 参数
	 * @return
	 */
	PageData getOneMonthOrder(PageData pd);
	/**
	 * 获取最近一个月收入
	 * @param pd 参数
	 * @return
	 */
	PageData getOneMonthMoney(PageData pd);
	/**
	 * 获取总订单
	 * @param pd 参数
	 * @return
	 */
	PageData getTotalOrder(PageData pd);
	/**
	 * 获取所有用户
	 * @param pd 参数
	 * @return
	 */
	PageData getTotalUser(PageData pd);
	/**
	 * 获取近6个月注册新用户
	 * @param pd 参数
	 * @return
	 */
	List<PageData> getSixMonthNewRegUser(PageData pd);
	/**
	 * 获取近6个月运动用户
	 * @param pd 参数
	 * @return
	 */
	List<PageData> getSixMonthSportUser(PageData pd);
	/**
	 * 获取近6个月就医宝用户
	 * @param pd 参数
	 * @return
	 */
	List<PageData> getSixMonthJiuYiBaoUser(PageData pd);
	/**
	 * 获取最近1年每月订单数
	 * @param pd 参数
	 * @return
	 */
	List<PageData> getOneYearOrderForMonth(PageData pd);
	/**
	 * 获取最近1年每月付款数
	 * @param pd 参数
	 * @return
	 */
	List<PageData> getOneYearMoneyForMonth(PageData pd);
	/**
	 * 获取最近1年每月发贴数
	 * @param pd 参数
	 * @return
	 */
	List<PageData> getOneYearPostsForMonth(PageData pd);
	/**
	 * 获取最近1年每月转发数
	 * @param pd 参数
	 * @return
	 */
	List<PageData> getOneYearForwardForMonth(PageData pd);
	/**
	 * 获取最近1年转发平台数
	 * @param pd 参数
	 * @return
	 */
	List<PageData> getOneYearForwardSys(PageData pd);
	/**
	 * 获取最近1年每月团体活动数量
	 * @param pd 参数
	 * @return
	 */
	List<PageData> getOneYearGroupActiveForMonth(PageData pd);
	/**
	 * 获取最近1年每月个人活动数量
	 * @param pd 参数
	 * @return
	 */
	List<PageData> getOneYearPersonActiveForMonth(PageData pd);
	/**
	 * 获取最近1年每月团体活动参与人数
	 * @param pd 参数
	 * @return
	 */
	List<PageData> getOneYearGroupActiveUsersForMonth(PageData pd);
	/**
	 * 获取最近1年每月个人活动参与人数
	 * @param pd 参数
	 * @return
	 */
	List<PageData> getOneYearPersonActiveUsersForMonth(PageData pd);
	/**
	 * 获取所有就医宝用户年龄分布
	 * @param pd 参数
	 * @return
	 */
	List<PageData> getJiuYiBaoUserAges(PageData pd);
	/**
	 * 查询当前用户是否是二级代理
	 * @param pd 参数
	 * @return
	 */
	PageData selectUserArea(PageData pd);
	
	
	
	
	/**
	 * 二级运营人员区域总收入
	 * @param pd 参数
	 * @return
	 */
	PageData getTotalIncomeByArea(PageData pd);
	/**
	 * 获取二级代理本月收入
	 * @param pd 参数
	 * @return
	 */
	PageData getTotalMonthlyIncomeByArea(PageData pd);
	/**
	 * 查询二级代理商的日收入
	 * @param pd 参数
	 * @return
	 */
	PageData getTotalDailyIncomeByArea(PageData pd);
	/**
	 * 获取二级代理本日订单
	 * @param pd 参数
	 * @return
	 */
	PageData getTotalDailyOrderByArea(PageData pd);
	/**
	 * 获取二级运营人员最近一月订单
	 * @param pd 参数
	 * @return
	 */
	PageData getOneMonthOrderByArea(PageData pd);
	/**
	 * 获取二级运营人员最近一个月收入
	 * @param pd 参数
	 * @return
	 */
	PageData getOneMonthMoneyByArea(PageData pd);
	/**
	 * 获取二级元婴人员总订单
	 * @param pd 参数
	 * @return
	 */
	PageData getTotalOrderByArea(PageData pd);
	/**
	 * 获取二级元婴人员总订单
	 * @param pd 参数
	 * @return
	 */
	List<PageData> getOneYearOrderForMonthByArea(PageData pd);
	/**
	 * 获取二级元婴人员总订单
	 * @param pd 参数
	 * @return
	 */
	List<PageData> getOneYearMoneyForMonthByArea(PageData pd);
	
	
}
