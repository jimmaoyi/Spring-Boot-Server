package com.maryun.restful.app.user.userinfo;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.maryun.mapper.app.user.userinfo.AppUserScanMapper;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.CommUtil;
import com.maryun.utils.WebResult;

/**
 * 类名称：AppUserScanRestful 创建人：MARYUN 创建时间：2016年2月13日
 * 
 * @version
 */
@RestController
@RequestMapping(value = "/app/user/scan")
public class AppUserScanRestful extends BaseRestful {
	@Autowired
	AppUserScanMapper appUserScanMapper;

	/**
	 * 用户浏览列表(专家)
	 * @return
	 */
	@RequestMapping(value = "/getScanList")
	@ResponseBody
	public PageData getScanList(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("UI_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			List<PageData> list = appUserScanMapper.getScanList(pd);
			return WebResult.requestSuccess(list);
		}
	}

	/**
	 * 添加获取更新
	 * @return
	 */
	@RequestMapping(value = "/addOrUpdate")
	@ResponseBody
	public PageData addScan(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("B_TYPE")) || StringUtils.isBlank(pd.getString("B_KEY_ID")) || StringUtils.isBlank(pd.getString("UI_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {// O_ID OE_STAR_LEVEL OE_TYPE UI_ID EVA_CONTENT
			PageData san = appUserScanMapper.getScan(pd);
			if (san != null) {
				appUserScanMapper.updateScan(pd);
			}
			else {
				appUserScanMapper.addScan(pd);
			}
			return WebResult.requestSuccess();
		}
	}

	/**
	 * 根据ID删除
	 * @return
	 */
	@RequestMapping(value = "/del")
	@ResponseBody
	public PageData delScanByIds(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("IDS"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {// O_ID OE_STAR_LEVEL OE_TYPE UI_ID EVA_CONTENT
			pd.put("IDS", CommUtil.arr2InStr(pd.getString("IDS")));
			appUserScanMapper.delScan(pd);
			return WebResult.requestSuccess();
		}
	}

	/**
	 * 删除我的所有浏览记录
	 * @return
	 */
	@RequestMapping(value = "/delSelf")
	@ResponseBody
	public PageData delScanAll(@RequestBody PageData pd) throws Exception {
		PageData result = new PageData();
		if (StringUtils.isBlank(pd.getString("UI_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {// O_ID OE_STAR_LEVEL OE_TYPE UI_ID EVA_CONTENT
			appUserScanMapper.delScanAll(pd);
			return WebResult.requestSuccess();
		}
	}
}
