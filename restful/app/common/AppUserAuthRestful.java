/**
 * 
 */
package com.maryun.restful.app.common;

import com.alibaba.fastjson.JSON;
import com.maryun.common.service.Qrcode;
import com.maryun.common.service.SendMsgService;
import com.maryun.common.service.UserInfoService;
import com.maryun.mapper.app.common.AppUserAuthMapper;
import com.maryun.mapper.app.user.userinfo.AppUserInfoMapper;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.*;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 类名称：MessageRestful 创建人：MARYUN 创建时间：2016年2月13日
 * 
 * @version
 */
@RestController
@RequestMapping(value = "/app/auth")
public class AppUserAuthRestful extends BaseRestful {
	@Autowired
	private AppUserAuthMapper userAuthMapper;

	@Autowired
	private SendMsgService sendMsgService;

	@Autowired
	private Qrcode qrcode;

	@Autowired
	private UserInfoService userInfoService;

	@Autowired
	AppUserInfoMapper appUserInfoMapper;

	@Value("${system.userlogin.token.pre}")
	private String tokenPre;

	@Value("${system.login.token.key}")
	private String tokenkey;

	@Value("${lbaner.userinfo.photo.male}")
	private String malephoto;

	@Value("${lbaner.userinfo.photo.female}")
	private String femalephoto;

	@Value("${system.file.uploads.basePath}")
	private String basePath;

	/**
	 * 消息列表（分页）
	 * @return
	 */
	@RequestMapping(value = "/userToken")
	@ResponseBody
	public PageData getUserToken(@RequestBody PageData pd) throws Exception {
		PageData result = new PageData();
		if (StringUtils.isBlank(pd.getString("userName")) || StringUtils.isBlank(pd.getString("pwd"))) {
			result.put("errorCode", "10001");
			result.put("errorMeg", "参数缺失！");
		}
		else {
			PageData userN = userAuthMapper.findUserByName(pd);
			if (null != userN) {
				PageData userNW = userAuthMapper.findUserByNamePwd(pd);
				if (null != userNW) {
					// 用户ID
					String userId = userNW.getString("USER_ID");
					String userToken = TockenUtil.createToken(userId, tokenkey);
					RedisTemplateUtils.set(tokenPre + userToken, userId);
					result.put("errorCode", "0");
					result.put("userToken", userToken);
					result.put("errorMeg", "登录成功");
				}
				else {
					result.put("errorCode", "200");
					result.put("errorMeg", "用户名密码错误");
					result.put("userToken", "null");
				}
			}
			else {
				result.put("errorCode", "100");
				result.put("errorMeg", "当前用户不存在");
				result.put("userToken", null);
			}
		}
		// 结果集封装
		return result;
	}

