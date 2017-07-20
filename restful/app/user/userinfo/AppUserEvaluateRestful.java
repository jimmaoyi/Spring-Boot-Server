package com.maryun.restful.app.user.userinfo;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.maryun.mapper.app.user.userinfo.AppUserEvaluateMapper;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.WebResult;

/**
 * 类名称：AppUserScanRestful 创建人：MARYUN 创建时间：2016年2月13日
 * 
 * @version
 */
@RestController
@RequestMapping(value = "/app/user/evaluate")
public class AppUserEvaluateRestful extends BaseRestful {
	@Autowired
	AppUserEvaluateMapper appUserEvaluateMapper;

	/**
	 * 用户评价列表(分页)
	 * @return
	 */
	@RequestMapping(value = "/getEvaluateList")
	@ResponseBody
	public PageData getEvaluateList(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("UI_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			List<PageData> list = appUserEvaluateMapper.getOrderEvaluateList(pd);
			return WebResult.requestSuccess(list);
		}
	}

	/**
	 * 添加评价
	 * @return
	 */
	@RequestMapping(value = "/add")
	@ResponseBody
	public PageData addEvaluate(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("UI_ID")) || StringUtils.isBlank(pd.getString("O_ID")) || StringUtils.isBlank(pd.getString("STAR_LEVEL"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {// O_ID OE_STAR_LEVEL OE_TYPE UI_ID EVA_CONTENT
			String starLevel = pd.getString("STAR_LEVEL");
			String[] starLevels = starLevel.split(",");
			String hel_sp_ai_id = pd.getString("OE_HEL_SP_AI_ID");
			String[] hel_sp_ai_ids = hel_sp_ai_id.split(",");
			
			
			if (starLevels.length < 3) {
				return WebResult.requestFailed(10039, "星级评定请全部填写！！", null);
			}
			else {
				//方法注释掉了，因为我也写了一个接口是插入评论内容的，而且插入评论内容之后直接改了订单状态
				//appUserEvaluateMapper.addEvaluate(pd);
				for (int i = 0; i < starLevels.length; i++) {
					System.out.println("---------------------------"+hel_sp_ai_ids[i]);
					PageData npd = new PageData();
					npd.put("UI_ID", pd.getString("UI_ID"));
					npd.put("O_ID", pd.getString("O_ID"));
					npd.put("OE_STAR_LEVEL", starLevels[i]);
					npd.put("OE_HEL_SP_AI_ID", hel_sp_ai_ids[i]);
					npd.put("OE_TYPE", i + 1);
					appUserEvaluateMapper.addEvaluateLevel(npd);
				}
				return WebResult.requestSuccess();
			}
		}
	}
	
	//查询已经评价的订单的不同角色医生，陪诊，代理的星级平均数
	@RequestMapping(value = "/checkOE_HEL_SP_AI_ID_AvgStar")
	@ResponseBody
	public PageData checkOE_HEL_SP_AI_ID_AvgStar(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("OE_TYPE"))||StringUtils.isBlank(pd.getString("OE_HEL_SP_AI_ID"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			PageData pdAvgNum = appUserEvaluateMapper.checkOE_HEL_SP_AI_ID_AvgStar(pd);
			return WebResult.requestSuccess(pdAvgNum);
		}
	}
	
	//根据不同角色的不同修改不同的角色的积分
	@RequestMapping(value = "/updateOE_HEL_SP_AI_Stars")
	@ResponseBody
	public PageData updateOE_HEL_SP_AI_Stars(@RequestBody PageData pd) throws Exception {
		if (StringUtils.isBlank(pd.getString("OE_TYPE"))) {
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		else {
			if("1".equals(pd.getString("OE_TYPE"))){//专家
				appUserEvaluateMapper.updateHEL_STARS(pd);
			}else if("2".equals(pd.getString("OE_TYPE"))){//代理
				appUserEvaluateMapper.updateSP_STARS(pd);
			}else if("3".equals(pd.getString("OE_TYPE"))){//陪诊
				appUserEvaluateMapper.updateAI_STARS(pd);
			}
			return WebResult.requestSuccess();
		}
	}
	
}
