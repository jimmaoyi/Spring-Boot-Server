package com.maryun.restful.system.auth;

import java.util.HashSet;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maryun.model.PageData;
import com.maryun.service.system.auth.AuthService;
import com.maryun.utils.WebResult;

/**
 * 类名称：AuthController 创建人：MARYUN 创建时间：2017年1月18日
 * 
 * @version
 */
@RestController
@RequestMapping(value = "/auth")
public class AuthRestful {
	@Resource(name = "authService")
	private AuthService authService;

	@RequestMapping(value = "/getUrlAuthByUser")
	public Object getUrlAuthByUser(@RequestBody PageData pd) {
		try {
			pd = authService.getUrlAuthByUser(pd);
			// 结果集封装
			return WebResult.requestSuccess(pd);
		} catch (Exception e) {
			e.printStackTrace();
			return WebResult.requestFailed(500, "服务器错误", null);
		}
	}

	@RequestMapping(value = "/getUrlAuthListByUser")
	public Object getUrlAuthListByUser(@RequestBody String useName) {
		try {
			List<String> list = authService.getUrlAuthListByUser(useName);
			// 结果集封装
			return WebResult.requestSuccess(list);
		} catch (Exception e) {
			e.printStackTrace();
			return WebResult.requestFailed(500, "服务器错误", null);
		}
	}

	@RequestMapping(value = "/getRolesByUser")
	public Object getRolesByUser(@RequestBody String useName) {
		try {
			HashSet<String> set = authService.getRolesByUser(useName);
			// 结果集封装
			return WebResult.requestSuccess(set);
		} catch (Exception e) {
			e.printStackTrace();
			return WebResult.requestFailed(500, "服务器错误", null);
		}
	}

	@RequestMapping(value = "/getRolesListByUser")
	public Object getRolesListByUser(@RequestBody String useName) {
		try {
			List<String> list = authService.getRolesListByUser(useName);
			// 结果集封装
			return WebResult.requestSuccess(list);
		} catch (Exception e) {
			e.printStackTrace();
			return WebResult.requestFailed(500, "服务器错误", null);
		}
	}

	@RequestMapping(value = "/getAuthListByUser")
	public Object getAuthListByUser(@RequestBody String userName) {
		try {
			List<String> list = authService.getAuthListByUser(userName);
			// 结果集封装
			return WebResult.requestSuccess(list);
		} catch (Exception e) {
			e.printStackTrace();
			return WebResult.requestFailed(500, "服务器错误", null);
		}
	}

	@RequestMapping(value = "/getGlobalBtnListByUser")
	public Object getGlobalBtnListByUser(@RequestBody String userName) {
		try {
			List<String> list = authService.getGlobalBtnListByUser(userName);
			// 结果集封装
			return WebResult.requestSuccess(list);
		} catch (Exception e) {
			e.printStackTrace();
			return WebResult.requestFailed(500, "服务器错误", null);
		}
	}

	@RequestMapping(value = "/getURLBtnListByUser")
	public Object getURLBtnListByUser(@RequestBody String userName) {
		try {
			List<String> list = authService.getURLBtnListByUser(userName);
			// 结果集封装
			return WebResult.requestSuccess(list);
		} catch (Exception e) {
			e.printStackTrace();
			return WebResult.requestFailed(500, "服务器错误", null);
		}
	}

	@RequestMapping(value = "/getButtonAuthByUser")
	public Object getButtonAuthByUser(@RequestBody PageData pd) {
		try {
			PageData auth = authService.getButtonAuthByUser(pd);
			// 结果集封装
			return WebResult.requestSuccess(auth);
		} catch (Exception e) {
			e.printStackTrace();
			return WebResult.requestFailed(500, "服务器错误", null);
		}
	}

}
