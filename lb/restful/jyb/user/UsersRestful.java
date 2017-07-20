package com.maryun.lb.restful.jyb.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.maryun.lb.mapper.jyb.user.UsersMapper;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.UuidUtil;
import com.maryun.utils.WebResult;

/**
 * 类名称：UserRestful 创建人：ChuMingZe 创建时间：2017年2月21日
 * 
 * @version
 */
@RestController
@RequestMapping(value = "/jyb/user")
public class UsersRestful extends BaseRestful {
	@Autowired
	private UsersMapper userMapper;

	/**
	 * 分页查询
	 * 
	 * @param pd
	 *            查询、分页条件
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/pageSearch")
	public PageData pageSearch(@RequestBody PageData pd) throws Exception {
		List<PageData> lists = null;
		if (pd.getPageNumber() != 0) {
			PageHelper.startPage(pd.getPageNumber(), pd.getPageSize());
		}
		lists = userMapper.listPage(pd);
		pd = this.getPagingPd(lists);
		return WebResult.requestSuccess(pd);
	}

	/**
	 * 按主键Id查询
	 * 
	 * @param pd
	 *            查询条件
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/findById")
	public PageData findById(@RequestBody PageData pd) throws Exception {
		pd = userMapper.findById(pd);
		return WebResult.requestSuccess(pd);
	}

	/**
	 * 删除
	 * 
	 * @param pd
	 *            参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/toDelete")
	public PageData toDelete(@RequestBody PageData pd) throws Exception {
		String s_IDS = pd.getString("IDS");
		if (null != s_IDS && !"".equals(s_IDS)) {
			pd.put("IDSs", s_IDS.split(","));
			userMapper.delete(pd);
			pd.put("msg", "success");
		} else {
			pd.put("msg", "failed");
		}
		return WebResult.requestSuccess(pd);
	}

	/**
	 * 修改冻结状态
	 * 
	 * @param pd
	 *            参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/toFreeze")
	public PageData toFreeze(@RequestBody PageData pd) throws Exception {
		userMapper.updateFreezeState(pd);
		pd.put("msg", "success");
		return WebResult.requestSuccess(pd);
	}

	/**
	 * 查询禁言用户原因及时间
	 * 
	 * @param pd
	 *            参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/toForbidSpeakReason")
	public PageData toForbidSpeakReason(@RequestBody PageData pd) throws Exception {
		pd = userMapper.findForbidSpeakLog(pd);
		return WebResult.requestSuccess(pd);
	}

	/**
	 * 修改禁言状态
	 * 
	 * @param pd
	 *            参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/toForbidSpeak")
	public PageData toForbidSpeak(@RequestBody PageData pd) throws Exception {
		if("0".equals(pd.getString("UI_FORBID_SPEAK"))){
			userMapper.updateForbidSpeakLog(pd);
		}else if("1".equals(pd.getString("UI_FORBID_SPEAK"))){
			pd.put("FS_ID", UuidUtil.get32UUID());
			userMapper.insertForbidSpeakLog(pd);
		}
		userMapper.updateForbidSpeakState(pd);
		pd.put("msg", "success");
		return WebResult.requestSuccess(pd);
	}
}
