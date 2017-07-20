package com.maryun.utils;

//import java.awt.Transparency;
//import java.awt.color.ColorSpace;
//import java.awt.image.BufferedImage;
//import java.awt.image.ColorModel;
//import java.awt.image.ComponentColorModel;
//import java.awt.image.DataBuffer;
//import java.awt.image.DataBufferByte;
//import java.awt.image.Raster;
//import java.awt.image.WritableRaster;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.Iterator;
//
//import javax.imageio.IIOException;
//import javax.imageio.ImageIO;
//import javax.imageio.ImageReader;
//import javax.imageio.stream.ImageInputStream;
//
//import com.sun.image.codec.jpeg.JPEGCodec;
//import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * @author: Libra
 * Date: 2017年4月26日 下午6:57:45
 * Version: 1.0
 * @Description:  cmyk图片转换为gbk文件
 */
@SuppressWarnings("restriction")
public class CmykToRgbUtils {
//
//	/**
//	 * 读取文件
//	 * @param filename 文件名
//	 * @return
//	 * @throws IOException
//	 */
//	@SuppressWarnings("rawtypes")
//	public static String cmykToRgb(String filename,String endStr) throws IOException {
//		File file = new File(filename);
//		ImageInputStream input = ImageIO.createImageInputStream(file);
//		Iterator readers = ImageIO.getImageReaders(input);
//		if (readers == null || !readers.hasNext()) {
//			throw new RuntimeException("1 No ImageReaders found");
//		}
//		ImageReader reader = (ImageReader) readers.next();
//		reader.setInput(input);
//		String format = reader.getFormatName();
//		BufferedImage image = null;
//		String newfilename = null;
//		
//		if ("JPEG".equalsIgnoreCase(format) || "JPG".equalsIgnoreCase(format)) {
//			try {
//				// 尝试读取图片 (包括颜色的转换).
//				image = reader.read(0); // RGB
//				System.out.println(image.toString());
//			} catch (IIOException e) {
//				// 读取Raster (没有颜色的转换).
//				Raster raster = reader.readRaster(0, null);// CMYK
//				image = createJPEG4(raster);
//			}
//			image.getGraphics().drawImage(image, 0, 0, null);
//			int index = filename.lastIndexOf(".");
//			newfilename = filename.substring(0, index) + endStr + ".jpg";
//			File newFile = new File(newfilename);
//			FileOutputStream out = new FileOutputStream(newFile);
//			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
//			encoder.encode(image);
//			out.flush();
//			out.close();
//		}
//		return newfilename;
//	}
//
//	/**
//	 * 创建jpg文件
//	 * @param raster
//	 * @return
//	 * @throws IOException 
//	 */
//	private static BufferedImage createJPEG4(Raster raster) throws IOException {
//		int w = raster.getWidth();
//		int h = raster.getHeight();
//		byte[] rgb = new byte[w * h * 3];
//		// 彩色空间转换
//		float[] Y = raster.getSamples(0, 0, w, h, 0, (float[]) null);
//		float[] Cb = raster.getSamples(0, 0, w, h, 1, (float[]) null);
//		float[] Cr = raster.getSamples(0, 0, w, h, 2, (float[]) null);
//		float[] K = raster.getSamples(0, 0, w, h, 3, (float[]) null);
//		for (int i = 0, imax = Y.length, base = 0; i < imax; i++, base += 3) {
//			float k = 220 - K[i], y = 255 - Y[i], cb = 255 - Cb[i], cr = 255 - Cr[i];
//
//			double val = y + 1.402 * (cr - 128) - k;
//			val = (val - 128) * .65f + 128;
//			rgb[base] = val < 0.0 ? (byte) 0 : val > 255.0 ? (byte) 0xff : (byte) (val + 0.5);
//
//			val = y - 0.34414 * (cb - 128) - 0.71414 * (cr - 128) - k;
//			val = (val - 128) * .65f + 128;
//			rgb[base + 1] = val < 0.0 ? (byte) 0 : val > 255.0 ? (byte) 0xff : (byte) (val + 0.5);
//
//			val = y + 1.772 * (cb - 128) - k;
//			val = (val - 128) * .65f + 128;
//			rgb[base + 2] = val < 0.0 ? (byte) 0 : val > 255.0 ? (byte) 0xff : (byte) (val + 0.5);
//		}
//		raster = Raster.createInterleavedRaster(new DataBufferByte(rgb, rgb.length), w, h, w * 3, 3,
//				new int[] { 0, 1, 2 }, null);
//		ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_sRGB);
//		ColorModel cm = new ComponentColorModel(cs, false, true, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
//		return new BufferedImage(cm, (WritableRaster) raster, true, null);
//	}
//
//	/**
//	 * @author: Libra
//	 * Date: 2017年4月26日 下午6:43:41
//	 * Version: 1.0
//	 * @Description: 校验gbk与cmyk图片，并将cmyk图片转换成gbk
//	 */
//	@SuppressWarnings({ "rawtypes", "unused" })
//	public static boolean isRgb(String filename) throws IOException {  
//        File file = new File(filename);  
//        boolean isRgb=true;//true是Rgb否则是Cmyk  
//        //创建输入流  
//        ImageInputStream input = ImageIO.createImageInputStream(file);  
//        Iterator readers = ImageIO.getImageReaders(input);  
//        if (readers == null || !readers.hasNext()) {  
//            throw new RuntimeException("No ImageReaders found");  
//        }  
//        ImageReader reader = (ImageReader) readers.next();  
//        reader.setInput(input);  
//        //获取文件格式  
//        BufferedImage image;  
//            try {  
//                // 尝试读取图片 (包括颜色的转换).  
//               reader.read(0); // RGB  
//    		   isRgb=true;  
//            } catch (IIOException e) {  
//                // 读取Raster (没有颜色的转换).  
//            	reader.readRaster(0, null);// CMYK  
//            	isRgb=false;  
//            }  
//        return isRgb;  
//    }  
//	
	
	
	/*public static void main(String[] args) throws IOException {
		String filename = "E:/370103199208083511.jpg";
		try {
			*//** 首先判断是否为rgb *//*
			if(isRgb(filename)){
				System.out.println("图片为rgb类型，不做处理");
			}else{
				String newFileName = cmykToRgb(filename,"rgb");
				*//** 再次判断新文件是否为rgb *//*
				if(isRgbOrCmyk(newFileName)){
					System.out.println("转换成功，文件名为：" + newFileName);
				}else{
					System.out.println("转换失败");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/

}
