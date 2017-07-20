package com.maryun.common.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.maryun.mapper.system.qrcodeinfo.QrcodeInfo;
import com.maryun.model.PageData;
import com.maryun.utils.DES;
import com.maryun.utils.UuidUtil;

import sun.misc.BASE64Encoder;
/**
 * 二维码工具
 * @author <a href="mailto:shiran@maryun.net">shiran</a>
 * @version 2017年2月21日
 *
 */
@Component
public class Qrcode {
	
	@Value("${QRCODE.DES.password}")
    private String password;
	@Resource
	private QrcodeInfo qrcodeInfo;
	@Value("${system.imageServer.uploads.basePath}")
	private String  imageServer;

	@Value("${system.file.uploads.basePath}")
	private String  basePath;
	/**
	 * 二维码生成 ，储存地址qrcodeFilePath
	 * 
	 * @param text 二维码内容
	 * @return Base64Code String
	 */
	@SuppressWarnings("restriction")
	public String createQrcode(String text){
		String Imgencode="";
        byte[] imageByte=null;
        String formatName="PNG";
        try {
        	//
            int qrcodeWidth = 300;
            int qrcodeHeight = 300;
            HashMap<EncodeHintType, String> hints = new HashMap<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, qrcodeWidth, qrcodeHeight, hints);
           
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
            ImageIO.write(bufferedImage, formatName, out);
            imageByte=out.toByteArray();
            Imgencode = new sun.misc.BASE64Encoder().encode(imageByte);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Imgencode;
    }
	/**
	 * 加密后的
	 * @param pd
	 * @return Base64Code String
	 */
	public String createDESQrcode(PageData pd){
		String Imgencode="";
        byte[] imageByte=null;
        String formatName="PNG";
        try {
        	  String encodeContent = pd.entrySet().
        			 stream().filter(map -> map.getKey().equals("content"))
        			 .map(kv->DES.encrypt(String.valueOf(kv.getValue()), password))
        			 .collect(Collectors.toList())
        			 .get(0);
        	
        	//String encrypt = DES.encrypt(encodeContent, password);
        	//
            int qrcodeWidth = 300;
            int qrcodeHeight = 300;
            HashMap<EncodeHintType, String> hints = new HashMap<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            BitMatrix bitMatrix = new MultiFormatWriter().encode(encodeContent, BarcodeFormat.QR_CODE, qrcodeWidth, qrcodeHeight, hints);
           
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
            ImageIO.write(bufferedImage, formatName, out);
            imageByte=out.toByteArray();
            Imgencode = new BASE64Encoder().encode(imageByte);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Imgencode;
	}
	/**
	 * 
	 * @Description: 根据传入的pd，生成json字符串作为二维码并内容保存到本地，返回保存到图片服务器的路径。
	 * @param pd
	 * @return
	 */
	public String createJsonQrcode(PageData pd){
		String text=JSON.toJSONString(pd);
        String qrcodeName = "/"+UuidUtil.get32UUID()+".jpg";
        //qrcode保存在本地的路径
      	String baseQrcodePath=basePath+"qrcodeInfo";
        String formatName="PNG";
        //qrcode保存在服务器的路径
		String imageServerPath=imageServer+"qrcodeInfo";
        try {
    		List<PageData> list = qrcodeInfo.list(pd);
    		String path="";
    		//如果已经保存过返回保存过的路径
    		if(list.size()>0){
    			path=list.get(0).getString("PATH");
    			return path;
    		}
    		
            int qrcodeWidth = 300;
            int qrcodeHeight = 300;
            HashMap<EncodeHintType, String> hints = new HashMap<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            //生成二维码
            BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, qrcodeWidth, qrcodeHeight, hints);
            //生成bufferimage
            BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
            //生成文件夹
            File file = new File(baseQrcodePath);
            if(!file.exists()){
            	file.mkdirs();
            }
            //把二维码写到指定路径
            ImageIO.write(bufferedImage, formatName, new File(baseQrcodePath+qrcodeName));
          //没存过则存
    		pd.put("ID", UuidUtil.get32UUID());
            pd.put("PATH", imageServerPath+qrcodeName);
    		qrcodeInfo.save(pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //保存到数据库,并返回保存到图片服务器的地址
        return imageServerPath+qrcodeName;
    }
	
	
}
