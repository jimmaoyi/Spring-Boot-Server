package com.maryun.lb.restful.jyb.guide;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.maryun.lb.mapper.jyb.guide.GuideMapper;
import com.maryun.mapper.system.user.UserMapper;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.UuidUtil;
import com.maryun.utils.WebResult;

/**
 * 类名称：GuideRestful 创建人：ChuMingZe 创建时间：2017年2月21日
 * 
 * @version
 */
@RestController
@RequestMapping(value = "/guide")
public class GuideRestful extends BaseRestful {
	@Autowired
	private GuideMapper guideMapper;
	
	@Autowired
	private UserMapper userMapper;
	
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
		lists = guideMapper.listPage(pd);
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
		pd = guideMapper.findById(pd);
		return WebResult.requestSuccess(pd);
	}
	
	/**
	 * 添加
	 * 
	 * @param pd
	 *            参数
	 * @return
	 * @throws Exception
	 */
	@Value("${system.role.guide}")
	public String ROLE_ID;
	@RequestMapping(value = "/saveAdd")
	public PageData saveAdd(@RequestBody PageData pd) throws Exception {
		boolean isHasByUserName = (boolean)pd.get("isHasByUserName");
		pd.put("GI_ID", UuidUtil.get32UUID());
		if(!isHasByUserName){
			PageData pd_sysUser = new PageData();
//			pd_sysUser.put("USER_ID", UuidUtil.get32UUID());
			pd_sysUser.put("USER_ID", pd.getString("USER_ID"));
			pd_sysUser.put("USERNAME", pd.getString("GI_EMP_NUM"));
			pd_sysUser.put("PASSWORD", pd.getString("PASSWORD"));
			pd_sysUser.put("NAME", pd.getString("GI_NAME"));
			pd_sysUser.put("STATUS", "0");
			pd_sysUser.put("SKIN", "default");
			pd_sysUser.put("PHONE", pd.getString("GI_PHONE"));
			pd_sysUser.put("TYPE", "2");
			pd_sysUser.put("CREATE_UID", pd.getString("CREATE_UID"));
			pd_sysUser.put("CHANGE_UID", pd.getString("CHANGE_UID"));
			pd_sysUser.put("DEL_STATE", pd.getString("DEL_STATE"));
			pd.put("ROLE_ID", ROLE_ID);
			pd.put("UR_ID", UuidUtil.get32UUID());
			userMapper.saveAddByOtherType(pd_sysUser);
			pd.put("SYS_UI_ID", pd_sysUser.getString("USER_ID"));
			guideMapper.save(pd);
			userMapper.saveUserRole(pd);
			pd.put("msg", "success");
		}else{
			pd.put("msg", "failed");
		}
		return WebResult.requestSuccess(pd);
	}
	
	/**
	 * 修改
	 * 
	 * @param pd
	 *            参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/saveEdit")
	public PageData saveEdit(@RequestBody PageData pd) throws Exception {
		boolean isHasByUserName = (boolean)pd.get("isHasByUserName");
		if(!isHasByUserName){
			PageData pd_sysUser = new PageData();
			pd_sysUser.put("USER_ID", pd.getString("USER_ID"));
			pd_sysUser.put("USERNAME", pd.getString("GI_EMP_NUM"));
			pd_sysUser.put("NAME", pd.getString("GI_NAME"));
			pd_sysUser.put("PHONE", pd.getString("GI_PHONE"));
			pd_sysUser.put("CHANGE_UID", pd.getString("CHANGE_UID"));
			userMapper.saveEditByOtherType(pd_sysUser);
			guideMapper.edit(pd);
			pd.put("msg", "success");
		}else{
			pd.put("msg", "failed");
		}
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
		if(null != s_IDS && !"".equals(s_IDS)){
			pd.put("IDSs", s_IDS.split(","));
			List<PageData> lists = guideMapper.findByIds(pd);
			String[] sa_sysUserIds = new String[lists.size()];
			if(lists != null && lists.size() > 0){
				int i_tmp = 0;
				for(PageData pd_tmp : lists){
					sa_sysUserIds[i_tmp] = pd_tmp.getString("SYS_UI_ID");
					i_tmp++;
				}
				userMapper.deleteAllU(sa_sysUserIds);
			}
			guideMapper.delete(pd);
			pd.put("msg", "success");
		}else{
			pd.put("msg", "failed");
		}
		return WebResult.requestSuccess(pd);
	}
}
