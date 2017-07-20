package com.maryun.restful.app.user.sport;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.maryun.mapper.app.user.sport.AppUserSportMapper;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.UuidUtil;
import com.maryun.utils.WebResult;

@RestController
@RequestMapping(value = "/app/user/sport")
public class AppUserSportRestful extends BaseRestful {
	@Autowired
	AppUserSportMapper appUserSportMapper;

	/**
	 * 用户个人运动信息
	 * @return
	 */
	@RequestMapping(value = "/step")
	@ResponseBody
	public PageData getUserSportStepData(@RequestBody PageData pd) throws Exception {
		if(StringUtils.isBlank(pd.getString("UI_ID"))){
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}else{
			PageData accInfo = appUserSportMapper.getUserSportStepData(pd);
			return WebResult.requestSuccess(accInfo);
		}
	}
	
	/**
	 * 用户运动排行榜信息
	 * @return
	 */
	@RequestMapping(value = "/step/ranking")
	@ResponseBody
	public PageData getUserSportStepRanking(@RequestBody PageData pd) throws Exception {
		if(StringUtils.isBlank(pd.getString("UI_ID"))){
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}else{
			List<PageData> accInfo = appUserSportMapper.getUserSportStepRanking(pd);
			return WebResult.requestSuccess(accInfo);
		}
	}

	/**
	 * 用户运动排行榜及点赞信息
	 * @return
	 */
	@RequestMapping(value = "/step/rankingLiked")
	@ResponseBody
	public PageData getUserSportStepRankingLiked(@RequestBody PageData pd) throws Exception {
		if(StringUtils.isBlank(pd.getString("UI_ID"))){
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}else{
			List<PageData> accInfo = appUserSportMapper.getUserSportStepRankingLiked(pd);
			return WebResult.requestSuccess(accInfo);
		}
	}

	/**
	 * 用户运动排行榜个人信息
	 * @return
	 */
	@RequestMapping(value = "/step/ranking/self")
	@ResponseBody
	public PageData getUserSportStepRankingBySelf(@RequestBody PageData pd) throws Exception {
		if(StringUtils.isBlank(pd.getString("UI_ID"))){
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}else{
			PageData accInfo = appUserSportMapper.getUserSportStepRankingBySelf(pd);
			return WebResult.requestSuccess(accInfo);
		}
	}

	/**
	 * 个人用户对朋友圈中今日运动的点赞情况所有的数据
	 * @return
	 */
	@RequestMapping(value = "/step/ranking/liked")
	@ResponseBody
	public PageData getUserStepRankingLiked(@RequestBody PageData pd) throws Exception {
		if(StringUtils.isBlank(pd.getString("UI_ID"))){
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}else{
			PageData accInfo = appUserSportMapper.getUserSportStepRankingBySelf(pd);
			return WebResult.requestSuccess(accInfo);
		}
	}

	/**
	 * 个人用户对朋友圈中今日运动的点赞情况个人的数据
	 * @return
	 */
	@RequestMapping(value = "/step/ranking/liked/self")
	@ResponseBody
	public PageData getUserStepRankingLikedByStepId(@RequestBody PageData pd) throws Exception {
		if(StringUtils.isBlank(pd.getString("UI_ID"))||StringUtils.isBlank(pd.getString("STEP_ID"))){
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}else{
			PageData accInfo = appUserSportMapper.getUserStepRankingLikedByStepId(pd);
			return WebResult.requestSuccess(accInfo);
		}
	}
	
	/**
	 * 运动排行榜中，给好友点赞好友点赞数增加
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/step/ranking/shareAdd")
	@ResponseBody
	public PageData addFriendStepShared(@RequestBody PageData pd) throws Exception {
		
		if(StringUtils.isBlank(pd.getString("UI_ID"))||StringUtils.isBlank(pd.getString("STEP_ID"))){
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}else{
			System.out.println("------------------------------------------------");
			String LIKE_ID = UuidUtil.get32UUID();
			pd.put("LIKE_ID", LIKE_ID);
			appUserSportMapper.addFriendStepShared(pd);
			return WebResult.requestSuccess(LIKE_ID);
		}
	}

}
