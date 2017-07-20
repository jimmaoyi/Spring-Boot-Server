package com.maryun.restful.app.TVdemand;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.maryun.mapper.app.TVdemand.CollectionMapper;
import com.maryun.mapper.app.TVdemand.VodHistoryMapper;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.UuidUtil;
import com.maryun.utils.WebResult;

/**
 * 类名称：MessageRestful 创建人：MARYUN 创建时间：2016年2月13日
 * 
 * @version
 */
@RestController
@RequestMapping(value = "/tvdemand/vodhistory")
public class VodHistoryRestful extends BaseRestful {

	@Autowired
	private VodHistoryMapper vodHistoryMapper;

	/**
	 * 收藏夹列表（分页）
	 * @return
	 */
	@RequestMapping(value = "/getvodHistoryList")
	@ResponseBody
	public PageData getvodHistoryList(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("userId")) || StringUtils.isBlank(pd.getString("wayType"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			List<PageData> list = null;
			List<PageData> todayList = vodHistoryMapper.getVodHistoryListByToday(pd);
			List<PageData> weekList = vodHistoryMapper.getVodHistoryListByWeek(pd);
			List<PageData> agoList = vodHistoryMapper.getVodHistoryListByAgo(pd);
			pd.put("todayList", todayList);
			pd.put("weekList", weekList);
			pd.put("agoList", agoList);
			return WebResult.requestSuccess(pd);
		}
	}

	/**
	 * 某节目是否被收藏
	 * @return
	 */
	@RequestMapping(value = "/getVodHistoryByID")
	@ResponseBody
	public PageData getVodHistoryByID(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("userId")) || StringUtils.isBlank(pd.getString("contentId")) || StringUtils.isBlank(pd.getString("wayType"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			pd = vodHistoryMapper.getVodHistoryByID(pd);
			return WebResult.requestSuccess(pd);
		}
	}

	/**
	 * 添加收藏
	 * @return
	 */
	@RequestMapping(value = "/insertVodHistory")
	@ResponseBody
	public PageData insertVodHistory(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("userId")) || StringUtils.isBlank(pd.getString("contentId")) || StringUtils.isBlank(pd.getString("name")) || StringUtils.isBlank(pd.getString("playedTime")) || StringUtils.isBlank(pd.getString("wayType"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			vodHistoryMapper.deleteRecoding(pd);
			pd.put("VH_ID", UuidUtil.get32UUID());
			vodHistoryMapper.insertVodHistory(pd);
			return WebResult.requestSuccess();
		}
	}

}
