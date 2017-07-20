package com.maryun.conf;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.text.SimpleDateFormat;
@Configuration
public class configUtils{
	//fastJson Date Conf.
//	@SuppressWarnings("deprecation")
//	@Bean
//	public FastJsonHttpMessageConverter fastJsonConfig() {
//		FastJsonHttpMessageConverter fjc = new FastJsonHttpMessageConverter();
//		fjc.setDateFormat("yyyy-MM-dd HH:mm:ss");
//		fjc.setDefaultCharset(Charset.forName("utf-8"));
//		return fjc;
//	}
	//json日期转换
	@Bean
	public MappingJackson2HttpMessageConverter jv(){
		MappingJackson2HttpMessageConverter jv=new MappingJackson2HttpMessageConverter();
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		jv.setObjectMapper(objectMapper);
		return jv;
	}
	

}
