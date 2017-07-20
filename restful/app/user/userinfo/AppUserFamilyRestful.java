package com.maryun.restful.app.user.userinfo;

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
import com.maryun.common.service.PushServiceForCall;
import com.maryun.mapper.app.user.userinfo.AppUserFamilyMapper;
import com.maryun.mapper.app.user.userinfo.AppUserInfoMapper;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.UuidUtil;
import com.maryun.utils.WebResult;

/**
 * 类名称：AppUserInfoRestful 创建人：MARYUN 创建时间：2016年2月13日
 * 
 * @version
 */
@RestController
@RequestMapping(value = "/app/user/family")
public class AppUserFamilyRestful extends BaseRestful {
	@Autowired
	AppUserFamilyMapper appUserFamilyMapper;

	@Autowired
	PushServiceForCall pushServiceForCall;

	@Autowired
	AppUserInfoMapper appUserInfoMapper;

	/**
	 * 创建家庭成员
	 * @return
	 */
	@RequestMapping(value = "/member/add")
	@ResponseBody
	public PageData addFamilyMember(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("F_ID")) || StringUtils.isBlank(pd.getString("UI_ID")) || StringUtils.isBlank(pd.getString("SYS_UI_ID"))
				|| StringUtils.isBlank(pd.getString("IS_MAIN"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			String FM_ID = UuidUtil.get32UUID();
			pd.put("FM_ID", FM_ID);
			// 重新发送邀请，不需要重新向家庭表中添加一条记录,pd.F_ID pd.UI_ID
			// 根据F_ID,UI_ID查询家庭成员表中是否还有这条记录
			PageData pd_param = new PageData();
			pd_param.put("F_ID", pd.get("F_ID"));
			pd_param.put("UI_ID", pd.get("UI_ID"));
			PageData accinfo = appUserFamilyMapper.getFamilyMemberByFIDAndUID(pd_param);
			if (accinfo == null) {
				appUserFamilyMapper.addFamilyMember(pd);
			}
			else {
				FM_ID = accinfo.getString("FM_ID");
			}

			Map<String, String> extra = new HashMap<String, String>();
			Map<String, String> KEY_PARAM = new HashMap<String, String>();
			KEY_PARAM.put("UI_ID_request", pd_param.getString("UI_ID"));// 请求加入家庭的用户ID
			// extra.put("UI_ID_request", pd_param.getString("UI_ID"));//
			// 请求加入家庭的用户ID

			// 添加成功后进行推送.
			List<String> userList = new ArrayList<String>();
			userList.add(pd.getString("UI_ID"));
			// 根据用户ID得到用户姓名
			pd.put("UI_ID", pd.getString("MAIN_UI_ID"));
			PageData user_info = appUserInfoMapper.getUserInfo(pd);
			KEY_PARAM.put("UI_ID_MAIN", pd.getString("UI_ID"));// 家庭主账号的用户ID

			extra.put("KEY_PARAM", JSON.toJSONString(KEY_PARAM));

			pushServiceForCall.setAPPKEYANDSecret("1");
			pushServiceForCall.alertTitleMsgExtraCallback("老伴儿", user_info.getString("UI_NAME") + "邀请你加入他所创建的家庭,是否同意？", extra, userList, "201",
					pd.getString("SYS_UI_ID"), FM_ID, "1");
			return WebResult.requestSuccess();
		}
	}

	/**
	 * 创建家庭成员直接加入
	 * @return
	 */
	@RequestMapping(value = "/member/add/yet")
	@ResponseBody
	public PageData addFamilyMemberYet(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("F_ID")) || StringUtils.isBlank(pd.getString("UI_ID")) || StringUtils.isBlank(pd.getString("SYS_UI_ID"))
				|| StringUtils.isBlank(pd.getString("IS_MAIN"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			PageData member = appUserFamilyMapper.getFamilyMemberInfo(pd);
			if (null == member) {
				String FM_ID = UuidUtil.get32UUID();
				pd.put("FM_ID", FM_ID);
				appUserFamilyMapper.addFamilyMember(pd);
			}
			return WebResult.requestSuccess();
		}
	}

