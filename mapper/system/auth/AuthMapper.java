package com.maryun.mapper.system.auth;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.maryun.model.PageData;

@Mapper
public interface AuthMapper {
	List<PageData> findUrlsByUser(PageData map);
	List<PageData> findRolesByUser(PageData map);
	List<PageData> findAllButtonByRoleUser(PageData map);
	List<PageData> findAllUrlByRoleUser(PageData map);
	List<PageData> findUrlAuthCodeByRoleUser(PageData map);
}
