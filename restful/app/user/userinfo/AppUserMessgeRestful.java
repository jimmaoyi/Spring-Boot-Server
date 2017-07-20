package com.maryun.restful.app.user.userinfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.maryun.mapper.app.user.userinfo.AppUserMessageMapper;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.CommUtil;
import com.maryun.utils.PushMongoUtil;
import com.maryun.utils.WebResult;

/**
 * 类名称：AppUserInfoRestful 创建人：MARYUN 创建时间：2016年2月13日
 * 
 * @version
 */
@RestController
@RequestMapping(value = "/app/user/message")
public class AppUserMessgeRestful extends BaseRestful {
	@Autowired
	AppUserMessageMapper appUserMessageMapper;

	@Resource
	private PushMongoUtil commonMongo;

	/**
	 * 获取消息列表
	 * @return
	 */
	@RequestMapping(value = "/list")
	@ResponseBody
	public PageData getMessageList(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("UI_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			List<PageData> list = appUserMessageMapper.getMessageList(pd);
			return WebResult.requestSuccess(list);
		}
	}

	/**
	 * 设置消息已读
	 * @return
	 */
	@RequestMapping(value = "/readed")
	@ResponseBody
	public PageData setMessageReaded(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("IDS"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			pd.put("IDS", CommUtil.arr2InStr(pd.getString("IDS")));
			appUserMessageMapper.upLikingReaded(pd);
			appUserMessageMapper.upDiscussReaded(pd);
			return WebResult.requestSuccess();
		}
	}

	/**
	 * 获取推送消息列表
	 * @return
	 */
	/*
	 * @RequestMapping(value = "/centerlist")
	 * 
	 * @ResponseBody public PageData getMessageCenterList(@RequestBody PageData
	 * pd) throws Exception { List<PageData> list =
	 * appUserMessageMapper.getMessageCenterList(pd); return
	 * WebResult.requestSuccess(list); }
	 */
	/**
	 * 获取推送消息列表
	 * @return
	 */
	@RequestMapping(value = "/centerlist")
	@ResponseBody
	public PageData getMessageCenterList(@RequestBody PageData pd) throws Exception {
		String M_GOAL = pd.getString("M_GOAL");
		String APP_TYPE = pd.getString("APP_TYPE");
		int offset = (int) pd.get("offset");
		int pageSize = Integer.parseInt(pd.getString("pageSize"));
		if (StringUtils.isNotBlank(M_GOAL) && StringUtils.isNotBlank(APP_TYPE)) {
			pd.remove("offset");
			pd.remove("pageSize");
			pd.put("DEL_STATE", "1");
			List<PageData> allCenterMessage = commonMongo.find(pd, "lb_jyb_message", pageSize, offset, "M_STATE", "M_TIME");

			for (int i = 0; i < allCenterMessage.size(); i++) {
				PageData pd_index = allCenterMessage.get(i);
				if ("all".equals(pd_index.get("M_GOAL"))) {
					// 根据M_ID向read表中去查询，，如果有值的话则是已读的
					PageData pd_read = new PageData();
					pd_read.put("M_ID", pd_index.get("M_ID"));
					pd_read.put("M_GOAL", pd.get("M_GOAL"));
					List<PageData> CenterMessage_read = commonMongo.findNoPage(pd_read, "lb_jyb_message_read");
					if (CenterMessage_read.size() != 0) {
						// 已读。。
						String del_state = (String) CenterMessage_read.get(0).get("DEL_STATE");
						if ("1".equals(del_state)) {
							// 未删除,把已读标志传到前台,只改变list集合中的值
							pd_index.put("M_STATE", "1");
							allCenterMessage.set(i, pd_index);
						}
						else {
							// 已删除,直接把list中对应的记录删除
							allCenterMessage.remove(i);
						}
					}
					else {
						// 未读,肯定也未删除

					}
				}
			}

			return WebResult.requestSuccess(allCenterMessage);
		}
		else {
			return WebResult.requestFailed(504, "参数错误", null);
		}
	}

