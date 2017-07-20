package com.maryun.lb.restful.jyb.datainterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.maryun.model.PageData;
import com.maryun.utils.HttpUtils;
import com.maryun.utils.HttpUtils.UHeader;

public class ByTueDataSync {
	public List<PageData> getCatalogDatas(String url) {
		String result = HttpUtils.getResponse(url, new HashMap(), new ArrayList<UHeader>());
		System.err.println(result);
		JSONObject jo = JSON.parseObject(result);
		List<PageData> pdList = JSON.parseObject(jo.getString("list"), new TypeReference<List<PageData>>() {});
		return pdList;
	}

	/**
	 * 获取百途视频缩略图
	 * 
	 * @param url
	 *            获取Url地址
	 * @return
	 */
	public List<PageData> getAssetDatas(String url) {
		String result = HttpUtils.getResponse(url, new HashMap(), new ArrayList<UHeader>());
		System.err.println(result);
		JSONObject jo = JSON.parseObject(result);
		List<PageData> pdList = JSON.parseObject(jo.getString("list"), new TypeReference<List<PageData>>() {});
		return pdList;
	}
	
	/**
	 * 获取百途视频
	 * 
	 * @param url
	 *            获取Url地址
	 * @return
	 */
	public HashMap<String, String> getVideosData(String url) {
		String result = HttpUtils.getResponse(url, new HashMap(), new ArrayList<UHeader>());
		System.err.println(result);
		HashMap<String, String> pdList = JSON.parseObject(result, HashMap.class);
		return pdList;
	}
}
