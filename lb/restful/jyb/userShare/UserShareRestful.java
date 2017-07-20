package com.maryun.lb.restful.jyb.userShare;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.maryun.lb.mapper.jyb.userShare.UserShareMapper;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.WebResult;

/**
 * 类名称：UserShareRestful 创建人：ChuMingZe 创建时间：2017年3月6日
 * 
 * @version
 */
@RestController
@RequestMapping(value = "/userShare")
public class UserShareRestful extends BaseRestful {
	@Autowired
	private UserShareMapper userShareMapper;

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
		lists = userShareMapper.listPage(pd);
		// 分页
		pd = this.getPagingPd(lists);
		// 结果集封装
		return WebResult.requestSuccess(pd);
	}

	/**
	 * 推荐、取消推荐
	 * 
	 * @param pd
	 *            参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/toRecommend")
	public PageData toPublish(@RequestBody PageData pd) throws Exception {
		String s_IDS = pd.getString("SHARE_ID");
		if (null != s_IDS && !"".equals(s_IDS)) {
			pd.put("IDSs", s_IDS.split(","));
			userShareMapper.updateRecommendState(pd);
			pd.put("msg", "success");
		}
		return WebResult.requestSuccess(pd);
	}
}
