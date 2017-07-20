package com.maryun.mapper.app.user.sport;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.maryun.model.PageData;

@Mapper
public interface AppUserSportMapper {
	PageData getUserSportStepData(PageData pd); // 查询用户运动数据信息

	List<PageData> getUserSportStepRanking(PageData pd); // 查询用户运动排行榜数据信息

	List<PageData> getUserSportStepRankingLiked(PageData pd); // 查询用户运动排行榜和点赞数据信息

	PageData getUserSportStepRankingBySelf(PageData pd); // 查询某个用户在运动排行榜排名情况

	List<PageData> getUserStepRankingLiked(PageData pd); // 查询某个用户在运动排行榜点赞

	PageData getUserStepRankingLikedByStepId(PageData pd);// 根据运动步数ID和用户ID查询用户是否点赞
	
	void addFriendStepShared(PageData pd);//用户点赞数增加
}