	/**
	 * 取消家庭成员 取消家庭成员,如果删除的是主账号，则把家庭解散.
	 * @return
	 */
	@RequestMapping(value = "/member/del")
	@ResponseBody
	public PageData delFamilyMember(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("F_ID")) || StringUtils.isBlank(pd.getString("UI_ID")) || StringUtils.isBlank(pd.getString("IS_MAIN"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			String is_main = pd.getString("IS_MAIN");

			// 用户信息
			PageData user = appUserInfoMapper.getUserInfo(pd);
			// 获取家庭信息
			PageData familyInfo = appUserFamilyMapper.getFamilyByID(pd);
			// 获取此家庭中的所有成员ID
			List<PageData> familyMembers = appUserFamilyMapper.getFamilyMemberList(pd);
			// 根据家庭ID和用户ID判断此用户是否已经确认通过加入了此家庭
			PageData pd_FMState = appUserFamilyMapper.getFamilyMemberByFIDAndUID(pd);
			// 找到此家庭中的主账号成员
			String mainUID = "";
			for (PageData pd_member : familyMembers) {
				if ("1".equals(pd_member.getString("IS_MAIN"))) {
					// 获取主账号的用户ID
					mainUID = pd_member.getString("UI_ID");
				}
			}

			// 主账号删除
			if ("1".equals(is_main)) {
				// 这里必须把此家庭内的所有成员都推送一条消息,通知所有家庭成员此家庭已经解散
				List<String> userList = new ArrayList<String>();
				// 推送给哪些人
				for (PageData pd_member : familyMembers) {
					// 除了主账号的，这个家庭内的成员全部推送
					if (!(pd_member.getString("UI_ID").equals(pd.getString("UI_ID")))) {
						// 只推送FM_STATE为1的，即家庭内已经确定加入此家庭的用户
						if ("1".equals(pd_member.getString("FM_STATE"))) {
							userList.add(pd_member.getString("UI_ID"));
						}
					}
				}
				Map<String, String> extra = new HashMap<String, String>();

				pushServiceForCall.setAPPKEYANDSecret("1");// 推送给用户
				pushServiceForCall.alertTitleMsgExtraCallback("老伴儿", user.getString("UI_NAME") + "已把【" + familyInfo.getString("F_NAME") + "】家庭解散！你已被强制退出此家庭！",
						extra, userList, "0", pd.getString("UI_ID"), pd.getString("F_ID"), "1");

				// 删除家庭
				appUserFamilyMapper.delFamily(pd);
			}

			// 在这里进行判断是自己退出还是被主账号用户强制退出
			if ("1".equals(pd.getString("pushFlag"))) {
				// 获取主账号用户的信息
				/*
				 * PageData pd_mainUser=new PageData(); pd_mainUser.put("UI_ID",
				 * mainUID); PageData user_main =
				 * appUserInfoMapper.getUserInfo(pd_mainUser);
				 */
				// 自己退出的,需要推送给家庭主账号告知谁已退出
				List<String> userList = new ArrayList<String>();
				userList.add(mainUID);

				Map<String, String> extra = new HashMap<String, String>();

				pushServiceForCall.setAPPKEYANDSecret("1");// 推送给用户
				pushServiceForCall.alertTitleMsgExtraCallback("老伴儿", user.getString("UI_NAME") + "已主动从【" + familyInfo.getString("F_NAME") + "】家庭中退出！", extra,
						userList, "0", pd.getString("UI_ID"), pd.getString("F_ID"), "1");

			}
			else if ("0".equals(pd.getString("pushFlag")) && "1".equals(pd_FMState.getString("FM_STATE"))) {
				// 获取主账号用户的信息
				PageData pd_mainUser = new PageData();
				pd_mainUser.put("UI_ID", mainUID);
				PageData user_main = appUserInfoMapper.getUserInfo(pd_mainUser);
				// 被主账号强制退出的,需要告知被删除的用户，你已被主账号用户强制请出，，，必须是已经确定加入的家庭成员才能推送！！
				List<String> userList = new ArrayList<String>();
				userList.add(pd.getString("UI_ID"));

				Map<String, String> extra = new HashMap<String, String>();

				pushServiceForCall.setAPPKEYANDSecret("1");// 推送给用户
				pushServiceForCall.alertTitleMsgExtraCallback("老伴儿", user_main.getString("UI_NAME") + "已强制把你从【" + familyInfo.getString("F_NAME") + "】家庭中请出！",
						extra, userList, "0", pd.getString("UI_ID"), pd.getString("F_ID"), "1");
			}

			// 删除家庭成员
			appUserFamilyMapper.delFamilyMember(pd);
			return WebResult.requestSuccess();
		}
	}

