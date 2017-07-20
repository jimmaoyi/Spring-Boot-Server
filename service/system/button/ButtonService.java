package com.maryun.service.system.button;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.maryun.mapper.system.button.ButtonMapper;
import com.maryun.model.PageData;

@Service("buttonService")
public class ButtonService{

	@Autowired
	private ButtonMapper buttonMapper;
	
	/*
	*列表
	*/
	public List<PageData> listPdPageRole(PageData pd)throws Exception{
		if (pd.getPageNumber()!=0) {
			PageHelper.startPage(pd.getPageNumber(), pd.getPageSize());
		}
		return buttonMapper.buttonlistPage(pd);
	}
	
	/*
	* 查找按钮
	*/
	public PageData findById(PageData pd)throws Exception{
		return buttonMapper.findById(pd);
	}
	
	/*
	* 保存按钮
	*/
	public void saveButton(PageData pd)throws Exception{
		buttonMapper.saveButton( pd);
	}
	
	/*
	* 修改按钮
	*/
	public void editButton(PageData pd)throws Exception{
		buttonMapper.editButton( pd);
	}
	
	/*
	* 批量删除按钮
	*/
	public void deleteAllButtons(String[] BUTTON_IDS)throws Exception{
		buttonMapper.deleteButton( BUTTON_IDS);
	}
	
	

}
