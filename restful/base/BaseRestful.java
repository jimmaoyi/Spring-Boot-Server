package com.maryun.restful.base;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageInfo;
import com.maryun.model.PageData;
import com.maryun.utils.TockenUtil;
import com.maryun.utils.UuidUtil;

public class BaseRestful {
	
	private static final long serialVersionUID = 6357869213649815390L;

	/**
	 * 得到PageData
	 */
	public PageData getPageData() {
		return new PageData(this.getRequest());
	}

	/**
	 * 得到ModelAndView
	 */
	public ModelAndView getModelAndView() {
		return new ModelAndView();
	}

	/**
	 * 得到request对象
	 */
	public HttpServletRequest getRequest() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		return request;
	}
	
	/**
	 * 得到32位的uuid
	 * @return
	 */
	public String get32UUID(){
		return UuidUtil.get32UUID();
	}
	
	/**
	 * 分页封装
	 * @param list
	 * @return
	 */
	 public PageData getPagingPd(List list){
		 PageData pd = new PageData();
		// 分页信息结果集整合到分页框架
		PageInfo pi = new PageInfo(list);
		// 查询结果与分页信息封装为bootstrampTable认可结构
		//pd = BootstrapUtils.parsePage2BootstrmpTable(pi.getTotal(), list);
		if (list == null)
			list = new ArrayList();
		pd.put("total", pi.getTotal());
		pd.put("rows", list);
		return pd;
	 }
	 
	 
	@Value("${system.login.token.key}")
	public String tokenKey;
	/**
	 * @Description: 根据用户信息获取token
	 * @param userKey
	 * @return    
	 * @return String    
	 * @throws
	 */
	public String getUserIdByToken() {
		try {
			String userKey = this.getRequest().getHeader("userKey");
			String token = TockenUtil.getTokenStr(userKey, tokenKey);// token需要完善
			String[] userArray = token.split(",");
			if (userArray.length == 3) {
				return userArray[2];
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}
	
	
	/**
	 * 
	 * @Description: 根据userToken保存信息
	 * @param pd
	 * @return    
	 * @return PageData    
	 * @throws
	 */
	public PageData savePage(PageData pd){
		String user=getUserIdByToken();
		pd.put("DEL_STATE","1");//1 有效，0或者null 无效
		pd.put("CREATE_UID", user);
		pd.put("CREATE_TIME", new Date());
		pd.put("CHANGE_UID", user);
		pd.put("CHANGE_TIME", new Date());
		return pd;
	}
	
	public PageData updatePage(PageData pd){
		String user=getUserIdByToken();
		pd.put("CHANGE_UID", user);
		pd.put("CHANGE_TIME", new Date());
		return pd;
	}
	
	
}
