package com.maryun.restful.app.user.suggestion;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.maryun.mapper.app.user.suggestion.AppSuggestionMapper;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.restful.system.fileuploads.FilesUploadsRestful;
import com.maryun.utils.CommUtil;
import com.maryun.utils.UuidUtil;
import com.maryun.utils.WebResult;

/**
 * 类名称：AppUserInfoRestful 创建人：MARYUN 创建时间：2016年2月13日
 * 
 * @version
 */
@RestController
@RequestMapping(value = "/app/suggestion")
public class AppSuggestionRestful extends BaseRestful {
	@Autowired
	AppSuggestionMapper appSuggestionMapper;

	@Resource
	FilesUploadsRestful filesUploadsRestful;

	/**
	 * 添加意见
	 * @return
	 */
	@RequestMapping(value = "/add")
	@ResponseBody
	public PageData addSuggestion(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("SF_CONTENT")) || StringUtils.isBlank(pd.getString("SF_PHONE"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			String SF_ID = UuidUtil.get32UUID();
			pd.put("SF_ID", SF_ID);
			// I_IMAGE_ADDR SF_CONTENT SF_PHONE
			appSuggestionMapper.addSuggestion(pd);
			// 遍历图片并保存,用户提交建议的情况下
			if (!StringUtils.isBlank(pd.getString("FILE_UPLOAD_ID_LIST"))) {
				String ids = pd.get("FILE_UPLOAD_ID_LIST").toString().replace("[", "").replace("]", "").trim();
				if (StringUtils.isNotBlank(ids)) {
					PageData temp = new PageData();
					temp.put("MASTER_ID", SF_ID);
					temp.put("IDS", CommUtil.arr2InStr(ids));
					appSuggestionMapper.updateMasterId(temp);
				}
			}
			// String image = pd.getString("I_IMAGE_ADDR");
			// 将图片保存至
			// 将
			return WebResult.requestSuccess();
		}
	}

	/**
	 * 获取意见
	 * @return
	 */
	@RequestMapping(value = "/get")
	@ResponseBody
	public PageData getSuggestion(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("SYS_UI_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			List<PageData> picList = appSuggestionMapper.getSuggestionPic(pd);
			PageData suggest = appSuggestionMapper.getSuggestion(pd);
			PageData result = new PageData();
			suggest.put("picList", picList);
			result.put("suggest", suggest);
			return WebResult.requestSuccess(result);
		}
	}
}
