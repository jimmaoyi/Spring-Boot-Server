package com.maryun.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.maryun.model.PageData;

public class XmlUtils {
	public static Map<String, Object> string2Map(String str) throws Exception {
		Document doc = null;
		try {
			doc = DocumentHelper.parseText(str);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		Map<String, Object> map = new HashMap<String, Object>();
		if (doc == null)
			return map;
		Element root = doc.getRootElement();
		if (root.hasContent()) {
			for (Iterator iterator = root.elementIterator(); iterator.hasNext();) {
				Element e = (Element) iterator.next();
				List list = e.elements();
				if (list.size() > 0) {
					map.put(e.getName(), Dom2Map(e, 0));
				} else {
					map.put(e.getName(), e.getText());
				}
			}
		} else {
			List<Attribute> list_attrs = root.attributes();
			if (null != list_attrs && list_attrs.size() > 0) {
				for (Attribute attr_tmp : list_attrs) {
					map.put(attr_tmp.getName(), attr_tmp.getValue());
				}
			}
		}
		return map;
	}

	public static Map Dom2Map(Element e, int i_num) {
		Map map = new HashMap();
		List list = e.elements();
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Element iter = (Element) list.get(i);
				List mapList = new ArrayList();
				if (iter.elements().size() > 0) {
					Map m = Dom2Map(iter, i);
					if (map.get(iter.getName()) != null) {
						Object obj = map.get(iter.getName());
						if (!obj.getClass().getName().equals("java.util.ArrayList")) {
							mapList = new ArrayList();
							mapList.add(obj);
							mapList.add(m);
						}
						if (obj.getClass().getName().equals("java.util.ArrayList")) {
							mapList = (List) obj;
							mapList.add(m);
						}
						map.put(iter.getName(), mapList);
						List<Attribute> list_attrs = iter.attributes();
						if(null != list_attrs && list_attrs.size() > 0){
							PageData pd_tmp = new PageData();
							for (Attribute attr_tmp : list_attrs) {
								pd_tmp.put(attr_tmp.getName(), attr_tmp.getValue());
							}
							map.put((int)(Math.random() * 1000000) + "", pd_tmp);
						}
					} else {
						List<Attribute> list_attrs = iter.attributes();
						if (null != list_attrs && list_attrs.size() > 0) {
							PageData pd_tmp = new PageData();
							for (Attribute attr_tmp : list_attrs) {
								pd_tmp.put(attr_tmp.getName(), attr_tmp.getValue());
							}
							map.put((int) (Math.random() * 1000000) + "", pd_tmp);
						} else {
							map.put(iter.getName(), m);
						}
					}
				} else {
					if (map.get(iter.getName()) != null) {
						Object obj = map.get(iter.getName());
						if (!obj.getClass().getName().equals("java.util.ArrayList")) {
							mapList = new ArrayList();
							mapList.add(obj);
							mapList.add(iter.getText());
						}
						if (obj.getClass().getName().equals("java.util.ArrayList")) {
							mapList = (List) obj;
							mapList.add(iter.getText());
						}
						map.put(iter.getName(), mapList);
						List<Attribute> list_attrs = iter.attributes();
						if(null != list_attrs && list_attrs.size() > 0){
							PageData pd_tmp = new PageData();
							for (Attribute attr_tmp : list_attrs) {
								pd_tmp.put(attr_tmp.getName(), attr_tmp.getValue());
							}
							map.put((int)(Math.random() * 1000000) + "", pd_tmp);
						}
					} else {
						// map.put(iter.getName(), iter.getText());
						List<Attribute> list_attrs = iter.attributes();
						if (null != list_attrs && list_attrs.size() > 0) {
							PageData pd_tmp = new PageData();
							for (Attribute attr_tmp : list_attrs) {
								pd_tmp.put(attr_tmp.getName(), attr_tmp.getValue());
							}
							map.put("b" + i, pd_tmp);
						}
					}
				}
			}
		} else {
			// map.put(e.getName(), e.getText());
			List<Attribute> list_attrs = e.attributes();
			if (null != list_attrs && list_attrs.size() > 0) {
				PageData pd_tmp = new PageData();
				for (Attribute attr_tmp : list_attrs) {
					pd_tmp.put(attr_tmp.getName(), attr_tmp.getValue());
				}
				map.put("c" + i_num, pd_tmp);
			}
		}
		return map;
	}

	public static String head = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	
	/*public static List<PageData> getAssetDatas(String xmlStr) throws Exception {
		Document document = DocumentHelper.parseText(xmlStr);
		List<PageData> res = null;
		if(null != document){
			res = new ArrayList<PageData>();
			//获取文档的根节点
			Element root = document.getRootElement();
			//取得某个节点的子节点
			Element element_sentence_list = root.element("ContentList");
			//取得某节点下所有名为“Series”的子节点，并进行遍历
			List nodes = element_sentence_list.elements("Series");
			for (Iterator it = nodes.iterator(); it.hasNext();) {
				Element elm = (Element) it.next();
				//取得某节点下所有名为“Series”的子节点，并进行遍历
				List nodes1 = elm.elements("PictureList");
				for (Iterator it1 = nodes1.iterator(); it1.hasNext();) {
					Element elm1 = (Element) it1.next();
					List nodes2 = elm1.elements("Picture");
					for (Iterator it2 = nodes2.iterator(); it2.hasNext();) {
						PageData pd_tmp = new PageData();
						Element elm2 = (Element) it2.next();
						pd_tmp.put("resourceCode", elm.attribute("contentId").getText());
						pd_tmp.put("name", elm.attribute("name").getText());
						pd_tmp.put("picFileURL", elm2.attribute("fileURL").getText());
//						System.out.println(elm.attribute("contentId").getText() + "-" + elm.attribute("name").getText() + "-" + elm2.attribute("fileURL").getText());
						res.add(pd_tmp);
					}
				}
			}
		}
		return res;
	}*/
	public static List<PageData> getAssetDatas(String xmlStr) throws Exception {
		Document document = DocumentHelper.parseText(xmlStr);
		List<PageData> res = null;
		if(null != document){
			res = new ArrayList<PageData>();
			//获取文档的根节点
			Element root = document.getRootElement();
			//取得某个节点的子节点
			Element element_sentence_list = root.element("ContentList");
			//取得某节点下所有名为“Program”的子节点，并进行遍历
			List nodes = element_sentence_list.elements("Program");
			for (Iterator it = nodes.iterator(); it.hasNext();) {
				PageData pd_tmp = new PageData();
				Element elm = (Element) it.next();
				pd_tmp.put("name", elm.attribute("name").getText());
				pd_tmp.put("resourceCode", elm.attribute("contentID").getText());
				//取得某节点下所有名为“Series”的子节点，并进行遍历
				List nodes1 = elm.elements("PictureList");
				for (Iterator it1 = nodes1.iterator(); it1.hasNext();) {
					Element elm1 = (Element) it1.next();
					List nodes2 = elm1.elements("Picture"); 
					for (Iterator it2 = nodes2.iterator(); it2.hasNext();) {
						Element elm2 = (Element) it2.next();
						pd_tmp.put("picFileURL", elm2.attribute("fileURL").getText());
//						System.out.println(elm.attribute("contentId").getText() + "-" + elm.attribute("name").getText() + "-" + elm2.attribute("fileURL").getText());
						res.add(pd_tmp);
					}
					break;
				}
			}
		}
		return res;
	}

