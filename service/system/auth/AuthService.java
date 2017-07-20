package com.maryun.service.system.auth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maryun.mapper.system.auth.AuthMapper;
import com.maryun.model.PageData;


@Service("authService")
public class AuthService {

	@Autowired
	private AuthMapper authMapper;
	
	
	/**
	 * 为shiro获取用户的全部的permission
	 * 按钮以 "user:add" "user:edit" "user:del"的形式授权
	 * 菜单以 "url:user/listUsers" 的形式授权
	 * @param userName
	 * @return
	 * @author panglin
	 */
	public List<String> getAuthListByUser(String userName){
		List<String> authList = new ArrayList<String>();
		PageData pd = new PageData();
		pd.put("USERNAME", userName);
		
		//获取按钮权限
		Map btnMap = getButtonAuthByUser(pd);
		
		Iterator<String> it = btnMap.keySet().iterator();
		while(it.hasNext()){
			authList.add(it.next());
		}
		//获取菜单权限
		Map urlMap = getUrlAuthByUser(pd);
		
		Iterator<String> it2 = urlMap.keySet().iterator();
		while(it2.hasNext()){
			authList.add(it2.next());
		}
		
		return authList;
	}
	
	public List<String> getGlobalBtnListByUser(String userName){
		List<String> buttonList = new ArrayList<String>();
		PageData pd = new PageData();
		pd.put("USERNAME", userName);
		
		PageData map = getGlobalBtnAuthByUser(pd);
		Iterator it = map.keySet().iterator();
		while(it.hasNext()){
			buttonList.add(it.next()+"");
		}
		return buttonList;
	}
	
	public List<String> getURLBtnListByUser(String userName){
		List<String> buttonList = new ArrayList<String>();
		PageData pd = new PageData();
		pd.put("USERNAME", userName);
		
		PageData map = getURLBtnAuthByUser(pd);
		Iterator it = map.keySet().iterator();
		while(it.hasNext()){
			buttonList.add(it.next()+"");
		}
		return buttonList;
	}
	
	/**
	 * 获取用户的菜单权限
	 * @param pd
	 * @return
	 */
	public PageData getUrlAuthByUser(PageData pd){
		PageData authMap = new PageData();
		try {
			List<PageData> urlList = authMapper.findUrlsByUser(pd);
			for(PageData pb:urlList){
				String url = pb.getString("MENU_URL");
				if(url==null||"".equals(url))
					continue;
				if(url.startsWith("/"))
					url = url.substring(1);
				
				authMap.put("url:"+url.split(".do")[0], "1");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return authMap;
	}
	
	public List<String> getUrlAuthListByUser(String userName){
		PageData pd = new PageData();
		pd.put("USERNAME", userName);
		List<String> retList = new ArrayList<String>();
		try {
			List<PageData> urlList = authMapper.findUrlsByUser(pd);
			for(PageData pb:urlList){
				String url = pb.getString("MENU_URL");
				if(url==null||"".equals(url))
					continue;
				retList.add(pb.getString("MENU_NAME"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return retList;
	}
	
	/**
	 * 使用用户名获取用户的按钮权限
	 * @param pd
	 * @return
	 * @author panglin
	 */
	public PageData getButtonAuthByUser(PageData pd){
		//获取全局设置的权限
		PageData allMap = getGlobalBtnAuthByUser(pd);
		PageData urlMap = getURLBtnAuthByUser(pd);
		
		allMap.putAll(urlMap);
		
		return allMap;
	}
	
	/**
	 * 获取用户的角色
	 * @param userName
	 * @return
	 */
	public HashSet<String> getRolesByUser(String userName){
		HashSet<String> rolesSet = new HashSet<String>();
		PageData pd = new PageData();
		pd.put("USERNAME", userName);
		try {
			List<PageData> rList = authMapper.findRolesByUser(pd);
			for(PageData r : rList){
				rolesSet.add(r.getString("ROLE_CODE"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rolesSet;
	}
	
	public List<String> getRolesListByUser(String userName){
		List<String> retStr = new ArrayList<String>();
		PageData pd = new PageData();
		pd.put("USERNAME", userName);
		try {
			List<PageData> rList = authMapper.findRolesByUser(pd);
			for(PageData r : rList){
				retStr.add(r.getString("ROLE_NAME"));
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retStr;
	}
	
	/**
	 * 使用用户名获取用户的在每个角色中全局设置的权限
	 * @param pd
	 * @return
	 * @author panglin
	 */
	private PageData getGlobalBtnAuthByUser(PageData pd){
		PageData authMap = new PageData();
		try {
			//获取全局设置的按钮
			List<PageData> bList = authMapper.findAllButtonByRoleUser(pd);
			
			//获取全局设置的url
			List<PageData> uList = authMapper.findAllUrlByRoleUser(pd);
			
			for(PageData pu:uList){
				for(PageData pb:bList){
					authMap.put(pu.getString("MENU_CODE")+":"+pb.getString("BUTTON_CODE"), "1");
				}
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return authMap;
	}
	
	/**
	 * 使用用户名获取用户的在每个角色中单独设置的权限
	 * @param pd
	 * @return
	 * @author panglin
	 */
	private PageData getURLBtnAuthByUser(PageData pd){
		PageData authMap = new PageData();
		try {
			List<PageData> list = authMapper.findUrlAuthCodeByRoleUser(pd);
			for(PageData u:list){
				authMap.put(u.getString("MENU_CODE")+":"+u.getString("BUTTON_CODE"), "1");
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return authMap;
	}
	
}
