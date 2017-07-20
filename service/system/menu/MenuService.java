package com.maryun.service.system.menu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.util.StringUtil;
import com.maryun.mapper.system.menu.MenuMapper;
import com.maryun.model.PageData;


@Service("menuService")
public class MenuService{
	
	@Autowired
	private MenuMapper menuMapper;
	
	public List<PageData> listMenu(PageData pageData)throws Exception{
		return menuMapper.getMenuList(pageData);
	}
	
	public List<PageData> listSubMenu(PageData pageData)throws Exception{
		return menuMapper.getSubMenuList(pageData);
	}
	
	/**
	 * 生成最新的部门ID
	 * @return
	 * @throws Exception
	 */
	public int getNewMenuId() throws Exception{
		List<PageData> list = menuMapper.findMaxId();
		int id = Integer.parseInt(list.get(0).get("MID").toString())+1;
		return id;
	}
	
	public void insertMenu(PageData pageData) throws Exception{
		menuMapper.insertMenu(pageData);
	}
	
	public void updateMenu(PageData pageData) throws Exception{
		menuMapper.updateMenu(pageData);
	}
	
	public void deleteMenu(String[] args){
		menuMapper.deleteMenu(args);
	}
	
	/**
	 * 根据用户名获取用户的所有菜单
	 * @param userName
	 * @return
	 */
	public List<PageData> listMenuByUserName(String userName) throws Exception{
		List<PageData> retList = new ArrayList<PageData>();
		PageData pd = new PageData();
		pd.put("SHOW", "1");
		
		List<PageData> allList = menuMapper.listAllMenu(pd);
		
		Map<String,PageData> allMap = new HashMap<String,PageData>();
		Map<String,PageData> tempMap = new HashMap<String,PageData>();
		
		for(PageData menu:allList){
			allMap.put(menu.getString("MENU_ID"), menu);
		}
		
		pd.put("USERNAME", userName);
		
		List<PageData> userList = (List<PageData>)menuMapper.listUserMenu(pd);
		
		for(PageData menu:userList){
			if(tempMap.containsKey(menu.getString("MENU_ID")))
				continue;
			
			String parentId="";
			if(StringUtils.isNotBlank(menu.getString("PARENT_ID"))||"0".equals(menu.getString("PARENT_ID"))){
				if(allMap.containsKey(menu.getString("MENU_ID")))
					retList.add(allMap.get(menu.getString("MENU_ID")));
				tempMap.put(menu.getString("MENU_ID"), menu);
			}else{
				PageData parentMenu = allMap.get(menu.getString("PARENT_ID"));
				PageData tempMenu = menu;
				do{
					Object subMenuList=parentMenu.getString("SubMenu");
					if(subMenuList==null){
						List subMenu=new ArrayList<PageData>();
						subMenu.add(tempMenu);
						parentMenu.put("SubMenu",subMenu);
					}else{
						parentMenu.put("SubMenu",((List)subMenuList).add(tempMenu));
					}
					String preParentId=parentMenu.getString("PARENT_ID");
					if(StringUtil.isNotEmpty(preParentId)||"0".equals(preParentId)){
						String preMenuId=parentMenu.getString("MENU_ID");
						if(!tempMap.containsKey(preMenuId)){
							if(allMap.containsKey(preMenuId))
								retList.add(allMap.get(preMenuId));
							tempMap.put(preMenuId, parentMenu);
						}
						break;	
					}
					tempMenu = parentMenu;
					parentMenu = allMap.get("preMenuId");
				}while(true);
			}
			
		}
	
		return retList;
	}
	
	public List listRoleMenu(PageData pd) throws Exception{
		return menuMapper.listRoleMenu(pd);
	}
	
	
	public List<PageData> listAllMenu() throws Exception {
		List<PageData> rl = this.listAllParentMenu();
		for(PageData menu : rl){
			List<PageData> subList = this.listSubMenuByParentId(menu.getString("MENU_ID"));
			menu.put("SubMenu",subList);
		}
		return rl;
	}
	
	public List<PageData> listAllParentMenu() throws Exception {
		return menuMapper.listAllParentMenu();
		
	}
	
	public List<PageData> listSubMenuByParentId(String parentId) throws Exception {
		return menuMapper.listSubMenuByParentId(parentId);
		
	}
	
	public List<PageData> listAllMenus() throws Exception{
		return menuMapper.listAllMenu(null);
	}
	
	
}
