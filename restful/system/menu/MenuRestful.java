package com.maryun.restful.system.menu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.github.pagehelper.util.StringUtil;
import com.maryun.mapper.system.menu.MenuMapper;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.BootstrapUtils;
import com.maryun.utils.ListUtil;
import com.maryun.utils.UuidUtil;
import com.maryun.utils.WebResult;

/**
 * 
 * @ClassName: MenuController
 * @Description: TODO
 * @author "PangLin"
 * @date 2015年11月30日 下午1:45:00
 *
 */
@RestController
@RequestMapping("/menu")
public class MenuRestful extends BaseRestful {
	
	@Autowired
	private MenuMapper menuMapper;
	
	@RequestMapping(value = "/getTree")
	public Object getDicTree(@RequestBody PageData pd)throws Exception{
		try{
			List dicTreeList = BootstrapUtils.getTree(menuMapper.getMenuList(null), "0", "系统菜单", "MENU_ID", "MENU_NAME", "PARENT_ID");
			//结果集封装
			return WebResult.requestSuccess(dicTreeList);
		} catch (Exception e) {
			e.printStackTrace();
			return WebResult.requestFailed(500, "服务器错误",null);
		}
	}
	
	@RequestMapping(value = "/listMenuByUserName")
	public Object listMenuByUserName(@RequestBody String userName)throws Exception{
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
				
				if(StringUtils.isBlank(menu.getString("PARENT_ID"))||"0".equals(menu.getString("PARENT_ID"))){
					if(allMap.containsKey(menu.getString("MENU_ID")))
						retList.add(allMap.get(menu.getString("MENU_ID")));
					tempMap.put(menu.getString("MENU_ID"), menu);
				}else{
					PageData parentMenu = allMap.get(menu.getString("PARENT_ID"));
					PageData tempMenu = menu;
					do{
						Object subMenuList=parentMenu.get("SubMenu");
						if(subMenuList==null){
							List subMenu=new ArrayList<PageData>();
							subMenu.add(tempMenu);
							parentMenu.put("SubMenu",subMenu);
						}else{
							List subList=(List)subMenuList;
							subList.add(tempMenu);
							parentMenu.put("SubMenu",subList);
						}
						String preParentId=parentMenu.getString("PARENT_ID");
						if(StringUtil.isEmpty(preParentId)||"0".equals(preParentId)){
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
			//结果集封装
			return WebResult.requestSuccess(retList);
	}

	

	@RequestMapping(value = "/toEdit")
	public Object toEdit(@RequestBody PageData pd)throws Exception{
			List menuList = menuMapper.getMenuList(pd);
			//结果集封装
			return WebResult.requestSuccess(menuList);
	}

	@RequestMapping(value = "/saveAdd")
	public Object saveAdd(@RequestBody PageData pd) throws Exception {
		pd.put("id", UuidUtil.get32UUID());
		String pid=pd.getString("pid");
		if(pid==null || pid.equals("") || pid==""){
			pd.put("pid", "0");
		}
		menuMapper.insertMenu(pd);
			//结果集封装
			return WebResult.requestSuccess();
	}

	@RequestMapping(value = "/saveEdit")
	public Object saveEdit(@RequestBody PageData pd) throws Exception {
			menuMapper.updateMenu(pd);
			//结果集封装
			return WebResult.requestSuccess();
	}

	@RequestMapping(value = "/delete")
	public Object delete(@RequestBody PageData pd)throws Exception {
			String[] idsArr = ListUtil.getSubtreeIdsArr(ListUtil.List2TreeMap(
					menuMapper.getMenuList(null), "MENU_ID", "PARENT_ID"),
					"MENU_ID", pd.getString("id"));
			menuMapper.deleteMenu(idsArr);
			//结果集封装
			pd.put("msg", "ok");
			return WebResult.requestSuccess(pd);
	}

	@RequestMapping(value = "/getSubTree")
	public Object getSubTree(@RequestBody PageData PageData)throws Exception{
			List dicList = menuMapper.getSubMenuList(PageData);
			//结果集封装
			return WebResult.requestSuccess(dicList);
	}

}
