package com.maryun.restful.app.yjb.message;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.maryun.mapper.app.user.userinfo.AppUserMessageMapper;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.WebResult;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * @author: Libra Date: 2017年4月18日 上午9:26:04 Version: 1.1
 * @Description: 消息推送的restful层
 */
/*
 * @RestController is a stereotype annotation that combines @ResponseBody and @Controller.
 * means : @RestController注解相当于@ResponseBody ＋ @Controller合在一起的作用。
 */
@RestController
@RequestMapping(value = "/app/jyb/message")
public class JybAppMessageRestful extends BaseRestful {

	@Resource
	private MongoTemplate mongoTemplate;

	@Autowired
	private AppUserMessageMapper appUserMessageMapper;

	/**
	 * 获取消息推送的信息列表
	 * @param pd
	 * @return
	 */
	@RequestMapping(value = "/messageList")
	@ResponseBody
	public PageData messageList(@RequestBody PageData pd) {

		BasicDBObject fieldsObject = new BasicDBObject();
		// 指定返回的字段
		fieldsObject.put("M_CONTENT", true);
		fieldsObject.put("CREATE_TIME", true);
		fieldsObject.put("APP_TYPE", true);
		fieldsObject.put("_id", false);

		// 查询条件
		DBObject dbObject = new BasicDBObject();
		if (null != pd.getString("CREATE_TIME") && !"".equals(pd.getString("CREATE_TIME"))) {
			dbObject.put("CREATE_TIME", pd.get("CREATE_TIME"));
		}
		if (null != pd.getString("APP_TYPE_NAME") && !"".equals(pd.getString("APP_TYPE_NAME"))) {
			dbObject.put("APP_TYPE", pd.get("APP_TYPE_NAME"));
		}
		if (null != pd.getString("tag") && !"".equals(pd.getString("tag"))) {
			dbObject.put("M_TYPE", pd.get("tag"));
		}

		int pageNumber = pd.getPageNumber(); // 获取页条数(当前页数)
		int pageSize = pd.getPageSize(); // 获取页码(每页多少条)
		Query query = new BasicQuery(dbObject, fieldsObject);
		// 排序
		query.with(new Sort(new Sort.Order(Sort.Direction.DESC, "CREATE_TIME")));
		// 分页
		query.skip((pageNumber - 1) * 10).limit(pageSize);
		List<PageData> messageList = mongoTemplate.find(query, PageData.class, "lb_jyb_message");
		for (PageData data : messageList) {
			String app = data.getString("APP_TYPE");
			if (null != app && !"".equals(app)) {
				formatAPPTYPE(app, data);
			}
			/** 解析json "M_CONTENT"字符串 */
			String content = data.getString("M_CONTENT");
			JSONObject object = JSON.parseObject(content);
			Object title = object.get("TITLE");
			if (null != title && !"".equals(title)) {
				data.put("TITLE", title);
			}
			Object tag = object.get("tag");
			if (null != tag && !"".equals(tag)) {
				formatTag(tag, data);
			}
			Object ALERT = object.get("ALERT");
			if (null != ALERT && !"".equals(ALERT)) {
				data.put("ALERT", ALERT);
			}
		}
		pd = this.getPagingPd(messageList);
		return WebResult.requestSuccess(pd);
	}

	/**
	 * @param tag 推送类型转换 ( 0 不需要跳转到新页面的消息 101导医分单 102代理商分单 103退款完结 104订单完结申请
	 * 201添加家庭成员 202添加好友 301申请加入团对 302加入团对邀请 401活动更新 402团队报名入选 403个人报名入选 501
	 * app轮播更新 502健康教育内容更新 )
	 * @param data
	 * @return
	 */
	private PageData formatTag(Object tag, PageData data) {
		if (tag.equals("0")) {
			data.put("tag", tag);
		}
		else if (tag.equals("101")) {
			data.put("tag", "导医分单");
		}
		else if (tag.equals("102")) {
			data.put("tag", "代理商分单");
		}
		else if (tag.equals("103")) {
			data.put("tag", "退款完结");
		}
		else if (tag.equals("104")) {
			data.put("tag", "订单完结申请");
		}
		else if (tag.equals("201")) {
			data.put("tag", "添加家庭成员");
		}
		else if (tag.equals("202")) {
			data.put("tag", "添加好友");
		}
		else if (tag.equals("301")) {
			data.put("tag", "申请加入团队");
		}
		else if (tag.equals("302")) {
			data.put("tag", "加入团对邀请");
		}
		else if (tag.equals("401")) {
			data.put("tag", "活动更新");
		}
		else if (tag.equals("402")) {
			data.put("tag", "团队报名入选");
		}
		else if (tag.equals("403")) {
			data.put("tag", "个人报名入选");
		}
		else if (tag.equals("501")) {
			data.put("tag", "app轮播更新");
		}
		else if (tag.equals("502")) {
			data.put("tag", "健康教育内容更新");
		}
		return data;
	}

	/**
	 * @param app 终端类型转换（1用户2陪诊3机构4医生）
	 * @param data
	 * @return
	 */
	private PageData formatAPPTYPE(Object app, PageData data) {
		if (app.equals("1")) {
			data.put("APP_TYPE_NAME", "用户");
		}
		else if (app.equals("2")) {
			data.put("APP_TYPE_NAME", "陪诊");
		}
		else if (app.equals("3")) {
			data.put("APP_TYPE_NAME", "机构");
		}
		else if (app.equals("4")) {
			data.put("APP_TYPE_NAME", "医生");
		}
		return data;
	}

	/**
	 * TV端消息列表
	 * @return
	 */
	@RequestMapping(value = "/getTVUserMessageList")
	@ResponseBody
	public PageData getTVUserMessageList(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("TVM_KEY"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			List<PageData> list = null;
			list = appUserMessageMapper.getTVUserMessageList(pd);
			return WebResult.requestSuccess(list);
		}
	}
}
