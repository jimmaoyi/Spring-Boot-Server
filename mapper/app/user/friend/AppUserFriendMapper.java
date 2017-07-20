package com.maryun.mapper.app.user.friend;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.maryun.model.PageData;

@Mapper
public interface AppUserFriendMapper {

	List<PageData> getFriendSharesList(PageData pd); // 查看朋友圈消息列表

	List<PageData> getSharesListByUiId(PageData pd); // 查看某人朋友圈消息列表

	List<PageData> getShareRecommendList(PageData pd); // 查看推荐用户分享信息

	List<PageData> getFriendShareCommentByShareId(PageData pd); // 查看朋友圈某条消息评论

	List<PageData> getFriendShareLikeByShareId(PageData pd); // 查看朋友圈某条消息赞

	List<PageData> getFriendShareFavoriteByShareId(PageData pd); // 查看朋友圈某条消息收藏

	List<PageData> getUserInfo(PageData pd); // 查找/搜索用户

	List<PageData> hasFriendExist(PageData pd); // 查询好友是否存在

	void confirmFriend(PageData pd); // 确认好友

	void delFriend(PageData pd); // 删除好友

	void addFriend(PageData pd); // 添加好友

	List<PageData> getNearUserInfoList(PageData pd); // 查看附近的人(5km以内)

	void addShareLike(PageData pd); // 添加赞

	void cancelShareLike(PageData pd); // 取消赞

	List<PageData> getFriendInfoList(PageData pd); // 查看好友列表

	PageData getFriendShareByShareId(PageData pd); // 查看朋友圈某条分享

	int getShareForwardByShareId(PageData pd); // 获取某条分享转发数

	List<PageData> isFirstFriend(PageData pd); // 查看是否是初步好友

	List<PageData> isFriend(PageData pd); // 查看是否好友并验证

	PageData getUserDetailInfo(PageData pd); // 得到用户基本信息

	void savePic(PageData pd);// 存图片

	void saveVideo(PageData pd);// 存视频

	PageData findPic(PageData pd);// 查图片

	PageData findVideo(PageData pd);// 查视频

	void deleteShareByShareId(PageData pd); // 删除分享

	void deleteShareCommentByShareId(PageData pd); // 删除评论

	void deleteShareLikeByShareId(PageData pd); // 删除赞

	void deleteSharePicByShareId(PageData pd); // 删除分享图片

	void deleteShareVideoByShareId(PageData pd); // 删除分享视频

	void deleteShareForwardByShareId(PageData pd); // 删除分享转发

	void insertShare(PageData pd);// 新增分享

	void updateSharePic(PageData pd);// 更新分享图片表

	void updateShareVideo(PageData pd);// 更新分享视频表

	PageData getUserLocationInfo(PageData pd);// 获得用户位置

	PageData getNewFriendCount(PageData pd);// 得到新好友人数

	List<PageData> getNewFriendList(PageData pd); // 查看新的好友列表

	void addComment(PageData pd);// 添加评论

}
