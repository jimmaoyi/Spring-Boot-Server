package com.maryun.mapper.app.doctor;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.maryun.model.PageData;

@Mapper
public interface AppDoctorMapper {
	
	List<PageData> getTheExpertCitys(PageData pd);//获得有专家的城市

	List<PageData> getNewOrderInfoList(PageData pd); // 新单提醒

	PageData getNewOrderInfoDetail(PageData pd); // 新单详情

	void setOrderIfAccept(PageData pd); // 设置接诊/拒绝接诊

	List<PageData> getTodayTreatList(PageData pd); // 今日陪诊提醒

	PageData getTodayTreatDetail(PageData pd); // 今日陪诊详情

	PageData getAllByOID(PageData pd); // 根据lb_jyb_order_hel来获取意向专家，进而获得科室和省份

}
