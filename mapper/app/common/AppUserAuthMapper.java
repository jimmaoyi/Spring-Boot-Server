/**
 * 
 */
package com.maryun.mapper.app.common;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.maryun.model.PageData;

/**
 * @author Administrator
 *
 */
@Mapper
public interface AppUserAuthMapper {
	/**
	 * 根据用户名验证用户是否存在
	 */
	PageData findUserByName(PageData map);
	/**
	 * 查询用户名是否被删除过 
	 */
	PageData findDelUserByName(PageData map);
	/**
	 * 根据用户名密码验证用户是否存在
	 */
	PageData findUserByNamePwd(PageData map);
	/**
     * 获取个人用户信息
     */
	PageData getUserInfo(PageData pd);
	/**
     * 获取个人用户信息
     */
	PageData getUserInfoByUIID(PageData pd);
	/**
     * 根据系统用户ID获取医生信息
     */
	PageData geDoctorInfo(PageData pd);
	/**
     * 获取系统用户ID机构信息
     */
	PageData getSPInfo(PageData pd);
	/**
     * 获取系统用户ID陪诊信息
     */
	PageData getAccompanyInfo(PageData pd);
	/**
     * 获取个人家庭列表
     */
	List<PageData> getFamilyList(PageData pd);
	/**
     * 获取个人家庭成员列表
     */
	List<PageData> getFamilyMemberList(PageData pd);
	/**
     * 获取个人家庭列表
     */
	PageData getFamilyByMAC(PageData pd);
	/**
     * 获取个人家庭成员列表
     */
	List<PageData> getFamilyMemberListByMAC(PageData pd);
	/**
	 * 添加用户至系统用户表
	 */
	void addSysUser(PageData pd);
	/**
	 * 添加用户至用户表
	 */
	void addUserInfo(PageData pd);
	void updatePushCode(PageData pd); // 更新用户设备号

}