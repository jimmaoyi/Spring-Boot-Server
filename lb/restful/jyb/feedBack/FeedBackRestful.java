package com.maryun.lb.restful.jyb.feedBack;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.maryun.lb.mapper.jyb.feedBack.FeedBackMapper;
import com.maryun.mapper.system.user.UserMapper;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.UuidUtil;
import com.maryun.utils.WebResult;
@RestController
@RequestMapping(value = "/feedBack")
public class FeedBackRestful extends BaseRestful {
	@Autowired
	private FeedBackMapper feedBackMapper;
	
	@Autowired
	private UserMapper userMapper;
	
	/**
	 * 分页查询
	 * 
	 * @param pd
	 *            查询、分页条件
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/pageSearch")
	public PageData pageSearch(@RequestBody PageData pd) throws Exception {
		List<PageData> lists = null;
		if (pd.getPageNumber() != 0) {
			PageHelper.startPage(pd.getPageNumber(), pd.getPageSize());
		}
		PageData pds = pd;
		lists = feedBackMapper.listPage(pds);
		pd = this.getPagingPd(lists);
		return WebResult.requestSuccess(pd);
	}
	
	/**
	 * 按主键Id查询
	 * 
	 * @param pd
	 *            查询条件
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/findById")
	public PageData findById(@RequestBody PageData pd) throws Exception {
		pd = feedBackMapper.findById(pd);
		return WebResult.requestSuccess(pd);
	}
	
	
	/**
	 * 删除
	 * 
	 * @param pd
	 *            参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/toDelete")
	public PageData toDelete(@RequestBody PageData pd) throws Exception {
		String s_IDS = pd.getString("IDS");
		if(null != s_IDS && !"".equals(s_IDS)){
			pd.put("IDSs", s_IDS.split(","));
			feedBackMapper.delete(pd);
			pd.put("msg", "success");
		}else{
			pd.put("msg", "failed");
		}
		return WebResult.requestSuccess(pd);
	}
	/**
	 * 查看图片信息
	 * 
	 * @param pd
	 *            参数
	 * @return
	 * @throws Exception
	 */
	@Value("${system.imageServer.uploads.basePath}")
	public String BASEPATH;
	@RequestMapping(value = "/selectPic")
	public PageData selectPic(@RequestBody PageData pd) throws Exception {
		List<PageData> lists = null;
		PageData pds = pd;
		lists = feedBackMapper.selectPic(pds);
		pd.put("lists", lists);
		pd.put("PASTHS", BASEPATH);
		return WebResult.requestSuccess(pd);
	}
	
}
