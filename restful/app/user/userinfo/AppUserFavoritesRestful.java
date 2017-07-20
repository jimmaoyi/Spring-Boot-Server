package com.maryun.restful.app.user.userinfo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.maryun.mapper.app.user.friend.AppUserFriendMapper;
import com.maryun.mapper.app.user.userinfo.AppUserFavoritesMapper;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.WebResult;

/**
 * 类名称：AppUserInfoRestful 创建人：MARYUN 创建时间：2016年2月13日
 * 
 * @version
 */
@RestController
@RequestMapping(value = "/app/user/favorites")
public class AppUserFavoritesRestful extends BaseRestful {
	@Autowired
	AppUserFavoritesMapper appUserFavoritesMapper;

	@Autowired
	AppUserFriendMapper appUserFriendMapper;

	/**
	 * 收藏列表
	 * @return
	 */
	@RequestMapping(value = "/list")
	@ResponseBody
	public PageData getFavoritingList(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("SYS_UI_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			List<PageData> list = appUserFavoritesMapper.getFavoritingDoctorList(pd);
			return WebResult.requestSuccess(list);
		}
	}

	/**
	 * 收藏朋友圈列表
	 * @return
	 */
	@RequestMapping(value = "/friendlist")
	@ResponseBody
	public PageData getFavoritingFriendsList(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("SYS_UI_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			List<PageData> resList = new ArrayList<PageData>();
			List<PageData> list = appUserFavoritesMapper.getFavoritingFriendsList(pd);
			// 如果是改用户没有任何好友的情况，则查询自己的分享，排除朋友圈没有任何分享的情况，
			// 因为如若查朋友圈没有任何分享，查询自己分享也必然不存在任何记录
			/*
			 * if (list.size() == 0) { list =
			 * appUserFriendMapper.getSharesListByUiId(pd); }
			 */
			for (int i = 0; i < list.size(); i++) {
				PageData temp = list.get(i);
				List<PageData> commentList = appUserFriendMapper.getFriendShareCommentByShareId(temp);
				List<PageData> likeList = appUserFriendMapper.getFriendShareLikeByShareId(temp);
				List<PageData> favoriteList = appUserFriendMapper.getFriendShareFavoriteByShareId(temp);
				temp.put("COMMENT_LIST", commentList);
				temp.put("LIKE_LIST", likeList);
				temp.put("FAVORITE_LIST", favoriteList);
				resList.add(temp);
			}
			return WebResult.requestSuccess(resList);

			// List<PageData> list =
			// appUserFavoritesMapper.getFavoritingFriendsList(pd);
			// return WebResult.requestSuccess(list);
		}
	}

	/**
	 * 创建收藏
	 * @return
	 */
	@RequestMapping(value = "/create")
	@ResponseBody
	public PageData favoriting(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("F_TYPE")) || StringUtils.isBlank(pd.getString("F_KEY_ID")) || StringUtils.isBlank(pd.getString("SYS_UI_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			appUserFavoritesMapper.favoriting(pd);
			return WebResult.requestSuccess();
		}
	}

	/**
	 * 取消收藏
	 * @return
	 */
	@RequestMapping(value = "/cancel")
	@ResponseBody
	public PageData cancelFavorited(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("F_KEY_ID")) || StringUtils.isBlank(pd.getString("SYS_UI_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			appUserFavoritesMapper.cancelFavorited(pd);
			return WebResult.requestSuccess();
		}
	}

	/**
	 * 根据专家库ID来查看是否被用户收藏
	 * @return
	 */
	@RequestMapping(value = "/findFavoriteByExpertLIbsId")
	@ResponseBody
	public PageData findFavoriteByExpertLIbsId(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("F_KEY_ID")) || StringUtils.isBlank(pd.getString("SYS_UI_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			PageData accinfo = appUserFavoritesMapper.findFavoriteByExpertLIbsId(pd);
			boolean flag = false;
			if (accinfo != null) {
				flag = true;
			}
			return WebResult.requestSuccess(flag);
		}
	}

	/**
	 * 根据专家库ID来查看是否被用户收藏
	 * @return
	 */
	@RequestMapping(value = "/findFavoriteByFriendsId")
	@ResponseBody
	public PageData findFavoriteByFriendsId(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("F_KEY_ID")) || StringUtils.isBlank(pd.getString("SYS_UI_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			PageData accinfo = appUserFavoritesMapper.findFavoriteByFriendsId(pd);
			boolean flag = false;
			if (accinfo != null) {
				flag = true;
			}
			return WebResult.requestSuccess(flag);
		}
	}

}