	/**
	 * 获取验证码
	 * @return
	 */
	@RequestMapping(value = "/verificationCode")
	@ResponseBody
	public PageData getVerificationCode(@RequestBody PageData pd) throws Exception {
		// 登陆：根据手机号判断当前账号是否存在，如果不存在则注册
		// 注册：根据手机号判断当前账号是否存在，如果存在则登录
		String sLoginType = pd.getString("loginType");
		String sLoginName = pd.getString("loginName");
		String sAppType = pd.getString("appType");// 根据登陆类型

		if (StringUtils.isBlank(sLoginType) || StringUtils.isBlank(sLoginName)) {
			return WebResult.requestFailed(400, "参数不正确", null);
		}
		else {
			PageData npd = new PageData();
			PageData userN = userAuthMapper.findUserByName(pd);
			// 登陆
			if ("login".equals(sLoginType)) {
				npd.put("product", "登录");
				if (null == userN) {
					return WebResult.requestFailed(403, "用户不存在，请先注册！", null);
				}
				else {
					// 根据apptype判断业务表中是否存在数据如果不存在则注册
					if ("1".equals(sAppType)||"7".equals(sAppType)) {// 用户APP
						npd.put("userId", userN.getString("USER_ID"));
						PageData user = userAuthMapper.getUserInfo(npd);
						if (null == user) {
							return WebResult.requestFailed(403, "用户不存在，请先注册！", null);
						}
					}
					else if ("2".equals(sAppType)) {// 陪诊APP
						// 根据apptype判断业务表中是否存在数据如果不存在则联系客服人员处理初始化账号
						npd.put("userId", userN.getString("USER_ID"));
						PageData user = userAuthMapper.getAccompanyInfo(npd);
						if (null == user) {
							return WebResult.requestFailed(403, "请联系客服人员处理，初始化账号！", null);
						}
					}
					else if ("3".equals(sAppType)) {// 机构APP
						// 根据apptype判断业务表中是否存在数据如果不存在则联系客服人员处理初始化账号
						npd.put("userId", userN.getString("USER_ID"));
						PageData user = userAuthMapper.getSPInfo(npd);
						if (null == user) {
							return WebResult.requestFailed(403, "请联系客服人员处理，初始化账号！", null);
						}
					}
					else if ("4".equals(sAppType)) {// 医生APP
						// 根据apptype判断业务表中是否存在数据如果不存在则联系客服人员处理初始化账号
						npd.put("userId", userN.getString("USER_ID"));
						PageData user = userAuthMapper.geDoctorInfo(npd);
						if (null == user) {
							return WebResult.requestFailed(403, "请联系客服人员处理，初始化账号！", null);
						}
					}
				}
			}
			else {// 注册
				npd.put("product", "注册");
				if (userN != null) {
					// return WebResult.requestFailed(404, "用户已存在，请直接登陆！",
					// null);
					if ("1".equals(sAppType)||"7".equals(sAppType)) {// 用户APP
						npd.put("userId", userN.getString("USER_ID"));
						PageData user = userAuthMapper.getUserInfo(npd);
						if (null != user) {
							return WebResult.requestFailed(403, "用户已存在，请直接登陆！", null);
						}
					}
					// 陪诊、医生、机构 不需要注册
					// else if ("2".equals(sAppType)){//陪诊APP
					// //根据apptype判断业务表中是否存在数据如果不存在则联系客服人员处理初始化账号
					// npd.put("userId", userN.getString("USER_ID"));
					// PageData user = userAuthMapper.getAccompanyInfo(npd);
					// if(null != user){
					// return WebResult.requestFailed(403, "用户已存在，请直接登陆！",
					// null);
					// }else{
					// return WebResult.requestFailed(493, "请联系客服人员处理，初始化账号！",
					// null);
					// }
					// }else if ("3".equals(sAppType)){//机构APP
					// //根据apptype判断业务表中是否存在数据如果不存在则联系客服人员处理初始化账号
					// npd.put("userId", userN.getString("USER_ID"));
					// PageData user = userAuthMapper.getSPInfo(npd);
					// if(null != user){
					// return WebResult.requestFailed(403, "用户已存在，请直接登陆！",
					// null);
					// }else{
					// return WebResult.requestFailed(493, "请联系客服人员处理，初始化账号！",
					// null);
					// }
					// }else if ("4".equals(sAppType)){//医生APP
					// //根据apptype判断业务表中是否存在数据如果存在则提示用户已存在，请直接登陆
					// npd.put("userId", userN.getString("USER_ID"));
					// PageData user = userAuthMapper.geDoctorInfo(npd);
					// if(null != user){
					// return WebResult.requestFailed(403, "用户已存在，请直接登陆！",
					// null);
					// }else{
					// return WebResult.requestFailed(493, "请联系客服人员处理，初始化账号！",
					// null);
					// }
					// }
				}
			}
			// 获取验证吗
			npd.put("type", "1");
			npd.put("recNum", sLoginName);
			PageData result = sendMsgService.sendMsg(npd);
			if ("200".equals(result.getString("state"))) {
				return WebResult.requestSuccess();
			}
			else {
				return result;
			}
		}
	}

	/**
	 * 验证验证码
	 * @return
	 */
	@RequestMapping(value = "/checkVCode")
	@ResponseBody
	public PageData checkVerificationCode(@RequestBody PageData pd) throws Exception {
		// 登陆：输入账号验证码
		String verificationCode = pd.getString("verificationCode");
		String sLoginName = pd.getString("loginName");
		if (StringUtils.isBlank(verificationCode) || StringUtils.isBlank(sLoginName)) {
			return WebResult.requestFailed(400, "参数缺失", null);
		}
		else {
			PageData vnpd = new PageData();
			vnpd.put("recNum", sLoginName);
			vnpd.put("msgCode", verificationCode);
			PageData returnD = sendMsgService.checkVerificationCode(vnpd);
			return WebResult.requestSuccess(returnD);
			// return returnD;
		}
	}

