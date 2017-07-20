/**
 * 
 */
package com.maryun.restful.app.common;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.maryun.mapper.app.common.AppVersionMapper;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.WebResult;

/**
 * @author Administrator
 *
 */
@RestController
@RequestMapping(value = "/app/version")
public class AppVersionRestful extends BaseRestful{
	@Autowired 
	AppVersionMapper appVersionMapper;
	/**
	 * 消息列表（分页）
	 * @return
	 */
	@RequestMapping(value = "/info")
	@ResponseBody
	public PageData getVersion(@RequestBody PageData pd) throws Exception {
		if(StringUtils.isBlank(pd.getString("APP_TYPE"))){
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}else{
			PageData versionInfo = appVersionMapper.getVersion(pd);
			return WebResult.requestSuccess(versionInfo);
		}
	}
}
