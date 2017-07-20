package com.maryun.mapper.app.user.active;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.maryun.model.PageData;

@Mapper
public interface JybAppUserMobileMyActiveMapper {
	
	
	List<PageData> myJoinActives(PageData pd);//我的活动列表
	
	PageData thisActiveInfor(PageData pd);//此活动详情
	
	List<PageData> thePersonOfThisActivity(PageData pd);//此活动的成员
	
	PageData checkFriendsIsJoinedThisActivity(PageData pd);//判断好友有没有加入过该活动
	
	void inviteFriendJoinActivityOrSignUpActivity(PageData pd);// 邀请好友加入活动，或者报名某活动
	
	void applyLeagueActivity(PageData pd);//申请退出此活动
	
	void cancelThisActivity(PageData pd);//取消此活动
	
	void deleteActiveMember(PageData pd);//团队负责人删除活动参与者

	List<PageData> checkAllActivitiesOfTheSportHtml(PageData pd);//运动中的精彩活动的活动列表 
	
	PageData theActivityInforInSportHtml(PageData pd);//运动中的活动详情信息
	
	List<PageData> checkTeamMemberJoinedThisActivity(PageData pd);//活动中的团体活动的活动成员 
	
	List<PageData> checkMyCreateGroup(PageData pd);//运动中的团队活动报名时查询我创建了几个团队 
	
	PageData theTeamIsJoinedThisActivity(PageData pd);//判断该团队是否参加此活动
	
	PageData checkRegistrationTeamInfor(PageData pd);//查询单一团队的信息
	
	void registrationThisActivity(PageData pd);//报名此活动
	
	PageData checkThisMemberIsJoinedThisActivity(PageData pd);//判断此成员是否以其他的途径参加过此项目
	
	void lahaoyoujiaruhuodong(PageData pd);//拉好友加入活动
	
	void personRegistrationThisActivity(PageData pd);//个人报名活动
	
	PageData checkTheNewActivityInfo(PageData pd);//
	
	PageData checkInviteTeamActivityInfo(PageData pd);//团队活动的报名参赛信息查询 
	
	PageData checkInvitePersonalActivityInfo(PageData pd);//个人活动的报名参赛信息查询
	
	List<PageData> checkTeamAndPersonalVideosInfo(PageData pd);//报名的视频查询
	
}
