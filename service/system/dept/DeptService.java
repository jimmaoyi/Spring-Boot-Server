package com.maryun.service.system.dept;


import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maryun.mapper.system.dept.DeptMapper;
import com.maryun.model.PageData;


@Service("deptService")
public class DeptService {

	@Autowired
	private DeptMapper deptMapper;
	
	/**
	 * 获取部门列表
	 * @param pageData
	 * @return
	 * @throws Exception
	 */
	public List<PageData> listDept(PageData pageData)throws Exception{
		return deptMapper.list(pageData);
	}
	
	/**
	 * 生成最新的部门ID
	 * @return
	 * @throws Exception
	 */
	public int getNewDeptId() throws Exception{
		List<PageData> list = deptMapper.getMaxDeptID();
		int id = Integer.parseInt(list.get(0).get("ID").toString())+1;
		return id;
	}
	
	/**
	 * 插入部门
	 * @param pageData
	 * @throws Exception
	 */
	public void insertDept(PageData pageData) throws Exception{
		deptMapper.save(pageData);
	}
	
	/**
	 * 更新部门
	 * @param pageData
	 * @throws Exception
	 */
	public void updateDept(PageData pageData) throws Exception{
		deptMapper.edit(pageData);
	}
	
	/**
	 * 删除部门
	 * @param args
	 * @throws Exception
	 */
	public void deleteDepts(String[] args) throws Exception{
		deptMapper.delete(args);
	}
	
	
	/**
	 * 获取班组列表通过班组id
	 * @param pageData
	 * @return
	 * @throws Exception
	 */
	public List<PageData> findDeptByDeptId(String[] args)throws Exception{
		return deptMapper.findByIds(args);
	}
	
}
