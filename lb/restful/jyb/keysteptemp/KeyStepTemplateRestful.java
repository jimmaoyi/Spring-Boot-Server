package com.maryun.lb.restful.jyb.keysteptemp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.maryun.lb.mapper.jyb.keysteptemp.KeystepTemplateMapper;
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
@RequestMapping(value = "/pc/keysteptemp")
public class KeyStepTemplateRestful extends BaseRestful {

	@Autowired
	private KeystepTemplateMapper keystepTemplateMapper;

	/**
	 * 分页查询
	 * 
	 * @param pd 查询、分页条件
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/pageSearch")
	public PageData pageSearch(@RequestBody PageData pd) throws Exception {
		List<PageData> lists = null;
		if (pd.getPageNumber() != 0) {
			PageHelper.startPage(pd.getPageNumber(), pd.getPageSize());
		}
		lists = keystepTemplateMapper.findKeystepTemp(pd);
		// 分页
		pd = this.getPagingPd(lists);
		// 结果集封装
		return WebResult.requestSuccess(pd);
	}

	/**
	 * 删除
	 * 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/delete")
	public PageData delete(@RequestBody PageData pd) throws Exception {
		String s_IDS = pd.getString("IDS");
		if (null != s_IDS && !"".equals(s_IDS)) {
			pd.put("IDSs", s_IDS.split(","));
			keystepTemplateMapper.delete(pd);
			pd.put("msg", "success");
		}
		else {
			pd.put("msg", "failed");
		}
		// 结果集封装
		return WebResult.requestSuccess(pd);
	}

	/**
	 * 跳转修改页面
	 * 
	 * @param pd 查询条件
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/toEdit")
	public PageData toEdit(@RequestBody PageData pd) throws Exception {
		pd = keystepTemplateMapper.findById(pd);
		return WebResult.requestSuccess(pd);
	}

	/**
	 * 添加
	 * 
	 * @param pd 参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/saveAdd")
	public PageData saveAdd(@RequestBody PageData pd) throws Exception {
		pd.put("NM_ID", UuidUtil.get32UUID());
		keystepTemplateMapper.save(pd);
		pd.put("msg", "success");
		return WebResult.requestSuccess(pd);
	}

	/**
	 * 修改
	 * 
	 * @param pd 参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/saveEdit")
	public PageData saveEdit(@RequestBody PageData pd) throws Exception {
		keystepTemplateMapper.edit(pd);
		pd.put("msg", "success");
		return WebResult.requestSuccess(pd);
	}
}
