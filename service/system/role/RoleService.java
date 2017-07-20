package com.maryun.service.system.role;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maryun.mapper.system.role.RoleMapper;
import com.maryun.model.PageData;

@Service("roleService")
public class RoleService{

	@Autowired
	private RoleMapper roleMapper;
	
	/*
	*角色列表(用户组)
	*/
	public List<PageData> listPdPageRole(PageData page)throws Exception{
		return roleMapper.list(page);
	}
	
	/*
	* 查找角色
	*/
	public PageData findById(PageData pd)throws Exception{
		return roleMapper.findById(pd);
	}
	
	/*
	* 保存角色
	*/
	public void saveRole(PageData pd)throws Exception{
		roleMapper.save(pd);
	}
	
	/*
	* 修改角色
	*/
	public void editRole(PageData pd)throws Exception{
		roleMapper.edit(pd);
	}
	
	/*
	* 批量删除角色
	*/
	public void deleteAllRoles(String[] ROLE_IDS)throws Exception{
		roleMapper.delete(ROLE_IDS);
	}
	
	
	/*
	 * 根据角色查询已经分配的用户
	 */
	public List findUserByRole(PageData pd) throws Exception{
		return roleMapper.findUserByRole(pd);
	}
	
	/*
	 * 根据角色查询未分配的用户
	 */
	public List findNewUserByRole(PageData pd) throws Exception{
		return roleMapper.findNewUserByRole(pd);
	}
	
	/*
	 * 批量增加用户
	 */
	public void batchAddUser(List list) throws Exception {
		roleMapper.addUser(list);
	}
	
	/*
	* 批量删除用户
	*/
	public void batchDeleteUser(PageData param)throws Exception{
		roleMapper.deleteUser(param);
	}
	
	/**
	 * 根据角色删除用户
	 * @param param
	 * @throws Exception
	 */
	public void deleteUserByRole(PageData param)throws Exception{
		roleMapper.deleteUserByRole(param);
	}
	
	/*
	 * 根据角色查询已有的菜单
	 * 
	 */
	public List findMenuByRole(PageData pd) throws Exception{
		return roleMapper.findMenuByRole(pd);
	}
	
	
	/*
	 * 根据角色查询已有的全局按钮
	 * 
	 */
	public List findGlobaLButtonByRole(PageData pd) throws Exception{
		return roleMapper.findGlobaLButtonByRole(pd);
	}
	
	
	/*
	 * 根据角色查询已有的按钮
	 * 
	 */
	public List findButtonByRole(PageData pd) throws Exception{
		return roleMapper.findButtonByRole(pd);
	}

	/*
	 * 批量增加菜单
	 */
	public void batchAddMenu(List list) throws Exception {
		roleMapper.addMenu(list);
	}
	
	
	/*
	* 根据角色删除菜单
	*/
	public void deleteMenuByRole(PageData param)throws Exception{
		roleMapper.deleteMenu(param);
	}

	
	
	/*
	* 根据角色和按钮删除角色按钮关联记录（全局的）
	*/
	public void DeleteButtonByRoleAndButton(PageData param)throws Exception{
		roleMapper.DeleteButtonByRoleAndButton(param);
	}
	
	/*
	* 根据角色和按钮添加角色按钮关联记录（全局的）
	*/
	public void AddButtonByRoleAndButton(PageData param)throws Exception{
		roleMapper.AddButtonByRoleAndButton(param);
	}
	
	
	/*
	* 根据角色、菜单、按钮删除角色按钮关联记录
	*/
	public void DeleteButtonByRoleAndMenuAndButton(PageData param)throws Exception{
		roleMapper.DeleteButtonByRoleAndMenuAndButton(param);
	}
	
	/*
	* 根据角色、菜单、按钮添加角色按钮关联记录
	*/
	public void AddButtonByRoleAndMenuAndButton(PageData param)throws Exception{
		roleMapper.AddButtonByRoleAndMenuAndButton(param);
	}
	
	/*
	 * 根据角色删除全局按钮
	 * */
	public void deleteGlobalButtonByRole(PageData param) throws Exception{
		roleMapper.deleteGlobalButtonByRole(param);
	}
	
	/*
	 * 根据角色删除菜单按钮
	 * */
	public void deleteMenuButtonByRole(PageData param) throws Exception{
		roleMapper.deleteMenuButtonByRole(param);
	}
	
	/*
	 * 批量增加全局按钮
	 */
	public void batchAddGlobalButton(List list) throws Exception{
		roleMapper.addGlobalButton(list);
	}
	
	/*
	 * 批量增加菜单按钮
	 */
	public void batchAddMenuButton(List list) throws Exception{
		roleMapper.addMenuButton(list);
	}
}
