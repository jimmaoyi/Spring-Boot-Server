package com.maryun.common.restful;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.IdcardValidator;
import com.maryun.utils.WebResult;

/**
 * 类名称：ValidatesRestful 创建人：ChuMingZe 创建时间：2017年2月21日
 * 
 * @version
 */
@RestController
@RequestMapping(value = "/validates")
public class ValidatesRestful extends BaseRestful {

	/**
	 * 验证身份证
	 * 
	 * @param pd 参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/validateIdCard")
	public PageData validateIdCard(@RequestBody PageData pd) throws Exception {
		IdcardValidator iv = new IdcardValidator();
		boolean bool_res = true;
		if (!iv.isValidatedAllIdcard(pd.getString("IDCARD"))) {
			bool_res = false;
		}
		return WebResult.requestSuccess(bool_res);
	}
}
