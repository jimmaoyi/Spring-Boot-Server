package com.maryun.lb.restful.main;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maryun.lb.mapper.main.OperationHomePageMapper;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.DateUtils;
import com.maryun.utils.WebResult;

/**
 * 类名称：OperationHomePageRestful 创建人：ChuMingZe 创建时间：2017年3月1日
 * 
 * @version
 */
@RestController
@RequestMapping(value = "/operation/main")
public class OperationHomePageRestful extends BaseRestful {
	@Autowired
	private OperationHomePageMapper operationHomePageMapper;

	/**
	 * 获取统计基础数据（收入、本月收入、本日收入、本日订单、总用户）
	 * 
	 * @param pd
	 *            查询条件
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getStatisticalBasisData")
	public PageData getStatisticalBasisData(@RequestBody PageData pd) throws Exception {
		//查询代育婴人员的区域
		PageData pds = operationHomePageMapper.selectUserArea(pd);
		if(pds != null){
			pd.put("UA_AREA", pds.getString("UA_AREA"));
			// 总收入
			PageData pd_total_income = operationHomePageMapper.getTotalIncomeByArea(pd);
			pd.put("TotalIncome", pd_total_income.getString("totalIncome"));
			// 本月收入
			PageData pd_total_monthly_income = operationHomePageMapper.getTotalMonthlyIncomeByArea(pd);
			pd.put("TotalMonthlyIncome", pd_total_monthly_income.getString("totalMonthlyIncome"));
			// 本日收入
			PageData pd_total_daily_income = operationHomePageMapper.getTotalDailyIncome(pd);
			pd.put("TotalDailyIncome", pd_total_daily_income.getString("totalDailyIncome"));
			// 本日订单
			PageData pd_total_daily_order = operationHomePageMapper.getTotalDailyOrderByArea(pd);
			pd.put("TotalDailyOrder", pd_total_daily_order.getString("totalDailyOrder"));
			// 最近一个月订单
			PageData pd_one_Month_order = operationHomePageMapper.getOneMonthOrderByArea(pd);
			pd.put("OneMonthOrder", pd_one_Month_order.getString("oneMonthOrder"));
			// 最近一个月收入
			PageData pd_one_Month_money = operationHomePageMapper.getOneMonthMoneyByArea(pd);
			pd.put("OneMonthMoney", pd_one_Month_money.getString("oneMonthMoney"));
			// 总订单
			PageData pd_total_order = operationHomePageMapper.getTotalOrderByArea(pd);
			pd.put("TotalOrder", pd_total_order.getString("totalOrder"));
		}else{
			// 总收入
			PageData pd_total_income = operationHomePageMapper.getTotalIncome(pd);
			pd.put("TotalIncome", pd_total_income.getString("totalIncome"));
			// 本月收入
			PageData pd_total_monthly_income = operationHomePageMapper.getTotalMonthlyIncome(pd);
			pd.put("TotalMonthlyIncome", pd_total_monthly_income.getString("totalMonthlyIncome"));
			// 本日收入
			PageData pd_total_daily_income = operationHomePageMapper.getTotalDailyIncome(pd);
			pd.put("TotalDailyIncome", pd_total_daily_income.getString("totalDailyIncome"));
			// 本日订单
			PageData pd_total_daily_order = operationHomePageMapper.getTotalDailyOrder(pd);
			pd.put("TotalDailyOrder", pd_total_daily_order.getString("totalDailyOrder"));
			// 最近一个月订单
			PageData pd_one_Month_order = operationHomePageMapper.getOneMonthOrder(pd);
			pd.put("OneMonthOrder", pd_one_Month_order.getString("oneMonthOrder"));
			// 最近一个月收入
			PageData pd_one_Month_money = operationHomePageMapper.getOneMonthMoney(pd);
			pd.put("OneMonthMoney", pd_one_Month_money.getString("oneMonthMoney"));
			// 总订单
			PageData pd_total_order = operationHomePageMapper.getTotalOrder(pd);
			pd.put("TotalOrder", pd_total_order.getString("totalOrder"));
			// 总用户
			PageData pd_total_user = operationHomePageMapper.getTotalUser(pd);
			pd.put("TotalUser", pd_total_user.getString("totalUser"));
		}
		return WebResult.requestSuccess(pd);
	}

	/**
	 * 获取近6个月注册新用户
	 * 
	 * @param pd
	 *            查询条件
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getSixMonthNewRegUser")
	public PageData getSixMonthNewRegUser(@RequestBody PageData pd) throws Exception {
		// 近6个月注册新用户
		List<PageData> lists_sixMonthNewRegUser = operationHomePageMapper.getSixMonthNewRegUser(pd);
		return WebResult.requestSuccess(lists_sixMonthNewRegUser);
	}

	/**
	 * 获取近6个月运动用户
	 * 
	 * @param pd
	 *            查询条件
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getSixMonthSportUser")
	public PageData getSixMonthSportUser(@RequestBody PageData pd) throws Exception {
		List<PageData> lists_sixMonthSportUser = null;
		// 近6个月运动用户
		List<PageData> lists_sixMonthSportUser_tmp = operationHomePageMapper.getSixMonthSportUser(pd);
		if (null != lists_sixMonthSportUser_tmp && lists_sixMonthSportUser_tmp.size() > 0) {
			lists_sixMonthSportUser = new ArrayList<PageData>();
			int i_tmp = 0;
			int i_users = 0;
			for (PageData pd_tmp : lists_sixMonthSportUser_tmp) {
				PageData pd_sixMonthNewRegUser_tmp = new PageData();
				pd_sixMonthNewRegUser_tmp.put("months", pd_tmp.getString("months"));
				if (i_tmp == 0) {
					i_users = Integer.parseInt(pd_tmp.getString("monthSportUser"));
				} else {
					i_users = Integer.parseInt(lists_sixMonthSportUser_tmp.get(i_tmp - 1).getString("monthSportUser"))
							+ Integer.parseInt(pd_tmp.getString("monthSportUser"));
				}
				pd_sixMonthNewRegUser_tmp.put("monthSportUser", i_users);
				i_tmp++;
				lists_sixMonthSportUser.add(pd_sixMonthNewRegUser_tmp);
			}
		}
		return WebResult.requestSuccess(lists_sixMonthSportUser);
	}

	/**
	 * 获取近6个月就医宝用户
	 * 
	 * @param pd
	 *            查询条件
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getSixMonthJiuYiBaoUser")
	public PageData getSixMonthJiuYiBaoUser(@RequestBody PageData pd) throws Exception {
		List<PageData> lists_sixMonthNewRegUser = null;
		// 近6个月就医宝用户
		List<PageData> lists_sixMonthNewRegUser_tmp = operationHomePageMapper.getSixMonthJiuYiBaoUser(pd);
		if (null != lists_sixMonthNewRegUser_tmp && lists_sixMonthNewRegUser_tmp.size() > 0) {
			lists_sixMonthNewRegUser = new ArrayList<PageData>();
			int i_tmp = 0;
			int i_users = 0;
			for (PageData pd_tmp : lists_sixMonthNewRegUser_tmp) {
				PageData pd_sixMonthNewRegUser_tmp = new PageData();
				pd_sixMonthNewRegUser_tmp.put("months", pd_tmp.getString("months"));
				if (i_tmp == 0) {
					i_users = Integer.parseInt(pd_tmp.getString("monthJiuYiBaoUser"));
				} else {
					i_users = Integer
							.parseInt(lists_sixMonthNewRegUser_tmp.get(i_tmp - 1).getString("monthJiuYiBaoUser"))
							+ Integer.parseInt(pd_tmp.getString("monthJiuYiBaoUser"));
				}
				pd_sixMonthNewRegUser_tmp.put("monthJiuYiBaoUser", i_users);
				i_tmp++;
				lists_sixMonthNewRegUser.add(pd_sixMonthNewRegUser_tmp);
			}
		}
		return WebResult.requestSuccess(lists_sixMonthNewRegUser);
	}

	/**
	 * 获取最近1年每月订单数
	 * 
	 * @param pd
	 *            查询条件
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getOneYearOrderForMonth")
	public PageData getOneYearOrderForMonth(@RequestBody PageData pd) throws Exception {
		// 近1年每月订单数
		pd.put("YEARMONTHS", getLastYearMonths());
		List<PageData> lists_oneYearOrderForMonth = null;
		PageData pds = operationHomePageMapper.selectUserArea(pd);
		if(pds != null){
			pd.put("UA_AREA", pds.getString("UA_AREA"));
			lists_oneYearOrderForMonth = operationHomePageMapper.getOneYearOrderForMonthByArea(pd);
		}else{
			lists_oneYearOrderForMonth = operationHomePageMapper.getOneYearOrderForMonth(pd);
		}
		return WebResult.requestSuccess(lists_oneYearOrderForMonth);
	}

	/**
	 * 获取最近1年每月付款数
	 * 
	 * @param pd
	 *            查询条件
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getOneYearMoneyForMonth")
	public PageData getOneYearMoneyForMonth(@RequestBody PageData pd) throws Exception {
		// 近1年每月付款数
		pd.put("YEARMONTHS", getLastYearMonths());
		List<PageData> lists_oneYearMoneyForMonth = null;
		PageData pds = operationHomePageMapper.selectUserArea(pd);
		if(pds != null){
			pd.put("UA_AREA", pds.getString("UA_AREA"));
			lists_oneYearMoneyForMonth = operationHomePageMapper.getOneYearMoneyForMonthByArea(pd);
		}else{
			lists_oneYearMoneyForMonth = operationHomePageMapper.getOneYearMoneyForMonth(pd);
		}
		
		return WebResult.requestSuccess(lists_oneYearMoneyForMonth);
	}

	/**
	 * 获取最近1年每月发贴数
	 * 
	 * @param pd
	 *            查询条件
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getOneYearPostsForMonth")
	public PageData getOneYearPostsForMonth(@RequestBody PageData pd) throws Exception {
		// 近1年每月发贴数
		pd.put("YEARMONTHS", getLastYearMonths());
		List<PageData> lists_oneYearPostsForMonth = operationHomePageMapper.getOneYearPostsForMonth(pd);
		return WebResult.requestSuccess(lists_oneYearPostsForMonth);
	}

	/**
	 * 获取最近1年每月转发数
	 * 
	 * @param pd
	 *            查询条件
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getOneYearForwardForMonth")
	public PageData getOneYearForwardForMonth(@RequestBody PageData pd) throws Exception {
		// 近1年每月转发数
		pd.put("YEARMONTHS", getLastYearMonths());
		List<PageData> lists_oneYearForwardForMonth = operationHomePageMapper.getOneYearForwardForMonth(pd);
		return WebResult.requestSuccess(lists_oneYearForwardForMonth);
	}

	/**
	 * 获取最近1年转发平台数
	 * 
	 * @param pd
	 *            查询条件
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getOneYearForwardSys")
	public PageData getOneYearForwardSys(@RequestBody PageData pd) throws Exception {
		// 近1年转发平台数
		List<PageData> lists_oneYearForwardSys = operationHomePageMapper.getOneYearForwardSys(pd);
		return WebResult.requestSuccess(lists_oneYearForwardSys);
	}

	/**
	 * 获取最近1年每月团体活动数量
	 * 
	 * @param pd
	 *            查询条件
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getOneYearGroupActiveForMonth")
	public PageData getOneYearGroupActiveForMonth(@RequestBody PageData pd) throws Exception {
		// 近1年每月团体活动数量
		pd.put("YEARMONTHS", getLastYearMonths());
		List<PageData> lists_oneYearGroupActiveForMonth = operationHomePageMapper.getOneYearGroupActiveForMonth(pd);
		return WebResult.requestSuccess(lists_oneYearGroupActiveForMonth);
	}

	/**
	 * 获取最近1年每月个人活动数量
	 * 
	 * @param pd
	 *            查询条件
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getOneYearPersonActiveForMonth")
	public PageData getOneYearPersonActiveForMonth(@RequestBody PageData pd) throws Exception {
		// 近1年每月个人活动数量
		pd.put("YEARMONTHS", getLastYearMonths());
		List<PageData> lists_oneYearPersonActiveForMonth = operationHomePageMapper.getOneYearPersonActiveForMonth(pd);
		return WebResult.requestSuccess(lists_oneYearPersonActiveForMonth);
	}

	/**
	 * 获取最近1年每月团体活动参与人数
	 * 
	 * @param pd
	 *            查询条件
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getOneYearGroupActiveUsersForMonth")
	public PageData getOneYearGroupActiveUsersForMonth(@RequestBody PageData pd) throws Exception {
		// 近1年每月团体活动参与人数
		pd.put("YEARMONTHS", getLastYearMonths());
		List<PageData> lists_oneYearGroupActiveUsersForMonth = operationHomePageMapper.getOneYearGroupActiveUsersForMonth(pd);
		return WebResult.requestSuccess(lists_oneYearGroupActiveUsersForMonth);
	}

	/**
	 * 获取最近1年每月个人活动参与人数
	 * 
	 * @param pd
	 *            查询条件
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getOneYearPersonActiveUsersForMonth")
	public PageData getOneYearPersonActiveUsersForMonth(@RequestBody PageData pd) throws Exception {
		// 近1年每月个人活动参与人数
		pd.put("YEARMONTHS", getLastYearMonths());
		List<PageData> lists_oneYearPersonActiveUsersForMonth = operationHomePageMapper.getOneYearPersonActiveUsersForMonth(pd);
		return WebResult.requestSuccess(lists_oneYearPersonActiveUsersForMonth);
	}

	/**
	 * 获取所有就医宝用户年龄分布
	 * 
	 * @param pd
	 *            查询条件
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getJiuYiBaoUserAges")
	public PageData getJiuYiBaoUserAges(@RequestBody PageData pd) throws Exception {
		// 所有就医宝用户年龄段分布
		List<PageData> lists_jiuYiBaoUserAges = operationHomePageMapper.getJiuYiBaoUserAges(pd);
		return WebResult.requestSuccess(lists_jiuYiBaoUserAges);
	}
	
	/**
	 * 获取最近1年所有月份的SQL数据
	 * @return
	 */
	public List<PageData> getLastYearMonths() {
		List<String> lists_yearMonths = DateUtils.getLastYearMonths();
		List<PageData> list_res = null;
		if(null != lists_yearMonths && lists_yearMonths.size() > 0){
			list_res = new ArrayList<PageData>();
			for (String s_tmp : lists_yearMonths) {
				String[] sa_tmp = s_tmp.split("-");
				PageData pd_tmp = new PageData();
				pd_tmp.put("months", sa_tmp[1]);
				pd_tmp.put("years", sa_tmp[0]);
				list_res.add(pd_tmp);
			}
		}
		return list_res;
	}
}
