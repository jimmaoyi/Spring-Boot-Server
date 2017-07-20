package com.maryun.mapper.app.user.userinfo;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.maryun.model.PageData;

@Mapper
public interface AppUserFamilyMapper {
	List<PageData> getFamilyList(PageData pd);// 家庭列表接口

	List<PageData> getFamilyMemberList(PageData pd);// 家庭成员列表接口

	PageData getFamilyInfo(PageData pd);// 家庭信息

	PageData getFamilyMemberInfo(PageData pd);// 家庭成员信息

	void addFamily(PageData pd);// 添加家庭成

	void addFamilyMember(PageData pd);// 添加家庭成员

	void delFamilyMember(PageData pd);// 删除家庭成员

	void delFamilyMemberByFMID(PageData pd);// 删除家庭成员(根据家庭成员ID)

	void delFamily(PageData pd);// 删除家庭

	PageData getISMAIN(PageData pd);// 根据用户ID来判断此用户是否是家庭主账号

	/**
	 * 获取单个家庭用户信息
	 */
	PageData getFamilyUserInfo(PageData pd);

	/**
	 * 根据用户id从家庭成员表中获取家庭成员ID
	 * @param pd
	 * @return
	 */
	PageData getFMIDByUID(PageData pd);

	/**
	 * 如果不是主账号，想从家庭中离开，只能自己离开，无法控制别的
	 * @param pd
	 * @return
	 */
	PageData delSelfAndISMAIN(PageData pd);

	/**
	 * 根据家庭用户ID来查询家庭用户信息
	 * @param pd
	 * @return
	 */
	PageData getFamilyMemberByID(PageData pd);

	/**
	 * 根据家庭ID来查询家庭信息
	 * @param pd
	 * @return
	 */
	PageData getFamilyByID(PageData pd);

	/**
	 * 根据家庭成员ID来获取家庭成员和家庭信息
	 * @param pd
	 * @return
	 */
	PageData getFamilyAndFamilyMemberByFMID(PageData pd);

	/**
	 * 根据家庭成员ID来获取家庭成员和家庭信息,,家庭成员删除了也能查找这个成员原来所属的家庭
	 * @param pd
	 * @return
	 */
	PageData getFamilyAndFamilyMemberByFMIDAll(PageData pd);

	/**
	 * 根据页面邀请状态来更新数据
	 * @param pd
	 * @return
	 */
	void updateFamilyMemberFM_STATE(PageData pd);

	/**
	 * 根据家庭ID和用户ID来查询家庭成员表中是否有这条记录
	 * @param pd
	 * @return
	 */
	PageData getFamilyMemberByFIDAndUID(PageData pd);

}
