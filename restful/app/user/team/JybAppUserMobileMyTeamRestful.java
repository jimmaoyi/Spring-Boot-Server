package com.maryun.restful.app.user.team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.maryun.common.service.PushServiceForCall;
import com.maryun.mapper.app.user.team.JybAppUserMobileMyTeamMapper;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.UuidUtil;
import com.maryun.utils.WebResult;

@RestController
@RequestMapping(value = "/app/user/team")
public class JybAppUserMobileMyTeamRestful extends BaseRestful {
	
	@Autowired
	private JybAppUserMobileMyTeamMapper jybAppUserMobileMyTeamMapper;
	
	@Autowired
	PushServiceForCall pushServiceForCall;
	
	//checkTeamInfoByGroupId
	@RequestMapping("/checkTeamInfoByGroupId")
	@ResponseBody
	@Transactional		
	public PageData checkTeamInfoByGroupId(@RequestBody PageData pd) throws Exception{
		if(StringUtils.isBlank(pd.getString("G_ID"))){
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		
		PageData pgTeam = jybAppUserMobileMyTeamMapper.checkTeamInfoByGroupId(pd);
		
		return WebResult.requestSuccess(pgTeam);
	}
	
	
	//用户创建团队 所需参数：G_NAME 团队名称，G_TEAM_LEADER:创建人id
	@RequestMapping("/createTeam")
	@ResponseBody
	@Transactional		
	public PageData createTeam(@RequestBody PageData pd) throws Exception{
		
		if(StringUtils.isBlank(pd.getString("G_NAME"))||StringUtils.isBlank(pd.getString("UI_ID"))
				){
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		String G_ID = UuidUtil.get32UUID();
		pd.put("G_ID", G_ID);		
		pd.put("G_TEAM_LEADER", pd.getString("UI_ID"));
		jybAppUserMobileMyTeamMapper.createTeam(pd);
		//将创建团队的人加入
		PageData ter = new PageData();
		ter.put("AGUR_ID", UuidUtil.get32UUID());
		ter.put("G_ID", G_ID);
		ter.put("UI_ID", pd.getString("UI_ID"));
		ter.put("AGUR_STATE", "5");
		jybAppUserMobileMyTeamMapper.inviteTeamerAndAddTeam(ter);
		return WebResult.requestSuccess(G_ID);
	}
	
	//我的团队查询 根据我的UI_ID查询我的团队  参数：UI_ID
	@RequestMapping("/checkMyTeams")
	@ResponseBody
	@Transactional		
	public PageData checkMyTeams(@RequestBody PageData pd) throws Exception{
		
		if(StringUtils.isBlank(pd.getString("UI_ID"))){
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		
		List<PageData> pgTeams = jybAppUserMobileMyTeamMapper.checkMyTeams(pd);
		
		return WebResult.requestSuccess(pgTeams);
	}
	
	
	
	//所有团队查询 参数UI_ID 用户id
	@RequestMapping("/checkTeams")
	@ResponseBody
	@Transactional		
	public PageData checkTeams(@RequestBody PageData pd) throws Exception{
		if(StringUtils.isBlank(pd.getString("UI_ID"))){
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		
		List<PageData> pgTeams = jybAppUserMobileMyTeamMapper.checkTeams(pd);
		
		return WebResult.requestSuccess(pgTeams);
	}
	
	//查询我的团队的所有列表，所有类型的列表 根据我的UI_ID 参数：UI_ID
	@RequestMapping("/checkAllStatusTeams")
	@ResponseBody
	@Transactional		
	public PageData checkAllStatusTeams(@RequestBody PageData pd) throws Exception{
		if(StringUtils.isBlank(pd.getString("UI_ID"))){
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		List<PageData> pgTeams = jybAppUserMobileMyTeamMapper.checkAllStatusTeams(pd);
		
		return WebResult.requestSuccess(pgTeams);
	}
	
	//根据我的团队G_ID查询该团队下的所有人  参数：G_ID 状态为通过和队长邀请的状态
	@RequestMapping("/checkAllTeamersByGID")
	@ResponseBody
	@Transactional		
	public PageData checkAllTeamersByGID(@RequestBody PageData pd) throws Exception{
		if(StringUtils.isBlank(pd.getString("G_ID"))){
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		List<PageData> pgTeams = jybAppUserMobileMyTeamMapper.checkAllTeamersByGID(pd);
		
		return WebResult.requestSuccess(pgTeams);
	}
	
	//队长邀请成员，或者成员选择团队参数：AGUR_ID,G_ID,UI_ID,AGUR_STATE
	@RequestMapping("/inviteTeamerAndAddTeam")
	@ResponseBody
	@Transactional		
	public PageData inviteTeamerAndAddTeam(@RequestBody PageData pd) throws Exception{
		if(StringUtils.isBlank(pd.getString("G_ID"))||StringUtils.isBlank(pd.getString("UI_ID"))
				||StringUtils.isBlank(pd.getString("AGUR_STATE"))){
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		String AGUR_ID = UuidUtil.get32UUID();
		pd.put("AGUR_ID", AGUR_ID);
		
		//再插入该数据时，因为有可能用户多次申请加入该团队，或者队长多次邀请成员
		//所以当出现多次邀请，或多次插入的时候，如果数据库没有当前插入信息，就插入
		PageData pdInfo = jybAppUserMobileMyTeamMapper.checkIsThisInviteTeamerOrAddTeamInfo(pd);
		if(pdInfo == null){
			jybAppUserMobileMyTeamMapper.inviteTeamerAndAddTeam(pd);
			
			//队长邀请团队成员的申请
			if("0".equals(pd.getString("AGUR_STATE"))){
				
				//推送给团员
				
				//推送时带的内容
				Map<String,String> extra = new HashMap<String,String>();
				Map<String,Object> extraContent = new HashMap<String,Object>();
				extraContent.put("G_ID", pd.getString("G_ID"));
				extraContent.put("AGUR_ID", AGUR_ID);
				extra.put("KEY_PARAM", JSON.toJSONString(extraContent));
				
				//推送接受者的id
				List<String> userList = new ArrayList<String>();
				userList.add(pd.getString("UI_ID"));
				
				//推送类型，如用户退给代理，这里就是3表示推给代理
				pushServiceForCall.setAPPKEYANDSecret("1");
				pushServiceForCall.alertTitleMsgExtraCallback(pd.getString("G_NAME")+"团队邀请您加入", "您有一条团队邀请信息", extra, userList, "302",
						pd.getString("USER_ID"), null, "1");
				
			}
			
		}
		
		return WebResult.requestSuccess(AGUR_ID);
	}
	
	//修改邀请，被邀请，拒绝时的状态   参数：关系表状态：AGUR_STATE 关系表id： AGUR_ID
	//消息中心中，有团队邀请时，接受，拒绝状态改变，当有团员申请时，队长接受，拒绝状态的改变
	@RequestMapping("/receiveAndRefuseAGURSTATE")
	@ResponseBody
	@Transactional		
	public PageData receiveAndRefuseAGURSTATE(@RequestBody PageData pd) throws Exception{
		if(StringUtils.isBlank(pd.getString("AGUR_STATE"))||StringUtils.isBlank(pd.getString("AGUR_ID"))
				){
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		
		jybAppUserMobileMyTeamMapper.receiveAndRefuseAGURSTATE(pd);
		
		//确定加入团队
		if("4".equals(pd.getString("AGUR_STATE"))){
			//推送时带的内容
			String title = "接受了您的邀请";
			String alertTitle = "接受了您的邀请";
			receiveAndRefuseInvite(pd,title,alertTitle);
		}//拒绝加入团队
		else if("2".equals(pd.getString("AGUR_STATE"))){
			String title = "拒绝了您的邀请";
			String alertTitle = "拒绝了您的邀请";
			receiveAndRefuseInvite(pd,title,alertTitle);
		}
		
		return WebResult.requestSuccess();
	}
	
	private void receiveAndRefuseInvite(PageData pd,String title,String alertTitle) throws Exception{
		Map<String,String> extra = new HashMap<String,String>();
		Map<String,Object> extraContent = new HashMap<String,Object>();
		extraContent.put("G_ID", pd.getString("G_ID"));
		extra.put("KEY_PARAM",JSON.toJSONString(extraContent));
		
		//推送接受者的id
		List<String> userList = new ArrayList<String>();
		userList.add(pd.getString("PUSHER"));
		
		pushServiceForCall.setAPPKEYANDSecret("1");
		pushServiceForCall.alertTitleMsgExtraCallback(pd.getString("UI_NAME")+title,pd.getString("UI_NAME")+alertTitle, extra, userList, "302",
				pd.getString("UI_ID"), null, "1");
	}
	
	
	
	//队长删除成员  参数：AGUR_ID 表id
	@RequestMapping("/deleteTeamer")
	@ResponseBody
	@Transactional		
	public PageData deleteTeamer(@RequestBody PageData pd) throws Exception{
		if(StringUtils.isBlank(pd.getString("AGUR_ID"))){
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		
		jybAppUserMobileMyTeamMapper.deleteTeamer(pd);
		
		return WebResult.requestSuccess();
	}
	
	//查询团队所报名的活动，根据G_ID
	@RequestMapping("/checkTeamActives")
	@ResponseBody
	@Transactional		
	public PageData checkTeamActives(@RequestBody PageData pd) throws Exception{
		if(StringUtils.isBlank(pd.getString("G_ID"))){
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		
		List<PageData> pdTeamActivies = jybAppUserMobileMyTeamMapper.checkTeamActives(pd);
		
		return WebResult.requestSuccess(pdTeamActivies);
	}
	
	//申请退团
	@RequestMapping("/leagueMember")
	@ResponseBody
	@Transactional		
	public PageData leagueMember(@RequestBody PageData pd) throws Exception{
		if(StringUtils.isBlank(pd.getString("G_ID"))||StringUtils.isBlank(pd.getString("UI_ID"))){
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		
		jybAppUserMobileMyTeamMapper.leagueMember(pd);
		
		return WebResult.requestSuccess();
	}
	
	
	//根据用户id和团队id查询用户和团队之间的关系，即：该用户有没有加入该团队，加入状态 
	@RequestMapping("/joinTeamState")
	@ResponseBody
	@Transactional		
	public PageData joinTeamState(@RequestBody PageData pd) throws Exception{
		if(StringUtils.isBlank(pd.getString("G_ID"))||StringUtils.isBlank(pd.getString("UI_ID"))){
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		
		PageData pdRes = jybAppUserMobileMyTeamMapper.joinTeamState(pd);
		
		return WebResult.requestSuccess(pdRes);
	}
	
	//解散团队
	@RequestMapping("/disbandTeam")
	@ResponseBody
	@Transactional		
	public PageData disbandTeam(@RequestBody PageData pd) throws Exception{
		if(StringUtils.isBlank(pd.getString("G_ID"))){
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		
		jybAppUserMobileMyTeamMapper.disbandTeam(pd);
		
		return WebResult.requestSuccess();
	}
}	
