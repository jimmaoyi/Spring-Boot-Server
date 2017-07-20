package com.maryun.mapper.system.user;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import com.maryun.mapper.BaseMapper;
import com.maryun.model.PageData;

@Mapper
@CacheConfig(cacheNames = "USERS")
public interface UserMapper extends BaseMapper{

	//@Cacheable(key="#p0.getString('USERNAME')+#p0.getString('USER_ID')", cacheNames = "USERS")
	PageData findByUId(PageData pd);
	
	PageData findByUE(PageData pd);
	PageData findByUN(PageData pd);
	
	//@Cacheable(key="#p0.getString('USERNAME')", cacheNames = "USERS")
	PageData getUserInfoByUsername(PageData pd);
	
//	@Cacheable(key="#p0.getString('USERNAME')+#p0.getString('PASSWORD')", cacheNames = "USERS")
	PageData getUserInfo(PageData pd);
//	@Cacheable(key="#p0", cacheNames = "USERS")
	PageData getUserAndRoleById(String userId);
	
	void updateGuide(PageData pd);
	void setSKIN(PageData pd);
	
	void editPwd(PageData pd);
	void saveIP(PageData pd);
	void updateLastLogin(PageData pd);
	void saveUserDeptMatchup(PageData pd);
	void updateUserDeptMatchup(PageData pd);
	void deleteMatchupByUserID(PageData pd);
	void deleteMatchupByUserIDS(String[] ArrayUSER_IDS);
	void deleteAllU(String[] ArrayUSER_IDS);
	List<PageData> hasPhone(PageData pd);
	PageData selectByPhone(PageData pd);
	
	List<PageData> getDeptIdByUserId(String ids);
	List<PageData> getUsersByBeforeCreateTime(PageData pd);
	
	
	List<PageData> list(PageData map);
	PageData findById(PageData map);
	void save(PageData map);
	/**
	 * 其他页面增加用户时，自动添加系统用户
	 * @param map
	 */
	void saveAddByOtherType(PageData pd);
	/**
	 * 其他页面增加用户时，自动修改系统用户
	 * @param map
	 */
	void saveEditByOtherType(PageData pd);
	/**
	 * 添加用户role
	 * @param map 数据集合
	 */
	void saveUserRole(PageData pd);
	/**
	 * 删除用户role
	 * @param map 数据集合
	 */
	void deleteUserRole(PageData pd);
	
	//@CachePut(value="USERS",key="#map.getString('USERNAME')") 
	void edit(PageData map);
	
	//@CacheEvict(value="USERS",key="#id")  
	void delete(String id);
	/**	将新增用户增添到 sys_user_area */
	void saveUserArea(PageData areaPd);
	/** 通过用户id来查找用户地域  */
	String getAreaByUserId(String userId);

	void deleteUserID(String uSER_ID);
	/** 二级平台运营人员 分页查询 */
	List<PageData> motionlist(PageData pd);
	/** 修改二级运营的地域信息*/
	void editUserArea(PageData areaPd);

	
	
	
	
	
}