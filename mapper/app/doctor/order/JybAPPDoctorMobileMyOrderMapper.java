package com.maryun.mapper.app.doctor.order;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.maryun.model.PageData;

@Mapper
public interface JybAPPDoctorMobileMyOrderMapper {
	/**
	 * 根据医生的hel_id和接单状态osha_status查询对应的所有订单
	 * @param pd
	 * @return
	 */
	List<PageData> orderList(PageData pd);

	/**
	 * 订单中心数量显示查询专家的已接单的订单为正在服务中状态的数量
	 * @param pd
	 * @return
	 */
	PageData checkAIOrderCenterNum(PageData pd);

	/**
	 * 医生接受用户订单
	 * @param pd
	 */
	void receiveOrder(PageData pd);

	/**
	 * 医生拒绝用户订单
	 * @param pd
	 */
	void refuseOrder(PageData pd);

	/**
	 * 获取订单分配状态
	 * @param pd
	 * @return
	 */
	List<PageData> getGuideSpAssign(PageData pd);

	/**
	 * 更新订单分配状态
	 * @param pd
	 * @return
	 */
	void updateGuideSpAssign(PageData pd);
}
