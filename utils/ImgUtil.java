package com.maryun.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import sun.misc.BASE64Decoder;



public class ImgUtil {

	public boolean createImgFile(String imageStr) throws IOException{
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] img=decoder.decodeBuffer(imageStr);
        Long size=Long.parseLong(img.length+"");
        InputStream stream = new ByteArrayInputStream(img);
	 return true;
	}
}
				