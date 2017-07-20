package com.maryun.mapper.app.accompany.order;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.maryun.model.PageData;

@Mapper
public interface JybAPPAccompanyMobileMyOrderMapper {

	/**
	 * 根据陪诊人员的ai_id和osha_status接单状态查询订单信息
	 * @param pd
	 * @return
	 */
	List<PageData> orderList(PageData pd);

	/**
	 * 根据订单的order_id查询该订单的详细信息
	 * @param pd
	 * @return
	 */
	/* PageData orderInfoByOrderId(PageData pd); */
	/**
	 * 陪诊人员接受用户订单
	 * @param pd
	 */
	void receiveOrder(PageData pd);

	/**
	 * 陪诊人员接受用户订单
	 * @param pd
	 */
	void updateOrderStatus(PageData pd);

	/**
	 * 陪诊人员拒绝用户订单
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

	/**
	 * 得到订单里程碑节点
	 * @param pd
	 * @return
	 */
	List<PageData> getOrderNodeList(PageData pd);

	/**
	 * 订单申请完结
	 * @param pd
	 */
	void applyOrderEnd(PageData pd);

	/**
	 * 订单中心数量显示查询陪诊的已接单的订单为正在服务中状态的数量
	 * @param pd
	 * @return
	 */
	PageData checkAIOrderCenterNum(PageData pd);

	/**
	 * 获得关键节点详情
	 * @param pd
	 * @return
	 */
	PageData getNodeDetail(PageData pd);

	/**
	 * 插入节点
	 * @param pd
	 */
	void insertNode(PageData pd);

	/**
	 * 更新节点
	 * @param pd
	 */
	void updateNode(PageData pd);

	/**
	 * 删除节点
	 * @param pd
	 */
	void deleteNodeById(PageData pd);

	/**
	 * 得到节点通用模板集合
	 * @param pd
	 * @return
	 */
	List<PageData> getNodeModel(PageData pd);

}
