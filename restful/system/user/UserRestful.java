package com.maryun.restful.system.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.maryun.mapper.system.dept.DeptMapper;
import com.maryun.mapper.system.user.UserMapper;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.service.system.auth.AuthService;
import com.maryun.utils.BootstrapUtils;
import com.maryun.utils.UuidUtil;
import com.maryun.utils.WebResult;

/**
 * 类名称：UserController 创建人：MARYUN 创建时间：2014年6月28日
 * 
 * @version
 */
@RestController
@RequestMapping(value = "/user")
public class UserRestful extends BaseRestful {
	@Autowired
	private UserMapper userMapper;
	@Resource(name = "authService")
	private AuthService authService;
	@Autowired
	private DeptMapper deptMapper;

	@RequestMapping(value = "/pageSearch")
	public Object pageSearch(@RequestBody PageData pd) throws Exception {
		List<PageData> userList = null;
			if (pd.getPageNumber()!=0) {
				PageHelper.startPage(pd.getPageNumber(), pd.getPageSize());
			}
			userList = userMapper.list(pd);
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
	@RequestMapping(value = "/saveAdd")
	public Object saveAdd(@RequestBody PageData pd) throws Exception{
			PageData retPd = new PageData();
			pd.put("LAST_LOGIN", ""); // 最后登录时间
			pd.put("IP", ""); // IP
			pd.put("STATUS", "0"); // 状态
			pd.put("SKIN", "default"); // 默认皮肤
			// pd.put("PASSWORD",new SimpleHash("SHA-1",
			// pd.getString("USERNAME"), pd.getString("PASSWORD")).toString());
			pd.put("CREATE_UID", "");
			pd.put("CHANGE_UID", "");
			pd.put("DEL_STATE", 1);
			// 将用户的所有班组放入用户班组关联表中
			if (StringUtils.isNotBlank(pd.getString("DEPT_IDS"))) {
				String[] deptIds = pd.getString("DEPT_IDS").split(",");
				for (String deptId : deptIds) {
					PageData param = new PageData();
					param.put("UDM_ID", this.get32UUID());
					param.put("USER_ID", pd.getString("USER_ID"));
					param.put("DEPT_ID", deptId);
					param.put("CREATE_UID", "");
					param.put("CHANGE_UID", "");
					param.put("DEL_STATE", 1);
					userMapper.saveUserDeptMatchup(param);
				}
			}
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
			userMapper.deleteMatchupByUserID(pd); // 删除该用户关联的所有班组
			// 将用户的所有班组更新在用户班组关联表中
			if (StringUtils.isNotBlank(pd.getString("DEPT_IDS"))) {
				String[] deptIds = pd.getString("DEPT_IDS").split(",");
				// 重新建立用户和班组的关联关系
				for (String deptId : deptIds) {
					PageData param = new PageData();
					param.put("UDM_ID", this.get32UUID());
					param.put("USER_ID", pd.getString("USER_ID"));
					param.put("DEPT_ID", deptId);
					param.put("CREATE_UID", "");
					param.put("CHANGE_UID", "");
					param.put("DEL_STATE", 1);
					userMapper.saveUserDeptMatchup(param);
				}
			}
			pd.put("CHANGE_UID", "");
			userMapper.edit(pd);

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