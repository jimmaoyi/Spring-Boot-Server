package com.maryun.lb.restful.jyb.hospitalGuide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.maryun.common.service.PushServiceForCall;
import com.maryun.lb.mapper.jyb.hospitalGuide.ExperLibsRelateMapper;
import com.maryun.lb.mapper.jyb.hospitalGuide.HospitalGuideMapper;
import com.maryun.lb.mapper.jyb.hospitalGuide.OrderHelMapper;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.UuidUtil;
import com.maryun.utils.WebResult;

/**
 * 类名称：AuthController 创建人：MARYUN 创建时间：2017年1月18日
 * 
 * @version
 */
@RestController
@RequestMapping(value = "/experLibsRelate")
public class ExperLibsRelateRestful extends BaseRestful {
	@Autowired
	private ExperLibsRelateMapper experLibsRelateMapper;
	@Autowired
	private HospitalGuideMapper hospitalGuideMapper;
	@Autowired
	private OrderHelMapper orderHelMapper;
	@Autowired
	PushServiceForCall pushServiceForCall;

	/**
	 * 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/findAgents")
	public PageData findAgents(@RequestBody PageData pd) throws Exception {
		List<PageData> lists = null;
		PageData pds = null;
		PageData pdes = hospitalGuideMapper.selectexpertid(pd);// 查询专家id
		/*pd.put("HEL_ID", pdes.getString("KEY_ID"));*///添加pd专家id
		if (pd.getPageNumber() != 0) {
			PageHelper.startPage(pd.getPageNumber(), pd.getPageSize());
		}
		if (pdes != null) {
			pdes.put("SP_NAME", pd.getString("SP_NAME"));
			pdes.put("O_ID", pd.getString("O_ID"));
			pdes.put("SP_REGISTER_ADDR", pd.getString("SP_REGISTER_ADDR"));
			lists = experLibsRelateMapper.listPage(pdes);// 查询代理商姓名
			pds = orderHelMapper.selectRecording(pd);// 查询是否有代理商记录
			if (pds != null) {
				pd.put("SP_ID", pds.getString("SP_ID"));
				pds = hospitalGuideMapper.selectcountent(pds);// 查询注意事项
				//pd.put("agencyrecords", pds);
			}
		}
		pd = this.getPagingPd(lists);
		pd.put("pds", pds);
		if(pdes != null){
			String HEL_ID = pdes.getString("KEY_ID");
			pd.put("HEL_ID", HEL_ID);
		}
		return WebResult.requestSuccess(pd);
	}

	@RequestMapping(value = "/toCheck")
	public PageData toCheck(@RequestBody PageData pd) throws Exception {
		PageData pds = null;
		pds = hospitalGuideMapper.selectStatus(pd);
		if (pds != null) {
			pd.put("judgment", "yes");
		} else {
			pd.put("judgment", "no");
		}
		return WebResult.requestSuccess(pd);
	}
	/**
	 * 
	 * @param 更改訂單注意事項以及訂單.代理商.專家分派記錄
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/saveEdit")
	public PageData saveEdit(@RequestBody PageData pd) throws Exception {
		PageData pds = null;
		PageData pdes = pd;
		pdes = orderHelMapper.selectGiId(pd);
		if(pdes != null){
			pd.put("GI_ID", pdes.getString("GI_ID"));
		}
		orderHelMapper.delrecording(pd);// 刪除記錄
		pd.put("OGS_ID", UuidUtil.get32UUID());// 生成uuid
		pd.put("OGS_STATUS", "0");
		pd.put("OGS_ASSIGN", "1");
		orderHelMapper.save(pd);// 保存
		hospitalGuideMapper.updatecountent(pd);// 更改备注
		experLibsRelateMapper.deleteRecording(pd);
		pd.put("OSHA_ID", UuidUtil.get32UUID());
		pd.put("OSHA_STATUS", "-1");
		experLibsRelateMapper.addRecording(pd);
		pd.put("KN_ID", UuidUtil.get32UUID());
		pd.put("NM_NAME", "导医分单");
		pd.put("KN_DAY","0" );
		pd.put("KN_CONTENT","导医已分单" );
		pd.put("CONFIRM_STATE", "0");
		pd.put("IS_SCHEDULE","0");
		pd.put("EDIT_STATE", "0");
		experLibsRelateMapper.saveKeyNode(pd);
		pd.put("succ", "success");// 设置成功字段
		return WebResult.requestSuccess(pd);
	}
	/**
	 * 
	 * @param 添加訂單注意事項以及訂單.代理商.專家分派記錄
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/saveAdd")
	public PageData saveAdd(@RequestBody PageData pd) throws Exception {
		PageData pds = null;
		PageData pdes = pd;
		pdes = orderHelMapper.selectGiId(pd);
		if(pdes != null){
			pd.put("GI_ID", pdes.getString("GI_ID"));
		}
		pd.put("OGS_ID", UuidUtil.get32UUID());
		pd.put("OGS_STATUS", "0");
		pd.put("OGS_ASSIGN", "1");
		orderHelMapper.save(pd);
		hospitalGuideMapper.updatecountent(pd);
		experLibsRelateMapper.deleteRecording(pd);
		pd.put("OSHA_ID", UuidUtil.get32UUID());
		pd.put("OSHA_STATUS", "-1");
		experLibsRelateMapper.addRecording(pd);
		pd.put("KN_ID", UuidUtil.get32UUID());
		pd.put("NM_NAME", "导医分单");
		pd.put("KN_DAY","0" );
		pd.put("KN_CONTENT","导医已分单" );
		pd.put("CONFIRM_STATE", "0");
		pd.put("IS_SCHEDULE","0");
		pd.put("EDIT_STATE", "0");
		experLibsRelateMapper.saveKeyNode(pd);
		pd.put("succ", "success");
		return WebResult.requestSuccess(pd);
	}
	/**
	 * 
	 * @param 查询是否有记录
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/findReCording")
	public PageData findReCording(@RequestBody PageData pd) throws Exception {
		pd = orderHelMapper.selectRecording(pd); // 查找是否有记录
		return WebResult.requestSuccess(pd);
	}
	/**
	 * 
	 * @param 给app端代理商推送一条数据
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/pushSP")
	public PageData pushSP(@RequestBody PageData pd) throws Exception {
		PageData pdes = pd;
		pdes = orderHelMapper.selectGiId(pd);
		if(pdes != null){
			pd.put("GI_ID", pdes.getString("GI_ID"));
		}
		// 推送时带的内容
	    Map<String, String> extra = new HashMap<String, String>();
	    Map<String, Object> extraContent = new HashMap<String, Object>();
	    extra.put("KEY_PARAM", JSON.toJSONString(extraContent));

	    // 推送接受者的id
	    List<String> userList = new ArrayList<String>();
	    userList.add(pd.getString("SP_ID"));

	    // 推送类型，如用户退给代理，这里就是3表示推给代理
	    pushServiceForCall.setAPPKEYANDSecret("3");

	    pushServiceForCall.alertTitleMsgExtraCallback("您有一条新单提醒", "您有一条新单提醒", extra, userList, "101",pd.getString("GI_ID"), pd.getString("O_ID"), "3");
	    
	    //推送消息给专家
	    pushExpert(pd);
	    pd.put("succ", "success");
		return WebResult.requestSuccess(pd);
	}
	/**
	 * 给朱娜家推送一条消息
	 */
	private void pushExpert(PageData pd)throws Exception{
		
		 //推送时所带的内容
		 Map<String, String> extra = new HashMap<String, String>();
		 Map<String, Object> extraContent = new HashMap<String, Object>();
		 extra.put("KEY_PARAM", JSON.toJSONString(extraContent));
		 
		 //推送接收者的id
		 List<String> userList = new ArrayList<String>();
		 userList.add(pd.getString("HEL_ID"));
		 
		 //推送类型
		 pushServiceForCall.setAPPKEYANDSecret("4");
		 
		 pushServiceForCall.alertTitleMsgExtraCallback("您有一条新单提醒", "您有一条新单提醒", extra, userList, "101", pd.getString("GI_ID"), pd.getString("O_ID"), "4");
		 
	}

}
