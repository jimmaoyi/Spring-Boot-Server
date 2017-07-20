package com.maryun.restful.app.pay.wechat.wechatUtil;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeChatPayRequest {

	public static String httpsRequest(String requestUrl, String requestMethod, String outputStr) {  
        try {  
             
            URL url = new URL(requestUrl);  
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
            
            conn.setDoOutput(true);  
            conn.setDoInput(true);  
            conn.setUseCaches(false);  
            // 设置请求方式（GET/POST）  
            conn.setRequestMethod(requestMethod);  
            conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");  
            // 当outputStr不为null时向输出流写数据  
            if (null != outputStr) {  
                OutputStream outputStream = conn.getOutputStream();  
                // 注意编码格式  
                outputStream.write(outputStr.getBytes("UTF-8"));  
                outputStream.close();  
            }  
            // 从输入流读取返回内容  
            InputStream inputStream = conn.getInputStream();  
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");  
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);  
            String str = null;  
            StringBuffer buffer = new StringBuffer();  
            while ((str = bufferedReader.readLine()) != null) {  
                buffer.append(str);  
            }  
            // 释放资源  
            bufferedReader.close();  
            inputStreamReader.close();  
            inputStream.close();  
            inputStream = null;  
            conn.disconnect();  
            return buffer.toString();  
        } catch (ConnectException ce) {  
            System.out.println("连接超时：{}"+ ce);  
        } catch (Exception e) {  
            System.out.println("https请求异常：{}"+ e);  
        }  
        return null;  
    } 
		
	
}
