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

public class TongzhouDataSync {
	public List<PageData> getCatalogDatas(String url) {
		String result = HttpUtils.getResponse(url, new HashMap(), new ArrayList<UHeader>());
		System.err.println(result);
		JSONObject jo = JSON.parseObject(result);
		List<PageData> pdList = JSON.parseObject(jo.getString("Catalog"), new TypeReference<List<PageData>>() {});
		return pdList;
	}

	/**
	 * 获取同洲视频缩略图
	 * 
	 * @param url
	 *            获取Url地址
	 * @return
	 */
	public List<PageData> getAssetDatas(String url) {
		String result = HttpUtils.getResponse(url, new HashMap(), new ArrayList<UHeader>());
		System.err.println(result);
		JSONObject jo = JSON.parseObject(result);
		List<PageData> pdList = JSON.parseObject(jo.getString("assetList"), new TypeReference<List<PageData>>() {});
		return pdList;
	}
//	public static void main(String[] args){
//		TongzhouDataSync tds = new TongzhouDataSync();
////		String url = "http://hl-iuc.snctv.com.cn:8088/iepg/getAssetList?terminalType=0&catalogId=60041";
//		String url = "http://hl-iuc.snctv.com.cn:8088/iepg/getPlayURL?terminalType=0&resourceCode=10284&playType=1";
////		tds.getDeptData(url);
//		String result = HttpUtils.getResponse(url, new HashMap(), new ArrayList<UHeader>());
//		System.err.println(result);
//	}
	
	/**
	 * 获取同洲视频
	 * 
	 * @param url
	 *            获取Url地址
	 * @return
	 */
	public List<String> getVideosData(String url) {
		String result = HttpUtils.getResponse(url, new HashMap(), new ArrayList<UHeader>());
		System.err.println(result);
		JSONObject jo = JSON.parseObject(result);
		List<String> pdList = JSON.parseArray(jo.getString("playURLList"), String.class);
//		List<PageData> pdList = JSON.parseObject(jo.getString("playURLList"), new TypeReference<List<PageData>>() {});
		return pdList;
	}
	
//	public static void main(String[] args) {
//		String ssss = "http://hl-iuc.snctv.com.cn:8088/iepg/getPlayURL?terminalType=0&resourceCode=10308&playType=1";
//		String result = HttpUtils.getResponse(ssss, new HashMap(), new ArrayList<UHeader>());
//		System.err.println(result);
//	}
}
