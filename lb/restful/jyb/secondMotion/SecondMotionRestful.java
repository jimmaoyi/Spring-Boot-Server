package com.maryun.lb.restful.jyb.secondMotion;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.maryun.mapper.system.dept.DeptMapper;
import com.maryun.mapper.system.role.RoleMapper;
import com.maryun.mapper.system.user.UserMapper;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.service.system.auth.AuthService;
import com.maryun.utils.BootstrapUtils;
import com.maryun.utils.UuidUtil;
import com.maryun.utils.WebResult;

/**
 * @author: Libra(Hyt)
 * Date: 2017年4月26日 下午4:02:08
 * Version: 1.3
 * @Description:  二级平台运营人员管理Restful
 */
@RestController
@RequestMapping(value = "jyb/motion")
public class SecondMotionRestful extends BaseRestful{

	@Autowired
	private  UserMapper userMapper;
	@Autowired
	private  RoleMapper roleMapper;
	@Autowired
	private  DeptMapper deptMapper;
	@Autowired
	private  AuthService authService;
	
	/**
	 * 二级平台运营人员 分页查询
	 * @param pd
	 * @return
	 */
	@RequestMapping(value = "/pageSearch")
	public PageData pageSearch(@RequestBody PageData pd){
		List<PageData> userList = null;
		if (pd.getPageNumber()!=0) {
			PageHelper.startPage(pd.getPageNumber(), pd.getPageSize());
		}
		userList = userMapper.motionlist(pd);
		// 分页
		pd = this.getPagingPd(userList);
		// 结果集封装
		return WebResult.requestSuccess(pd);
	}
	
	/**
	 * 去修改用户页面
	 */
	@RequestMapping(value = "/toEdit")
	public Object toEdit(@RequestBody PageData pd) throws Exception {
			pd = userMapper.findById(pd); // 根据ID读取
			String userId = pd.getString("USER_ID");
			List<PageData> deptList = userMapper.getDeptIdByUserId(userId);
			StringBuilder deptBuilder = new StringBuilder();
			for (int i = 0; i < deptList.size(); i++) {
				deptBuilder.append(deptList.get(i).getString("DEPT_ID"));
				if (i != deptList.size() - 1) {
					deptBuilder.append(",");
				}
			}
			String[] deptId = deptBuilder.toString().split(",");
			List<PageData> pdDatas = deptMapper.findByIds(deptId);
			String deptName = "";
			for (PageData pageData : pdDatas) {
				deptName += pageData.getString("DEPT_NAME") + ",";
			}
			if (pdDatas.size() > 0)
				deptName = deptName.substring(0, deptName.length() - 1);
			pd.put("DEPT_NAME", deptName);
			pd.put("DEPT_IDS", deptBuilder.toString());
			String area = userMapper.getAreaByUserId(userId);
			pd.put("HE_AREA", area);
			// 结果集封装
			return WebResult.requestSuccess(pd);
	}
	
	/**
	 * 其他页面增加用户时，自动添加系统用户
	 * @param pd 参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/saveAddByOtherType")
	public PageData saveAddByOtherType(@RequestBody PageData pd) throws Exception {
		pd.put("USER_ID", UuidUtil.get32UUID());
		userMapper.saveAddByOtherType(pd);
		pd.put("msg", "success");
		return WebResult.requestSuccess(pd);
	}
	
	/**
	 * 
	 * @Description: 更改密码
	 * @param pd
	 * @return
	 * @throws Exception    
	 * @return PageData    
	 * @throws
	 */
	@RequestMapping(value = "/editPwd")
	public PageData editPwd(@RequestBody PageData pd) throws Exception {
		userMapper.editPwd(pd);
		pd.put("msg", "success");
		return WebResult.requestSuccess(pd);
	}
	
	

	/**
	 * 保存用户
	 */
	@Value("${system.role.motion}")
	public String MOTOIN;
	@RequestMapping(value = "/saveAdd")
	public Object saveAdd(@RequestBody PageData pd) throws Exception{
			PageData retPd = new PageData();
			pd.put("LAST_LOGIN", ""); // 最后登录时间
			pd.put("IP", ""); // IP
			pd.put("STATUS", "0"); // 状态
			pd.put("SKIN", "default"); // 默认皮肤
			pd.put("CREATE_UID", pd.getString("USER_ID"));
			pd.put("CHANGE_UID", pd.getString("USER_ID"));
			pd.put("DEL_STATE", 1);
			
			/**	将新增用户增添到 sys_user_area */
			PageData areaPd = new PageData();
			areaPd.put("UA_ID", this.get32UUID());
			areaPd.put("UA_AREA", pd.getString("HE_AREA"));
			areaPd.put("USER_ID", pd.getString("USER_ID"));
			areaPd.put("CREATE_UID", pd.getString("USER_ID"));
			areaPd.put("CHANGE_UID", pd.getString("USER_ID"));
			areaPd.put("CREATE_TIME", pd.getString("CREATE_TIME"));
			areaPd.put("CHANGE_TIME", pd.getString("CHANGE_TIME"));
			areaPd.put("DEL_STATE", pd.getString("DEL_STATE"));
			userMapper.saveUserArea(areaPd);
			/** 赋予二级平台运营角色*/
			PageData rolePd = new PageData();
			rolePd.put("UR_ID", this.get32UUID());
			rolePd.put("USER_ID", pd.getString("USER_ID"));
			rolePd.put("ROLE_ID", MOTOIN);
			userMapper.saveUserRole(rolePd);
			
			if (null == userMapper.findByUId(pd)) {
				userMapper.save(pd);
				retPd.put("msg", "success");
			} else {
				retPd.put("msg", "failed");
			}

			// 结果集封装
			return WebResult.requestSuccess(pd);
	}

