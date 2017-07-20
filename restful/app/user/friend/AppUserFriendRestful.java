package com.maryun.restful.app.user.friend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.maryun.common.service.KeywordFilter;
import com.maryun.common.service.PushServiceForCall;
import com.maryun.mapper.app.user.friend.AppUserFriendMapper;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.CommUtil;
import com.maryun.utils.UuidUtil;
import com.maryun.utils.WebResult;

/**
 * 类名称：AppUserHealthRestful 创建人：MARYUN 创建时间：2016年2月13日
 * 
 * @version
 */
@RestController
@RequestMapping(value = "/app/user/friend")
public class AppUserFriendRestful extends BaseRestful {
	@Autowired
	AppUserFriendMapper appUserFriendMapper;

	@Autowired
	PushServiceForCall pushServiceForCall;

	@Autowired
	KeywordFilter keywordFilter;

	/**
	 * 获取朋友圈分享消息
	 * @return
	 */
	@RequestMapping(value = "/getFriendSharesList")
	@ResponseBody
	public PageData getFriendSharesList(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("UI_ID")) || StringUtils.isBlank(pd.getString("TYPE"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			List<PageData> resList = new ArrayList<PageData>();
			List<PageData> list = appUserFriendMapper.getFriendSharesList(pd);
			// 如果是改用户没有任何好友的情况，则查询自己的分享，排除朋友圈没有任何分享的情况，
			// 因为如若查朋友圈没有任何分享，查询自己分享也必然不存在任何记录
			if (list.size() == 0) {
				list = appUserFriendMapper.getSharesListByUiId(pd);
			}
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
		}
	}

	/**
	 * 获取某人朋友圈分享消息
	 * @return
	 */
	@RequestMapping(value = "/getSharesListByUiId")
	@ResponseBody
	public PageData getSharesListByUiId(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("UI_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			List<PageData> resList = new ArrayList<PageData>();
			List<PageData> list = appUserFriendMapper.getSharesListByUiId(pd);
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
		}
	}

	/**
	 * 获取朋友圈分享消息
	 * @return
	 */
	@RequestMapping(value = "/getFriendShareByShareId")
	@ResponseBody
	public PageData getFriendShareByShareId(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("SHARE_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			PageData sharePd = appUserFriendMapper.getFriendShareByShareId(pd);
			List<PageData> commentList = appUserFriendMapper.getFriendShareCommentByShareId(pd);
			List<PageData> likeList = appUserFriendMapper.getFriendShareLikeByShareId(pd);
			List<PageData> favoriteList = appUserFriendMapper.getFriendShareFavoriteByShareId(pd);
			int count = appUserFriendMapper.getShareForwardByShareId(pd);
			sharePd.put("COMMENT_LIST", commentList);
			sharePd.put("LIKE_LIST", likeList);
			sharePd.put("FAVORITE_LIST", favoriteList);
			sharePd.put("FORWARD_COUNT", count);
			return WebResult.requestSuccess(sharePd);
		}
	}

	/**
	 * 查看推荐用户分享信息
	 * @return
	 */
	@RequestMapping(value = "/getShareRecommendList")
	@ResponseBody
	public PageData getShareRecommendList(@RequestBody PageData pd) throws Exception {
		List<PageData> list = appUserFriendMapper.getShareRecommendList(pd);
		List<PageData> resList = new ArrayList<PageData>();
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
		return WebResult.requestSuccess(list);
	}

	/**
	 * 查找新朋友
	 * @return
	 */
	@RequestMapping(value = "/getUserInfo")
	@ResponseBody
	public PageData getUserInfo(@RequestBody PageData pd) throws Exception {
		List<PageData> list = appUserFriendMapper.getUserInfo(pd);
		return WebResult.requestSuccess(list);
	}

	/**
	 * 查找用户位置
	 * @return
	 */
	@RequestMapping(value = "/getUserLocationInfo")
	@ResponseBody
	public PageData getUserLocationInfo(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("UI_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			PageData res = appUserFriendMapper.getUserLocationInfo(pd);
			return WebResult.requestSuccess(res);
		}
	}

