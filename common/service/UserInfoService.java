package com.maryun.common.service;

import com.maryun.mapper.app.common.AppUserAuthMapper;
import com.maryun.model.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/*
**
* 类名称：UserInfoService 创建人：MARYUN 创建时间：2016年2月13日
*
* @version
*/
@Component("userInfoService")
public class UserInfoService {
	@Value("${system.imageServer.uploads.basePath}")
	private String imageServer;

	@Autowired
	AppUserAuthMapper appUserAuthMapper;
	/**
	 * 根据系统用户ID获取用户信息、用户家庭信息、用户家庭成员信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData getUserInfoById( PageData pd) throws Exception {
		pd.put("IMAGEPATH",imageServer);
		PageData userInfo = appUserAuthMapper.getUserInfoByUIID(pd);
//		List<PageData> familyList = appUserAuthMapper.getFamilyList(pd);
//		List<PageData> familyMemberList = appUserAuthMapper.getFamilyMemberList(pd);
//		List<PageData> nfamilyList = new ArrayList<PageData>();
//		//遍历家庭列表
//		if(familyList!=null&&familyList.size()>0){
//			for(int i = 0 ;i<familyList.size();i++){
//				PageData family = familyList.get(i);
//				List<PageData> nfamilyMemberList = new ArrayList<PageData>();
//				//遍历家庭成员列表
//				if(familyList!=null&&familyList.size()>0){
//					for(int j = 0 ;j<familyMemberList.size();j++){
//						PageData member = familyMemberList.get(j);
//						if(family.getString("F_ID").equals(member.getString("F_ID"))){
//							member.remove("F_ID");
//							nfamilyMemberList.add(member);
//						}
//					}
//				}
//				family.put("f_member", nfamilyMemberList);
//				nfamilyList.add(family);
//			}
//		}
		PageData familyList = appUserAuthMapper.getFamilyByMAC(pd);
		List<PageData> familyMemberList = appUserAuthMapper.getFamilyMemberListByMAC(pd);
		familyList.put("f_member", familyMemberList);
		PageData returnPd = new PageData();
		returnPd.put("user_info", userInfo);
		returnPd.put("family_info", familyList);
		return returnPd;
	}



	/**
	 * 根据系统用户ID获取用户信息、用户家庭信息、用户家庭成员信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData getUserInfoFamily( PageData pd) throws Exception {
		pd.put("IMAGEPATH",imageServer);
		PageData userInfo = appUserAuthMapper.getUserInfoByUIID(pd);
		List<PageData> familyList = appUserAuthMapper.getFamilyList(pd);
		List<PageData> familyMemberList = appUserAuthMapper.getFamilyMemberList(pd);
		List<PageData> nfamilyList = new ArrayList<PageData>();
		//遍历家庭列表
		if(familyList!=null&&familyList.size()>0){
			for(int i = 0 ;i<familyList.size();i++){
				PageData family = familyList.get(i);
				List<PageData> nfamilyMemberList = new ArrayList<PageData>();
				//遍历家庭成员列表
				if(familyList!=null&&familyList.size()>0){
					for(int j = 0 ;j<familyMemberList.size();j++){
						PageData member = familyMemberList.get(j);
						if(family.getString("F_ID").equals(member.getString("F_ID"))){
							member.remove("F_ID");
							nfamilyMemberList.add(member);
						}
					}
				}
				family.put("f_member", nfamilyMemberList);
				nfamilyList.add(family);
			}
		}
		PageData returnPd = new PageData();
		returnPd.put("user_info", userInfo);
		returnPd.put("family_info", familyList);
		return returnPd;
	}

}
