package com.maryun.restful.app.user.recommend;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.maryun.mapper.app.user.recommend.AppUserRecommendMapper;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.WebResult;

/**
 * 类名称：AppUserHealthRestful 创建人：MARYUN 创建时间：2016年2月13日
 * 
 * @version
 */
@RestController
@RequestMapping(value = "/app/user/recommend")
public class AppUserRecommendRestful extends BaseRestful {
	@Autowired
	AppUserRecommendMapper appUserRecommendMapper;

	/**
	 * 获取轮播列表
	 * @return
	 */
	@RequestMapping(value = "/getActiveRotationList")
	@ResponseBody
	public PageData getActiveRotationList(@RequestBody PageData pd) throws Exception {
		List<PageData> list = appUserRecommendMapper.getActiveRotationList(pd);
		return WebResult.requestSuccess(list);
	}
	/**
	 * 获取轮播列表
	 * @return
	 */
	@RequestMapping(value = "/getRotationList")
	@ResponseBody
	public PageData getRotationList(@RequestBody PageData pd) throws Exception {
		List<PageData> list = appUserRecommendMapper.getRotationList(pd);
		return WebResult.requestSuccess(list);
	}

	/**
	 * 轮播信息详情
	 * @return
	 */
	@RequestMapping(value = "/getRotationDetail")
	@ResponseBody
	public PageData getRotationDetail(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("ARTICLE_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			pd = appUserRecommendMapper.getRotationDetail(pd);
			return WebResult.requestSuccess(pd);
		}
	}

	/**
	 * 获取健康教育列表
	 * @return
	 */
	@RequestMapping(value = "/getHealthArticleList")
	@ResponseBody
	public PageData getHealthArticleList(@RequestBody PageData pd) throws Exception {
		List<PageData> list = appUserRecommendMapper.getHealthArticleList(pd);
		return WebResult.requestSuccess(list);
	}

	/**
	 * 获取健康教育详情
	 * @return
	 */
	@RequestMapping(value = "/getHealthArticleDetail")
	@ResponseBody
	public PageData getHealthArticleDetail(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("ARTICLE_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			pd = appUserRecommendMapper.getHealthArticleDetail(pd);
			return WebResult.requestSuccess(pd);
		}
	}

}
