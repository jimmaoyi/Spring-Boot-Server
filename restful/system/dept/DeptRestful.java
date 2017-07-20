package com.maryun.restful.system.dept;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.maryun.mapper.system.dept.DeptMapper;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.service.system.dept.DeptService;
import com.maryun.utils.BootstrapUtils;
import com.maryun.utils.ListUtil;
import com.maryun.utils.UuidUtil;
import com.maryun.utils.WebResult;
/**
 * 
 * @ClassName: DeptRestful 
 * @Description: 部门Restful接口
 * @author SR 
 * @date 2017年3月2日
 */
@RestController
@RequestMapping("/dept")
public class DeptRestful extends BaseRestful {
	@Autowired
	private DeptMapper deptMapper;
	/**
	 * 
	 * @Description: 去添加部门页面
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/toAdd")
	public Object  toAdd(@RequestBody PageData pd) throws Exception{
		return WebResult.requestSuccess(pd);
	}
	/**
	 * 
	 * @Description: 去修改页面
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/toEdit")
	public Object  toEdit(@RequestBody PageData pd) throws Exception{
			List<PageData> deptList = deptMapper.list(pd);
			PageData pageData =null;
			//结果集封装
			if(deptList.size()==1) {
				pageData = deptList.get(0);
			}
			return WebResult.requestSuccess(pageData);
	}
	/**
	 * 
	 * @Description: 获得树形结构
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/getTree")
	public Object getDeptTree(@RequestBody PageData pd) throws Exception{
			List deptTreeList = BootstrapUtils.getTree(deptMapper.list(null), "0", "部门", "DEPT_ID", "DEPT_NAME", "PARENT_ID");
			//结果集封装
			
			return WebResult.requestSuccess(deptTreeList);
	}
	/**
	 * 
	 * @Description: 保存添加
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/saveAdd")
	public Object saveAdd(@RequestBody PageData pd) throws Exception{
			pd.put("id", UuidUtil.get32UUID());
			deptMapper.save(pd);
			//结果集封装
			return WebResult.requestSuccess();
	}
	/**
	 * 
	 * @Description: 保存修改
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/saveEdit")
	public Object saveEdit(@RequestBody PageData pd) throws Exception{
			deptMapper.edit(pd);
			//结果集封装
			return WebResult.requestSuccess();
	}
	/**
	 * 
	 * @Description:批量删除
	 * @param pd
	 * @return
	 */
	@RequestMapping(value="/delete")
	public Object delete(@RequestBody PageData pd){
		String[] idsArr = ListUtil.getSubtreeIdsArr(ListUtil.List2TreeMap(deptMapper.list(null), "DEPT_ID", "PARENT_ID"), 
                "DEPT_ID", 
                pd.getString("id")
                );

			deptMapper.delete(idsArr);
				//结果集封装
				Map msg =new HashMap<>();
				msg.put("msg", "ok");
				return WebResult.requestSuccess(msg);
	}
}
