package com.maryun.service.system.user;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.maryun.mapper.system.user.UserMapper;
import com.maryun.model.PageData;


@Service("userService")
public class UserService {
	@Autowired
	private UserMapper userMapper;
	/*
	* 通过id获取数据
	*/
	public PageData findByUiId(PageData pd)throws Exception{
		return userMapper.findById(pd);
	}
	/*
	* 通过loginname获取数据
	*/
	public PageData findByUId(PageData pd)throws Exception{
		return userMapper.findByUId(pd);
	}
	
	/*
	* 通过邮箱获取数据
	*/
	public PageData findByUE(PageData pd)throws Exception{
		return userMapper.findByUE(pd);
	}
	
	/*
	* 通过编号获取数据
	*/
	public PageData findByUN(PageData pd)throws Exception{
		return userMapper.findByUN(pd);
	}
	
	/*
	* 保存用户
	*/
	public void saveU(PageData pd)throws Exception{
		userMapper.save(pd);
	}
	/*
	* 修改用户
	*/
	public void editU(PageData pd)throws Exception{
		userMapper.edit(pd);
	}
	/*
	* 换皮肤
	*/
	public void setSKIN(PageData pd)throws Exception{
		userMapper.setSKIN(pd);
	}
	
	/*
	* 批量删除用户
	*/
	public void delete(String[] USER_IDS)throws Exception{
		userMapper.delete(USER_IDS);
	}
	/*
	*用户列表(用户组)
	*/
	public List<PageData> list(PageData pd)throws Exception{
		if (pd.getPageNumber()!=0) {
			PageHelper.startPage(pd.getPageNumber(), pd.getPageSize());
		}
		return userMapper.list(pd);
	}
	
	/*
	* 保存用户IP
	*/
	public void saveIP(PageData pd)throws Exception{
		userMapper.saveIP(pd);
	}
	
	/*
	* 登录判断
	*/
	public PageData getUserByNameAndPwd(PageData pd)throws Exception{
		return userMapper.getUserInfo(pd);
	}
	/*
	* 跟新登录时间
	*/
	public void updateLastLogin(PageData pd)throws Exception{
		userMapper.updateLastLogin(pd);
	}
	
	/*
	*通过id获取数据
	*/
	public PageData getUserAndRoleById(String USER_ID) throws Exception {
		return userMapper.getUserAndRoleById(USER_ID);
	}
	
	
	/*
	* 保存班组
	*/
	public void saveUserDeptMatchup(PageData pd)throws Exception{
		userMapper.saveUserDeptMatchup(pd);
	}
	
	
	
	/*
	* 更新班组
	*/
	public void updateUserDeptMatchup(PageData pd)throws Exception{
		userMapper.updateUserDeptMatchup(pd);
	}
	
	
	
	
	/*
	* 删除班组
	*/
	public void deleteMatchupByUserID(PageData pd)throws Exception{
		userMapper.deleteMatchupByUserID(pd);
	}

	
	
	/*
	* 根据用户id删除所有班组
	*/
	public void deleteMatchupByUserIDS(String[] USER_IDS)throws Exception{
		userMapper.deleteMatchupByUserIDS(USER_IDS);
	}
	
	
	/*
	 * 根据用户id取班组
	 */
	public List<PageData> getDeptIdByUserId(String userId)throws Exception{
		return userMapper.getDeptIdByUserId(userId);
	}
	
	/**
	 * 修改密码
	 */
	public void updatePwd(PageData pd)throws Exception{
		userMapper.editPwd(pd);
	}
	public PageData getUserByUsername(PageData pd) throws Exception{
		 return userMapper.getUserInfoByUsername(pd);
	}
	public void updateGuide(PageData pd) throws Exception{
		userMapper.updateGuide(pd);
	}
	
}
