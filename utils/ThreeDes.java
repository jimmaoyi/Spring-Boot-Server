package com.maryun.utils;

import java.io.ByteArrayOutputStream;
import java.security.Security;

import javax.crypto.Cipher; 
import javax.crypto.SecretKey;  
import javax.crypto.spec.SecretKeySpec;

/**
 * 对称加密
 * @author ll
 *
 */
public class ThreeDes {
	
	private static final String Algorithm = "TripleDES";
	
	public static byte[] ecrypt(byte[] keybyte, byte[] source){
		try {            
			//生成密钥
			SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
			//加密           
			Cipher c1 = Cipher.getInstance("TripleDES/ECB/PKCS5Padding");
			c1.init(Cipher.ENCRYPT_MODE, deskey);
			return c1.doFinal(source);        
		} catch (java.security.NoSuchAlgorithmException e1) {
			e1.printStackTrace();        
		} catch (javax.crypto.NoSuchPaddingException e2) {
			e2.printStackTrace();        
		} catch (java.lang.Exception e3) {
			e3.printStackTrace();        
		}
		return null;
	}
	
	public static byte[] decrypt(byte[] keybyte, byte[] source)throws Exception{
		try {
			//生成密钥
			SecretKey deskey = new SecretKeySpec(keybyte,Algorithm);
			//解密           
			Cipher c1 = Cipher.getInstance("TripleDES/ECB/PKCS5Padding");
			c1.init(Cipher.DECRYPT_MODE, deskey);
			return c1.doFinal(source);        
		} catch (java.security.NoSuchAlgorithmException e1) {
			e1.printStackTrace();        
			throw e1;
		} catch (javax.crypto.NoSuchPaddingException e2) {
			e2.printStackTrace();
			throw e2;
		} catch (java.lang.Exception e3) {    
			e3.printStackTrace();
			throw e3;
		}
	}
	
    public static byte[] hexStringToBytes(String in) {  
    	byte[] arrB = in.getBytes();  
    	int iLen = arrB.length;  
    	// 两个字符表示一个字节，所以字节数组长度是字符串长度除以2   
    	byte[] arrOut = new byte[iLen / 2];  
    	for (int i = 0; i < iLen; i = i + 2) {  
    		String strTmp = new String(arrB, i, 2);  
    		arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);  
    	}  
    	return arrOut;  
    }
    
    public static byte[] decode(String bytes) 

    { 

	    ByteArrayOutputStream baos=new ByteArrayOutputStream(bytes.length()/2); 
	
	    //将每2位16进制整数组装成一个字节 
	
	    String hexString="0123456789ABCDEF"; 
	
	    for(int i=0;i<bytes.length();i+=2){
	    	 baos.write((hexString.indexOf(bytes.charAt(i))<<4 |hexString.indexOf(bytes.charAt(i+1))));
	    }
	    return baos.toByteArray(); 
    }
    
    public static byte[] decode2(String hexString) throws Exception{
    	byte[] bts = new byte[hexString.length()/2];
    	for (int i = 0; i < bts.length; i++) {
			bts[i] = (byte)Integer.parseInt(hexString.substring(2*i, 2*i+2),16);
		}
    	return bts;
    }

    public static String byte2hex(byte[] b) {
        String hs="";
        String stmp="";
        for (int n=0;n<b.length;n++){
          stmp=(java.lang.Integer.toHexString(b[n] & 0XFF));
          if (stmp.length()==1)
            hs=hs+"0"+stmp;
          else hs=hs+stmp;
            if (n<b.length-1)  hs=hs+"";
          }
        return hs.toUpperCase();
      }
    
    /*public static void main(String[] args) throws Exception {
		Security.addProvider(new com.sun.crypto.provider.SunJCE());
		String authenticator = "69556765$2F8997E4234BFDDD9B6557E8$8002002601147666$001001990070114000034C09B4BE1B65$192.168.1.104$4C:09:B4:BE:1B:65$990070|$CTC";
//		byte[] source = decode(authenticator);
		//byte[] source = authenticator.getBytes();
		String userID = "1234567";
		int lenth = userID.length();............................................................
		for (int i = 0; i < 24-lenth; i++) {
			userID = userID+"0";
		}
		System.out.println("userID:"+userID);
		byte[] keybyte = userID.getBytes();
		System.out.println("keybyte="+new String(keybyte)+"--length:"+keybyte.length);
		
		byte[] source = authenticator.getBytes();
		byte[] data3 = ecrypt(keybyte, source);
		String data = byte2hex(data3);
		System.out.println("密文："+data+"--length:"+data.length());
		
//		source = decode("04C2520F10FD9099F55478EDEE2D1CCD628C9096FE81F2C0AE0A8C17670BFF03B547B7C1D41BA4EDF4148195BA02F9505C186CECC646B0559386DB72FACFB8484D0945B82CF6103EC101AA2A9B17A62E8C0884DADA9CC4DFC776207C53BFB3D66D786EBD30BEF2EC6E46A5290ECEAAEC499AAD26AB296431");
		source =decode(data);
		byte[] data2 = decrypt(keybyte, source);
		
		System.out.println("data2:"+data2);
		
		String realData = new String(data2);
//		String realData = "12639364$ 3DE1B6ED6EDD0C47AB6F013CADF0309E$11111111$001001990070114000034C09B4BE1B65$192.168.200.70$4C:09:B4:BE:1B:65$990070|$CTC";
		System.out.println("明文："+realData+"---"+realData.indexOf("$"));
		String[] dataStr = realData.split("\\$");
		System.out.println(dataStr.length+"--"+dataStr[0]);
		String stbid = dataStr[3];
		String stbip = dataStr[4];
		String mac = dataStr[5];
		
		System.out.println("STBID = " + stbid);
		System.out.println("IP = " + stbip);
		System.out.println("MAC = " + mac);
    	
	}*/
}
