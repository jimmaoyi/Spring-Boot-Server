package com.maryun.common.service;

import com.maryun.mapper.app.common.JPushAccpMapper;
import com.maryun.mapper.app.common.JPushDoctorMapper;
import com.maryun.mapper.app.common.JPushOrgaMapper;
import com.maryun.mapper.app.common.JPushUserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by SR on 2017/3/24.
 */
@Service
public class JPushMapperFactory {
    @Resource
    private JPushUserMapper jPushUserMapper;
    @Resource
    private JPushAccpMapper jPushAccpMapper;
    @Resource
    private JPushDoctorMapper jPushDoctorMapper;
    @Resource
    private JPushOrgaMapper jPushOrgaMapper;

    public JPushUserMapper jPushUserFactory() {
        return jPushUserMapper;
    }

    public JPushAccpMapper jPushAccpMapper() {
        return jPushAccpMapper;
    }

    public JPushDoctorMapper jPushDoctorMapper() {
        return jPushDoctorMapper;
    }

    public JPushOrgaMapper jPushOrgaMapper() {
        return jPushOrgaMapper;
    }
}
