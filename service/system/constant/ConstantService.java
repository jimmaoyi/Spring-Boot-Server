package com.maryun.service.system.constant;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.maryun.mapper.system.constant.ConstantMapper;
import com.maryun.model.PageData;

@Service("constantService")
public class ConstantService {
	@Autowired
	private ConstantMapper constantMapper;
	/*
	*列表
	*/
	public List<PageData> listPdPageSystemConstant(PageData pd)throws Exception{
		if (pd.getPageNumber()!=0) {
			PageHelper.startPage(pd.getPageNumber(), pd.getPageSize());
		}
		return constantMapper.list(pd);
	}
	

	
	/*
	* 查找系统常量
	*/
	public PageData findById(PageData pd)throws Exception{
		return constantMapper.findById(pd);
	}
	
	
	/*
	* 新增系统常量
	*/
	public void saveSystemConstant(PageData pd)throws Exception{
		constantMapper.save(pd);
	}
	
	
	
	/*
	* 修改系统常量
	*/
	public void editSystemConstant(PageData pd)throws Exception{
		constantMapper.edit(pd);
	}
	
	/*
	* 批量删除系统常量
	*/
	public void deleteAllSystemConstants(String[] SYSTEM_CONSTANT_IDS)throws Exception{
		constantMapper.delete(SYSTEM_CONSTANT_IDS);
	}
}
