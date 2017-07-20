package com.maryun.mapper.app.pay;

import org.apache.ibatis.annotations.Mapper;

import com.maryun.model.PageData;

@Mapper
public interface AppBankAccountMapper {
	PageData getBankAccontInfo(); // 查询账号信息
}