	/**
	 * 泰极登录
	 * @return
	 */
	@RequestMapping(value = "/net/login")
	@ResponseBody
	public PageData tjLogin(@RequestBody PageData pd) throws Exception {
		// 登陆：输入账号验证码
		String verificationCode = pd.getString("verificationCode");
		String sLoginName = pd.getString("loginName");
		String phoneMac = this.getRequest().getHeader("mac");
		String sysCode = pd.getString("sysCode") + "_";
		if (StringUtils.isBlank(verificationCode) || StringUtils.isBlank(sLoginName) || StringUtils.isBlank(phoneMac)) {
			return WebResult.requestFailed(400, "参数缺失", null);
		}
		else {
			PageData userN = userAuthMapper.findUserByName(pd);
			// 登陆
			if (null != userN) {
				PageData npd = new PageData();
				npd.put("recNum", sLoginName);
				npd.put("msgCode", verificationCode);
				PageData returnD = sendMsgService.checkVerificationCode(npd);
				if ("200".equals(returnD.getString("state"))) {
					// 系统用户ID
					String userId = userN.getString("USER_ID");
					npd.put("userId", userId);
					PageData user = userAuthMapper.getUserInfo(npd);
					// 用户业务表主键
					String sUIID = user.getString("UI_ID");
					npd.put("userId", sUIID);
					// 获取用户信息和token
					return WebResult.requestSuccess(userInfoService.getUserInfoById(npd));
				}
				else {
					return returnD;
				}

			}
			else {
				return WebResult.requestFailed(403, "用户不存在，请先注册！", null);
			}
		}
	}

	/**
	 * 打开第三方应用获取用户信息
	 * @return
	 */
	@RequestMapping(value = "/net/userInfo")
	@ResponseBody
	public PageData netUsrInfo(@RequestBody PageData pd)throws Exception{
		// 登陆：输入账号验证码
		String phoneMac = this.getRequest().getHeader("mac");
		String userkey = this.getRequest().getHeader("userkey");
		String sysCode = pd.getString("sysCode") + "_";
		if (StringUtils.isBlank(userkey) || StringUtils.isBlank(phoneMac) || StringUtils.isBlank(sysCode)) {
			return WebResult.requestFailed(400, "参数缺失", null);
		}else {
			String token = TockenUtil.getTokenStr(userkey, tokenKey);// token需要完善
			String[] userArray = token.split(",");
			if (userArray.length == 3) {
				PageData queryPd = new PageData();
				queryPd.put("userId", userArray[2]);
				PageData userInfo=userInfoService.getUserInfoFamily(queryPd);
				PageData retPd=new PageData();
				retPd.put("userInfo", JSON.toJSONString(userInfo));
				retPd.put("option", "2");
				retPd.put("token", "");
				return WebResult.requestSuccess(retPd);
			}else{
				return WebResult.requestFailed(400, "参数缺失", null);
			}
		}
	}