	/**
	 * 创建家庭
	 * @return
	 */
	@RequestMapping(value = "/add")
	@ResponseBody
	public PageData addFamily(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("F_NAME")) || StringUtils.isBlank(pd.getString("F_MAC")) || StringUtils.isBlank(pd.getString("F_ACCOUNT"))
				|| StringUtils.isBlank(pd.getString("F_TYPE")) || StringUtils.isBlank(pd.getString("SYS_UI_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			PageData family = appUserFamilyMapper.getFamilyInfo(pd);
			if (null == family) {
				String F_ID = UuidUtil.get32UUID();
				pd.put("F_ID", F_ID);
				appUserFamilyMapper.addFamily(pd);
			}
			else {
				pd.put("F_ID", family.get("F_ID"));
			}
			PageData member = appUserFamilyMapper.getFamilyMemberInfo(pd);
			if (null == member) {
				String FM_ID = UuidUtil.get32UUID();
				pd.put("FM_ID", FM_ID);
				pd.put("IS_MAIN", "1");
				pd.put("FM_STATE", "1");
				appUserFamilyMapper.addFamilyMember(pd);
			}
			PageData info = appUserFamilyMapper.getFamilyInfo(pd);
			return WebResult.requestSuccess(info);
		}
	}

	/**
	 * 家庭详细\根据家庭ID或MAC地址
	 * @return
	 */
	@RequestMapping(value = "/info/detail")
	@ResponseBody
	public PageData getFamilyDetail(@RequestBody PageData pd) throws Exception {
		PageData list = appUserFamilyMapper.getFamilyInfo(pd);
		return WebResult.requestSuccess(list);
	}

	/**
	 * 家庭列表
	 * @return
	 */
	@RequestMapping(value = "/info/list")
	@ResponseBody
	public PageData getFamilyList(@RequestBody PageData pd) throws Exception {
		List<PageData> list = appUserFamilyMapper.getFamilyList(pd);
		return WebResult.requestSuccess(list);
	}

	/**
	 * 家庭成员列表
	 * @return
	 */
	@RequestMapping(value = "/member/list")
	@ResponseBody
	public PageData getFamilyMemberList(@RequestBody PageData pd) throws Exception {
		PageData result = new PageData();
		// if (StringUtils.isBlank(pd.getString("F_ID")) ||
		// StringUtils.isBlank(pd.getString("IS_MAIN"))) {
		// return WebResult.requestFailed(10001, "参数缺失！", null);
		// }
		// else {
		List<PageData> list = appUserFamilyMapper.getFamilyMemberList(pd);
		return WebResult.requestSuccess(list);
		// }
	}

	/**
	 * 获取用户是否是家庭主账号
	 * @return
	 */
	@RequestMapping(value = "/member/getISMAIN")
	@ResponseBody
	public PageData getISMAIN(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("UI_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			PageData accinfo = appUserFamilyMapper.getISMAIN(pd);
			return WebResult.requestSuccess(accinfo);
		}
	}

	/**
	 * 判断用户是否已在所属家庭中
	 * @return
	 */
	@RequestMapping(value = "/member/getFamilyUserInfo")
	@ResponseBody
	public PageData getFamilyUserInfo(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("UI_PHONE")) || StringUtils.isBlank(pd.getString("F_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			PageData accinfo = appUserFamilyMapper.getFamilyUserInfo(pd);
			// boolean flag = true;
			// if (accinfo != null) {
			// flag = false;// 用户已存在，无需重复添加
			// }
			return WebResult.requestSuccess(accinfo);
		}
	}

	/**
	 * 判断用户是否已在所属家庭中并返回家庭用户关系对象
	 * @return
	 */
	@RequestMapping(value = "/member/info")
	@ResponseBody
	public PageData getFamilyMemberInfo(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("UI_ID")) || StringUtils.isBlank(pd.getString("F_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			PageData accinfo = appUserFamilyMapper.getFamilyMemberInfo(pd);
			return WebResult.requestSuccess(accinfo);
		}
	}

	/**
	 * 根据用户id从家庭成员表中获取家庭成员ID
	 * @return
	 */
	@RequestMapping(value = "/member/getFMIDByUID")
	@ResponseBody
	public PageData getFMIDByUID(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("UI_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			PageData accinfo = appUserFamilyMapper.getFMIDByUID(pd);
			return WebResult.requestSuccess(accinfo);
		}
	}

