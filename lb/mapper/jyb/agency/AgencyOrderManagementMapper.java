package com.maryun.lb.mapper.jyb.agency;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.maryun.model.PageData;

@Mapper
public interface AgencyOrderManagementMapper {
	List<PageData> listPage(PageData pd); // 未处理订单查询

	List<PageData> listPageMEMO(PageData pd); // 用户信息沟通备忘录

	List<PageData> listPageAll(PageData pd); // 全部订单查询

	List<PageData> listPageAll_expert(PageData pd); // 全部专家(代理商分派)

	List<PageData> listPageAll_accompany(PageData pd); // 全部陪诊人员(代理商分派)

	List<PageData> listPagehistoryorder(PageData pd); // 订单查询中的历史订单表格

	List<PageData> uinameDesc(PageData pd); // 订单查询中的用户历史订单信息

	List<PageData> onumDesc(PageData pd); // 订单查询中的订单详细信息

	List<PageData> expertLibsDesc(PageData pd); // 订单查询中的专家库信息

	List<PageData> spDesc(PageData pd); // 订单查询中的代理商信息

	List<PageData> accompanyDesc(PageData pd); // 订单查询中的陪诊人员信息

	List<PageData> keyNodeDesc(PageData pd); // 订单查询中的就诊过程记录信息

	List<PageData> evalDesc(PageData pd); // 订单查询中的评价内容信息

	List<PageData> evallevelDesc(PageData pd); // 订单查询中的评价星级信息

	void changeSpStatus(PageData pd); // 修改接单状态

	void changerefund(PageData pd); // 退款

	PageData findOrderStatusById(PageData pd);// 根据id去查询订单的状态

	List<PageData> assignToExpert(PageData pd);// 代理商未分派给专家

	List<PageData> assignToAccompany(PageData pd);// 代理商未分派给陪诊人员

	List<PageData> assignToExpertAndAccompany(PageData pd);// 代理商既没有分派给专家也没有分派给陪诊人员

	void addAssignExpert(PageData pd);// 向专家、代理商、订单表中添加一条数据

	void addAssignAccompany(PageData pd);// 向陪诊人员、代理商、订单表中添加一条数据

	void changeAssignExpertDelStatus(PageData pd);// 专家、代理商、订单表中修改删除状态未0

	void changeAssignAccompanyDelStatus(PageData pd);// 陪诊人员、代理商、订单表中修改删除状态为0

	PageData findAssignExpertById(PageData pd);// 根据O_ID向专家、代理商、订单表中查找

	PageData findAssignAccompanyById(PageData pd);// 根据O_ID向陪诊人员、代理商、订单表中查找
	
	void updateoshaid(PageData pd);//代理商接单更改专家接单状态
	
	void deleteOrderRecoding(PageData pd);//代理商退单删除代理商专家订单表激励
	
	void updateOrderRecoding(PageData pd);//代理商退单更改导医代理商订单表记录
	
	void updateOrderAssign(PageData pd);
	
	PageData findID (PageData pd);
	
	void updateOrderHel(PageData pd);//修改代理商专家订单表
	
	PageData selectExpertOrders(PageData pd);//查询专家是否已接单
	
	PageData selectAccompantOrders(PageData pd);//查询专家是否
	
	List<PageData> selectUserId(PageData pd);//查询订单和退单所推用户id
	
	PageData selectSPID(PageData pd);//查询代理商ID
	
	void updateExpert(PageData pd);//修改order中的专家id

}
