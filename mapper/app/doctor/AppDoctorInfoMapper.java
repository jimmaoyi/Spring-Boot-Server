/**
 * 
 */
package com.maryun.mapper.app.doctor;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.maryun.model.PageData;

/**
 * @author Administrator
 *
 */
@Mapper
public interface AppDoctorInfoMapper {
	PageData getDoctorInfo(PageData pd); // 查询医生个人信息

	List<PageData> getDoctorList(PageData pd); // 查询医生列表信息

	List<PageData> getTVDoctorList(PageData pd); // 查询医生列表信息

	List<PageData> getDoctorSimpleList(); // 查询医生列表信息

	List<PageData> getDoctorsChurchList(PageData pd); // 查询名医堂列表信息

	List<PageData> getDoctorVideoList(PageData pd); // 查询医生视频列表信息

	List<PageData> getDeptVideoList(PageData pd); // 查询科室视频列表信息

	List<PageData> getDoctorListByName(PageData pd);// 根据医生名字来获取医生(模糊查询)
													// 具体代理商下面的专家

	List<PageData> getDoctorListByName_user(PageData pd);// 根据医生名字来获取医生(模糊查询)
															// 所有专家

	PageData getRankLast(PageData pd);// 接单排名和累计接单数量

	PageData getRankMonth(PageData pd);// 每月接单数量

	List<PageData> getEvaluateOfDoctorList(PageData pd); // 专家评价列表
}
