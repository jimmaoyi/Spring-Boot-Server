package com.maryun.mapper.system.fileuploads;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.maryun.mapper.BaseMapper;
import com.maryun.model.PageData;
@Mapper
public interface FileUploadsMapper extends BaseMapper{
	PageData get(PageData pd);
	List<PageData> listPage(PageData pd);
	List<PageData> findAllList(PageData pd);
	PageData findCount(PageData pd);
	void insert(PageData pd);
	void update(PageData pd);
	void updateMasterId(PageData pd);
	void delete(String[] str);
	void deleteByMaster(String[] ids);
	void deleteSolve(String[] str);
	void deleteById(String[] str);
	List<PageData> findByMasterId(PageData pd);
	PageData findByHttpId(PageData pd);
	List<PageData> findByHttpIdOrMasterId(PageData pd);
}