	/**
	 * 登录
	 * @return
	 */
	@RequestMapping(value = "/login")
	@ResponseBody
	public PageData login(@RequestBody PageData pd) throws Exception {
		// 登陆：输入账号验证码
		String verificationCode = pd.getString("verificationCode");
		String sLoginName = pd.getString("loginName");
		String phoneMac = this.getRequest().getHeader("mac");
		String sLoginType = pd.getString("loginType");
		String sAppType = pd.getString("appType");// 根据登陆类型
		if (StringUtils.isBlank(sLoginType) || StringUtils.isBlank(verificationCode) || StringUtils.isBlank(sLoginName) || StringUtils.isBlank(phoneMac)) {
			return WebResult.requestFailed(400, "参数缺失", null);
		}
		else {
			verificationCode = fillVCode(verificationCode);
			if ("login".equals(sLoginType)) {
				// 登陆
				PageData userN = userAuthMapper.findUserByName(pd);
				if (null != userN) {
					PageData vnpd = new PageData();
					vnpd.put("recNum", sLoginName);
					vnpd.put("msgCode", verificationCode);
					PageData returnD = sendMsgService.checkVerificationCode(vnpd);
					if ("200".equals(returnD.getString("state"))) {
						// 获取用户信息和token
						PageData returnpd = new PageData();

						String userId = "";
						// 用户ID
						PageData npd = new PageData();
						npd.put("userId", userN.getString("USER_ID"));
						npd.put("USER_ID", userN.getString("USER_ID"));
						// 根据apptype判断业务表中是否存在数据如果不存在则注册
						if ("1".equals(sAppType)||"7".equals(sAppType)) {// 用户APP
							PageData user = userAuthMapper.getUserInfo(npd);
							if (null == user) {
								return WebResult.requestFailed(403, "用户不存在，请先注册！", null);
							}
							else {
								userId = user.getString("UI_ID");
								npd.put("USER_ID", userN.getString("USER_ID"));
								returnpd.put("userInfo", appUserInfoMapper.getUserInfo(npd));
							}
						}
						else if ("2".equals(sAppType)) {// 陪诊APP
							// 根据apptype判断业务表中是否存在数据如果不存在则联系客服人员处理初始化账号
							PageData user = userAuthMapper.getAccompanyInfo(npd);
							if (null == user) {
								return WebResult.requestFailed(403, "请联系客服人员处理，初始化账号！", null);
							}
							else {
								userId = user.getString("AI_ID");
								returnpd.put("userInfo", user);
							}
						}
						else if ("3".equals(sAppType)) {// 机构APP
							// 根据apptype判断业务表中是否存在数据如果不存在则联系客服人员处理初始化账号
							PageData user = userAuthMapper.getSPInfo(npd);
							if (null == user) {
								return WebResult.requestFailed(403, "请联系客服人员处理，初始化账号！", null);
							}
							else {
								userId = user.getString("SP_ID");
								returnpd.put("userInfo", user);
							}
						}
						else if ("4".equals(sAppType)) {// 医生APP
							// 根据apptype判断业务表中是否存在数据如果不存在则联系客服人员处理初始化账号
							PageData user = userAuthMapper.geDoctorInfo(npd);
							if (null == user) {
								return WebResult.requestFailed(403, "请联系客服人员处理，初始化账号！", null);
							}
							else {
								userId = user.getString("HEL_ID");
								returnpd.put("userInfo", user);
							}
						}

						PageData token = new PageData();
						token.put("userId", userId);
						token.put("loginMac", phoneMac);
						String userToken = TockenUtil.createToken(String.join(",", tokenPre, "APP", userId), tokenkey);
						RedisTemplateUtils.set(userToken, token);
						returnpd.put("userToken", userToken);
						return WebResult.requestSuccess(returnpd);
					}
					else {
						return returnD;
					}
				}
				else {
					if ("1".equals(sAppType)) {
						return WebResult.requestFailed(403, "用户不存在，请先注册！", null);
					}
					else {
						return WebResult.requestFailed(403, "请联系客服人员处理，初始化账号！", null);
					}
				}
			}
			else {// 注册
				PageData isdel = userAuthMapper.findDelUserByName(pd);
				if (isdel != null) {
					return WebResult.requestFailed(403, "该账户存在异常请联系客服人员！", null);
				}
				PageData vnpd = new PageData();
				vnpd.put("recNum", sLoginName);
				vnpd.put("msgCode", verificationCode);
				PageData returnD = sendMsgService.checkVerificationCode(vnpd);
				return returnD;
			}
		}
	}

	/**
	 * 退出
	 * @return
	 */
	@RequestMapping(value = "/logout")
	@ResponseBody
	public PageData logout(@RequestBody PageData pd) throws Exception {
		// 登陆：输入账号验证码
		String userkey = this.getRequest().getHeader("userkey");
		if (StringUtils.isBlank(userkey)) {
			return WebResult.requestFailed(400, "参数缺失", null);
		}
		else {
			RedisTemplateUtils.del(userkey);
			return WebResult.requestSuccess();
		}
	}

