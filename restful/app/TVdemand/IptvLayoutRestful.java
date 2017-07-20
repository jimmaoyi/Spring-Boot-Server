package com.maryun.restful.app.TVdemand;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.maryun.mapper.app.TVdemand.IptvLayoutMapper;
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
@RequestMapping(value = "/tvdemand/iptvlayout")
public class IptvLayoutRestful extends BaseRestful {

	@Autowired
	private IptvLayoutMapper iptvLayoutMapper;

	/**
	 * 收藏夹列表（分页）
	 * @return
	 */
	@RequestMapping(value = "/getCollectionList")
	@ResponseBody
	public PageData getCollectionList(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("IL_TERMINAL_VERSION"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			pd = iptvLayoutMapper.getLayoutList(pd);
			if(pd != null){
				List<PageData> TAB_LIST = iptvLayoutMapper.getTabList(pd);
				if( TAB_LIST != null){
					for (PageData pageData : TAB_LIST) {
						List<PageData> BLOCK_LIST = iptvLayoutMapper.getLayoutDetailList(pageData);
						pageData.put("BLOCK_LIST", BLOCK_LIST);
					}
				}
				pd.put("TAB_LIST", TAB_LIST);
			}
			return WebResult.requestSuccess(pd);
		}
	}

	

}
