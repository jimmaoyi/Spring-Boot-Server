package com.maryun.restful.system.role;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageHelper;
import com.maryun.mapper.system.button.ButtonMapper;
import com.maryun.mapper.system.menu.MenuMapper;
import com.maryun.mapper.system.role.RoleMapper;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.service.system.button.ButtonService;
import com.maryun.service.system.menu.MenuService;
import com.maryun.service.system.role.RoleService;
import com.maryun.utils.BootstrapUtils;
import com.maryun.utils.WebResult;

@RestController
@RequestMapping(value = "/role")
public class RoleRestful extends BaseRestful {
	@Autowired
	private RoleMapper roleMapper;
	
	@Autowired
	private MenuMapper menuMapper;
	
	@Autowired
	private ButtonMapper buttonMapper;

	@RequestMapping(value = "/pageSearch")
	public Object pageSearch(@RequestBody PageData pd)throws Exception {
		List<PageData> userList = null;
			if (pd.getPageNumber()!=0) {
				PageHelper.startPage(pd.getPageNumber(), pd.getPageSize());
			}
			userList = roleMapper.list(pd);
			// 分页
			pd = this.getPagingPd(userList);
			// 结果集封装
			return WebResult.requestSuccess(pd);
	}

	/**
	 * 去修改页面
	 */
	@RequestMapping(value = "/toEdit")
	public Object toEdit(@RequestBody PageData pd) throws Exception {
			pd = roleMapper.findById(pd); // 根据ID读取
			// 结果集封装
			return WebResult.requestSuccess(pd);
	}

	/**
	 * 保存角色
	 */
	@RequestMapping(value = "/saveAdd")
	public Object saveAdd(@RequestBody PageData pd) throws Exception {
			Map<String, Object> msg = new HashMap<String, Object>();
			ModelAndView mv = this.getModelAndView();
			pd.put("ROLE_ID", this.get32UUID()); // ID
			if (null == roleMapper.findById(pd)) {
				roleMapper.save(pd);
				msg.put("msg", "success");
			} else {
				msg.put("msg", "failed");
			}
			// 结果集封装
			return WebResult.requestSuccess(msg);
	}

	/**
	 * 修改用户
	 */
	@RequestMapping(value = "/saveEdit")
	public Object saveEdit(@RequestBody PageData pd) throws Exception {
			roleMapper.edit(pd);
			// 结果集封装
			return WebResult.requestSuccess();
	}

	/**
	 * 批量删除
	 */
	@RequestMapping(value = "/delete")
	public Object delete(@RequestBody PageData pd) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
			List<PageData> pdList = new ArrayList<PageData>();
			String USER_IDS = pd.getString("IDS");

