package com.maryun.restful.system.customer;

import com.github.pagehelper.PageHelper;
import com.maryun.mapper.system.customer.ImTitleMapper;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.UuidUtil;
import com.maryun.utils.WebResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("ImTitle")
public class ImTitleRestful extends BaseRestful{
	@Autowired
	private ImTitleMapper imTitleMapper;
	

	/**
	 * 获取table tree
	 * @Description: TODO
	 * @return    
	 * @return PageData    
	 * @throws
	 */
	@RequestMapping("pageSearch")
	public PageData pageSearch(@RequestBody PageData pd){
		List<PageData> buttonList = null;
		if (pd.getPageNumber()!=0) {
			PageHelper.startPage(pd.getPageNumber(), pd.getPageSize());
		}
		buttonList = imTitleMapper.list(pd);;
		//分页
		pd=this.getPagingPd(buttonList);
		//结果集封装
		return WebResult.requestSuccess(pd);
	}
	
	/**
	 * 去修改按钮页面
	 */
	@RequestMapping(value = "/toEdit")
	public PageData toEdit(@RequestBody PageData pd) throws Exception {
			pd = imTitleMapper.findById(pd);
			return WebResult.requestSuccess(pd);
	}
	/**
	 * 修改按钮
	 */
	@RequestMapping(value = "/saveEdit")
	public Object saveEdit(@RequestBody PageData pd) throws Exception {
			imTitleMapper.edit(pd);
			pd.put("msg", "success");
			return WebResult.requestSuccess(pd);
	}
	
	/**
	 * 保存用户
	 */
	@RequestMapping(value = "/saveAdd")
	public PageData saveAdd(@RequestBody PageData pd) throws Exception {
		pd.put("BUTTON_ID", UuidUtil.get32UUID()); // ID
			if (null == imTitleMapper.findById(pd)) {
				imTitleMapper.save(pd);
				pd.put("msg", "success");
			} else {
				pd.put("msg", "failed");
			}
			return WebResult.requestSuccess(pd);
	}
	/**
	 * 批量删除
	 */
	@RequestMapping(value = "/delete")
	public PageData delete(@RequestBody PageData pd)throws Exception {
			String BUTTON_IDS = pd.getString("IDS");
			if (null != BUTTON_IDS && !"".equals(BUTTON_IDS)) {
				String ArrayBUTTON_IDS[] = BUTTON_IDS.split(",");
				imTitleMapper.delete(ArrayBUTTON_IDS);
				pd.put("msg", "ok");
			} else {
				pd.put("msg", "no");
			}
			return WebResult.requestSuccess(pd);
	}
}
