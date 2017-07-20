package com.maryun.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

/**
 * 解析HttpServletRequest参数
 * 
 * @author lgq
 * @version 
 */
public class RequestUtil {

    /**
     * 获取所有request请求参数key-value
     * 
     * @param request
     * @return
     */
    public static Map<String, String> getRequestParams(HttpServletRequest request){
        
        Map<String, String> params = new HashMap<String, String>();
        if(null != request){
            Set<String> paramsKey = request.getParameterMap().keySet();
            for(String key : paramsKey){
                params.put(key, request.getParameter(key));
            }
        }
        return params;
    }
}