	/**
	 * 根据用户名查询用户
	 */
	@RequestMapping(value = "/findByUId")
	public Object findByUId(@RequestBody PageData pd) throws Exception{
			pd = userMapper.findByUId(pd);
			// 结果集封装
			return WebResult.requestSuccess(pd);
	}

	/**
	 * 根据用户名查询用户
	 */
	@RequestMapping(value = "/getUserByNameAndPwd")
	public Object getUserByNameAndPwd(@RequestBody PageData pd) throws Exception{
			pd = userMapper.getUserInfo(pd);
			// 结果集封装
			return WebResult.requestSuccess(pd);
	}
	
	/**
	 * 根据用户名查询用户
	 */
	@RequestMapping(value = "/getUserByUderName")
	public Object getUserByName(@RequestBody PageData pd) throws Exception{
		pd = userMapper.getUserInfoByUsername(pd);
		// 结果集封装
		return WebResult.requestSuccess(pd);
	}

	/**
	 * 修改用户
	 */
	@RequestMapping(value = "/saveEdit")
	public Object saveEdit(@RequestBody PageData pd) throws Exception{
			userMapper.edit(pd);
			/** 修改二级运营的地域信息  */
			userMapper.editUserArea(pd);

			PageData retPd = new PageData();
			retPd.put("msg", "success");
			// 结果集封装
			return WebResult.requestSuccess(retPd);
	}

	/**
	 * 批量删除
	 */
	@RequestMapping(value = "/delete")
	public Object delete(@RequestBody PageData pd) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
			List<PageData> pdList = new ArrayList<PageData>();
			String USER_IDS = pd.getString("IDS");

			if (null != USER_IDS && !"".equals(USER_IDS)) {
				String[] ArrayUSER_IDS = USER_IDS.split(",");
				userMapper.deleteAllU(ArrayUSER_IDS);
				for(String USER_ID : ArrayUSER_IDS){
					userMapper.deleteUserID(USER_ID);
				}
				userMapper.deleteMatchupByUserIDS(ArrayUSER_IDS);// 删除用户级联删除用户班组表中所有关联记录
				map.put("msg", "ok");
			} else {
				map.put("msg", "no");
			}
			// 结果集封装
			return WebResult.requestSuccess(map);
	}

	/**
	 * 判断用户名是否存在
	 */
	@RequestMapping(value = "/hasU")
	public Object hasU(@RequestBody PageData pd) throws Exception{
		PageData map = new PageData();
		String errInfo = "true";//用户名不存在
		if (userMapper.findByUId(pd) != null) {
			errInfo = "false";//用户名已存在
		}else{
			errInfo = "true";//用户名不存在
		}
		map.put("result", errInfo); // 返回结果
		// 结果集封装
		return WebResult.requestSuccess(map);
	}

	/**
	 * 判断邮箱是否存在
	 */
	@RequestMapping(value = "/hasE")
	public Object hasE(@RequestBody PageData pd) throws Exception{
		PageData map = new PageData();
		String errInfo = "success";
			if (userMapper.findByUE(pd) != null) {
				errInfo = "error";
			}
			map.put("result", errInfo); // 返回结果
			// 结果集封装
			return WebResult.requestSuccess(map);
	}

	/**
	 * 判断编码是否存在
	 */
	@RequestMapping(value = "/hasN")
	public Object hasN(@RequestBody PageData pd) throws Exception{
		PageData map = new PageData();
		String errInfo = "success";
			if (userMapper.findByUN(pd) != null) {
				errInfo = "error";
			}
			map.put("result", errInfo); // 返回结果
			// 结果集封装
			return WebResult.requestSuccess(map);
	}

	@RequestMapping(value = "/updateGuide")
	public Object updateGuide(@RequestBody PageData pd) throws Exception{
		PageData map = new PageData();
		userMapper.updateGuide(pd);
			map.put("result", "success"); // 返回结果
			// 结果集封装
			return WebResult.requestSuccess(map);
	}

	/**
	 * 够造班组树bootstrap treeview插件
	 * 
	 * @return @
	 * @throws Exception 
	 */
	@RequestMapping(value = "/getTree")
	public Object getDeptTree(@RequestBody PageData pd) throws Exception {
			String[] deptIds = pd.getString("DEPT_ID").split(",");
			List deptTreeList = BootstrapUtils.getCheckTree(deptMapper.list(pd),
					deptMapper.findByIds(deptIds), "0", "班组列表", "DEPT_ID", "DEPT_NAME", "PARENT_ID");
			// 结果集封装
			return WebResult.requestSuccess(deptTreeList);
	}

	/**
	 * 修改用户
	 */
	@RequestMapping(value = "/showRP")
	public Object showRP(@RequestBody PageData pd) throws Exception{
			PageData user = userMapper.findById(pd);
			String loginName = user.getString("USERNAME");
			List<String> roles = authService.getRolesListByUser(loginName);
			List<String> urls = authService.getUrlAuthListByUser(loginName);
			List<String> globalButton = authService.getGlobalBtnListByUser(loginName);
			List<String> urlButton = authService.getURLBtnListByUser(loginName);
			PageData retPd = new PageData();
			retPd.put("roles", roles);
			retPd.put("urls", urls);
			retPd.put("globalButton", globalButton);
			retPd.put("urlButton", urlButton);
			// 结果集封装
			return WebResult.requestSuccess(retPd);
	}	
	
	
}
