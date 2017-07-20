package com.maryun.restful.app.TVdemand;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.maryun.mapper.app.TVdemand.CollectionMapper;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.UuidUtil;
import com.maryun.utils.WebResult;

/**
 * 类名称：MessageRestful 创建人：MARYUN 创建时间：2016年2月13日
 * 
 * @version
 */
@RestController
@RequestMapping(value = "/tvdemand/collection")
public class CollectionRestful extends BaseRestful {

	@Autowired
	private CollectionMapper collectionMapper;

	/**
	 * 收藏夹列表（分页）
	 * @return
	 */
	@RequestMapping(value = "/getCollectionList")
	@ResponseBody
	public PageData getCollectionList(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("userId")) || StringUtils.isBlank(pd.getString("wayType"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			List<PageData> list = null;
			list = collectionMapper.getCollectonList(pd);
			return WebResult.requestSuccess(list);
		}
	}

	/**
	 * 某节目是否被收藏
	 * @return
	 */
	@RequestMapping(value = "/getCollectionByID")
	@ResponseBody
	public PageData getCollectionByID(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("userId")) || StringUtils.isBlank(pd.getString("contentId")) || StringUtils.isBlank(pd.getString("wayType"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			pd = collectionMapper.getCollectionByID(pd);
			if(pd != null){
				return WebResult.requestSuccess("true");
			}else{
				return WebResult.requestSuccess("false");
			}
			
		}
	}

	/**
	 * 添加收藏
	 * @return
	 */
	@RequestMapping(value = "/insertCollection")
	@ResponseBody
	public PageData insertCollection(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("userId")) || StringUtils.isBlank(pd.getString("contentId")) ||  StringUtils.isBlank(pd.getString("name")) || StringUtils.isBlank(pd.getString("isCollect")) ||  StringUtils.isBlank(pd.getString("wayType"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			
			if(pd.getString("isCollect").equals("0")){
				collectionMapper.deleteCollection(pd);
			}else if(pd.getString("isCollect").equals("1")){
				PageData pds = pd;
				pds = collectionMapper.getCollectionByID(pds);
				if(pds != null){
					
				}else{
					pd.put("F_ID", UuidUtil.get32UUID());
					pd.put("F_TYPE", "2");
					collectionMapper.insertCollection(pd);
				}
			}
			return WebResult.requestSuccess();
		}
	}

}