//	@SuppressWarnings("rawtypes")
//	public static void main(String[] args) {
//		try {
////			String result = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><GetContentListResult totalCount =\"6\" currentCount =\"6\" ><ContentList><Series contentId=\"6871939168438119\" name=\"小灰灰上学记\" originalName=\"小灰灰上学记\" volumnCount=\"30\" orgAirDate=\"\" description=\"济南少儿2013年12月12日《动画片》：小灰灰上学记：成狼礼\" contentProvider =\"null\" viewPoint=\"济南少儿2013年12月12日《动画片》：小灰灰上学记：成狼礼\" starLevel=\"6\" rating=\"\" awards=\"\" favoriteFlg=\"\" ><PictureList><Picture fileURL=\"http://219.146.10.218/images/iptv/2014/4/25/990810020000000100000000085460069/19f3dba4-643d-4faf-8aac-2e84b04d48df.jpg\" type =\"1\"/></PictureList></Series><Series contentId=\"8619527146142818\" name=\"摩尔庄园 第三季\" originalName=\"摩尔庄园 第三季\" volumnCount=\"52\" orgAirDate=\"\" description=\"济南少儿频道2013年12月16日播出：摩尔庄园：一块冲浪板\" contentProvider =\"null\" viewPoint=\"济南少儿频道2013年12月16日播出：摩尔庄园：一块冲浪板\" starLevel=\"6\" rating=\"\" awards=\"\" favoriteFlg=\"\" ><PictureList><Picture fileURL=\"http://219.146.10.218/images/iptv/2014/4/25/990810020000000100000000086417539/8cfba6c3-42a2-4fc5-bc94-1ae5bbb1f012.jpg\" type =\"1\"/></PictureList></Series><Series contentId=\"3453128411921671\" name=\"猫鼠看片会2\" originalName=\"猫鼠看片会2\" volumnCount=\"60\" orgAirDate=\"\" description=\"汤姆是一只常见的家猫，它有一种强烈的欲望，总想抓住与它同居一室却难以抓住的老鼠杰瑞，它不断地努力驱赶这只讨厌的房客，但总是遭到失败，而且还被杰瑞捉弄受些皮肉之苦。\" contentProvider =\"null\" viewPoint=\"汤姆是一只常见的家猫，它有一种强烈的欲望，总想抓住与它同居一室却难以抓住的老鼠杰瑞，它不断地努力驱赶这只讨厌的房客，但总是遭到失败，而且还被杰瑞捉弄受些皮肉之苦。\" starLevel=\"6\" rating=\"\" awards=\"\" favoriteFlg=\"\" ><PictureList><Picture fileURL=\"http://219.146.10.218/images/iptv/2014/3/7/990810020000000100000000080549069/6661938b-d3cf-4281-8b66-adbfa72b1553.jpg\" type =\"1\"/></PictureList></Series><Series contentId=\"2157246112837158\" name=\"猫鼠看片会\" originalName=\"猫鼠看片会\" volumnCount=\"60\" orgAirDate=\"\" description=\"济南少儿《动画片》2013年11月25日播出：猫和老鼠 足智多谋的杰瑞（二）猫卡通\" contentProvider =\"null\" viewPoint=\"济南少儿《动画片》2013年11月25日播出：猫和老鼠 足智多谋的杰瑞（二）猫卡通\" starLevel=\"6\" rating=\"\" awards=\"\" favoriteFlg=\"\" ><PictureList><Picture fileURL=\"http://219.146.10.218/images/iptv/2014/1/21/990810020000000100000000082340979/0e623bb7-9cfe-496b-9ff5-cac6f1ce1714.jpg\" type =\"1\"/></PictureList></Series><Series contentId=\"6557623158217572\" name=\"铠甲勇士拿瓦\" originalName=\"铠甲勇士拿瓦\" volumnCount=\"52\" orgAirDate=\"\" description=\"铠甲勇士拿瓦一\" contentProvider =\"null\" viewPoint=\"铠甲勇士拿瓦一\" starLevel=\"6\" rating=\"\" awards=\"\" favoriteFlg=\"\" ><PictureList><Picture fileURL=\"http://219.146.10.218/images/iptv/2014/4/25/990810020000000100000000085957569/f78533d2-7086-4331-aad4-51ae1843abe6.jpg\" type =\"1\"/></PictureList></Series><Series contentId=\"9351757516971161\" name=\"唐老鸭从军记\" originalName=\"唐老鸭从军记\" volumnCount=\"6\" orgAirDate=\"\" description=\"济南少儿频道《动画片》2013年12月4日播出：唐老鸭和米老鼠 家庭防卫站\" contentProvider =\"null\" viewPoint=\"济南少儿频道《动画片》2013年12月4日播出：唐老鸭和米老鼠 家庭防卫站\" starLevel=\"6\" rating=\"\" awards=\"\" favoriteFlg=\"\" ><PictureList><Picture fileURL=\"http://219.146.10.218/images/iptv/2014/4/25/990810020000000100000000083657959/aba6449e-b27f-4cb7-9986-38d79bacef8a.jpg\" type =\"1\"/></PictureList></Series></ContentList></GetContentListResult>";
////			getAssetDatas(result);
//			/*
//			 * String ss=
//			 * head+"<xml><ProId>2016062201</ProId><ProName></ProName><TransFlag>0</TransFlag><PClasses><PClass><PCDate>2016-06-23 00:00:00</PCDate><PClassId>1</PClassId><PCStartTime>2016-06-22 09:00:00</PCStartTime><ExpectTime>180</ExpectTime><InterCount>6</InterCount></PClass><PClass><PCDate>2016-06-24 00:00:00</PCDate><PClassId>1</PClassId><PCStartTime>2016-06-22 14:00:00</PCStartTime><ExpectTime>180</ExpectTime><InterCount>8</InterCount></PClass><PClass><PCDate>2016-06-25 00:00:00</PCDate><PClassId>1</PClassId><PCStartTime>2016-06-22 09:00:00</PCStartTime><ExpectTime>180</ExpectTime><InterCount>12</InterCount></PClass></PClasses><TransDate>2016-06-22 17:09:43</TransDate></xml>"
//			 * ; Map map=string2Map(ss); System.out.println(map);
//			 * System.out.println(map.get("PClasses"));
//			 */
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
}
