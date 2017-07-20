package com.maryun.lb.restful.jyb.image;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maryun.lb.mapper.jyb.image.ImageMapper;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.UuidUtil;
import com.maryun.utils.WebResult;
/**
 * 类名称：ImageRestful 创建人：SunYongLiang 创建时间：2017年2月2*日
 * 
 * @version
 */
@RestController
@RequestMapping(value = "/image")
public class ImageRestful extends BaseRestful {
	@Autowired
	private ImageMapper imageMapper;
	/**
	 * 根据类型进行查询（青年版，老年版）
	 * 
	 * @param pd
	 *            查询、分页条件
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/findAll")
	public PageData findAll(@RequestBody PageData pds) throws Exception {
		List<PageData> lists = imageMapper.findAll(pds);
		PageData pd = new PageData ();
		for (PageData pageData : lists) {
			String type = pageData.getString("I_TYPE");
			if(type.equals("2")){
				pd.put("I_IDY", pageData.getString("I_ID"));
				pd.put("I_TYPEY", pageData.getString("I_TYPE"));
				pd.put("I_IMAGE_ADDRY", pageData.getString("I_IMAGE_ADDR"));
				pd.put("FILE_UPLOAD_IDY", pageData.getString("FILE_UPLOAD_ID"));
			}
			if(type.equals("3")){
				pd.put("I_IDO", pageData.getString("I_ID"));
				pd.put("I_TYPEO", pageData.getString("I_TYPE"));
				pd.put("I_IMAGE_ADDRO", pageData.getString("I_IMAGE_ADDR"));
				pd.put("FILE_UPLOAD_IDO", pageData.getString("FILE_UPLOAD_ID"));
			}
			
		}
		return WebResult.requestSuccess(pd);
	}
	
	
	/**
	 * 添加青年版图片
	 * 
	 * @param pd
	 *            参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/saveYoung")
	public Object saveYoung(@RequestBody PageData pd) throws Exception {
		imageMapper.delete(pd);
		imageMapper.saveAdd(pd);
		return WebResult.requestSuccess(pd);
	}
	/**
	 * 添加老年版图片
	 * 
	 * @param pd
	 *            参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/saveOld")
	public Object saveOld(@RequestBody PageData pd) throws Exception {
		imageMapper.delete(pd);
		imageMapper.saveAdd(pd);
		return WebResult.requestSuccess(pd);
	}
	/**
	 * 查询科室图标 findByOMS
	 * 
	 * @param pd
	 *            参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/findByOMS")
	public Object findByOMS(@RequestBody PageData pd) throws Exception {
		pd.put("I_TYPE", "4");
		pd = imageMapper.selectOne(pd);
		return WebResult.requestSuccess(pd);
	}
	/**
	 * 添加科室图标
	 * 
	 * @param pd
	 *            参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/save")
	public Object save(@RequestBody PageData pd) throws Exception {
		imageMapper.deleteOne(pd);
		imageMapper.saveAdd(pd);
		return WebResult.requestSuccess(pd);
	}
}
