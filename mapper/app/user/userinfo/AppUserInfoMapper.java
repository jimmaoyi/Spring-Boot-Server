package com.maryun.mapper.app.user.userinfo;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.maryun.model.PageData;

@Mapper
public interface AppUserInfoMapper {
	PageData getUserInfo(PageData pd); // 查询用户个人信息

	void addUserInfo(PageData pd); // 添加用户个人信息

	void updateUserInfo(PageData pd); // 添加用户个人信息

	void addSysUserInfo(PageData pd); // 添加系统用户信息

	void updateUserPushCode(PageData pd); // 更新用户设备号
	
	void updateDoctorPushCode(PageData pd); // 更新医生设备号
	
	void updateSPPushCode(PageData pd); // 更新机构设备号
	
	void updateAccompanyPushCode(PageData pd); // 更新陪诊设备号
	
	void updatePushCode(PageData pd);//更新推送码
	
	void insertPushCode(PageData pd);//插入推送码

	void addUserLoc(PageData pd); // 添加用户位置

	void updateUserLoc(PageData pd); // 更新用户位置

	List<PageData> getUserLoc(PageData pd); // 查询用户位置信息是否存在

	PageData getUserByPhone(PageData pd);// 根据电话号码来查询用户
}
