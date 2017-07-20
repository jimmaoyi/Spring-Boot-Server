package com.maryun.restful.app.user.login;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.maryun.common.service.UserInfoService;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.HttpUtils;
import com.maryun.utils.TockenUtil;
import com.maryun.utils.WebResult;

/**
 * TV登录以及 登录校验
 * @ClassName: AppUserLoginRestful 
 * @Description: TODO
 * @author lh 
 * @date 2017年2月26日
 */
@RestController
@RequestMapping(value = "/app/user/login")
public class AppUserLoginRestful extends BaseRestful {

	private static String redisKeyPre = "TVUUID_";
	private static  String desKey = "88900772";
	@Autowired
	private UserInfoService userInfoService;
	@Value("${system.userlogin.token.pre}")
	private  String userTokenPre;
	@Resource
	private RedisTemplate<String, Object> redisTemplate;
	@Value("${system.redis.tvtoken.time}")
	private  int redisTimeout;

	/**
	 * UUID注册
	 * 
	 * @return
	 */
	@RequestMapping(value = "/registerUUID")
	@ResponseBody
	public PageData registerUUID(@RequestBody PageData pd) {
		try {
			// 1.生成UUID,状态为0，失效时间10分钟
			// TYPE 盒子类型 0公网盒子1 IPTV盒子
			// OPERATOR 运营商（iptv） 0移动 1联通 2电信

			String UUID = pd.getString("uuid");
			String TYPE = pd.getString("type");
			String OPERATOR = pd.getString("operator");
			String MAC = pd.getString("mac");

			if (StringUtils.isNotBlank(UUID) && StringUtils.isNotBlank(TYPE) && StringUtils.isNotBlank(OPERATOR)) {
				String md5=DigestUtils.md5Hex(UUID);
				String redisUUID = String.join("", redisKeyPre,md5);
				ValueOperations<String, Object> redisOpt = redisTemplate.opsForValue();
				if (!redisTemplate.hasKey(redisUUID)) {
					// 存储到redis 缓存,十分钟有效
					PageData map = new PageData();
					map.put("type", TYPE);
					map.put("operator", OPERATOR);
					map.put("mac", MAC);
					map.put("UUID", UUID);
					map.put("option", "0");
					redisOpt.set(redisUUID, map, redisTimeout, TimeUnit.SECONDS);
					PageData retPd=new PageData();
					retPd.put("signature", md5);
					return WebResult.requestSuccess(retPd);
				} else {
					return WebResult.requestFailed(500, String.join("", UUID, "已存在"), null);
				}
			} else {
				return WebResult.requestFailed(500, "参数不正确", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return WebResult.requestFailed(400, "参数不正确", null);
		}
	}

	/**
	 * 二维码扫描
	 * 
	 * @param pd
	 * @return
	 */
	@RequestMapping(value = "/codeScanning")
	@ResponseBody
	public PageData codeScanning(@RequestBody PageData pd) {
		try {
			// 判断二维码和手机号，更新状态
			String scanning = pd.getString("uuid");
			String option = pd.getString("option");// 操作
			String userkey = this.getRequest().getHeader("userkey");
			String phoneMac = this.getRequest().getHeader("mac");
			if (StringUtils.isNotBlank(scanning) && StringUtils.isNotBlank(phoneMac) && StringUtils.isNotBlank(userkey)
					&& StringUtils.isNotBlank(option)) {
				String redisUUID = String.join("", redisKeyPre,scanning);
				// 获取redis值
				ValueOperations<String, Object> redisOpt = redisTemplate.opsForValue();
				if (redisTemplate.hasKey(redisUUID)) {
					PageData optionPd = (PageData) redisOpt.get(redisUUID);
					
					String uuidStatic = optionPd.get("option") + "";
					// 更新状态, 校验失效时间是不是有效
					if ("1".equals(option)) {//
						optionPd.put("option", "1");
						redisOpt.set(redisUUID, optionPd, redisTimeout, TimeUnit.SECONDS);
					} else if ("2".equals(option) && "1".equals(uuidStatic)) {
						optionPd.put("option", "2");
						
						//获取用户家庭信息
						PageData userPd=getUserInfo(userkey,optionPd.getString("mac"));
						if(userPd!=null&&userPd.get("userInfo")!=null){
							String userId=userPd.getString("userId");
							userPd.put("loginMac", optionPd.getString("mac"));
							//redis用户
							String userTocken=TockenUtil.createToken(String.join(",",userTokenPre,"TVAPP",userId),tokenKey);
							redisOpt.set(userTocken, userPd);
							// 更新状态
							optionPd.put("userToken", userTocken);
							redisOpt.set(redisUUID, optionPd, redisTimeout, TimeUnit.SECONDS);
						}else{
							return WebResult.requestFailed(501, "参数查询不到用户信息", null);
						}
						
					} else {
						return WebResult.requestFailed(502, "二维码失效", null);
					}
					return WebResult.requestSuccess(optionPd);
				} else {
					return WebResult.requestFailed(500, "参数不正确", null);
				}
			} else {
				return WebResult.requestFailed(500, "参数不正确", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return WebResult.requestFailed(400, "系统错误，请联系管理员", null);
		}
	}

	/**
	 * 轮训UUID
	 * 
	 * @param pd
	 * @return
	 */
	@RequestMapping(value = "/queryUUID")
	@ResponseBody
	public PageData queryUUID(@RequestBody PageData pd) {
		try {
			// 判断二维码状态
			String UUID = pd.getString("uuid");
			if (StringUtils.isNotBlank(UUID)) {
				PageData optionPageData = new PageData();
				ValueOperations<String, Object> redisOpt = redisTemplate.opsForValue();
				// 获取二维码扫描状态
				String redisUUID = String.join("", redisKeyPre, UUID);
				if (redisTemplate.hasKey(redisUUID)) {
					PageData optionPd = (PageData) redisOpt.get(redisUUID);
					String uuidStatic = optionPd.getString("option");
					optionPageData.put("option", uuidStatic);
					// 获取用户信息
					String userToken=optionPd.getString("userToken");
					if (StringUtils.isNotBlank(userToken)) {
						PageData userObj = (PageData)redisOpt.get(optionPd.getString("userToken"));
						optionPageData.put("token", userToken);
						optionPageData.put("userInfo", JSON.toJSONString(userObj.get("userInfo")));
					}
					return WebResult.requestSuccess(optionPageData);
				} else {
					return WebResult.requestFailed(509, "二维码已过期！", null);
				}
			} else {
				return WebResult.requestFailed(500, "参数不正确", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return WebResult.requestFailed(400, "系统错误，请联系管理员", null);
		}
	}

	/**md
	 * 检验token
	 * 
	 * @param pd
	 * @return
	 */
	@RequestMapping(value = "/checkToken")
	@ResponseBody
	public PageData checkToken() {
		try {
			HttpServletRequest request = this.getRequest();
			String userToken = request.getHeader("userKey");// 从header中获取
			String phoneMac = request.getHeader("mac");// 从header中获取
			if (StringUtils.isNotBlank(userToken) && StringUtils.isNotBlank(phoneMac)) {
				// 获取redis值,判断token是否存在和用户登录mac是否一致
				if (redisTemplate.hasKey(userToken)) {
					ValueOperations<String, Object> redisOpt = redisTemplate.opsForValue();
					PageData userPd = (PageData)redisOpt.get(userToken);
					String userMac = userPd.getString("loginMac");
					if (userMac.equals(phoneMac)) {
						return WebResult.requestSuccess(userPd);
					} else {
						return WebResult.requestFailed(505, "用户token失效", null);
					}
				} else {
					return WebResult.requestFailed(505, "用户token失效", null);
				}
			} else {
				return WebResult.requestFailed(500, "参数不正确", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return WebResult.requestFailed(400, "系统错误，请联系管理员", null);
		}
	}

	/**
	 * 退出token
	 * 
	 * @param pd
	 * @return
	 */
	@RequestMapping(value = "/logoutToken")
	@ResponseBody
	public PageData logoutToken() {
		try {
			String userToken = this.getRequest().getHeader("userKey");// 从header中获取
			String phoneMac =  this.getRequest().getHeader("mac");// 从header中获取
			if (StringUtils.isNotBlank(userToken) && StringUtils.isNotBlank(phoneMac)) {
				// 获取redis值,判断token是否存在和用户登录mac是否一致
				if (redisTemplate.hasKey(userToken)) {
					ValueOperations<String, Object> redisOpt = redisTemplate.opsForValue();
					PageData userPd = (PageData)redisOpt.get(userToken);
					String userMac = userPd.getString("loginMac");
					if (userMac.equals(phoneMac)) {
						redisTemplate.delete(userToken);
						return WebResult.requestSuccess();
					} else {
						return WebResult.requestFailed(502, "token已失效", null);
					}
				} else {
					return WebResult.requestFailed(502, "token已失效", null);
				}
			} else {
				return WebResult.requestFailed(500, "参数不正确", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return WebResult.requestFailed(400, "系统错误，请联系管理员", null);
		}
	}
	
	@Value("${pay.webchart.qrcode.appid}")
	private String weixinAppid;
	@Value("${wexin.access.token.secret}")
	private String weixinSecret;
	@Value("${wexin.access.token.url}")
	private String weixinTockenUrl;
	
	/**
	 * @Description: 获取微信token
	 * @return    
	 * @return PageData    
	 * @throws
	 */
	@RequestMapping(value = "/weixinAccessToken")
	@ResponseBody
	public PageData weixinAccessToken() {
		try {
			ValueOperations<String, Object> redisOpt = redisTemplate.opsForValue();
			if(redisTemplate.hasKey("WEIXINACCESSTOKEN")){
				return WebResult.requestSuccess(redisOpt.get("WEIXINACCESSTOKEN"));
			}else{
				LocalDateTime localTime=LocalDateTime.now();
				HashMap pd=new HashMap();
				pd.put("grant_type", "client_credential");
				pd.put("appid", weixinAppid);
				pd.put("secret", weixinSecret);
				String info=HttpUtils.httpGet(weixinTockenUrl, pd, null);
				PageData tokenPd=JSON.parseObject(info, PageData.class);
				if(tokenPd.containsKey("access_token")){
					int expiressIn=(int)tokenPd.get("expires_in");
					localTime=localTime.plusSeconds(expiressIn);
					tokenPd.put("expires_in", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(localTime));
					redisOpt.set("WEIXINACCESSTOKEN",tokenPd, expiressIn, TimeUnit.SECONDS);
					return WebResult.requestSuccess(tokenPd);
				}else{
					return WebResult.requestFailed(505, "请求token失败，请联系管理员", null);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return WebResult.requestFailed(400, "系统错误，请联系管理员", null);
		}
	}
	
	
	
	// 根据用户token获取用户家庭信息
	private PageData getUserInfo(String userKey,String tvMac) {
		try {
			String token = TockenUtil.getTokenStr(userKey, tokenKey);// token需要完善
			String[] userArray = token.split(",");
			if (userArray.length == 3&&"APP".equals(userArray[1])) {
				PageData pd = new PageData();
				pd.put("userId", userArray[2]);
				pd.put("TVMAC", tvMac);
				PageData userInfo=userInfoService.getUserInfoById(pd);
				userInfo.put("userToken", userKey);
				pd.put("userInfo",userInfo);
				return pd;
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}

}
