package com.maryun.restful.app.common;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.maryun.mapper.app.common.TvBackGroudImageMapper;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.WebResult;
/**
 * 类名称：MessageRestful 创建人：MARYUN 创建时间：2016年2月13日
 * 
 * @version
 */
@RestController
@RequestMapping(value = "/tv/backgroudimage")
public class TvBackGroudImageRestful extends BaseRestful {
	@Autowired
	private TvBackGroudImageMapper tvBackGroudImageMapper;
	/**
	 * 消息列表（分页）
	 * @return
	 */
	@RequestMapping(value = "/info")
	@ResponseBody
	public PageData getTvBackGroudImageList(@RequestBody PageData pd) throws Exception {
		PageData list = tvBackGroudImageMapper.getTvBackGroudImage(pd);
		return WebResult.requestSuccess(list);
	}
}