	/**
	 * 添加新朋友
	 * @return
	 */
	@RequestMapping(value = "/addFriend")
	@ResponseBody
	public PageData addFriend(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("UI_ID")) || StringUtils.isBlank(pd.getString("FRIEND_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			List<PageData> l = appUserFriendMapper.hasFriendExist(pd);
			if (l!=null && l.size() > 0) {
				// PageData m = l.get(0);
				// String state = m.getString("CONFIRM_STATE");
				// if ("0".equals(state)) {
				// return WebResult.requestFailed(10032, "好友已添加等待好友通过！", null);
				// }
				// else {
				// return WebResult.requestFailed(10033, "好友已添加！", null);
				// }
			}
			else {
				String ui_id = pd.getString("UI_ID");
				String friend_id = pd.getString("FRIEND_ID");
				pd.put("SPONSOR", ui_id);// 发起人
				appUserFriendMapper.addFriend(pd);
				PageData userInfo = appUserFriendMapper.getUserDetailInfo(pd); // 获得用户的个人信息
				pd.put("FRIEND_ID", ui_id);
				pd.put("UI_ID", friend_id);
				appUserFriendMapper.addFriend(pd);				
				Map<String, String> extra = new HashMap<String, String>();
				Map<String, String> paramMap = new HashMap<String, String>();
				paramMap.put("UI_ID", ui_id);
				extra.put("KEY_PARAM", JSON.toJSONString(paramMap));
				// 添加成功后进行推送.
				List<String> userList = new ArrayList<String>();
				userList.add(friend_id);
				pushServiceForCall.setAPPKEYANDSecret("7");
				pushServiceForCall.alertTitleMsgExtraCallback("老伴儿", userInfo.getString("UI_NAME") + "添加你为好友", extra, userList, "202", ui_id, null, "7");
			}
			return WebResult.requestSuccess();
		}
	}

	/**
	 * 删除新朋友
	 * @return
	 */
	@RequestMapping(value = "/delFriend")
	@ResponseBody
	public PageData delFriend(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("UI_ID")) || StringUtils.isBlank(pd.getString("FRIEND_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			appUserFriendMapper.delFriend(pd);
			return WebResult.requestSuccess();
		}
	}

	/**
	 * 确认添加新朋友
	 * @return
	 */
	@RequestMapping(value = "/confirmFriend")
	@ResponseBody
	public PageData confirmFriend(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("UI_ID")) || StringUtils.isBlank(pd.getString("FRIEND_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			appUserFriendMapper.confirmFriend(pd);
			// 添加推送.
			PageData userInfo = appUserFriendMapper.getUserDetailInfo(pd); // 获得用户的个人信息
			Map<String, String> extra = new HashMap<String, String>();
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("UI_ID", pd.getString("UI_ID"));
			extra.put("KEY_PARAM", JSON.toJSONString(paramMap));
			List<String> userList = new ArrayList<String>();
			userList.add(pd.getString("FRIEND_ID"));
			pushServiceForCall.setAPPKEYANDSecret("7");
			pushServiceForCall.alertTitleMsgExtraCallback("老伴儿", userInfo.getString("UI_NAME") + "通过了你的好友验证", extra, userList, "202", pd.getString("UI_ID"),
					null, "7");
			return WebResult.requestSuccess();
		}
	}

	/**
	 * 查看附件的人(5km以内)
	 * @return
	 */
	@RequestMapping(value = "/getNearUserInfoList")
	@ResponseBody
	public PageData getNearUserInfoList(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("UI_ID")) || StringUtils.isBlank(pd.getString("UL_LAT")) || StringUtils.isBlank(pd.getString("UL_LON"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			List<PageData> resList = appUserFriendMapper.getNearUserInfoList(pd);
			return WebResult.requestSuccess(resList);
		}
	}

	/**
	 * 点赞或者取消赞
	 * @return
	 */
	@RequestMapping(value = "/addOrCancelShareLike")
	@ResponseBody
	public PageData addOrCancelShareLike(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("UI_ID")) || StringUtils.isBlank(pd.getString("SHARE_ID")) || StringUtils.isBlank(pd.getString("TYPE"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			if ("0".equals(pd.getString("TYPE"))) {
				appUserFriendMapper.addShareLike(pd);// 添加赞
			}
			else {
				appUserFriendMapper.cancelShareLike(pd);// 取消赞
			}
		}
		return WebResult.requestSuccess();
	}

	/**
	 * 查看好友列表
	 * @return
	 */
	@RequestMapping(value = "/getFriendInfoList")
	@ResponseBody
	public PageData getFriendInfoList(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("UI_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			List<PageData> list = appUserFriendMapper.getFriendInfoList(pd);
			return WebResult.requestSuccess(list);
		}
	}

	/**
	 * 查看某条分享的赞
	 * @return
	 */
	@RequestMapping(value = "/getFriendShareLikeByShareId")
	@ResponseBody
	public PageData getFriendShareLikeByShareId(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("SHARE_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			List<PageData> list = appUserFriendMapper.getFriendShareLikeByShareId(pd);
			return WebResult.requestSuccess(list);
		}
	}

