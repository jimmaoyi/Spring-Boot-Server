package com.maryun.restful.app.user.active;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.maryun.mapper.app.user.active.JybAppUserMobileMyActiveMapper;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.UuidUtil;
import com.maryun.utils.WebResult;

@RestController
@RequestMapping(value = "/app/user/active")
public class JybAppUserMobileMyActiveRestful extends BaseRestful {
		
	@Autowired
	private JybAppUserMobileMyActiveMapper jybAppUserMobileMyActiveMapper; 
	
	//我的活动列表
	@RequestMapping("/myJoinActives")
	@ResponseBody
	@Transactional		
	public PageData myJoinActives(@RequestBody PageData pd) throws Exception{
	
		if(StringUtils.isBlank(pd.getString("UI_ID"))){
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		List<PageData>  pdActivies = jybAppUserMobileMyActiveMapper.myJoinActives(pd);
		
		return WebResult.requestSuccess(pdActivies);
		
	}
	
	//此活动详情
	@RequestMapping("/thisActiveInfor")
	@ResponseBody
	@Transactional		
	public PageData thisActiveInfor(@RequestBody PageData pd) throws Exception{
	
		if(StringUtils.isBlank(pd.getString("A_ID"))){
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		PageData pdActivies = jybAppUserMobileMyActiveMapper.thisActiveInfor(pd);
		
		return WebResult.requestSuccess(pdActivies);
		
	}
	
	//checkTheNewActivityInfo
	@RequestMapping("/checkTheNewActivityInfo")
	@ResponseBody
	@Transactional		
	public PageData checkTheNewActivityInfo(@RequestBody PageData pd) throws Exception{
	
		PageData pdActivie = jybAppUserMobileMyActiveMapper.checkTheNewActivityInfo(pd);
		
		return WebResult.requestSuccess(pdActivie);
		
	}
	
	//此活动的成员
	@RequestMapping("/thePersonOfThisActivity")
	@ResponseBody
	@Transactional		
	public PageData thePersonOfThisActivity(@RequestBody PageData pd) throws Exception{
	
		if(StringUtils.isBlank(pd.getString("A_ID"))){
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		List<PageData> pdLists = jybAppUserMobileMyActiveMapper.thePersonOfThisActivity(pd);
		
		return WebResult.requestSuccess(pdLists);
		
	}
	
	///判断好友有没有加入过该活动
	@RequestMapping("/checkFriendsIsJoinedThisActivity")
	@ResponseBody
	@Transactional		
	public PageData checkFriendsIsJoinedThisActivity(@RequestBody PageData pd) throws Exception{
	
		if(StringUtils.isBlank(pd.getString("A_ID"))||StringUtils.isBlank(pd.getString("UI_ID"))){
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		PageData pdRes = jybAppUserMobileMyActiveMapper.checkFriendsIsJoinedThisActivity(pd);
		
		return WebResult.requestSuccess(pdRes);
		
	}
	
	// 邀请好友加入活动，或者报名某活动 
	@RequestMapping("/inviteFriendJoinActivityOrSignUpActivity")
	@ResponseBody
	@Transactional		
	public PageData inviteFriendJoinActivityOrSignUpActivity(@RequestBody PageData pd) throws Exception{
	
		if(StringUtils.isBlank(pd.getString("A_ID"))||StringUtils.isBlank(pd.getString("UI_ID"))
				||StringUtils.isBlank(pd.getString("AUR_SEL_STATE"))){
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		pd.put("AUR_ID", UuidUtil.get32UUID());
		jybAppUserMobileMyActiveMapper.inviteFriendJoinActivityOrSignUpActivity(pd);
		
		return WebResult.requestSuccess();
		
	}
	
	//申请退出此活动
	@RequestMapping("/applyLeagueActivity")
	@ResponseBody
	@Transactional		
	public PageData applyLeagueActivity(@RequestBody PageData pd) throws Exception{
	
		if(StringUtils.isBlank(pd.getString("A_ID"))||StringUtils.isBlank(pd.getString("UI_ID"))
				){
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		
		jybAppUserMobileMyActiveMapper.applyLeagueActivity(pd);
		
		return WebResult.requestSuccess();
		
	}
	
	//取消此活动
	@RequestMapping("/cancelThisActivity")
	@ResponseBody
	@Transactional		
	public PageData cancelThisActivity(@RequestBody PageData pd) throws Exception{
	
		if(StringUtils.isBlank(pd.getString("A_ID"))
				){
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		
		jybAppUserMobileMyActiveMapper.cancelThisActivity(pd);
		
		return WebResult.requestSuccess();
		
	}
	
	
	//活动负责人删除参与活动者
	@RequestMapping("/deleteActiveMember")
	@ResponseBody
	@Transactional		
	public PageData deleteActiveMember(@RequestBody PageData pd) throws Exception{
	
		if(StringUtils.isBlank(pd.getString("AGUR_ID"))
				){
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		
		jybAppUserMobileMyActiveMapper.deleteActiveMember(pd);
		
		return WebResult.requestSuccess();
		
	}
	
	
	//查看运动中的精彩活动的活动列表
	@RequestMapping("/checkAllActivitiesOfTheSportHtml")
	@ResponseBody
	@Transactional		
	public PageData checkAllActivitiesOfTheSportHtml(@RequestBody PageData pd) throws Exception{
	
		if(StringUtils.isBlank(pd.getString("UI_ID"))
				){
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
			
		List<PageData> pdLists = jybAppUserMobileMyActiveMapper.checkAllActivitiesOfTheSportHtml(pd);
		
		return WebResult.requestSuccess(pdLists);
		
	}
	
	
	//运动中的活动详情信息
	@RequestMapping("/theActivityInforInSportHtml")
	@ResponseBody
	@Transactional		
	public PageData theActivityInforInSportHtml(@RequestBody PageData pd) throws Exception{
	
		if(StringUtils.isBlank(pd.getString("A_ID"))
				){
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
			
		PageData pdRes = jybAppUserMobileMyActiveMapper.theActivityInforInSportHtml(pd);
		
		return WebResult.requestSuccess(pdRes);
		
	}
	
	
	//运动中的团体活动的活动成员 
	@RequestMapping("/checkTeamMemberJoinedThisActivity")
	@ResponseBody
	@Transactional		
	public PageData checkTeamMemberJoinedThisActivity(@RequestBody PageData pd) throws Exception{
	
		if(StringUtils.isBlank(pd.getString("A_ID"))||StringUtils.isBlank(pd.getString("G_ID"))
				){
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
			
		List<PageData> pdLists = jybAppUserMobileMyActiveMapper.checkTeamMemberJoinedThisActivity(pd);
		
		return WebResult.requestSuccess(pdLists);
		
	}
	
	//运动中的团队活动报名时查询我创建了几个团队 
	@RequestMapping("/checkMyCreateGroup")
	@ResponseBody
	@Transactional		
	public PageData checkMyCreateGroup(@RequestBody PageData pd) throws Exception{
	
		if(StringUtils.isBlank(pd.getString("UI_ID"))
				){
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
			
		List<PageData> pdLists = jybAppUserMobileMyActiveMapper.checkMyCreateGroup(pd);
		
		return WebResult.requestSuccess(pdLists);
		
	}
	
	//判断该用户创建的团队是否参加过此活动
	@RequestMapping("/theTeamIsJoinedThisActivity")
	@ResponseBody
	@Transactional		
	public PageData theTeamIsJoinedThisActivity(@RequestBody PageData pd) throws Exception{
	
		if(StringUtils.isBlank(pd.getString("UI_ID"))||StringUtils.isBlank(pd.getString("A_ID"))
				){
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
			
		PageData pdRes = jybAppUserMobileMyActiveMapper.theTeamIsJoinedThisActivity(pd);
		
		return WebResult.requestSuccess(pdRes);
		
	}
	
	
	//查询单一团队的信息
	@RequestMapping("/checkRegistrationTeamInfor")
	@ResponseBody
	@Transactional		
	public PageData checkRegistrationTeamInfor(@RequestBody PageData pd) throws Exception{
	
		if(StringUtils.isBlank(pd.getString("G_ID"))
				){
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
			
		PageData pdRes = jybAppUserMobileMyActiveMapper.checkRegistrationTeamInfor(pd);
		
		return WebResult.requestSuccess(pdRes);
		
	}
	
	//团队报名此活动AGR_ID,A_ID,G_ID,AGR_SEL_STATE,LEADER_NAME,LEADER_PHONE,URGENT_NAME,URGENT_PHONE,REMARK
	@RequestMapping("/registrationThisActivity")
	@ResponseBody
	@Transactional		
	public PageData registrationThisActivity(@RequestBody PageData pd) throws Exception{
	
		if(StringUtils.isBlank(pd.getString("A_ID"))||
				StringUtils.isBlank(pd.getString("G_ID"))||
				StringUtils.isBlank(pd.getString("LEADER_NAME"))||
				StringUtils.isBlank(pd.getString("LEADER_PHONE"))||
				StringUtils.isBlank(pd.getString("URGENT_NAME"))||
				StringUtils.isBlank(pd.getString("URGENT_PHONE"))||
				StringUtils.isBlank(pd.getString("REMARK"))
				){
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		
		pd.put("AGR_ID", UuidUtil.get32UUID());
		pd.put("AGR_SEL_STATE", "0");
		jybAppUserMobileMyActiveMapper.registrationThisActivity(pd);
		
		return WebResult.requestSuccess();
		
	}
	
	
	//判断此成员是否以其他的途径参加过此项目
	@RequestMapping("/checkThisMemberIsJoinedThisActivity")
	@ResponseBody
	@Transactional		
	public PageData checkThisMemberIsJoinedThisActivity(@RequestBody PageData pd) throws Exception{
	
		if(StringUtils.isBlank(pd.getString("A_ID"))||
				StringUtils.isBlank(pd.getString("UI_ID"))
				){
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		
		PageData pdRes = jybAppUserMobileMyActiveMapper.checkThisMemberIsJoinedThisActivity(pd);
		
		return WebResult.requestSuccess(pdRes);
		
	}
	
	//拉好友加入活动
	@RequestMapping("/lahaoyoujiaruhuodong")
	@ResponseBody
	@Transactional		
	public PageData lahaoyoujiaruhuodong(@RequestBody PageData pd) throws Exception{
	
		if(StringUtils.isBlank(pd.getString("A_ID"))||
				StringUtils.isBlank(pd.getString("UI_ID"))||
				StringUtils.isBlank(pd.getString("G_ID"))
				){
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		pd.put("AGUR_ID", UuidUtil.get32UUID());
		jybAppUserMobileMyActiveMapper.lahaoyoujiaruhuodong(pd);
		
		return WebResult.requestSuccess();
		
	}
	
	//个人报名活动AUR_ID,A_ID,UI_ID,AUR_SEL_STATE,URGENT_NAME,URGENT_PHONE,REMARK
	@RequestMapping("/personRegistrationThisActivity")
	@ResponseBody
	@Transactional		
	public PageData personRegistrationThisActivity(@RequestBody PageData pd) throws Exception{
	
		if(StringUtils.isBlank(pd.getString("A_ID"))||
				StringUtils.isBlank(pd.getString("UI_ID"))||
				StringUtils.isBlank(pd.getString("URGENT_NAME"))||
				StringUtils.isBlank(pd.getString("URGENT_PHONE"))||
				StringUtils.isBlank(pd.getString("REMARK"))
				){
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		
		pd.put("AUR_ID", UuidUtil.get32UUID());
		pd.put("AUR_SEL_STATE", "0");
		jybAppUserMobileMyActiveMapper.personRegistrationThisActivity(pd);
		
		return WebResult.requestSuccess();
		
	}
	
	//团队活动的报名参赛信息查询
	@RequestMapping("/checkInviteTeamActivityInfo")
	@ResponseBody
	@Transactional		
	public PageData checkInviteTeamActivityInfo(@RequestBody PageData pd) throws Exception{
	
		if(StringUtils.isBlank(pd.getString("A_ID"))||
				StringUtils.isBlank(pd.getString("G_ID"))
				){
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		
		PageData pdResult = jybAppUserMobileMyActiveMapper.checkInviteTeamActivityInfo(pd);
		List<PageData> pdVideoList = jybAppUserMobileMyActiveMapper.checkTeamAndPersonalVideosInfo(pd);
		pdResult.put("videoList", pdVideoList);
		return WebResult.requestSuccess(pdResult);
		
	}
	
	
	
	//个人活动的报名参赛信息查询
	@RequestMapping("/checkInvitePersonalActivityInfo")
	@ResponseBody
	@Transactional		
	public PageData checkInvitePersonalActivityInfo(@RequestBody PageData pd) throws Exception{
	
		if(StringUtils.isBlank(pd.getString("A_ID"))||
				StringUtils.isBlank(pd.getString("UI_ID"))
				){
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		
		PageData pdResult = jybAppUserMobileMyActiveMapper.checkInvitePersonalActivityInfo(pd);
		List<PageData> pdVideoList = jybAppUserMobileMyActiveMapper.checkTeamAndPersonalVideosInfo(pd);
		pdResult.put("videoList", pdVideoList);
		return WebResult.requestSuccess(pdResult);
		
	}
	
}
