package com.maryun.mapper.system.role;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.maryun.mapper.BaseMapper;
import com.maryun.model.PageData;
@Mapper
public interface RoleMapper extends BaseMapper{
	List<PageData> findUserByRole(PageData pd);
	List<PageData> findNewUserByRole(PageData pd);
	void batchAddUser(List list);
	void addUser(List list);
	void deleteUser(PageData pd);
	void deleteUserByRole(PageData pd);
	void addMenu(List pd);
	void deleteMenu(PageData pd);
	void DeleteButtonByRoleAndButton(PageData pd);
	void AddButtonByRoleAndButton(PageData pd);
	void DeleteButtonByRoleAndMenuAndButton(PageData pd);
	void AddButtonByRoleAndMenuAndButton(PageData pd);
	void deleteGlobalButtonByRole(PageData pd);
	void deleteMenuButtonByRole(PageData pd);
	void addGlobalButton(List pd);
	void addMenuButton(List pd);
	List<PageData> findMenuByRole(PageData pd);
	List<PageData> findGlobaLButtonByRole(PageData pd);
	List<PageData> findButtonByRole(PageData pd);

}
