package com.maryun.service.system.dictionaries;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maryun.mapper.system.dictionaries.DictionariesMapper;
import com.maryun.model.PageData;


@Service("dictionariesService")
public class DictionariesService{

	@Autowired
	private DictionariesMapper dictionariesMapper;
	
	public List<PageData> listDic(PageData pageData)throws Exception{
		return dictionariesMapper.list(pageData);
	}
	
	public List<PageData> listSubDic(PageData pageData)throws Exception{
		return dictionariesMapper.getSubDicList(pageData);
	}
	
	public List<PageData> lsitSubDicByParentCode(PageData pageData)throws Exception{
		return dictionariesMapper.getSubDicListByParentCode(pageData);
	}
	
	public void insertDic(PageData pageData) throws Exception{
		dictionariesMapper.save(pageData);
	}
	
	public void updateDic(PageData pageData) throws Exception{
		dictionariesMapper.edit(pageData);
	}
	
	public void deleteDics(String[] args) throws Exception{
		dictionariesMapper.delete(args);
	}
	
	public List<PageData> findSameCode(PageData pageData)throws Exception{
		return dictionariesMapper.findSameCode(pageData);
	}

}
