package com.maryun.restful.system.fileuploads;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;

import com.maryun.model.PageData;
import com.maryun.utils.WebResult;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author SR
 */
public class UploadChange {

    /**
     * @param is
     * @param outPath
     * @param width
     * @param height
     * @return void
     * @throws
     */
    public static void resizeVideo(String is, String outPath, int width, int height) throws IOException {
        List<String> cutpic = new ArrayList<String>();
        cutpic.add("D://ffmpeg/ffmpeg.exe");
        cutpic.add("-i");
        cutpic.add(is); // 同上（指定的文件即可以是转换为flv格式之前的文件，也可以是转换的flv文件）
        cutpic.add("-y");
        cutpic.add("-f");
        cutpic.add("image2");
        cutpic.add("-ss"); // 添加参数＂-ss＂，该参数指定截取的起始时间
        cutpic.add("1"); // 添加起始时间为第1秒
        cutpic.add("-t"); // 添加参数＂-t＂，该参数指定持续时间
        cutpic.add("0.001"); // 添加持续时间为1毫秒
        cutpic.add("-s"); // 添加参数＂-s＂，该参数指定截取的图片大小
        cutpic.add(String.valueOf(height) + "*" + String.valueOf(width)); // 添加截取的图片大小为350*240
        cutpic.add(outPath); // 添加截取的图片的保存路径

        ProcessBuilder builder = new ProcessBuilder();
        try {

            builder.command(cutpic);
            builder.redirectErrorStream(true);
            // 如果此属性为 true，则任何由通过此对象的 start() 方法启动的后续子进程生成的错误输出都将与标准输出合并，
            //因此两者均可使用 Process.getInputStream() 方法读取。这使得关联错误消息和相应的输出变得更容易
            builder.start();
            System.out.println("视频截图开始...");

            Process process = builder.start();
            InputStream in = process.getInputStream();
            byte[] bytes = new byte[1024];
            System.out.print("正在进行截图，请稍候");
            while (in.read(bytes)!= -1){
                System.out.println(".");
                shuiyin(outPath);
            }
            System.out.println("");
            System.out.println("视频截取完成...");

        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    public static void shuiyin(String inPath){

        File file = null;
        try {
            file = ResourceUtils.getFile("classpath:play.png");
            Thumbnails.of(inPath).size(150,150)
                    .watermark(Positions.CENTER,ImageIO.read(file),0.5f)
                    .outputQuality(1f).toFile(inPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**裁剪图片
     * @param imgsrc
     * @param imgdist
     * @param widthdist
     * @param heightdist
     */
    public static void reduceImg(String imgsrc, String imgdist, int widthdist,
                                 int heightdist) {
        try {
            File srcfile = new File(imgsrc);
            if (!srcfile.exists()) {
                return;
            }
            Image src = javax.imageio.ImageIO.read(srcfile);

            BufferedImage tag = new BufferedImage((int) widthdist, (int) heightdist,
                    BufferedImage.TYPE_INT_RGB);

            tag.getGraphics().drawImage(src.getScaledInstance(widthdist, heightdist, Image.SCALE_SMOOTH), 0, 0, null);
            ImageIO.write(tag, srcfile.getName(), new File(imgsrc));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**生成缩略图
     * @param is
     * @param os
     * @param height
     * @return
     */
    public static PageData generateThum(String is, String os, int height) {
        BufferedImage read = null;
        try {
            read = ImageIO.read(new File(is));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        int height1 = read.getHeight();
        int width1 = read.getWidth();
        //根据高的缩放算出宽应该缩放的比例
        BigDecimal bg = new BigDecimal(height).divide(new BigDecimal(height1), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(width1));
        reduceImg(is, os, bg.intValue(), height);
        return WebResult.requestSuccess();
    }

    /**
     * @param is
     * @param os
     * @param width
     * @param height
     * @return
     */
    public static PageData generateHeightFixationThum(String is, String os, int width, int height) {
        try {
            Thumbnails.of(is).sourceRegion(Positions.CENTER, width, height)
                    .size(width, height).toFile(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return WebResult.requestSuccess();
    }

//    /**
//     * 图片翻转时，计算图片翻转到正常显示需旋转角度
//     */
//    public static int getRotateAngleForPhoto(MultipartFile file) throws IOException {
//
//        InputStream file1 = file.getInputStream();
//
//        int angel = 0;
//        Metadata metadata;
//
//        try {
//            metadata = JpegMetadataReader.readMetadata(file1);
//            Iterable<Directory> directories = metadata.getDirectories();
//            Directory directory = directories.iterator().next();
//            Iterator<Directory> iterator = directories.iterator();
//            while (iterator.hasNext()) {
//                Directory next = iterator.next();
//                next.getDescription(1);
//            }
//            if (directory.containsTag(ExifImageDirectory.TAG_ORIENTATION)) {
//                // Exif信息中方向　　
//                int orientation = directory.getInt(ExifImageDirectory.TAG_ORIENTATION);
//                // 原图片的方向信息
//                if (6 == orientation) {
//                    //6旋转90
//                    angel = 90;
//                } else if (3 == orientation) {
//                    //3旋转180
//                    angel = 180;
//                } else if (8 == orientation) {
//                    //8旋转90
//                    angel = 270;
//                }
//            }
//        } catch (JpegProcessingException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        System.out.println("图片旋转角度：" + angel);
//        return angel;
//    }
//	public static void main(String[] args) throws IOException {
//        UploadChange.shuiyin("D:\\tomcat7\\webapps\\lbaner\\images\\20170407slt\\5c948937697b47019575eb1beac395c6slt.jpg");
//	}
}
