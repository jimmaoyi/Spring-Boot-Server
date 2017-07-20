package com.maryun.mapper.app.user.userinfo;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.maryun.model.PageData;

@Mapper
public interface AppUserEvaluateMapper {
	List<PageData> getOrderEvaluateList(PageData pd);// 用户评价列表
	void addEvaluateLevel(PageData pd);// 添加评价星级
	void addEvaluate(PageData pd);// 添加评价内容
	PageData checkOE_HEL_SP_AI_ID_AvgStar(PageData pd);//查询已经评价的订单的不同角色医生，陪诊，代理的星级平均数
	void updateAI_STARS(PageData pd);//
	void updateSP_STARS(PageData pd);
	void updateHEL_STARS(PageData pd);
}
