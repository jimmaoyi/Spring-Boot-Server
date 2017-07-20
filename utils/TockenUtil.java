package com.maryun.utils;

public class TockenUtil {
	
	public static String createToken(String playload, String keyStr) throws Exception {
		return DES.encrypt(playload, keyStr);
		//return String.join("",playload, keyStr);
	}
	public static String getTokenStr(String token, String keyStr) throws Exception {
		return DES.decrypt(token, keyStr);
	}
	
	
//	public static String createTocken(Object playload, String keyStr) throws JoseException {
//		Key key = new AesKey(keyStr.getBytes());
//		JsonWebEncryption jwe = new JsonWebEncryption();
//		jwe.setPayload(JSON.toJSONString(playload));
//		jwe.setAlgorithmHeaderValue(KeyManagementAlgorithmIdentifiers.A128KW);
//		jwe.setEncryptionMethodHeaderParameter(ContentEncryptionAlgorithmIdentifiers.AES_128_CBC_HMAC_SHA_256);
//		jwe.setKey(key);
//		String serializedJwe = jwe.getCompactSerialization();
//		return serializedJwe;
//	}
//
//	public static String encrypTocken(String tocken, String keyStr) throws JoseException {
//		Key key = new AesKey(keyStr.getBytes());
//		JsonWebEncryption jwe = new JsonWebEncryption();
//		jwe.setKey(key);
//		 jwe.setCompactSerialization(tocken);
//		return jwe.getPayload();
//	}
	
}
