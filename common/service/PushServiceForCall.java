package com.maryun.common.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import ch.qos.logback.classic.Logger;
import com.maryun.mapper.app.common.*;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import cn.jiguang.common.ClientConfig;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.PushPayload;

import com.alibaba.fastjson.JSON;
import com.maryun.lb.mapper.jyb.user.UsersMapper;
import com.maryun.model.PageData;

/**
 * 内部调用push推送消息
 *
 * @author <a href="mailto:shiran@maryun.net">shiran</a>
 * @version 2017年2月26日
 */
@Component
public class PushServiceForCall {
	@Value("${JPush.USER_APPKEY}")
	private String USER_APPKEY;

	@Value("${JPush.USER_MASTER_SECRET}")
	private String USER_MASTER_SECRET;

	@Value("${JPush.ACCP_APPKEY}")
	private String ACCP_APPKEY;

	@Value("${JPush.ACCP_MASTER_SECRET}")
	private String ACCP_MASTER_SECRET;

	@Value("${JPush.ORGA_USER_APPKEY}")
	private String ORGA_APPKEY;

	@Value("${JPush.ORGA_MASTER_SECRET}")
	private String ORGA_MASTER_SECRET;

	@Value("${JPush.DOCTOR_APPKEY}")
	private String DOCTOR_APPKEY;

	@Value("${JPush.DOCTOR_MASTER_SECRET}")
	private String DOCTOR_MASTER_SECRET;

    @Value("${JPush.SPORT_APPKEY}")
    private String SPORT_APPKEY;

    @Value("${JPush.SPORT_MASTER_SECRET}")
    private String SPORT_MASTER_SECRET;

	private String APPKEY = "";

	private String MASTER_SECRET = "";

	@Resource
	private PushService push;

	@Resource
	private JPushUserMapper jPushUserMapper;

	@Resource
	private JPushAccpMapper jPushAccpMapper;

	@Resource
	private JPushDoctorMapper jPushDoctorMapper;

	@Resource
	private JPushOrgaMapper jPushOrgaMapper;

	@Autowired
	UsersMapper usersMapper;

	@Resource
	MongoTemplate mongoTemplate;

	@Resource
	PushCodeMapper pushCodeMapper;

	private static Logger logger = (Logger) LoggerFactory.getLogger(PushServiceForCall.class);
	/**
	 * 给用户推送消息，包括推送信息栏的标题，点击推送标题弹出alert，推送消息是附加的内容 ，业务类型。
	 * 返回为一个有失败次数和失败uid的map。如果返回为空，检查传入的UserList长度是不是为0.
	 *
	 * @param title<String> 推送信息栏的标题
	 * @param alert<String> 点击推送标题弹出alert
	 * @param extra Map<String,String> 推送消息是附加的内容
	 * @param userList List<String> 要推送的用户UID
	 * @param tag<String> 业务类型，如：订单推送，活动推送等
	 * @param sys_uiid 创建人，修改人
	 * @param o_id 订单ID
	 * @param type App的类型 1用户2陪诊3机构4医生
	 * @return Map<String,Object> failedTime<String> 表示失败的次数; failedUID<List
	 * <String>> 表示推送失败的UID List.
	 * @throws Exception
	 */
	public PageData alertTitleMsgExtraCallback(String title, String alert, Map<String, String> extra, List<String> userList, String tag, String sys_uiid,
			String o_id, String type) throws Exception {
		extra.put("tag", tag);
		extra.put("O_ID", o_id);
		// 推送失败的次数
		int failedTime = 0;
		// 推送失败的ID List
		List<String> failedUID = new ArrayList<String>();
		// 返回状态码200的储存
		boolean pushState = false;
		// 如果userList为null，说明要推送给所有用户消息
		if (userList == null || userList.size() == 0 || userList.get(0).equals("") || userList.get(0) == "") {
			try {
				PushPayload payload = push.buildPushObject_id_alert_title_extra(alert, title, extra, null);
				pushState = jPushClient().sendPush(payload).isResultOK();
				System.out.println(pushState);
			}
			catch (Exception e) {
				System.err.println("推送用户失败，第二次尝试推送此用户" + e);
			}
			// 如果返回状态200,储存之。
			// 如果返回状态200,储存之。
			if (pushState) {
				save("all", title, alert, extra, tag, sys_uiid, o_id, type,"success");

			}
			else {
				save("all", title, alert, extra, tag, sys_uiid, o_id, type,"failed");
				failedTime++;
				failedUID.add("all");
			}
			// 发送失败的次数和用户ID放到Map中
			PageData failedArguments = new PageData();
			failedArguments.put("failedTime", failedTime);
			failedArguments.put("failedUID", failedUID);
			return failedArguments;
		}

		int size = userList.size();
		// 每5000条Id分割一次查询
		int splitTime = size / 5000;
		// 开始分割的条数
		int fromIndex = 0;
		// 结束分割的条数
		int toIndex = 0;
		// 从userlist取的值的userid的下标
		int t = 0;
		// 如果为userList大小小于等于0,返回空值

		if (userList.size() <= 0) {
			return null;
		}
		for (int i = 0; i < splitTime + 1; i++) {
			toIndex += 5000;
			// 如果toIndex大于size，超出范围。设置toIndex=size;
			if (toIndex > size) {
				toIndex = size;
			}
			if (i == 0) {// 第一次循环单独处理
				fromIndex = 0;
			}
			else {
				fromIndex = i * 5000 + 1;
			}
			// 分割list
			List<String> strings = userList.subList(fromIndex, toIndex);
			List<PageData> regIds = new ArrayList<>();
            regIds = pushCodeMapper.select(type, strings);
            if (regIds.size() == 0) {
				return null;
			}
			// 循环推送
			for (PageData regId : regIds) {
				String PUSH_CODE = regId.getString("PUSH_CODE");
				if (PUSH_CODE == "" || PUSH_CODE.equals("")) {
					return null;
				}
				try {
					PushPayload payload = push.buildPushObject_id_alert_title_extra(alert, title, extra, PUSH_CODE);
					JPushClient jPushClient = jPushClient();
					PushResult pushResult = jPushClient.sendPush(payload);
					pushState = pushResult.isResultOK();
					String originalContent = pushResult.getOriginalContent();
					System.err.println(originalContent);
				}
				catch (Exception e) {
					System.err.println("推送用户：" + regId + "失败，第二次尝试推送此用户" + e);
					try {
						PushPayload payload = push.buildPushObject_id_alert_title_extra(alert, title, extra, PUSH_CODE);
						jPushClient().sendPush(payload);
					}
					catch (Exception e2) {
						failedTime++;
						failedUID.add(PUSH_CODE);
						System.err.println("推送用户：" + regId + "失败" + e);
					}
				}
				// 如果返回状态200,储存之。
				if (pushState) {
					save(userList.get(t++), title, alert, extra, tag, sys_uiid, o_id, type,"success");

				}
				else {
					save(userList.get(t++), title, alert, extra, tag, sys_uiid, o_id, type,"failed");
					failedTime++;
					failedUID.add(userList.get(i));
				}
			}
		}

		// 发送失败的次数和用户ID放到Map中
		PageData failedArguments = new PageData();
		failedArguments.put("failedTime", failedTime);
		failedArguments.put("failedUID", failedUID);
		return failedArguments;
	}

