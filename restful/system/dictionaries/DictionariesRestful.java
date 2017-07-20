package com.maryun.restful.system.dictionaries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.maryun.mapper.system.dictionaries.DictionariesMapper;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.service.system.dictionaries.DictionariesService;
import com.maryun.utils.BootstrapUtils;
import com.maryun.utils.ListUtil;
import com.maryun.utils.WebResult;

/**
 * 
 * @ClassName: DictionariesRestful 
 * @Description: 数据字典
 * @author SR 
 * @date 2017年3月2日
 */
@RestController
@RequestMapping("/dic")
public class DictionariesRestful extends BaseRestful {
	@Autowired
	private DictionariesMapper dictionariesMapper;
	/**
	 * 
	 * @Description: 获得展示的树形结构
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getTree")
	public Object getDicTree(@RequestBody PageData pd) throws Exception {
		List dicTreeList = BootstrapUtils.getTree(dictionariesMapper.list(null), "0", "数据字典", "ID", "NAME", "PID",
				"code", "CODE", "levels", "LEVELS");
		// 结果集封装
		return WebResult.requestSuccess(dicTreeList);
	}
	/**
	 * 
	 * @Description: 去添加页面
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/toAdd")
	public Object toAdd(@RequestBody PageData pd) throws Exception {
		return WebResult.requestSuccess(pd);
	}
	/**
	 * 
	 * @Description: 去编辑页面
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/toEdit")
	public Object findDic(@RequestBody PageData pd) throws Exception {
		List dicList = dictionariesMapper.list(pd);
		// 结果集封装
		return WebResult.requestSuccess(dicList.get(0));
	}

	/**
	 * 
	 * @Description: 保存添加
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/saveAdd")
	public Object saveAdd(@RequestBody PageData pd) throws Exception {
		pd.put("id", get32UUID());
		String p_code = pd.getString("p_code");

		if (StringUtils.isNotBlank(p_code)) {
			pd.put("pcode", p_code + "_" + pd.getString("code"));
		} else {
			pd.put("pcode", pd.getString("code"));
			pd.put("levels", "1");
			pd.put("pid", "0");
		}
		dictionariesMapper.save(pd);
		// 结果集封装
		return WebResult.requestSuccess();
	}

	/**
	 * 
	 * @Description: 保存修改
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/saveEdit")
	public Object saveEdit(@RequestBody PageData pd) throws Exception {
		String code = pd.getString("code");
		String pCode = pd.getString("pcode");

		if (pCode.indexOf("_") > 0) {
			pCode = pCode.substring(0, pCode.lastIndexOf("_")) + "_" + code;
		} else {
			pCode = code;
		}
		pd.put("pcode", pCode);

		dictionariesMapper.edit(pd);
		// 结果集封装
		return WebResult.requestSuccess();
	}

	/**
	 * 
	 * @Description: 批量删除
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/delete")
	public Object delete(@RequestBody PageData pd) throws Exception {
		Map<String, String> msg = new HashMap<String, String>();
		String[] idsArr = ListUtil.getSubtreeStrIdsArr(
				ListUtil.List2TreeMap(dictionariesMapper.list(null), "ID", "PID"), "ID", pd.getString("id"));
		dictionariesMapper.delete(idsArr);
		// 结果集封装
		msg.put("msg", "ok");
		return WebResult.requestSuccess(msg);
	}

	@RequestMapping(value = "/getSubTree")
	public Object getSubTree(@RequestBody PageData pd) throws Exception {
		List dicList = dictionariesMapper.getSubDicList(pd);
		// 结果集封装
		return WebResult.requestSuccess(dicList);
	}

	/**
	 * 
	 * @Description: 在新增子节点前判断父节点的code是否与其他节点存在重复，
	 * 如果存在重复则无法进行缓存
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/hasSameCode")

	public Object hasSameCode(@RequestBody PageData pd) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		String errInfo = "success";
		pd = this.getPageData();
		if (dictionariesMapper.findSameCode(pd).size() > 0) {
			errInfo = "error";
		}
		map.put("result", errInfo); // 返回结果
		// 结果集封装
		return WebResult.requestSuccess(map);
	}

	/**
	 * 
	 * @Description: 判断当前节点是否有子节点
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/hasChild")
	public Object hasChild(@RequestBody PageData pd) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		String errInfo = "true";
		pd = this.getPageData();
		if (dictionariesMapper.getSubDicList(pd).size() > 0) {
			errInfo = "true";
		} else {
			errInfo = "false";
		}
		map.put("result", errInfo); // 返回结果
		// 结果集封装
		return WebResult.requestSuccess(errInfo);
	}

	/**
	 * 
	 * @Description: 根据数据字典code,获取当前Code下属所有子类
	 * @param pd
	 * @return
	 * @throws Exception
	 */

	@RequestMapping(value = "/getDicForSelect")
	public Object getDicForSelect(@RequestBody PageData pd) throws Exception {
		List dicList = dictionariesMapper.getSubDicListByParentCode(pd);
		// 结果集封装
		return WebResult.requestSuccess(dicList);
	}
	
	/**
	 * 
	 * @Description: 根据全国行政地区数据字典code,
	 * 获取当前Code下属所有子类
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getDicAreaForSelect")
	public Object getDicAreaForSelect(@RequestBody PageData pd) throws Exception {
		List dicList = dictionariesMapper.getSubDicAreaListByParentCode(pd);
		// 结果集封装
		return WebResult.requestSuccess(dicList);
	}
	
	/**
	 * 获取省份相应的城市
	 * @param pd
	 * @return
	 */
	@RequestMapping(value = "/getSubDicAreaListByProvinceCode")
	public Object getSubDicAreaListByProvinceCode(@RequestBody PageData pd){
		List dicList = dictionariesMapper.getSubDicAreaListByProvinceCode(pd);
		// 结果集封装
		for(int i = 0; i < dicList.size(); i++){
			if(((PageData)dicList.get(i)).get("TEXT").equals("县")){
				dicList.remove(i);
				i--;
			}
		}
		return WebResult.requestSuccess(dicList);
	}
	
	/**
	 * 
	 * @Description: 根据code获取信息
	 * @param pdS
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getInfoByCode")
	public Object getInfoByCode(@RequestBody PageData pd) throws Exception {
		List dicList = dictionariesMapper.getInfoByCode(pd);
		// 结果集封装
		return WebResult.requestSuccess(dicList);
	}
}