	/**
	 * 如果不是主账号，想从家庭中离开，只能自己离开，无法控制别的
	 * @return
	 */
	@RequestMapping(value = "/member/delSelfAndISMAIN")
	@ResponseBody
	public PageData delSelfAndISMAIN(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("UI_ID")) || StringUtils.isBlank(pd.getString("F_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			PageData accinfo = appUserFamilyMapper.delSelfAndISMAIN(pd);
			// boolean editFlag = false;
			// if (accinfo != null) {
			// editFlag = true;
			// }
			return WebResult.requestSuccess(accinfo);
		}
	}

	/**
	 * 根据家庭用户ID来查询家庭用户信息
	 * @return
	 */
	@RequestMapping(value = "/member/getFamilyMemberByID")
	@ResponseBody
	public PageData getFamilyMemberByID(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("FM_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			PageData accinfo = appUserFamilyMapper.getFamilyByID(pd);
			return WebResult.requestSuccess(accinfo);
		}
	}

	/**
	 * 根据家庭ID来查询家庭信息
	 * @return
	 */
	@RequestMapping(value = "/getFamilyByID")
	@ResponseBody
	public PageData getFamilyByID(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("F_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			PageData accinfo = appUserFamilyMapper.getFamilyByID(pd);
			return WebResult.requestSuccess(accinfo);
		}
	}

	/**
	 * 根据家庭成员ID来获取家庭成员和家庭信息
	 * @return
	 */
	@RequestMapping(value = "/getFamilyAndFamilyMemberByFMID")
	@ResponseBody
	public PageData getFamilyAndFamilyMemberByFMID(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("FM_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			PageData accinfo = appUserFamilyMapper.getFamilyAndFamilyMemberByFMID(pd);
			return WebResult.requestSuccess(accinfo);
		}
	}

	/**
	 * 根据家庭成员ID来获取家庭成员和家庭信息,,家庭成员删除了也能查找这个成员原来所属的家庭
	 * @return
	 */
	@RequestMapping(value = "/getFamilyAndFamilyMemberByFMIDAll")
	@ResponseBody
	public PageData getFamilyAndFamilyMemberByFMIDAll(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("FM_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			PageData accinfo = appUserFamilyMapper.getFamilyAndFamilyMemberByFMIDAll(pd);
			return WebResult.requestSuccess(accinfo);
		}
	}

	/**
	 * 根据页面邀请状态来更新数据
	 * @return
	 */
	@RequestMapping(value = "/member/updateFamilyMemberFM_STATE")
	@ResponseBody
	public PageData updateFamilyMemberFM_STATE(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("FM_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			appUserFamilyMapper.updateFamilyMemberFM_STATE(pd);

			// 在这里还必须把是否接受加入家庭的消息推送给主账户
			// 首先得到2个用户的信息
			PageData request = new PageData();
			request.put("UI_ID", pd.getString("UI_ID_request"));
			PageData user_info_request = appUserInfoMapper.getUserInfo(request);
			PageData mainUser = new PageData();
			mainUser.put("UI_ID", pd.getString("UI_ID_MAIN"));
			PageData user_info_mainUser = appUserInfoMapper.getUserInfo(mainUser);

			// 添加成功后进行推送.
			List<String> userList = new ArrayList<String>();
			userList.add(pd.getString("UI_ID_MAIN"));

			Map<String, String> extra = new HashMap<String, String>();
			// extra.put("UI_ID_MAIN", pd.getString("UI_ID"));// 家庭主账号的用户ID

			String result = "";
			if ("1".equals(pd.getString("FM_STATE"))) {
				result = "已经加入你的家庭！";
			}
			else {
				result = "拒绝加入你的家庭！";
			}

			pushServiceForCall.setAPPKEYANDSecret("1");
			pushServiceForCall.alertTitleMsgExtraCallback("老伴儿", user_info_request.getString("UI_NAME") + result, extra, userList, "0",
					pd.getString("UI_ID_request"), pd.getString("FM_ID"), "1");

			return WebResult.requestSuccess();
		}
	}

	/**
	 * 根据家庭成员ID来删除家庭成员
	 * @return
	 */
	@RequestMapping(value = "/member/delFamilyMemberByFMID")
	@ResponseBody
	public PageData delFamilyMemberByFMID(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("FM_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			appUserFamilyMapper.delFamilyMemberByFMID(pd);
			return WebResult.requestSuccess();
		}
	}

}
