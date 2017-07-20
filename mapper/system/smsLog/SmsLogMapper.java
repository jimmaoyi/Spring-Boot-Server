package com.maryun.mapper.system.smsLog;

import com.maryun.mapper.BaseMapper;
import com.maryun.model.PageData;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SmsLogMapper extends BaseMapper{
	void saveSmsLog(PageData map);
	List<PageData> findByPhone(PageData map);

}
