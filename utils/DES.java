package com.maryun.utils;


import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;

import javax.crypto.spec.DESKeySpec;

import org.apache.commons.lang.StringUtils;

import com.maryun.model.PageData;

import javax.crypto.SecretKeyFactory;
import javax.crypto.SecretKey;
import javax.crypto.Cipher;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;


/**
DES加密介绍
DES是一种对称加密算法，所谓对称加密算法即：加密和解密使用相同密钥的算法。DES加密算法出自IBM的研究，
后来被美国政府正式采用，之后开始广泛流传，但是近些年使用越来越少，因为DES使用56位密钥，以现代计算能力，
24小时内即可被破解。虽然如此，在某些简单应用中，我们还是可以使用DES加密算法，本文简单讲解DES的JAVA实现
。
注意：DES加密和解密过程中，密钥长度都必须是8的倍数
*/
public class DES {
	public DES() {//encrypt(String.valueOf(kv.getValue()), "12345678")
	}
//	public static void main(String[] args) {
//		PageData pd=new PageData();
//		pd.put("content", "ss");
//		 String string = pd.entrySet().
//    			 stream().filter(map -> map.getKey().equals("content"))
//    			 .map(kv->DES::encrypt).collect(Collectors.toList()).get(0);
//	System.out.println(string);
//	}
//	// 测试
//	public static void main(String args[]) {
//		// 待加密内容
//		String str = "727i7DMKpBU=";
//		// 密码，长度要是8的倍数
//		String password = "12345678";
//		String result = DES.encrypt(str, password);
//		try {
//			System.out.println("加密后：" +result);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//
//		}
//		// 直接将如上内容解密
//		try {
//			System.out.println("解密后：" + DES.decrypt("RBdIlzbcSUrlmdkMDkPiAZpPRGh3vwkyEN+PSM0iVi5W7ty5HuMQIg==", password));
//			
//			
//			
//		} catch (Exception e1) {
//			e1.printStackTrace();
//		}
//
//	}

	/**
	* 加密
	* @param datasource byte[]
	* @param password String
	* @return byte[]
	*/
	public static String encrypt(String datasource, String password) {
		try {
			if(StringUtils.trimToNull(datasource) == null){
				return null;
			}
			SecureRandom random = new SecureRandom();
			DESKeySpec desKey = new DESKeySpec(password.getBytes());
			// 创建一个密匙工厂，然后用它把DESKeySpec转换成
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey securekey = keyFactory.generateSecret(desKey);
			// Cipher对象实际完成加密操作
			Cipher cipher = Cipher.getInstance("DES");
			// 用密匙初始化Cipher对象
			cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
			// 现在，获取数据并加密
			// 正式执行加密操作
			 return new BASE64Encoder().encode(cipher.doFinal(datasource.getBytes()));
			
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	* 解密
	* @param src byte[]
	* @param password String
	* @return String
	* @throws Exception
	*/
	public static String decrypt(String decodeStr, String password) throws Exception {
		byte[] str=new BASE64Decoder().decodeBuffer(decodeStr);
		// DES算法要求有一个可信任的随机数源
		SecureRandom random = new SecureRandom();
		// 创建一个DESKeySpec对象
		DESKeySpec desKey = new DESKeySpec(password.getBytes());
		// 创建一个密匙工厂
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		// 将DESKeySpec对象转换成SecretKey对象
		SecretKey securekey = keyFactory.generateSecret(desKey);
		// Cipher对象实际完成解密操作
		Cipher cipher = Cipher.getInstance("DES");
		// 用密匙初始化Cipher对象
		cipher.init(Cipher.DECRYPT_MODE, securekey, random);
		// 真正开始解密操作
		byte[] passByte=cipher.doFinal(str);
		return new String(passByte);
	}
}
