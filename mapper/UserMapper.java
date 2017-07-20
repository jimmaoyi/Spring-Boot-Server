package com.maryun.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

//@Mapper
//@CacheConfig(cacheNames = "USERS")
public interface UserMapper  {
//	//@Cacheable(value="user")
//	@Select("select * from user WHERE 1=1 ")
//	List<User> selectAll123(@Param("user") User user);
//	
//	//@Cacheable(value="user")
//	List<User> queryAll();
//	
//	//@Cacheable(key="#p0", cacheNames = "USERS")
//	void deleteByIds(String[] sa_ids);
//	/**
//	 * 新增用户
//	 * @param map
//	 * @return
//	 */
//	//@CacheEvict(value="user")
//	int insertTest(Map map);
//	/**
//	 * 删除用户
//	 * @param i
//	 * @return
//	 */
//	//@CacheEvict(value="user")
//	int deleteTest(int i);
}

//@Mapper
////@CacheConfig(cacheNames = "USERS")
//public interface UserMapper extends MyMapper<User> {
//	//@Cacheable(value="user")
//	@Select("select * from user WHERE 1=1 ")
//	List<User> selectAll123(@Param("user") User user);
//	
//	//@Cacheable(value="user")
//	List<User> queryAll();
//	
//	//@Cacheable(key="#p0", cacheNames = "USERS")
//	void deleteByIds(String[] sa_ids);
//	/**
//	 * 新增用户
//	 * @param map
//	 * @return
//	 */
//	//@CacheEvict(value="user")
//	int insertTest(Map map);
//	/**
//	 * 删除用户
//	 * @param i
//	 * @return
//	 */
//	//@CacheEvict(value="user")
//	int deleteTest(int i);
//}