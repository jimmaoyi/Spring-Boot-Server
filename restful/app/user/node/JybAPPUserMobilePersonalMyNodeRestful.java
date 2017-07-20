package com.maryun.restful.app.user.node;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.maryun.mapper.app.user.node.JybAPPUserMobilePersonalMyNodeMapper;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.UuidUtil;
import com.maryun.utils.WebResult;

@RestController
@RequestMapping(value = "/app/user/node/")
public class JybAPPUserMobilePersonalMyNodeRestful extends BaseRestful {

	@Autowired
	private JybAPPUserMobilePersonalMyNodeMapper JybAPPUserMobilePersonalMyNodeMapper;
	/**
	 * 根据订单的O_ID查询该订单的所有服务状态节点信息
	 * @param O_ID
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/nodeList")
	@ResponseBody
	public PageData nodeList(@RequestBody PageData pd) throws Exception{
		if(StringUtils.isBlank(pd.getString("O_ID"))){
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		//pd.put("O_ID", O_ID);
		List<PageData> nodeLists = null;
		nodeLists = JybAPPUserMobilePersonalMyNodeMapper.nodeList(pd);
		return WebResult.requestSuccess(nodeLists);
	}

	/**
	 * 查询用户最新的订单的订单节点状态中的状态更新时间最新的内容显示
	 * @param UI_ID
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/theNewServiceNodeStateByOrder")
	@ResponseBody
	public PageData theNewServiceNodeStateByOrder(@RequestBody PageData pd) throws Exception{
		if(StringUtils.isBlank(pd.getString("UI_ID"))){
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		
		PageData theNewServiceNode = JybAPPUserMobilePersonalMyNodeMapper.theNewServiceNodeStateByOrder(pd);
		return WebResult.requestSuccess(theNewServiceNode);
	}
	
	/**
	 * 陪诊人员编辑修改订单不同服务状态节点下的内容
	 * @param O_ID
	 * @param NM_ID
	 * @param KN_CONTENT
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/updateNodeContent")
	@ResponseBody
	public PageData updateNodeContent(@RequestBody PageData pd) throws Exception{
		if(StringUtils.isBlank(pd.getString("UI_ID"))||StringUtils.isBlank(pd.getString("NM_ID"))
				||StringUtils.isBlank(pd.getString("KN_CONTENT"))){
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		
		JybAPPUserMobilePersonalMyNodeMapper.updateNodeContent(pd);
		return WebResult.requestSuccess();
	}
	
	/**
	 *  陪诊人员更新不同服务节点的地点信息
	 * @param O_ID
	 * @param NM_ID
	 * @param KN_LOC
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/updateNodeAddress")
	@ResponseBody
	public PageData updateNodeAddress(@RequestBody PageData pd) throws Exception{
		if(StringUtils.isBlank(pd.getString("O_ID"))||StringUtils.isBlank(pd.getString("NM_ID"))
				||StringUtils.isBlank(pd.getString("KN_LOC"))){
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		
		JybAPPUserMobilePersonalMyNodeMapper.updateNodeAddress(pd);
		return WebResult.requestSuccess();
	}
	/**
	 *  陪诊人员更新不同服务节点的照片
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/updateNodePhoto")
	@ResponseBody
	public PageData updateNodePhoto(@RequestBody PageData pd) throws Exception{
		if(StringUtils.isBlank(pd.getString("O_ID"))||StringUtils.isBlank(pd.getString("NM_ID"))
				||StringUtils.isBlank(pd.getString("KN_PHOTO"))){
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		JybAPPUserMobilePersonalMyNodeMapper.updateNodePhoto(pd);
		return WebResult.requestSuccess();
	}
	
	
	//查询就诊模板的所有模板信息已下列列表的形式显示出现
	//select NM_ID,NM_TITLE,NM_ORDER from lb_jyb_node_modal where DEL_STATE='1' ORDER BY NM_ORDER
	@RequestMapping("/nodeModelLists")
	@ResponseBody
	public PageData nodeModelLists(@RequestBody PageData pd) throws Exception{
		
		List<PageData> nodeModelLists = JybAPPUserMobilePersonalMyNodeMapper.nodeModelLists();
		
		return WebResult.requestSuccess(nodeModelLists);
		
	}
	
	
	//根据模板的lb_jyb_node_model里的NM_ID和NM_TITLE（O_ID和KN_TIME要注意）去插入LB_JYB_KEY_NODE中
	//O_ID,NM_ID,NM_NAME,KN_LOC,KN_PHOTO,KN_CONTENT
	@RequestMapping("/accompanyInsertKeyNodeInfor")
	@ResponseBody
	public PageData accompanyInsertKeyNodeInfor(@RequestBody PageData pd) throws Exception{
		
		if(StringUtils.isBlank(pd.getString("O_ID"))||StringUtils.isBlank(pd.getString("NM_ID"))
				||StringUtils.isBlank(pd.getString("KN_CONTENT"))){
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		
		//如果没有数据，还有修改此数据的接口
		if(pd.getString("KN_LOC") == null) pd.put("KN_LOC", "");
		if(pd.getString("KN_PHOTO") == null) pd.put("KN_PHOTO", "");
		
		pd.put("KN_ID", UuidUtil.get32UUID());
		JybAPPUserMobilePersonalMyNodeMapper.accompanyInsertKeyNodeInfor(pd);
		
		return WebResult.requestSuccess();
		
	}
	
	//可以在确定保存的时候执行该方法根据O_ID和NM_ID
	//插入数据之后紧接着修改confimStatus状态为1
	@RequestMapping("/updateKeyNodeConfimStatus")
	@ResponseBody
	public PageData updateKeyNodeConfimStatus(@RequestBody PageData pd) throws Exception{
		
		if(StringUtils.isBlank(pd.getString("KN_ID"))){
			return WebResult.requestFailed(10001, "参数缺失！", null);
		}
		
		JybAPPUserMobilePersonalMyNodeMapper.updateKeyNodeConfimStatus(pd);
		
		return WebResult.requestSuccess();
		
	}
}
