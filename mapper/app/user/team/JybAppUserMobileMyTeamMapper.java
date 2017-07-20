package com.maryun.mapper.app.user.team;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.maryun.model.PageData;

@Mapper
public interface JybAppUserMobileMyTeamMapper {
	
	PageData checkTeamInfoByGroupId(PageData pd);//
	
	void createTeam(PageData pd);//创建团队
	
	List<PageData> checkMyTeams(PageData pd);//查询我的团队
	
	List<PageData> checkTeams(PageData pd);//查询所有团队
	
	List<PageData> checkAllStatusTeams(PageData pd);//查询我的所有的团队信息
	
	List<PageData> checkAllTeamersByGID(PageData pd);//根据团队id,查询该团队下的所有成员
	
	//
	PageData checkIsThisInviteTeamerOrAddTeamInfo(PageData pd);//被邀请和申请加入团队时，首先去看表中有没有此插入信息
	void inviteTeamerAndAddTeam(PageData pd);//队长邀请成员，或者成员选择团队
	
	void receiveAndRefuseAGURSTATE(PageData pd);////消息中心中，有团队邀请时，接受，拒绝状态改变，当有团员申请时，队长接受，拒绝状态的改变
	
	void deleteTeamer(PageData pd);//删除成员
	
	List<PageData> checkTeamActives(PageData pd);//查询团队所报名的活动，根据G_ID
	
	void leagueMember(PageData pd);//申请退团
	
	PageData joinTeamState(PageData pd);//根据用户id和团队id查询用户和团队之间的关系，即：该用户有没有加入该团队，加入状态 
	
	void disbandTeam(PageData pd);//解散团队
}
