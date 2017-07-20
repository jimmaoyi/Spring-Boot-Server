package com.maryun.restful.app.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maryun.common.service.PushServiceForCall;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.WebResult;

/**
 * 
 * @ClassName: PushRestful
 * @Description: app消息推送接口
 * @author SR
 * @date 2017年3月2日
 */
@RestController
@RequestMapping("push")
public class PushRestful extends BaseRestful {
	@Resource
	private PushServiceForCall pushServiceForCall;

	/**
	 * 
	 * @Description: 给指定账号推送或者全部人员推送
	 * @return
	 * @return WebResult
	 * @throws
	 */
	@RequestMapping("pushUserOrAll")
	public PageData pushUserOrAll(@RequestBody PageData pd) {
		// PageData pd = this.getPageData();
		PageData alertTitleMsgExtraCallback = new PageData();
		String title;
		String alert;
		String tag;
		String sys_uiid;
		String o_id;
		String extra1;
		String extra2;
		String user_id;
		String type;
		try {
			title = pd.getString("title");
			alert = pd.getString("alert");
			tag = pd.getString("tag");
			sys_uiid = pd.getString("sys_uiid");
			o_id = pd.getString("o_id");
			extra1 = pd.getString("extra1");
			extra2 = pd.getString("extra2");
			user_id = pd.getString("user_id");
			type = pd.getString("type");
		}
		catch (Exception e) {
			System.out.println("参数错误" + e);
			return WebResult.requestFailed(400, "参数错误", pd);
		}
		try {
			List<String> userList = Arrays.asList(user_id);
			Map<String, String> extra = new HashMap<>();
			extra.put(extra1, extra2);
			pushServiceForCall.setAPPKEYANDSecret(type);
			alertTitleMsgExtraCallback = pushServiceForCall.alertTitleMsgExtraCallback(title, alert, extra, userList, tag, sys_uiid, o_id, type);
			if (alertTitleMsgExtraCallback == null) {
				alertTitleMsgExtraCallback.put("failedTime", 0);
				alertTitleMsgExtraCallback.put("failedUID", new ArrayList<>());
				return WebResult.requestFailed(501, "PUSH_CODE为空", alertTitleMsgExtraCallback);
			}
		}
		catch (Exception e) {
			alertTitleMsgExtraCallback.put("failedTime", 0);
			alertTitleMsgExtraCallback.put("failedUID", new ArrayList<>());
			return WebResult.requestFailed(500, "服务器错误", alertTitleMsgExtraCallback);
		}

		return WebResult.requestSuccess(alertTitleMsgExtraCallback);
	}
}
