package com.maryun.mapper.app.user.userinfo;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.maryun.model.PageData;

@Mapper
public interface AppUserMessageMapper {
	List<PageData> getMessageList(PageData pd); // 查询用户个人消息信息

	void upLikingReaded(PageData pd); // 将点赞设置已读

	void upDiscussReaded(PageData pd); // 将评论设置已读

	List<PageData> getMessageCenterList(PageData pd); // 查询推送消息中心列表

	PageData getMessageCenterCountList(PageData pd); // 清空推送消息列表

	void clearMessageCenterList(PageData pd); // 清空推送消息列表

	void addPushMessage(PageData pd);// 向推送表中添加一条记录

	void messageCenterReaded(PageData pd); // 将推送消息设置已读

	void deleteBatchMessage(String[] ArrayMessage_IDS);

	List<PageData> getTVUserMessageList(PageData pd); // 查询电视端消息信息
}
