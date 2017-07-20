package com.maryun.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maryun.mapper.UserMapper;

//@Service
public class UserService {

//	@Autowired
//	private UserMapper userMapper;
//	
//
//	/**
//	 * 根据条件查询数据
//	 * 
//	 * @param user
//	 *            用户对象
//	 * @return
//	 */
//	public List<User> getLists(User user) {
////		if (null != user && null != user.getPageNum() && null != user.getPageSize()) {
////			PageHelper.startPage(user.getPageNum(), user.getPageSize());
////		}
//		return userMapper.queryAll();
//	}
//	
//	/**
//	 * 新增用户
//	 * @param map
//	 * @return
//	 */
//	public int insertUser(User user) {
//		return userMapper.insert(user);
//	}
//	
//	/**
//	 * 新增用户
//	 * @param map
//	 * @return
//	 */
//	public int insertTest(Map map) {
//		return userMapper.insertTest(map);
//	}
//	
//	/**
//	 * 删除用户
//	 * @return
//	 */
//	public int deleteTest() {
//		return userMapper.deleteTest(0);
//	}
//
//	/**
//	 * 根据主键Id查询数据
//	 * 
//	 * @param id
//	 *            主键Id
//	 * @return
//	 */
//	public User getById(Integer id) {
//		return userMapper.selectByPrimaryKey(id);
//	}
//
//	/**
//	 * 保存 增加或修改操作
//	 * 
//	 * @param user
//	 *            用户对象
//	 */
//	public void save(User user) {
//		if (null != user && null != user.getId()) {
//			userMapper.updateByPrimaryKey(user);
//		} else {
//			userMapper.insert(user);
//		}
//	}
//
//	/**
//	 * 根据主键Id删除数据
//	 * 
//	 * @param id
//	 *            主键Id
//	 */
//	public void deleteById(Integer id) {
//		userMapper.deleteByPrimaryKey(id);
//	}
//	
//	public void batchDelete(String[] sa_ids) {
//		userMapper.deleteByIds(sa_ids);
//	}
}