	/**
	 * 获取推送消息列表的总数
	 * @return
	 */
	// @RequestMapping(value = "/centercountlist")
	// @ResponseBody
	// public PageData getMessageCenterCountList(@RequestBody PageData pd)
	// throws Exception {
	// PageData count = appUserMessageMapper.getMessageCenterCountList(pd);
	// return WebResult.requestSuccess(count);
	// }
	/**
	 * 获取推送消息列表的总数
	 * @return
	 */
	@RequestMapping(value = "/centercountlist")
	@ResponseBody
	public PageData getMessageCenterCountList(@RequestBody PageData pd) throws Exception {
		String M_GOAL = pd.getString("M_GOAL");
		String APP_TYPE = pd.getString("APP_TYPE");
		pd.put("DEL_STATE", "1");
		if (StringUtils.isNotBlank(M_GOAL) && StringUtils.isNotBlank(APP_TYPE)) {
			List<PageData> allCenterMessage = commonMongo.findNoPage(pd, "lb_jyb_message");
			PageData count = new PageData();
			count.put("count", allCenterMessage.size());
			return WebResult.requestSuccess(count);
		}
		else {
			return WebResult.requestFailed(504, "参数错误", null);
		}

	}

	/**
	 * 清空推送消息列表
	 * @return
	 */
	// @RequestMapping(value = "/clearcenterlist")
	// @ResponseBody
	// public PageData clearMessageCenterList(@RequestBody PageData pd) throws
	// Exception {
	// if (StringUtils.isBlank(pd.getString("M_GOAL"))) {
	// return WebResult.requestFailed(10001, "参数缺失！", null);
	// }
	// else {
	// appUserMessageMapper.clearMessageCenterList(pd);
	// return WebResult.requestSuccess();
	// }
	// }
	/**
	 * 清空推送消息列表
	 * @return
	 */
	@RequestMapping(value = "/clearcenterlist")
	@ResponseBody
	public PageData clearMessageCenterList(@RequestBody PageData pd) throws Exception {
		String M_GOAL = pd.getString("M_GOAL");
		// String APP_TYPE = "1";
		PageData setPd = new PageData();
		setPd.put("DEL_STATE", "0");
		if (StringUtils.isNotBlank(M_GOAL)) {
			// 遍历所有的消息中心。
			pd.put("DEL_STATE", "1");
			List<PageData> allCenterMessage = commonMongo.findNoPage(pd, "lb_jyb_message");
			for (int i = 0; i < allCenterMessage.size(); i++) {
				PageData pd_index = allCenterMessage.get(i);
				if ("all".equals(pd_index.get("M_GOAL"))) {
					// 根据M_ID向read表中去查询，，如果有值的话则是已读的
					PageData pd_read = new PageData();
					pd_read.put("M_ID", pd_index.get("M_ID"));
					pd_read.put("M_GOAL", pd.get("M_GOAL"));
					List<PageData> CenterMessage_read = commonMongo.findNoPage(pd_read, "lb_jyb_message_read");
					if (CenterMessage_read.size() != 0) {

					}
					else {
						// 这里直接添加一条已经删除的read
						PageData pd_read_del = new PageData();
						pd_read_del.put("READ_STATE", "0");
						pd_read_del.put("DEL_STATE", "0");
						pd_read_del.put("M_GOAL", M_GOAL);
						pd_read_del.put("M_ID", pd_index.getString("M_ID"));
						commonMongo.insert(pd_read_del, "lb_jyb_message_read");

					}
				}
			}

			commonMongo.update(pd, setPd, "lb_jyb_message");
			// 还需要把all的表read中DEL_STATE置为0
			pd.remove("APP_TYPE");
			commonMongo.update(pd, setPd, "lb_jyb_message_read");
			return WebResult.requestSuccess();
		}
		else {
			return WebResult.requestFailed(504, "参数错误", null);
		}

	}

	/**
	 * 添加推送表（在推送的时候添加。。消息中心内不处理）
	 * @return
	 */
	@RequestMapping(value = "/addPushMessage")
	@ResponseBody
	public PageData addPushMessage(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("M_GOAL")) || StringUtils.isBlank(pd.getString("M_TYPE")) || StringUtils.isBlank(pd.getString("M_STATE"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			appUserMessageMapper.addPushMessage(pd);
			return WebResult.requestSuccess();
		}
	}

	/**
	 * 设置消息已读
	 * @return
	 */
	// @RequestMapping(value = "/messageCenterReaded")
	// @ResponseBody
	// public PageData messageCenterReaded(@RequestBody PageData pd) throws
	// Exception {
	// if (StringUtils.isBlank(pd.getString("M_ID"))) {
	// return WebResult.requestFailed(10001, "参数缺失！", null);
	// }
	// else {
	// appUserMessageMapper.messageCenterReaded(pd);
	// return WebResult.requestSuccess();
	// }
	// }

