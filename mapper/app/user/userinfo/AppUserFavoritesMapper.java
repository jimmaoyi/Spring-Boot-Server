package com.maryun.mapper.app.user.userinfo;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.maryun.model.PageData;

@Mapper
public interface AppUserFavoritesMapper {

	void favoriting(PageData pd);// 添加收藏

	void cancelFavorited(PageData pd);// 取消收藏

	List<PageData> getFavoritingDoctorList(PageData pd);// 获取收藏
	
	List<PageData> getFavoritingFriendsList(PageData pd);// 获取收藏朋友圈

	PageData findFavoriteByExpertLIbsId(PageData pd);// 根据专家库ID来查看是否被用户收藏
	
	PageData findFavoriteByFriendsId(PageData pd);// 根据朋友圈ID来查看是否被用户收藏
}