	/**
	 * 查看两人之间的关系状态
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getTwoPersonRelation")
	@ResponseBody
	public PageData getTwoPersonRelation(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("UI_ID")) || StringUtils.isBlank(pd.getString("FRIEND_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			PageData res = new PageData();
			List<PageData> list = appUserFriendMapper.isFirstFriend(pd);
			if (list.size() == 0) {
				res.put("STATUS", "0"); // 不是好友
			}
			else {
				PageData temp = list.get(0);
				String ui_id = pd.getString("UI_ID"); // 用户id
				if ("0".equals(temp.getString("CONFIRM_STATE"))) {
					// 未通过验证
					if (ui_id.equals(temp.getString("SPONSOR"))) {
						res.put("STATUS", "1"); // 是好友等待对方确认
					}
					else {
						res.put("STATUS", "2"); // 是好友我方确认点击确认
						res.put("CONFIRM_MSG", temp.getString("CONFIRM_MSG")); // 发送的信息
					}
				}
				else {
					res.put("STATUS", "3"); // 双方是好友
				}
			}
			return WebResult.requestSuccess(res);
		}
	}

	/**
	 * 获取某个用户的基本信息
	 * @return
	 */
	@RequestMapping(value = "/getUserDetailInfo")
	@ResponseBody
	public PageData getUserDetailInfo(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("UI_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			PageData res = appUserFriendMapper.getUserDetailInfo(pd);
			return WebResult.requestSuccess(res);
		}
	}

	/**
	 * 删除分享（级联删除所有分享有关数据）
	 * @return
	 */
	@RequestMapping(value = "/deleteShareByShareId")
	@ResponseBody
	public PageData deleteShareByShareId(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("SHARE_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			appUserFriendMapper.deleteShareByShareId(pd);
			appUserFriendMapper.deleteShareCommentByShareId(pd);
			appUserFriendMapper.deleteShareLikeByShareId(pd);
			appUserFriendMapper.deleteShareForwardByShareId(pd);
			appUserFriendMapper.deleteSharePicByShareId(pd);
			appUserFriendMapper.deleteShareVideoByShareId(pd);
			return WebResult.requestSuccess();
		}
	}

	/**
	 * 上传分享（同时更新分享图片表和视频表）
	 * @return
	 */
	@RequestMapping(value = "/uploadShare")
	@ResponseBody
	public PageData uploadShare(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("SHARE_TYPE")) || pd.get("MEDIA_ID_LIST") == null || StringUtils.isBlank(pd.getString("PUBLISH_UID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			String mString = pd.get("MEDIA_ID_LIST").toString().replace("[", "").replace("]", "").trim();
			String[] mediaList = mString.split(",");
			String shareID = UuidUtil.get32UUID();
			pd.put("CHANGE_UID", pd.get("PUBLISH_UID").toString());// 更新人
			pd.put("SHARE_ID", shareID);
			pd.put("UI_ID", pd.getString("PUBLISH_UID"));
			PageData locData = appUserFriendMapper.getUserLocationInfo(pd); // 获取用户位置
			pd.put("LOCATION", locData.getString("UL_ADDRESS"));
			appUserFriendMapper.insertShare(pd); // 新增分享
			if ("2".equals(pd.getString("SHARE_TYPE"))) {
				// 图片
				PageData param = new PageData();
				param.put("SHARE_ID", shareID);
				param.put("IDS", CommUtil.arr2InStr(mString));
				appUserFriendMapper.updateSharePic(param);

			}
			else {
				// 视频
				for (int i = 0; i < mediaList.length; i++) {
					PageData param = new PageData();
					param.put("SHARE_ID", shareID);
					param.put("VIDEO_ID", mediaList[i]);
					appUserFriendMapper.updateShareVideo(param);
				}
			}

			return WebResult.requestSuccess();
		}
	}

	/**
	 * 获取新的好友数量
	 * @return
	 */
	@RequestMapping(value = "/getNewFriendCount")
	@ResponseBody
	public PageData getNewFriendCount(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("UI_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			PageData res = appUserFriendMapper.getNewFriendCount(pd);
			return WebResult.requestSuccess(res);
		}
	}

	/**
	 * 获取新的好友列表
	 * @return
	 */
	@RequestMapping(value = "/getNewFriendList")
	@ResponseBody
	public PageData getNewFriendList(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("UI_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			List<PageData> res = appUserFriendMapper.getNewFriendList(pd);
			return WebResult.requestSuccess(res);
		}
	}

	/**
	 * 添加评论
	 * @return
	 */
	@RequestMapping(value = "/addComment")
	@ResponseBody
	public PageData addComment(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("UI_ID")) || StringUtils.isBlank(pd.getString("DISCUSS_COMMENT"))
				|| StringUtils.isBlank(pd.getString("SHARE_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			appUserFriendMapper.addComment(pd);
			return WebResult.requestSuccess();
		}
	}

	/**
	 * 敏感词过滤
	 * 
	 * @param pd 参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/checkKeyWords")
	public PageData checkKeyWords(@RequestBody PageData pd) throws Exception {
		PageData returnState = keywordFilter.checkWords(pd);
		return WebResult.requestSuccess(returnState);
	}
}
