package com.maryun.restful.app.user.health;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
//import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.maryun.mapper.app.user.health.AppUserHealthMapper; 
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.HttpUtils;
import com.maryun.utils.WebResult;

import cn.com.remoteInterfaces.util.Des;
import cn.com.remoteInterfaces.util.HMacMD5;

import com.maryun.utils.HttpUtils.UHeader;
/**
 * 类名称：AppUserHealthRestful 创建人：MARYUN 创建时间：2016年2月13日
 * 
 * @version
 */
@RestController
@RequestMapping(value = "/app/user/health")
public class AppUserHealthRestful extends BaseRestful{
	@Autowired
	AppUserHealthMapper appUserHealthMapper;
	//血压数据请求地址
	@Value("${lbaner.tj.blood.url}")
	private String tjUrl;
	//血压数据请求地址
	@Value("${lbaner.tj.blood.key}")
	private String key;
	
	/**
	 * 用户健康信息
	 * @return
	 */
	@RequestMapping(value = "/get")
	@ResponseBody
	public PageData getUserHealthData(@RequestBody PageData pd) throws Exception {
		if(StringUtils.isBlank(pd.getString("UI_IDCARD"))){
		return WebResult.requestFailed(10001, "参数缺失！", null);
	}else{
		HashMap<String,String> params = new HashMap<String,String> ();
//		params.put("idCardNo", "371323199401023411");
		params.put("idCardNo", pd.getString("UI_IDCARD"));
		List<UHeader> headerList = new ArrayList<UHeader>();
		UHeader conType = new UHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
		headerList.add(conType);
		String result = HttpUtils.getPostResponse(tjUrl, params, headerList);
		if(!"".equals(result)){
			result = Des.decrypt(result);
			HashMap mp = JSON.parseObject(result, HashMap.class);
			JSONArray datalist = (JSONArray) mp.get("dataList");
			List dataList= new ArrayList();
			for(int i=0;i<datalist.size();i++){
				dataList.add(JSON.parseObject(((JSONObject)datalist.get(i)).toJSONString(),HashMap.class));
			}
			try {
				listSort(dataList);
				if(dataList.size()>0){
					dataList.get(0);
					return WebResult.requestSuccess(dataList.get(0));
				}else{
					return WebResult.requestSuccess(null);
				}
			} catch (Exception e) {
//				e.printStackTrace();
				return WebResult.requestFailed(100, "排序失败！", null);
			}
		}else{
			return WebResult.requestSuccess(null);
		}
	}
	}
	
	/**
	 * 获取太极用户健康信息
	 * @return
	 */
	@RequestMapping(value = "/tjdata")
	@ResponseBody
	public PageData getUserBloodData(@RequestBody PageData pd) throws Exception {
		if(StringUtils.isBlank(pd.getString("UI_ID"))){
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}else{
			String hash = HMacMD5.encrypt( pd.getString("UI_ID"), key);
			HashMap<String,String> params = new HashMap<String,String> ();
			params.put("userId", pd.getString("UI_ID"));
			params.put("bgtime", "2017-03-31");
			params.put("edtime", "2017-03-31");
			params.put("hash", hash);
			
			List<UHeader> headerList = new ArrayList<UHeader>();
			String result = HttpUtils.getPostResponse(tjUrl, params, headerList);
			result = Des.decrypt(result);
			JSONArray jobj = JSON.parseArray(result) ;
			Map resp = new HashMap();
			resp.put("UI_ID", pd.getString("UI_ID"));
			resp.put("UI_ID", pd.getString("UI_ID"));
			resp.put("HEART_BEATING", ((JSONObject)jobj.get(0)).get("pulse")==null?"0":((JSONObject)jobj.get(0)).get("pulse"));
			resp.put("DIASTOLIC_BP",  ((JSONObject)jobj.get(0)).get("dbp")==null?"0":((JSONObject)jobj.get(0)).get("dbp"));
			resp.put("SYSTOLIC_BP",((JSONObject)jobj.get(0)).get("sbp")==null?"0":((JSONObject)jobj.get(0)).get("sbp"));
			resp.put("COUNT_DATE", ((JSONObject)jobj.get(0)).get("detectionTime")==null?"0":((JSONObject)jobj.get(0)).get("detectionTime"));
			resp.put("DEVIDE_ID", ((JSONObject)jobj.get(0)).get("deviceId")==null?"0":((JSONObject)jobj.get(0)).get("deviceId"));
			return WebResult.requestSuccess(resp);
		}
	}
	/**
	 * 根据用户身份证号获取用户数据
	 * @return
	 */
	@RequestMapping(value = "/data")
	@ResponseBody
	public PageData getUserData(@RequestBody PageData pd) throws Exception {
//		if(StringUtils.isBlank(pd.getString("IDCARD"))){
//			return WebResult.requestFailed(10001, "参数缺失！", null);
//		}else{
			HashMap<String,String> params = new HashMap<String,String> ();
			params.put("idCardNo", "371323199401023411");
			params.put("idCardNo", pd.getString("IDCARD"));
			List<UHeader> headerList = new ArrayList<UHeader>();
			UHeader conType = new UHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
			headerList.add(conType);
			String result = HttpUtils.getPostResponse(tjUrl, params, headerList);
			if(!"".equals(result)){
				result = Des.decrypt(result);
				HashMap mp = JSON.parseObject(result, HashMap.class);
				JSONArray datalist = (JSONArray) mp.get("dataList");
				List dataList= new ArrayList();
				for(int i=0;i<datalist.size();i++){
					dataList.add(JSON.parseObject(((JSONObject)datalist.get(i)).toJSONString(),HashMap.class));
				}
				try {
					listSort(dataList);
					if(dataList.size()>0){
						dataList.get(0);
						return WebResult.requestSuccess(dataList.get(0));
					}else{
						return WebResult.requestSuccess(null);
					}
				} catch (Exception e) {
//					e.printStackTrace();
					return WebResult.requestFailed(100, "排序失败！", null);
				}
			}else{
				return WebResult.requestSuccess(null);
			}
//		}
	}
	/**
	 * 按照时间排序
	 * @param resultList
	 * @throws Exception
	 */
	public static void listSort(List<Map<String,Object>> resultList) throws Exception{  
        // resultList是需要排序的list，其内放的是Map  
        // 返回的结果集  
        Collections.sort(resultList,new Comparator<Map<String,Object>>() {  

         public int compare(Map<String, Object> o1,Map<String, Object> o2) {  

          //o1，o2是list中的Map，可以在其内取得值，按其排序，此例为升序，s1和s2是排序字段值  
        	 String s1 = (String) o1.get("detectionTime");  
        	 String s2 = (String) o2.get("detectionTime");  

          if(s1.compareTo(s2)>=0) {  
           return -1;  
          }else {  
           return 1;  
          }  
         }  
        });  
         
       }  
}
