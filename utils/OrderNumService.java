package com.maryun.utils;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
public class OrderNumService {
	private static SimpleDateFormat rediskeyFormat= new SimpleDateFormat("yyyyMM");
	private static SimpleDateFormat orderPreFormat= new SimpleDateFormat("yyMMdd");
	private static RedisTemplate<String, Object> redisTemplate = (RedisTemplate<String, Object>) SpringBeanUtil.getBean("redisTemplate");
	/**
	 * 
	 * @Description: 生成订单号
	 * @param orderType 订单类型
	 * @return    
	 * @return String    
	 * @throws
	 */
	public static String getOrderNum(String orderType){
		Date sysDate=new Date();
		if(StringUtils.isBlank(orderType)){
			orderType="DJ";
		}
		String rediskey=String.join("", "SYSTEMORDER_",orderType.toUpperCase(),rediskeyFormat.format(sysDate));
		String orderPre=orderPreFormat.format(sysDate);
		Long num=redisTemplate.opsForValue().increment(rediskey, 1);
		redisTemplate.expire(rediskey, 1, TimeUnit.DAYS);
		String str = String.format("%06d", num);
		return String.join("",orderType.toUpperCase(),orderPre,str);
	}
	
	/**
	 * 
	 * @Description: 线下支付流水号
	 * @param orderType
	 * @return    
	 * @return String    
	 * @throws
	 */
	public static String getOfflineNum(){
		String rediskey="SYSTEMOFFLINENUM_ALLWAYS";
		Long num=redisTemplate.opsForValue().increment(rediskey, 1);
		redisTemplate.expire(rediskey, 365, TimeUnit.DAYS);
		String str = String.format("%06d", num);
		return str;
	}
	
}
