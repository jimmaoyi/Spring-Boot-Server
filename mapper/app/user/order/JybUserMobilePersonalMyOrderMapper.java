package com.maryun.mapper.app.user.order;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.maryun.model.PageData;

@Mapper
public interface JybUserMobilePersonalMyOrderMapper {

	/**
	 * 用户下单创建新订单
	 * @param pd
	 */
	void createNewOrder(PageData pd);
	// 下单后在订单支付类型表插入该订单支付类型

	// 用户下单产生新的订单前，需要判断用户下单的该医生的订单表里有没有未完结的订单，如果有问他是否需要继续下单
	List<PageData> checkThisDorcorIsNotFinishOrders(PageData pd);

	void insertOderPayWay(PageData pd);

	/**
	 * 生成订单马上去lb_jyb_node_mode表查出所有的模板并插入陪诊的模板节点表lb_jyb_key_node
	 * @return
	 */
	List<PageData> checkModelInforByLB_JYB_NODE_MODE();

	/**
	 * 查出所有的模板和订单id一起插入陪诊的模板节点表lb_jyb_key_node
	 * @param pd
	 */
	void insertModelInforIntoLb_JYB_KEY_NODE(PageData pd);

	/**
	 * 修改陪诊节点中的导医接单
	 * @param pd
	 */
	void customerServiceReceiverOrder(PageData pd);

	PageData checkMyOrderStateNum(PageData pd);// 根据用户的id和订单状态查询订单数量

	// 查询订单的状态
	PageData checkThisOrderStatus(PageData pd);

	/**
	 * 根据用户的Id查询该用户的所有订单信息
	 * @param pd
	 * @return
	 */
	List<PageData> jybUserMobilePersonalMyOrderListByUserId(PageData pd);

	/**
	 * 根据订单Id去取消订单
	 * @param pd
	 */
	void cancelOrder(PageData pd);

	/**
	 * 根据订单Id去评价该订单
	 * @param pd
	 */
	void insertOrderEvalContent(PageData pd);

	/**
	 * 根据订单Id去修改评价的订单状态为已评价
	 * @param pd
	 */
	void updateOrderStatusIsEval(PageData pd);

	/**
	 * 根据订单Id去查询该订单的详细信息
	 * @param pd
	 * @return
	 */
	PageData orderInfoByOrderId(PageData pd);

	/**
	 * 根据订单id和订单状态值o_statu去修改订单的状态
	 * @param pd
	 */
	void updateOrderStatus(PageData pd);

	/**
	 * 根据订单id去申请退单
	 * @param pd
	 */
	void chargebackOrder(PageData pd);

	//
	void updatePayStatus(PageData pd);

	//
	void updatePayWay(PageData pd);

	/**
	 * 根据订单Id去查询该订单支付表中数据
	 * @param pd
	 * @return
	 */
	PageData getOrderPayINfoByOID(PageData pd);

	/**
	 * 根据订单Id去查询该订单表中数据
	 * @param pd
	 * @return
	 */
	PageData getOrderInfoByOID(PageData pd);

	void updateREMIT_ID(PageData pd);

	void updateMasterId(PageData pd); // 更新fileupload MasterID
}
