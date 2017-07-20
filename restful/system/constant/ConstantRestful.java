package com.maryun.restful.system.constant;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.maryun.mapper.system.constant.ConstantMapper;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.service.system.constant.ConstantService;
import com.maryun.utils.IOUtils;
import com.maryun.utils.WebResult;
/**
 * 
 * @ClassName: ConstantRestful 
 * @Description: 系统常量
 * @author SR 
 * @date 2017年3月2日
 */
@RestController
@RequestMapping(value = "/constant")
public class ConstantRestful extends BaseRestful {
	@Autowired
	private ConstantMapper constantMapper;
	
	/**
	 * 
	 * @Description: 查询
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/pageSearch")
	@ResponseBody
	public Object pageSearch(@RequestBody PageData pd) throws Exception{
		List<PageData> systemConstantList = null;
			if (pd.getPageNumber()!=0) {
				PageHelper.startPage(pd.getPageNumber(), pd.getPageSize());
			}
			systemConstantList= constantMapper.list(pd);
			//分页
			pd=this.getPagingPd(systemConstantList);
			//结果集封装
			return WebResult.requestSuccess(pd);
	}

	
	
	

	/**
	 * 
	 * @Description: 去修改系统常量页面
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/toEdit")
	public Object toEdit(@RequestBody PageData pd)throws Exception{
			pd = constantMapper.findById(pd);
			//结果集封装
			return WebResult.requestSuccess(pd);
	}

	
	
	/** 
	 * 
	 * @Description: 保存添加的系统常量
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/saveAdd")
	public Object saveAdd(@RequestBody PageData pd)throws Exception{
		Map<String, Object> msg = new HashMap<String, Object>();
		pd.put("SC_ID", this.get32UUID()); // ID
		if (null == constantMapper.findById(pd)) {
			constantMapper.save(pd);
			msg.put("msg", "success");
		} else {
			msg.put("msg", "failed");
		}
		//结果集封装
		return WebResult.requestSuccess(msg);
	}

	
	
	/**
	 * 
	 * @Description: 修改系统常量保存
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/saveEdit")
	public Object saveEdit(@RequestBody PageData pd) throws Exception{
			Map<String, Object> msg = new HashMap<String, Object>();
			//用户id
			constantMapper.edit(pd);
			msg.put("msg", "success");
			//结果集封装
			return WebResult.requestSuccess(msg);
	}
	
	/**
	 * 
	 * @Description: 批量删除
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/delete")
	public Object delete(@RequestBody PageData pd) throws Exception{
			Map<String, Object> map = new HashMap<String, Object>();
			String SYSTEM_CONSTANT_IDS = pd.getString("IDS");
			if (null != SYSTEM_CONSTANT_IDS && !"".equals(SYSTEM_CONSTANT_IDS)) {
				String ArraySystemConstant_IDS[] = SYSTEM_CONSTANT_IDS.split(",");
				constantMapper.delete(ArraySystemConstant_IDS);
				map.put("msg", "ok");
			} else {
				map.put("msg", "no");
			}
			//结果集封装
			return WebResult.requestSuccess(map);
	}
	

}
