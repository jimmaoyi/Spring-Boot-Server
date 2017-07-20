package com.maryun.mapper.app.common;

import com.maryun.model.PageData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author SR
 */
@Mapper
public interface PushCodeMapper {
    List<PageData> select(@Param("APP_TYPE")String APP_ID,@Param("list") List<String> IDS);
}
