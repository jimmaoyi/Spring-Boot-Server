package com.maryun.lb.restful.jyb.datainterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.maryun.model.PageData;
import com.maryun.utils.HttpUtils;
import com.maryun.utils.HttpUtils.UHeader;
import com.maryun.utils.XmlUtils;

public class XiWeiErDataSync {
	/**
	 * 获取西维尔EncryToken
	 * @param url 获取Url地址
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getEncryTokenDatas(String url) throws Exception {
//		String result = HttpUtils.getResponse(url, new HashMap(), new ArrayList<UHeader>());
		String result = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><AuthenticationResult><encryToken>6607AC9239F5F81E6CC3DE07</encryToken></AuthenticationResult>";
		Map<String, Object> res = XmlUtils.string2Map(result);
		return res;
	}

	/**
	 * 获取西维尔Auth
	 * @param url 获取Url地址
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getAuthDatas(String url) throws Exception {
//		String result = HttpUtils.getResponse(url, new HashMap(), new ArrayList<UHeader>());
		String result = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><AuthResult userToken=\"A6F893E316993100DB78D12E\" epgDomain=\"http://10.13.14.20/ydjinan/\" epgDomainBackup=\"http://10.13.14.20/ydjinan/\" upgradeDomain=\"http://172.25.36.221:8080\" upgradeDomainBackup=\"http://172.25.36.221:8080\" managementDomain=\"http://172.25.36.221:8080\" managementDomainBackup=\"http://172.25.36.221:8080\" ntpDomain=\"http://172.25.36.221:8087\" ntpDomainBackup=\"http://172.25.36.221:8087\" drmDomain=\"http://223.99.253.14:14002\" drmDomainBackup=\"http://223.99.253.14:14002\" userGroupNMB=\"YD0531\" ></AuthResult>";
		Map<String, Object> res = XmlUtils.string2Map(result);
		return res;
	}
	
	/**
	 * 获取西维尔栏目
	 * @param url 获取Url地址
	 * @return
	 */
	public List<PageData> getCatalogDatas(String url) throws Exception {
		String result = HttpUtils.getResponse(url, new HashMap(), new ArrayList<UHeader>());
//		String result = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><GetCategoryListResult><CategoryList><Category id=\"000000092017032200000001\" type =\"1\" name=\"商晓红|shangxiaohong\" code =\"000000092017032200000001\" imgurl =\"\" isLeaf=\"0\"/><Category id=\"000000092017032100000002\" type =\"1\" name=\"马沛然|mapeiran\" code =\"000000092017032100000002\" imgurl =\"\" isLeaf=\"0\"/></CategoryList></GetCategoryListResult>";
		Map<String, Object> res_map = XmlUtils.string2Map(result);
		List<PageData> pdList = null;
		if(null != res_map && res_map.size() > 0){
			pdList = new ArrayList<PageData>();
			Map<String, PageData> m_tmp = (Map<String, PageData>)res_map.get("CategoryList");
			for (String s_key : m_tmp.keySet()) {
				pdList.add(m_tmp.get(s_key));
			}
		}
		return pdList;
	}

