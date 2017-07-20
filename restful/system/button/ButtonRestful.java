package com.maryun.restful.system.button;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.maryun.mapper.system.button.ButtonMapper;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.service.system.button.ButtonService;
import com.maryun.service.system.menu.MenuService;
import com.maryun.service.system.user.UserService;
import com.maryun.utils.UuidUtil;
import com.maryun.utils.WebResult;

/**
 * 
 * @ClassName: ButtonRestful 
 * @Description: 按钮rest接口
 * @author SR 
 * @date 2017年3月2日
 */
@RestController
@RequestMapping(value = "/button")
public class ButtonRestful extends BaseRestful {
	String menuUrl = "button/listButtons"; // 菜单地址(权限用)
	
	@Autowired
	private ButtonMapper buttonMapper;


	/**
	 * 
	 * @Description: 分页查询
	 * @param pd
	 * @return
	 * @throws Exception    
	 * @return PageData    
	 * @throws
	 */
	@RequestMapping(value = "/pageSearch")
	public PageData pageSearch(@RequestBody PageData pd) throws Exception{
		List<PageData> buttonList = null;
		if (pd.getPageNumber()!=0) {
			PageHelper.startPage(pd.getPageNumber(), pd.getPageSize());
		}
		buttonList = buttonMapper.buttonlistPage(pd);;
		//分页
		pd=this.getPagingPd(buttonList);
		//结果集封装
		return WebResult.requestSuccess(pd);
	}
	
	/**
	 * 
	 * @Description: 去修改按钮页面
	 * @param pd
	 * @return
	 * @throws Exception    
	 * @return PageData    
	 * @throws
	 */
	@RequestMapping(value = "/toEdit")
	public PageData toEdit(@RequestBody PageData pd) throws Exception {
			pd = buttonMapper.findById(pd);
			return WebResult.requestSuccess(pd);
	}

	/**
	 * 
	 * @Description: 保存用户
	 * @param pd
	 * @return
	 * @throws Exception    
	 * @return PageData    
	 * @throws
	 */
	@RequestMapping(value = "/saveAdd")
	public PageData saveAdd(@RequestBody PageData pd) throws Exception {
		pd.put("BUTTON_ID", UuidUtil.get32UUID()); // ID
			if (null == buttonMapper.findById(pd)) {
				buttonMapper.saveButton(pd);
				pd.put("msg", "success");
			} else {
				pd.put("msg", "failed");
			}
			return WebResult.requestSuccess(pd);
	}

	/**
	 * 
	 * @Description: 保存修改按钮
	 * @param pd
	 * @return
	 * @throws Exception    
	 * @return Object    
	 * @throws
	 */
	@RequestMapping(value = "/saveEdit")
	public Object saveEdit(@RequestBody PageData pd) throws Exception {
			buttonMapper.editButton(pd);
			pd.put("msg", "success");
			return WebResult.requestSuccess(pd);
	}

	/**
	 * 
	 * @Description: 批量删除
	 * @param pd
	 * @return
	 * @throws Exception    
	 * @return PageData    
	 * @throws
	 */
	@RequestMapping(value = "/delete")
	public PageData delete(@RequestBody PageData pd)throws Exception {
			String BUTTON_IDS = pd.getString("IDS");
			if (null != BUTTON_IDS && !"".equals(BUTTON_IDS)) {
				String ArrayBUTTON_IDS[] = BUTTON_IDS.split(",");
				buttonMapper.deleteButton(ArrayBUTTON_IDS);
				pd.put("msg", "ok");
			} else {
				pd.put("msg", "no");
			}
			return WebResult.requestSuccess(pd);
	}

}
