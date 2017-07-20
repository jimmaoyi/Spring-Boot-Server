package com.maryun.restful.app.user.userinfo;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.maryun.mapper.app.user.userinfo.AppUserInfoMapper;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.ChineseToEnglish;
import com.maryun.utils.UuidUtil;
import com.maryun.utils.WebResult;

/**
 * 类名称：AppUserInfoRestful 创建人：MARYUN 创建时间：2016年2月13日
 * 
 * @version
 */
@RestController
@RequestMapping(value = "/app/user/info")
public class AppUserInfoRestful extends BaseRestful {
	@Autowired
	AppUserInfoMapper appUserInfoMapper;

	/**
	 * 用户信息
	 * @return
	 */
	@RequestMapping(value = "/get")
	@ResponseBody
	public PageData getUserInfo(@RequestBody PageData pd) throws Exception {
		PageData accInfo = appUserInfoMapper.getUserInfo(pd);
		return WebResult.requestSuccess(accInfo);
	}

	/**
	 * 用户信息
	 * @return
	 */
	@RequestMapping(value = "/add")
	@ResponseBody
	public PageData addUserInfo(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("UI_PHONE")) || StringUtils.isBlank(pd.getString("UI_NAME")) || StringUtils.isBlank(pd.getString("PUSH_CODE"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			pd.put("USER_ID", UuidUtil.get32UUID());
			pd.put("PASSWORD", "223ce7b851123353479d85757fbbf4e320d1e251");
			appUserInfoMapper.addUserInfo(pd);
			appUserInfoMapper.addSysUserInfo(pd);
			return WebResult.requestSuccess();
		}
	}

	/**
	 * 更改用户信息
	 * @return
	 */
	@RequestMapping(value = "/update")
	@ResponseBody
	public PageData updateUserInfo(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("UI_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			// 处理姓名
			String sUI_NAME = pd.getString("UI_NAME");
			String sUI_NAME_LETTER = ChineseToEnglish.getPingYin(sUI_NAME);
			pd.put("UI_NAME_LETTER", sUI_NAME_LETTER);
			appUserInfoMapper.updateUserInfo(pd);
			return WebResult.requestSuccess();
		}
	}

	/**
	 * 更新用户设备号信息
	 * @return
	 */
	@RequestMapping(value = "/uppushCode")
	@ResponseBody
	public PageData updatePushCode(@RequestBody PageData pd) throws Exception {
		PageData result = new PageData();
		if (StringUtils.isBlank(pd.getString("UI_ID")) || StringUtils.isBlank(pd.getString("PUSH_CODE"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			appUserInfoMapper.updateUserPushCode(pd);
			return WebResult.requestSuccess();
		}
	}

	/**
	 * 更新用户位置信息
	 * @return
	 */
	@RequestMapping(value = "/updateLoc")
	@ResponseBody
	public PageData updateLoc(@RequestBody PageData pd) throws Exception {
		if (pd.getString("UI_ID") != null) {
			List<PageData> list = appUserInfoMapper.getUserLoc(pd);// 查询用户位置信息是否存在
			if (list.size() > 0) {
				appUserInfoMapper.updateUserLoc(pd); // 更新用户位置
			}
			else {
				pd.put("UL_ID", UuidUtil.get32UUID());
				appUserInfoMapper.addUserLoc(pd); // 增加用户位置
			}

		}
		return WebResult.requestSuccess();
	}

	/**
	 * 根据电话号码来查询用户
	 * @return
	 */
	@RequestMapping(value = "/getUserByPhone")
	@ResponseBody
	public PageData getUserByPhone(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("UI_PHONE"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			PageData accinfo = appUserInfoMapper.getUserByPhone(pd);
			return WebResult.requestSuccess(accinfo);
		}
	}
}
