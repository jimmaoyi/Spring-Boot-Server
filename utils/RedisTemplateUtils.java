package com.maryun.utils;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.core.RedisTemplate;
public class RedisTemplateUtils {
	@SuppressWarnings("unchecked")
	private static RedisTemplate<String, Object> redisTemplate = (RedisTemplate<String, Object>) SpringBeanUtil.getBean("redisTemplate");  
	  
    /** 
     * 写入缓存有时效要求 
     *  
     * @param key 
     * @param value 
     * @param expire 
     */  
    public static void set( String key,  Object value,int savaTime) {  
        redisTemplate.opsForValue().set(key, value, savaTime, TimeUnit.SECONDS);  
    }  
    /** 
     * 写入缓存无时效要求
     *  
     * @param key 
     * @param value 
     * @param expire 
     */  
    public static void set( String key,  Object value) {  
    	redisTemplate.opsForValue().set(key, value);  
    }  
  
    /** 
     * 读取缓存 
     *  
     * @param key 
     * @param clazz 
     * @return 
     */  
    @SuppressWarnings("unchecked")  
    public static <T> T get(final String key, Class<T> clazz) {  
        return (T) redisTemplate.boundValueOps(key).get();  
    }  
      
    /** 
     * 读取缓存 
     * @param key 
     * @return 
     */  
    public static Object getObj(final String key){  
        return redisTemplate.boundValueOps(key).get();  
    }  
  
    /** 
     * 删除，根据key精确匹配 
     *  
     * @param key 
     */  
    public static void del(final String... key) {  
        redisTemplate.delete(Arrays.asList(key));  
    }  
  
    /** 
     * 批量删除，根据key模糊匹配 
     *  
     * @param pattern 
     */  
    public static void delpn(final String... pattern) {  
        for (String kp : pattern) {  
            redisTemplate.delete(redisTemplate.keys(kp + "*"));  
        }  
    }  
  
    /** 
     * key是否存在 
     *  
     * @param key 
     */  
    public static boolean exists(final String key) {  
        return redisTemplate.hasKey(key);  
    }  
  
}
