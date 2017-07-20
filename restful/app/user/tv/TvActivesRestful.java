package com.maryun.restful.app.user.tv;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.maryun.mapper.app.user.tv.TvActivesMapper;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.WebResult;

/**
 * 类名称：AppUserScanRestful 创建人：MARYUN 创建时间：2016年2月13日
 * 
 * @version
 */
@RestController
@RequestMapping(value = "/tv/active")
public class TvActivesRestful extends BaseRestful {
	@Autowired
	TvActivesMapper tvActivesMapper;

	/**
	 * 活动列表
	 * @return
	 */
	@RequestMapping(value = "/list")
	@ResponseBody
	public PageData getActivesList(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("UI_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			String type = pd.getString("TYPE");
			if("MY".equals(type)){//查询自己参加的活动列表
				List<PageData> list = tvActivesMapper.getMyActivesList(pd);
				return WebResult.requestSuccess(list);
			}else{//查询全部活动列表
				List<PageData> list = tvActivesMapper.getActivesList(pd);
				return WebResult.requestSuccess(list);
			}
		}
	}
	/**
	 * 活动详情
	 * @return
	 */
	@RequestMapping(value = "/detail")
	@ResponseBody
	public PageData getActivesDetail(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("A_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			PageData detail = tvActivesMapper.getActivesDetail(pd);
			List<PageData> shareList = tvActivesMapper.getActivesVideoList(pd);
			List<PageData> officialList = tvActivesMapper.getActivesOfficialVideoList(pd);
			detail.put("shareList", shareList);
			detail.put("officialList", officialList);
			return WebResult.requestSuccess(detail);
		}
	}
}
