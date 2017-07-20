package com.maryun.mapper.system.menu;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

import com.maryun.model.PageData;

@Mapper
public interface MenuMapper {
	List<PageData> getMenuList(PageData map);
	List<PageData> getSubMenuList(PageData map);
	void insertMenu(PageData map);
	void updateMenu(PageData map);
	void deleteMenu(String[] list);
	List<PageData> listAllMenu(PageData map);
	List<PageData> listUserMenu(PageData map);
	List<PageData> listRoleMenu(PageData map);
	List<PageData> listAllParentMenu();
	List<PageData> listSubMenuByParentId(String id);
	List<PageData> parameterType(PageData map);
	List<PageData> findMaxId();
	void deleteByIds(String[] sa_ids);
}