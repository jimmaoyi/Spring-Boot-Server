package com.maryun.lb.mapper.jyb.hospDept;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.maryun.model.PageData;
/**
 * 类名称：HospDeptMapper 创建人：SunYongLiang 创建时间：2017年2月23日
 * 
 * @version
 */
@Mapper
public interface HospDeptMapper {
	List<PageData> getAllHospDept();
	List<PageData> listPage(PageData pd);
	void deleteAll(PageData pd);
	void saveAdd(PageData pd);
	void saveAddLocation(PageData pd);//保存科室
	void saveEditLocation(PageData pd);//修改科室
	PageData selectById(PageData pd);//根据id进行查询
	void deleteMany(PageData pd);//删除多个
}