	/**
	 * 设置消息已读
	 * @return
	 */
	@RequestMapping(value = "/messageCenterReaded")
	@ResponseBody
	public PageData messageCenterReaded(@RequestBody PageData pd) throws Exception {
		PageData readPd = new PageData();

		String M_GOAL = pd.getString("M_GOAL");
		if ("all".equals(M_GOAL)) {
			PageData pd_read = new PageData();
			pd_read.put("READ_STATE", "1");
			pd_read.put("DEL_STATE", "1");
			pd_read.put("M_GOAL", pd.getString("UI_ID"));
			pd_read.put("M_ID", pd.getString("M_ID"));
			// 推送给全部人的,向lb_jyb_message_read表中添加一条记录，，
			// 字段：DEL_STATE,READ_STATE,M_ID,M_GOAL
			pd.put("M_GOAL", pd.getString("UI_ID"));
			pd.remove("UI_ID");
			// 先查询read表，看是否有对应的M_ID,如果已经存在了，则不再插入
			List<PageData> readByMID = commonMongo.findNoPage(pd, "lb_jyb_message_read");
			if (readByMID.size() == 0) {

				commonMongo.insert(pd_read, "lb_jyb_message_read");
			}
		}
		else {
			// 推送给具体用户的
			readPd.put("M_STATE", "1");
			pd.remove("UI_ID");
			commonMongo.update(pd, readPd, "lb_jyb_message");
		}

		return WebResult.requestSuccess();

	}

	/**
	 * 批量删除消息中心的消息
	 */
	// @RequestMapping(value = "/deleteBatchMessage")
	// public Object deleteBatchMessage(@RequestBody PageData pd) throws
	// Exception {
	// Map<String, Object> map = new HashMap<String, Object>();
	// String MESSAGE_IDS = pd.getString("IDS");
	//
	// if (null != MESSAGE_IDS && !"".equals(MESSAGE_IDS)) {
	// String[] ArrayMESSAGE_IDS = MESSAGE_IDS.split(",");
	// appUserMessageMapper.deleteBatchMessage(ArrayMESSAGE_IDS);
	// map.put("msg", "ok");
	// }
	// else {
	// map.put("msg", "no");
	// }
	// // 结果集封装
	// return WebResult.requestSuccess(map);
	// }
	/**
	 * 批量删除消息中心的消息
	 */
	@RequestMapping(value = "/deleteBatchMessage")
	public Object deleteBatchMessage(@RequestBody PageData pd) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String MESSAGE_IDS = pd.getString("IDS");

		String M_GOAL = pd.getString("M_GOAL");// 实质是UI_ID
		PageData delPd = new PageData();
		delPd.put("DEL_STATE", "0");

		if (null != MESSAGE_IDS && !"".equals(MESSAGE_IDS)) {
			String[] ArrayMESSAGE_IDS = MESSAGE_IDS.split(",");
			for (int j = 0; j < ArrayMESSAGE_IDS.length; j++) {
				PageData ArrayMESSAGE_IDS_index = new PageData();

				ArrayMESSAGE_IDS_index.put("M_ID", ArrayMESSAGE_IDS[j]);

				// 根据M_ID去查询message表，看是否M_GOAL是all
				ArrayMESSAGE_IDS_index.put("DEL_STATE", "1");
				List<PageData> pd_all = commonMongo.findNoPage(ArrayMESSAGE_IDS_index, "lb_jyb_message");
				if (pd_all != null && pd_all.size() == 1) {
					PageData pd_one = pd_all.get(0);
					if ("all".equals(pd_one.get("M_GOAL"))) {
						// 先向read表中查询数据，如果有此条数据，则直接更新，如果没有的话，则直接添加,需要M_ID和Ｍ＿ＧＯＡＬ
						PageData pd_isAlive = new PageData();
						pd_isAlive.put("M_GOAL", M_GOAL);
						pd_isAlive.put("DEL_STATE", 1);
						pd_isAlive.put("M_ID", pd_one.getString("M_ID"));
						List<PageData> pd_all_isAlive = commonMongo.findNoPage(pd_isAlive, "lb_jyb_message_read");
						if (pd_all_isAlive != null) {
							// 查询条件pd_isAlive
							// 更新条件
							PageData pd_updata_del = new PageData();
							pd_updata_del.put("DEL_STATE", 0);
							// read表中已存在数据
							commonMongo.update(pd_isAlive, pd_updata_del, "lb_jyb_message_read");
						}
						else {
							// 这里直接添加一条已经删除的read
							PageData pd_read_del = new PageData();
							pd_read_del.put("READ_STATE", "0");
							pd_read_del.put("DEL_STATE", "0");
							pd_read_del.put("M_GOAL", M_GOAL);
							pd_read_del.put("M_ID", ArrayMESSAGE_IDS[j]);
							commonMongo.insert(pd_read_del, "lb_jyb_message_read");
						}

					}
					else {
						commonMongo.update(ArrayMESSAGE_IDS_index, delPd, "lb_jyb_message");
					}
				}

			}
			map.put("msg", "ok");
		}
		else {
			map.put("msg", "no");
		}