	/**
	 * 创建JpushClient
	 *
	 * @return
	 */
	private JPushClient jPushClient() {
		ClientConfig clientConfig = ClientConfig.getInstance();
		JPushClient jpushClient = new JPushClient(this.MASTER_SECRET, this.APPKEY, null, clientConfig);
		return jpushClient;
	}

	/**
	 * 储存推送成功的消息 @Description: TODO @param UI_ID @param PL_TITLE @param
	 * PL_EXTEND_DATA @param PL_TAG @return void @throws
	 */
	private void save(String UI_ID, String PL_TITLE, String ALERT, Map<String, String> PL_EXTEND_DATA, String PL_TAG, String sys_uiid, String o_id,
			String type,String queue) {
		PL_EXTEND_DATA.put("TITLE", PL_TITLE);
		PL_EXTEND_DATA.put("ALERT", ALERT);

		PageData pd = new PageData();
		pd.put("M_ID", UUID.randomUUID().toString().replaceAll("-", ""));
		pd.put("M_TYPE", PL_TAG);
		pd.put("O_ID", o_id);
		pd.put("M_GOAL", UI_ID);
		pd.put("M_CONTENT", JSON.toJSON(PL_EXTEND_DATA).toString());
		pd.put("M_TIME", new Date());
		pd.put("M_STATE", "2");
		pd.put("CREATE_UID", sys_uiid);
		pd.put("CREATE_TIME", new Date());
		pd.put("CHANGE_UID", sys_uiid);
		pd.put("CHANGE_TIME", new Date());
		pd.put("DEL_STATE", "1");
		pd.put("APP_TYPE", type);
		usersMapper.saveSuccessPush(pd);
		//成功失败插入不同表
		if (queue=="success"){
			mongoTemplate.insert(pd,"lb_jyb_message");
			logger.info("savesuccess "+"推送成功！"+JSON.toJSONString(pd));
		}else {
			mongoTemplate.insert(pd,"lb_jyb_message_failed");
			logger.info("savesuccess "+"推送成功！"+JSON.toJSONString(pd));
		}
		//usersMapper.saveSuccessPush(pd);
	}

	public void setAPPKEYANDSecret(String type) {
		if (type.equals("1")) {
			this.APPKEY = USER_APPKEY;
			this.MASTER_SECRET = USER_MASTER_SECRET;
		}
		if (type.equals("2")) {
			this.APPKEY = ACCP_APPKEY;
			this.MASTER_SECRET = ACCP_MASTER_SECRET;
		}
		if (type.equals("3")) {
			this.APPKEY = ORGA_APPKEY;
			this.MASTER_SECRET = ORGA_MASTER_SECRET;
		}
		if (type.equals("4")) {
			this.APPKEY = DOCTOR_APPKEY;
			this.MASTER_SECRET = DOCTOR_MASTER_SECRET;
		}
		if (type.equals("7")){
            this.APPKEY = SPORT_APPKEY;
            this.MASTER_SECRET = SPORT_MASTER_SECRET;
        }
	}
}