			if (null != USER_IDS && !"".equals(USER_IDS)) {
				String ArrayUSER_IDS[] = USER_IDS.split(",");
				roleMapper.delete(ArrayUSER_IDS);
				map.put("msg", "ok");
			} else {
				map.put("msg", "no");
			}
			// 结果集封装
			return WebResult.requestSuccess(map);
	}

	/**
	 * 角色用户
	 * 
	 * @return @
	 */
	@RequestMapping(value = "/toUser")
	public Object toUser(@RequestBody PageData pd) throws Exception {
			Map<String, Object> msg = new HashMap<String, Object>();
			List lUserList = roleMapper.findNewUserByRole(pd);
			List rUserList = roleMapper.findUserByRole(pd);

			msg.put("roleid", pd.getString("ROLEID"));
			msg.put("lUserList", lUserList);
			msg.put("rUserList", rUserList);
			// 结果集封装
			return WebResult.requestSuccess(msg);
	}

	/**
	 * 构造treegrid结构
	 * 
	 * @return @
	 */
	@RequestMapping(value = "/getTreeGrid")
	public Object getButtonTreeGrid(@RequestBody PageData pd) throws Exception {
			// 利用util中的方法类构造treeGrid返回String直接拼入前台table标签中
			String treeGridData = BootstrapUtils.getTreeGrid(menuMapper.listAllMenu(null), // 所有的菜单项
					menuMapper.listRoleMenu(pd), // 当前角色的菜单
					buttonMapper.buttonlistPage(new PageData()), // 所有的按钮
					roleMapper.findGlobaLButtonByRole(pd), // 全局的勾选按钮
					roleMapper.findButtonByRole(pd)); // 当前角色所属菜单勾选按钮
			String data = URLEncoder.encode(treeGridData.toString(), "UTF-8") // 中文出现乱码则先编码，空格被编码成“+”
																				// 所以替换+号成空格所编码：
					.replace("+", "%20");
			// 结果集封装
			return WebResult.requestSuccess(data);
	}

	/**
	 * 够造角色菜单bootstrap treeview插件
	 * 
	 * @return @
	 */
	@RequestMapping(value = "/getTree")
	public Object getMenuTree(@RequestBody PageData pd) throws Exception {
			List treeList = BootstrapUtils.getCheckTree(menuMapper.getMenuList(null), roleMapper.findMenuByRole(pd), "0",
					"菜单", "MENU_ID", "MENU_NAME", "PARENT_ID");
			// 结果集封装
			return WebResult.requestSuccess(treeList);
	}

	/**
	 * 用户操作
	 */
	@RequestMapping(value = "/editUser")
	public Object editUser(@RequestBody PageData pd) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
			// 删除原有用户
			roleMapper.deleteUserByRole(pd);
			// 重新保存
			List<PageData> pdList = new ArrayList<PageData>();
			String USER_IDS = pd.getString("IDS");
			if (null != USER_IDS && !"".equals(USER_IDS)) {
				String ArrayUSER_IDS[] = USER_IDS.split(",");
				String role = pd.getString("ROLE_ID");
				List urList = new ArrayList();
				for (String id : ArrayUSER_IDS) {
					Map ur = new HashMap();
					ur.put("URID", get32UUID());
					ur.put("USERID", id);
					ur.put("ROLEID", role);
					urList.add(ur);
				}
				if (urList.size() > 0)
					roleMapper.addUser(urList);
				map.put("msg", "ok");
			} else {
				map.put("msg", "ok");
			}
			// 结果集封装
			return WebResult.requestSuccess(map);
	}

	/**
	 * 菜单操作
	 */
	@RequestMapping(value = "/editMenu")
	public Object editMenu(@RequestBody PageData pd) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
			String MENU_IDS = pd.getString("MENU_IDS");
			String role = pd.getString("ROLEID");

			if (null != MENU_IDS) {
				roleMapper.deleteMenu(pd); // 删除所有关联关系
				if (!MENU_IDS.equals("")) {
					String ArrayMenu_IDS[] = MENU_IDS.split(",");
					// 增加操作
					List rmList = new ArrayList();
					for (String id : ArrayMenu_IDS) {
						Map rm = new HashMap();
						rm.put("AUTH_ID", get32UUID());
						rm.put("ROLE_ID", role);
						rm.put("MENU_ID", id);
						rmList.add(rm);
					}
					roleMapper.addMenu(rmList);
				}
				map.put("msg", "ok");

			} else {
				map.put("msg", "no");
			}
			// 结果集封装
			return WebResult.requestSuccess(map);
	}

	/**
	 * 按钮操作
	 */
	@RequestMapping(value = "/editButton")
	public Object editButton(@RequestBody PageData pd)throws Exception  {
		Map<String, Object> map = new HashMap<String, Object>();
		
			String gb = pd.getString("gb");
			String mb = pd.getString("mb");
			String role = pd.getString("ROLE_ID");

			if (null == gb)
				gb = "";
			if (null == mb)
				mb = "";

			// 删除全局按钮
			roleMapper.deleteGlobalButtonByRole(pd);
			// 删除菜单按钮
			roleMapper.deleteMenuButtonByRole(pd);
			// 新增全局按钮
			String[] gbArr = gb.split(",");
			List<PageData> gList = new ArrayList<PageData>();
			for (String id : gbArr) {
				if (!"".equals(id)) {
					PageData pdData = new PageData();
					pdData.put("AUTH_ID", get32UUID());
					pdData.put("ROLE_ID", role);
					pdData.put("BUTTON_ID", id);
					gList.add(pdData);
				}
			}

			if (gList.size() > 0)
				roleMapper.addGlobalButton(gList);

			// 新增菜单按钮

			String[] mbArr = mb.split(":");
			List<PageData> mList = new ArrayList<PageData>();
			for (String id : mbArr) {
				if (!"".equals(id)) {
					String[] idArr = id.split(",");
					PageData pdData = new PageData();
					pdData.put("AUTH_ID", get32UUID());
					pdData.put("ROLE_ID", role);
					pdData.put("MENU_ID", idArr[0]);
					pdData.put("BUTTON_ID", idArr[1]);
					mList.add(pdData);
				}
			}

			if (mList.size() > 0)
				roleMapper.addMenuButton(mList);

			map.put("msg", "ok");
			// 结果集封装
			return WebResult.requestSuccess(map);
	}
}
