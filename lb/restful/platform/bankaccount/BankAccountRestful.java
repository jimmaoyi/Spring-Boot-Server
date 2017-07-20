package com.maryun.lb.restful.platform.bankaccount;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maryun.lb.mapper.platform.bankaccount.BankAccountMapper;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.UuidUtil;
import com.maryun.utils.WebResult;
import com.sun.jmx.snmp.Timestamp;

/**
 * 类名称：BankAccountRestful 创建人：SunYongLiang 创建时间：2017年2月14日
 * 
 * @version
 */
@RestController
@RequestMapping(value = "/bankaccount")
public class BankAccountRestful extends BaseRestful {
	@Autowired
	private BankAccountMapper bankAccountMapper;

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
		pd = bankAccountMapper.find();
		return WebResult.requestSuccess(pd);
	}

	/**
	 * 跳转修改页面
	 * 
	 * @param pd
	 *            查询条件
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/toEdit")
	public PageData toEdit(@RequestBody PageData pd) throws Exception {
		
		return null;
	}

	/**
	 * 添加
	 * 
	 * @param pd
	 *            参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/saveAdd")
	public PageData saveAdd(@RequestBody PageData pd) throws Exception {
		pd.put("BA_ID", UuidUtil.get32UUID());
		bankAccountMapper.save(pd);
		pd.put("msg", "success");
		pd.put("succe", "save");
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
		bankAccountMapper.edit(pd);
		pd.put("msg", "success");
		pd.put("succe", "edit");
		return WebResult.requestSuccess(pd);
	}


}
