package com.maryun.lb.restful.jyb.registered;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.maryun.common.service.SendMsgService;
import com.maryun.lb.mapper.jyb.agent.AgentMapper;
import com.maryun.lb.mapper.jyb.hospitalExpertTmp.HospitalExpertTmpMapper;
import com.maryun.lb.mapper.jyb.registered.RegisteredMapper;
import com.maryun.mapper.system.user.UserMapper;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.UuidUtil;
import com.maryun.utils.WebResult;

/**
 * 类名称：HospitalExpertTmpRestful 创建人：SunYongLiang 创建时间：2017年2月20日
 * 
 * @version
 */
@RestController
@RequestMapping(value = "/registered")
public class RegisteredRestful extends BaseRestful {
	@Autowired
	private HospitalExpertTmpMapper hospitalExpertTmpMapper;
	@Autowired
	private AgentMapper agentMapper;
	@Autowired
	private UserMapper userMapper;
	@Resource
	private SendMsgService sendMsgService;
	@Autowired
	private RegisteredMapper registeredMapper;
	/**
	 * 分页查询
	 * 
	 * @param pd
	 * 查询、分页条件
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/pageSearch")
	public PageData pageSearch(@RequestBody PageData pd) throws Exception {
		List<PageData> lists = null;
		if (pd.getPageNumber() != 0) {
			PageHelper.startPage(pd.getPageNumber(), pd.getPageSize());
		}
		lists = hospitalExpertTmpMapper.listPage(pd);
		// 分页
		pd = this.getPagingPd(lists);
		// 结果集封装
		return WebResult.requestSuccess(pd);
	}
	/**
	 * 根据id查询
	 * 
	 * @param pd
	 * 查询、分页条件
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/findById")
	public PageData findById(@RequestBody PageData pd) throws Exception {
		pd = hospitalExpertTmpMapper.findById(pd);
		// 结果集封装
		return WebResult.requestSuccess(pd);
	}

	/**
	 * 重复专家统计信息
	 * 
	 * @param pd
	 *            查询、分布条件
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/mergePageSearch")
	public PageData mergePageSearch(@RequestBody PageData pd) throws Exception {
		List<PageData> lists = null;
		if (pd.getPageNumber() != 0) {
			PageHelper.startPage(pd.getPageNumber(), pd.getPageSize());
		}
		//lists = hospitalExpertLibsMapper.mergeListsPage(pd);
		pd = this.getPagingPd(lists);
		return WebResult.requestSuccess(pd);
	}

	/**
	 * 重复专家详情信息
	 * 
	 * @param pd
	 *            查询、分页条件
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/mergeViewPageSearch")
	public PageData mergeViewPageSearch(@RequestBody PageData pd) throws Exception {
		List<PageData> lists = null;
		if (pd.getPageNumber() != 0) {
			PageHelper.startPage(pd.getPageNumber(), pd.getPageSize());
		}
		/*lists = hospitalExpertLibsMapper.mergeViewPageSearch(pd);*/
		pd = this.getPagingPd(lists);
		return WebResult.requestSuccess(pd);
	}

	/**
	 * 设置专家模板
	 * 
	 * @param pd
	 *            参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/setTemplate")
	public PageData setTemplate(@RequestBody PageData pd) throws Exception {
		pd.put("HEL_ID", pd.getString("IDS"));
		pd.put("HE_ID", UuidUtil.get32UUID());
		pd.put("HE_TEMPLATE", "1");
		pd.put("HE_MERGE", "1");
		/*hospitalExpertLibsMapper.setExpertTemplateDelete(pd);
		hospitalExpertLibsMapper.setOtherExpertTemplate(pd);
		hospitalExpertLibsMapper.setExpertTemplate(pd);
		hospitalExpertLibsMapper.insert(pd);*/
		pd.put("msg", "success");
		return WebResult.requestSuccess(pd);
	}

	/**
	 * 跳转到对比合并页面
	 * 
	 * @param pd
	 *            参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/toMerge")
	public PageData toMerge(@RequestBody PageData pd) throws Exception {
		/*List<PageData> lists = hospitalExpertLibsMapper.compareLists(pd);*/
		return WebResult.requestSuccess(null);
	}
	/**
	 * 修改专家信息
	 * 
	 * @param pd
	 *            参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/saveEdit")
	public PageData saveEdit(@RequestBody PageData pd) throws Exception {
		hospitalExpertTmpMapper.saveEdit(pd);
		pd.put("msg", "success");
		return WebResult.requestSuccess(pd);
	}
	/**
	 * 添加信息
	 * 
	 * @param pd
	 *            参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/saveAdd")
	public PageData saveAdd(@RequestBody PageData pd) throws Exception {
		PageData pds = registeredMapper.findBySysUser(pd);
		if(pds != null){
			pd.put("USER_ID", pds.getString("USER_ID"));
		}else{
			registeredMapper.saveUser(pd);
		}
		registeredMapper.saveAdd(pd);
		return WebResult.requestSuccess(pd);
	}
	/**
	 * 验证验证码
	 * 
	 * @param pd
	 *            参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/verification")
	public PageData verification(@RequestBody PageData pd) throws Exception {
		pd.put("product", "注册");
		pd.put("recNum", pd.getString("UI_PHONE"));
		pd = sendMsgService.checkVerificationCode(pd);
		return WebResult.requestSuccess(pd);
	}
	/**
	 * 发送验证码
	 * 
	 * @param pd
	 *            参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/sendCode")
	public PageData sendCode(@RequestBody PageData pd) throws Exception {
		pd.put("type", "1");
		pd.put("product", "注册");
		pd.put("recNum", pd.getString("UI_PHONE"));
//		PageData npd=new PageData();
//		npd.put("product", "注册");
//		npd.put("type", "1");
//		npd.put("recNum", "15610566867");
		pd = sendMsgService.sendMsg(pd);
		return WebResult.requestSuccess(pd);
	}
	/**
	 * 查询手机号是否已被占用
	 * 
	 * @param pd
	 *            参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/selectPhone")
	public Object selectPhone(@RequestBody PageData pd) throws Exception {
		PageData map = new PageData();
		String errInfo = "true";//success
			if (registeredMapper.findByUId(pd) != null) {
				errInfo = "false";//error
			}
			map.put("result", errInfo); // 返回结果
			// 结果集封装
			return WebResult.requestSuccess(map);
	}
}
