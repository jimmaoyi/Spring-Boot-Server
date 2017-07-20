package com.maryun.lb.restful.jyb.hospDept;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.github.pagehelper.PageHelper;
import com.maryun.lb.mapper.jyb.hospDept.HospDeptMapper;
import com.maryun.lb.mapper.jyb.image.ImageMapper;
import com.maryun.lb.restful.jyb.datainterface.XiWeiErDataSync;
import com.maryun.mapper.system.fileuploads.FileUploadsMapper;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.ChineseToEnglish;
import com.maryun.utils.HttpUtils;
import com.maryun.utils.HttpUtils.UHeader;
import com.maryun.utils.UuidUtil;
import com.maryun.utils.WebResult;
/**
 * 类名称：HospDeptRestful 创建人：SunYongLiang 创建时间：2017年2月23日
 * 
 * @version
 */
@RestController
@RequestMapping("/hospDept")
public class HospDeptRestful extends BaseRestful {
	@Autowired
	private HospDeptMapper hospDeptMapper;
	@Autowired
	private FileUploadsMapper fileUploadsMapper;
	@Autowired
	private ImageMapper imageMapper;
	/**
	 * 查询
	 * 
	 * @param pd
	 *            查询、分页条件+
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getAllHospDept")
	public Object getAllHospDept(@RequestBody PageData pd) throws Exception {
		List<PageData> lists = null;
		lists = hospDeptMapper.getAllHospDept();
		return WebResult.requestSuccess(lists);
	}
	/**
	 * 查询并且现在列表中
	 * 
	 * @param pd
	 *            查询、分页条件
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/listPage")
	public Object listPage(@RequestBody PageData pd) throws Exception {
		List<PageData> lists = null;
		if (pd.getPageNumber() != 0) {
			PageHelper.startPage(pd.getPageNumber(), pd.getPageSize());
		}
		lists = hospDeptMapper.listPage(pd);
		pd = this.getPagingPd(lists);		
		return WebResult.requestSuccess(pd);
	}
	/**
	 *同步科室数据( 删除数据查询数据,将数据保存到数据库中,并将其返回)
	 * 
	 * @param pd
	 *            查询、分页条件
	 * @return
	 * @throws Exception
	 */
	@Value("${lbaner.sync.xiweier.url.dept}")
	public String syncUrlDeptXiWeiEr;
	@RequestMapping(value = "/saveAddByXiWeiEr")
	public Object saveAddByXiWeiEr(@RequestBody PageData pd) throws Exception {
		hospDeptMapper.deleteAll(pd);
		String url = "http://hl-iuc.snctv.com.cn:8088/iepg/getCatalog?terminalType=0&parentId=60040";
		String result = HttpUtils.getResponse(url, new HashMap(), new ArrayList<UHeader>());
		JSONObject jo =JSON.parseObject(result);
		XiWeiErDataSync xziweierdata = new XiWeiErDataSync();
		List<PageData> pdList=xziweierdata.getCatalogDatas("");
		List<PageData> list = new ArrayList<PageData> ();
		for (PageData pageData : pdList) {
			pageData.put("HD_ID", UuidUtil.get32UUID());
			pageData.put("CREATE_UID", pd.getString("CREATE_UID"));
			hospDeptMapper.saveAdd(pageData);
			list.add(pageData);
		}
		return WebResult.requestSuccess(list);
	}
	/**
	 *同步同洲数据( 删除数据查询数据,将数据保存到数据库中,并将其返回)
	 * 
	 * @param pd
	 *            查询、分页条件
	 * @return
	 * @throws Exception
	 */
	//百途数据接口-科室
	@Value("${lbaner.sync.bytue.url.dept}")
	public String syncUrlDeptByTue;
	@RequestMapping(value = "/saveAddByTu")
	public Object saveAddByTu(@RequestBody PageData pd) throws Exception {
		hospDeptMapper.deleteAll(pd);
		String url = syncUrlDeptByTue;
		String result = HttpUtils.getResponse(url, new HashMap(), new ArrayList<UHeader>());
		JSONObject jo =JSON.parseObject(result);
		List<PageData> pdList=JSON.parseObject(jo.getString("Catalog"),new TypeReference<List<PageData>>() {});
		List<PageData> list = new ArrayList<PageData> ();
		for (PageData pageData : pdList) {
			pageData.put("HD_ID", UuidUtil.get32UUID());
			pageData.put("CREATE_UID", pd.getString("CREATE_UID"));
			hospDeptMapper.saveAdd(pageData);
			list.add(pageData);
		}
		return WebResult.requestSuccess(list);
	}
	/**
	 *同步百途数据( 删除数据查询数据,将数据保存到数据库中,并将其返回)
	 * 
	 * @param pd
	 *            查询、分页条件
	 * @return
	 * @throws Exception
	 */
	//同洲数据接口-科室
	@Value("${lbaner.sync.tongzhou.url.dept}")
	public String syncUrlDeptTongZhou;
	@RequestMapping(value = "/saveAddTongZhou")
	public Object saveAddTongZhou(@RequestBody PageData pd) throws Exception {
		hospDeptMapper.deleteAll(pd);
		String url = syncUrlDeptTongZhou;
		String result = HttpUtils.getResponse(url, new HashMap(), new ArrayList<UHeader>());
		JSONObject jo =JSON.parseObject(result);
		List<PageData> pdList=JSON.parseObject(jo.getString("Catalog"),new TypeReference<List<PageData>>() {});
		List<PageData> list = new ArrayList<PageData> ();
		for (PageData pageData : pdList) {
			pageData.put("HD_ID", UuidUtil.get32UUID());
			pageData.put("CREATE_UID", pd.getString("CREATE_UID"));
			hospDeptMapper.saveAdd(pageData);
			list.add(pageData);
		}
		return WebResult.requestSuccess(list);
	}
	/**
	 * 将之前图片显示出来
	 * 
	 * @param pd
	 *            查询、分页条件
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/toEdit")
	public Object toEdit(@RequestBody PageData pds) throws Exception {
		PageData pd = new PageData ();
		pd = fileUploadsMapper.get(pds);
		pd.put("HD_ID", pds.getString("HD_ID"));
		return WebResult.requestSuccess(pd);
	}
	/**
	 * 将之前图片显示出来
	 * 
	 * @param pd
	 *            查询、分页条件
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/saveEditLocation")
	public Object saveEditLocation(@RequestBody PageData pd) throws Exception {
		hospDeptMapper.saveEditLocation(pd);
		pd.put("success", "success");
		return WebResult.requestSuccess(pd);
	}
	/**
	 * 添加科室
	 * 
	 * @param pd
	 *            查询、分页条件
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/saveAddLocation")
	public Object saveAddLocation(@RequestBody PageData pd) throws Exception {
		String OMS_ID = ChineseToEnglish.getPingYin(pd.getString("HD_NAME"));
		pd.put("OMS_ID", OMS_ID);
		pd.put("HD_ID", UuidUtil.get32UUID());
		hospDeptMapper.saveAddLocation(pd);
		pd.put("success", "success");
		return WebResult.requestSuccess(pd);
	}
	/**
	 * 根据id查询科室
	 * 
	 * @param pd
	 *            查询、分页条件
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/selectById")
	public Object selectById(@RequestBody PageData pd) throws Exception {
		pd = hospDeptMapper.selectById(pd);
		return WebResult.requestSuccess(pd);
	}
	/**
	 * 删除信息
	 * 
	 * @param pd
	 *            查询、分页条件
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/deleteMany")
	public Object deleteMany(@RequestBody PageData pd) throws Exception {
		String s_IDS = pd.getString("HD_ID");
		if(null != s_IDS && !"".equals(s_IDS)){
			pd.put("IDSs", s_IDS.split(","));
			hospDeptMapper.deleteMany(pd);
			pd.put("msg", "success");
		}else{
			pd.put("msg", "failed");
		}
		return WebResult.requestSuccess(pd);
	}
	
}