	/**
	 * 注册
	 * @return
	 */
	@RequestMapping(value = "/register")
	@ResponseBody
	public PageData register(@RequestBody PageData pd) throws Exception {
		// 添加用户信息、添加系统用户表
		String sLoginName = pd.getString("UI_PHONE");
		String sUI_NAME = pd.getString("UI_NAME");
		String sUI_IDCARD = pd.getString("UI_IDCARD");
		String sUI_SEX = pd.getString("UI_SEX");
		String sUI_BIRTHDAY = pd.getString("UI_BIRTHDAY");
		String sUI_ADDRESS = pd.getString("UI_ADDRESS");
		String sUI_PHOTO = pd.getString("UI_PHOTO");
		String sURGENT_NAME = pd.getString("URGENT_NAME");
		String sURGENT_PHONE = pd.getString("URGENT_PHONE");
		String sUI_THUM_PHOTO = pd.getString("UI_THUM_PHOTO");
		String phoneMac = this.getRequest().getHeader("mac");
		String userToken = this.getRequest().getHeader("userToken");
		if (StringUtils.isBlank(sLoginName) || StringUtils.isBlank(sUI_NAME) || StringUtils.isBlank(sUI_IDCARD) || StringUtils.isBlank(sUI_SEX)
				|| StringUtils.isBlank(sUI_BIRTHDAY) || StringUtils.isBlank(sURGENT_NAME) || StringUtils.isBlank(sURGENT_PHONE)) {
			return WebResult.requestFailed(400, "参数不正确", null);
		}
		else {
			PageData query = new PageData();
			query.put("loginName", sLoginName);
			String USER_ID = UuidUtil.get32UUID();
			PageData isSysExist = userAuthMapper.findUserByName(query);
			if (isSysExist != null) {// 如果存在 判断业务用户表是否存在
				USER_ID = isSysExist.getString("USER_ID");
				query.put("userId", isSysExist.getString("USER_ID"));
				PageData user = userAuthMapper.getUserInfo(query);
				if (null != user) {
					return WebResult.requestFailed(403, "用户已存在，请登录！", null);
				}
			}
			else {
				PageData isdel = userAuthMapper.findDelUserByName(query);
				if (isdel != null) {
					return WebResult.requestFailed(403, "该账户存在异常请联系客服人员！", null);
				}
				// 生成系统用户表ID
				String passwd =DigestUtils.md5Hex(String.join(",",USER_ID,"123456"));
				PageData sysData = new PageData();
				sysData.put("USER_ID", USER_ID);
				sysData.put("USER_NAME", sLoginName);
				sysData.put("UI_NAME", sUI_NAME);
				sysData.put("TYPE", "1");
				sysData.put("UI_PHONE", sLoginName);
				sysData.put("PASSWORD", passwd);
				appUserInfoMapper.addSysUserInfo(sysData);
			}
			//
			String sUI_NAME_LETTER = ChineseToEnglish.getPingYin(sUI_NAME);
			//
			PageData userData = new PageData();
			String UI_ID = UuidUtil.get32UUID();
			// 如果没有头像 则创建默认头像
			String photoUrl = femalephoto;
			if ("1".equals(sUI_SEX)) {
				photoUrl = malephoto;
			}
			if ("".equals(sUI_PHOTO) || "".equals(sUI_THUM_PHOTO)) {
				sUI_PHOTO = photoUrl;
				sUI_THUM_PHOTO = photoUrl;
			}
			userData.put("UI_ID", UI_ID);
			userData.put("SYS_UI_ID", USER_ID);
			userData.put("UI_NAME", sUI_NAME);
			userData.put("UI_SEX", sUI_SEX);
			userData.put("UI_BIRTHDAY", sUI_BIRTHDAY);
			userData.put("UI_IDCARD", sUI_IDCARD);
			userData.put("UI_PHONE", sLoginName);
			userData.put("UI_ADDRESS", sUI_ADDRESS);
			userData.put("UI_NAME_LETTER", sUI_NAME_LETTER);
			userData.put("URGENT_NAME", sURGENT_NAME);
			userData.put("URGENT_PHONE", sURGENT_PHONE);
			userData.put("UI_PHOTO", sUI_PHOTO);
			userData.put("UI_THUM_PHOTO", sUI_THUM_PHOTO);
			appUserInfoMapper.addUserInfo(userData);
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
//			String appType = pd.getString("appType");
//			if ("1".equals(appType)) {// 用户
//				appUserInfoMapper.updateUserPushCode(pd);
//			}
//			else if ("2".equals(appType)) {// 陪诊
//				appUserInfoMapper.updateAccompanyPushCode(pd);
//			}
//			else if ("3".equals(appType)) {// 机构
//				appUserInfoMapper.updateSPPushCode(pd);
//			}
//			else if ("4".equals(appType)) {// 医生
//				appUserInfoMapper.updateDoctorPushCode(pd);
//			}
			appUserInfoMapper.updatePushCode(pd);
			return WebResult.requestSuccess();
		}
	}

	/**
	 * 将userkey转换成UI_ID信息
	 * @return
	 */
	@RequestMapping(value = "/userKey2UIID")
	@ResponseBody
	public PageData userKey2UIID(@RequestBody PageData pd) throws Exception {
		PageData result = new PageData();
		result.put("UIID", TockenUtil.getTokenStr(pd.getString("userkey"), tokenkey));
		return WebResult.requestSuccess(result);
	}

	/**
	 * 生成二维码type 和 signature
	 * @return
	 */
	@RequestMapping(value = "/toQRCODE")
	@ResponseBody
	public PageData toQRCODE(@RequestBody PageData pd) throws Exception {
		String qr = qrcode.createJsonQrcode(pd);
		return WebResult.requestSuccess(qr);
	}
	/**
	 * 不足4位不足4位
	 * @param code
	 * @return
	 */
	public String fillVCode(String code){
		if(!StringUtils.isBlank(code)){
			for (int i = code.length();i<4;i++){
				code = "0"+code;
			}
		}
		return code;
	}
}
