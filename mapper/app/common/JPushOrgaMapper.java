package com.maryun.mapper.app.common;

import com.maryun.model.PageData;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by SR on 2017/3/24.
 */
@Mapper
public interface JPushOrgaMapper {
    List<PageData> selectOrga(List<String> pd);
}