		// 结果集封装
		return WebResult.requestSuccess(map);
	}

	/**
	 * 获取未读消息总数 pd M_GOAL
	 * @return
	 */
	@RequestMapping(value = "/messageCenterUnReadedCount")
	@ResponseBody
	public PageData messageCenterUnReadedCount(@RequestBody PageData pd) throws Exception {
		pd.put("DEL_STATE", "1");
		pd.put("M_STATE", "2");
		List<PageData> centerMessageUnReaded = commonMongo.findNoPage(pd, "lb_jyb_message");

		int normalNum = 0;
		int allNum = 0;
		for (int i = 0; i < centerMessageUnReaded.size(); i++) {
			PageData pd_index = centerMessageUnReaded.get(i);
			if ("all".equals(pd_index.get("M_GOAL"))) {
				// 根据M_ID向read表中去查询，，如果有值的话则是已读的
				PageData pd_read = new PageData();
				pd_read.put("M_ID", pd_index.get("M_ID"));
				pd_read.put("M_GOAL", pd.get("M_GOAL"));
				List<PageData> CenterMessage_read = commonMongo.findNoPage(pd_read, "lb_jyb_message_read");
				if (CenterMessage_read.size() != 0) {
					// 已读或者已删除，不处理
				}
				else {
					// 未读,未读消息数量+1
					allNum++;
				}
			}
			else {
				// 计算不是all的情况下的未读消息总数
				normalNum++;
			}
		}

		// 总的未读消息数
		int unreadnum = normalNum + allNum;

		PageData unreadCount = new PageData();
		unreadCount.put("unreadCount", unreadnum);

		return WebResult.requestSuccess(unreadCount);
	}

	/**
	 * 设为全部已读
	 * @return
	 */
	@RequestMapping(value = "/messageCenterAllRead")
	@ResponseBody
	public PageData messageCenterAllRead(@RequestBody PageData pd) throws Exception {
		PageData readPd = new PageData();
		pd.put("DEL_STATE", "1");
		List<PageData> centerMessageUnReaded = commonMongo.findNoPage(pd, "lb_jyb_message");

		for (int i = 0; i < centerMessageUnReaded.size(); i++) {
			PageData pd_index = centerMessageUnReaded.get(i);
			if ("all".equals(pd_index.get("M_GOAL"))) {
				// 根据M_ID向read表中去查询，，如果有值的话则是已读的
				PageData pd_read = new PageData();
				pd_read.put("M_ID", pd_index.get("M_ID"));
				pd_read.put("M_GOAL", pd.getString("M_GOAL"));
				List<PageData> CenterMessage_read = commonMongo.findNoPage(pd_read, "lb_jyb_message_read");
				if (CenterMessage_read.size() != 0) {
					// 已读或者已删除，不处理
				}
				else {
					// 未读的。向read表中添加一条字段，声明已读
					PageData pd_read_insert = new PageData();
					pd_read_insert.put("READ_STATE", "1");
					pd_read_insert.put("DEL_STATE", "1");
					pd_read_insert.put("M_GOAL", pd.getString("M_GOAL"));
					pd_read_insert.put("M_ID", pd_index.getString("M_ID"));
					commonMongo.insert(pd_read_insert, "lb_jyb_message_read");
				}
			}
			else if ("2".equals(pd_index.get("M_STATE"))) {
				// 不是all的情况下的未读消息,更改状态为1
				readPd.put("M_STATE", "1");
				pd.remove("UI_ID");
				commonMongo.update(pd, readPd, "lb_jyb_message");
			}
		}

		return WebResult.requestSuccess();
	}
}
