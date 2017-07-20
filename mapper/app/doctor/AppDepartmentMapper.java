/**
 * 
 */
package com.maryun.mapper.app.doctor;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.maryun.model.PageData;

/**
 * @author Administrator
 *
 */
@Mapper
public interface AppDepartmentMapper {
	List<PageData> getDeptAPPList(); // 查询科室列表信息
	
	List<PageData> getDeptList(PageData pd); // 查询科室列表信息

	PageData getDeptListByFirstId(); // 查询科室列表第一条信息的ID

	PageData getDeptByDeptName(PageData pd); // 根据科室名字获取科室

	PageData getDeptByDeptID(PageData pd); // 根据科室ID获取科室
}