	/**
	 * 获取西维尔视频缩略图
	 * 
	 * @param url
	 *            获取Url地址
	 * @return
	 */
	public List<PageData> getAssetDatas(String url) throws Exception {
		String result = HttpUtils.getResponse(url, new HashMap(), new ArrayList<UHeader>());
//		String result = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><GetContentListResult totalCount =\"6\" currentCount =\"6\" ><ContentList><Series contentId=\"6871939168438119\" name=\"小灰灰上学记\" originalName=\"小灰灰上学记\" volumnCount=\"30\" orgAirDate=\"\" description=\"济南少儿2013年12月12日《动画片》：小灰灰上学记：成狼礼\" contentProvider =\"null\" viewPoint=\"济南少儿2013年12月12日《动画片》：小灰灰上学记：成狼礼\" starLevel=\"6\" rating=\"\" awards=\"\" favoriteFlg=\"\" ><PictureList><Picture fileURL=\"http://219.146.10.218/images/iptv/2014/4/25/990810020000000100000000085460069/19f3dba4-643d-4faf-8aac-2e84b04d48df.jpg\" type =\"1\"/></PictureList></Series><Series contentId=\"8619527146142818\" name=\"摩尔庄园 第三季\" originalName=\"摩尔庄园 第三季\" volumnCount=\"52\" orgAirDate=\"\" description=\"济南少儿频道2013年12月16日播出：摩尔庄园：一块冲浪板\" contentProvider =\"null\" viewPoint=\"济南少儿频道2013年12月16日播出：摩尔庄园：一块冲浪板\" starLevel=\"6\" rating=\"\" awards=\"\" favoriteFlg=\"\" ><PictureList><Picture fileURL=\"http://219.146.10.218/images/iptv/2014/4/25/990810020000000100000000086417539/8cfba6c3-42a2-4fc5-bc94-1ae5bbb1f012.jpg\" type =\"1\"/></PictureList></Series><Series contentId=\"3453128411921671\" name=\"猫鼠看片会2\" originalName=\"猫鼠看片会2\" volumnCount=\"60\" orgAirDate=\"\" description=\"汤姆是一只常见的家猫，它有一种强烈的欲望，总想抓住与它同居一室却难以抓住的老鼠杰瑞，它不断地努力驱赶这只讨厌的房客，但总是遭到失败，而且还被杰瑞捉弄受些皮肉之苦。\" contentProvider =\"null\" viewPoint=\"汤姆是一只常见的家猫，它有一种强烈的欲望，总想抓住与它同居一室却难以抓住的老鼠杰瑞，它不断地努力驱赶这只讨厌的房客，但总是遭到失败，而且还被杰瑞捉弄受些皮肉之苦。\" starLevel=\"6\" rating=\"\" awards=\"\" favoriteFlg=\"\" ><PictureList><Picture fileURL=\"http://219.146.10.218/images/iptv/2014/3/7/990810020000000100000000080549069/6661938b-d3cf-4281-8b66-adbfa72b1553.jpg\" type =\"1\"/></PictureList></Series><Series contentId=\"2157246112837158\" name=\"猫鼠看片会\" originalName=\"猫鼠看片会\" volumnCount=\"60\" orgAirDate=\"\" description=\"济南少儿《动画片》2013年11月25日播出：猫和老鼠 足智多谋的杰瑞（二）猫卡通\" contentProvider =\"null\" viewPoint=\"济南少儿《动画片》2013年11月25日播出：猫和老鼠 足智多谋的杰瑞（二）猫卡通\" starLevel=\"6\" rating=\"\" awards=\"\" favoriteFlg=\"\" ><PictureList><Picture fileURL=\"http://219.146.10.218/images/iptv/2014/1/21/990810020000000100000000082340979/0e623bb7-9cfe-496b-9ff5-cac6f1ce1714.jpg\" type =\"1\"/></PictureList></Series><Series contentId=\"6557623158217572\" name=\"铠甲勇士拿瓦\" originalName=\"铠甲勇士拿瓦\" volumnCount=\"52\" orgAirDate=\"\" description=\"铠甲勇士拿瓦一\" contentProvider =\"null\" viewPoint=\"铠甲勇士拿瓦一\" starLevel=\"6\" rating=\"\" awards=\"\" favoriteFlg=\"\" ><PictureList><Picture fileURL=\"http://219.146.10.218/images/iptv/2014/4/25/990810020000000100000000085957569/f78533d2-7086-4331-aad4-51ae1843abe6.jpg\" type =\"1\"/></PictureList></Series><Series contentId=\"9351757516971161\" name=\"唐老鸭从军记\" originalName=\"唐老鸭从军记\" volumnCount=\"6\" orgAirDate=\"\" description=\"济南少儿频道《动画片》2013年12月4日播出：唐老鸭和米老鼠 家庭防卫站\" contentProvider =\"null\" viewPoint=\"济南少儿频道《动画片》2013年12月4日播出：唐老鸭和米老鼠 家庭防卫站\" starLevel=\"6\" rating=\"\" awards=\"\" favoriteFlg=\"\" ><PictureList><Picture fileURL=\"http://219.146.10.218/images/iptv/2014/4/25/990810020000000100000000083657959/aba6449e-b27f-4cb7-9986-38d79bacef8a.jpg\" type =\"1\"/></PictureList></Series></ContentList></GetContentListResult>";
		return XmlUtils.getAssetDatas(result);
	}
	
	/**
	 * 获取西维尔视频
	 * 
	 * @param url
	 *            获取Url地址
	 * @return
	 */
	public Map<String, Object> getVideosData(String url) throws Exception {
		String result = HttpUtils.getResponse(url, new HashMap(), new ArrayList<UHeader>());
//		String result = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><ServiceAuthResult playUrl =\"http://219.146.10.201:8081/00/iptv/null/index.m3u8\" expiredTime =\"null\" balance =\"null\"></ServiceAuthResult>";
		Map<String, Object> res = XmlUtils.string2Map(result);
		return res;
	}
}
