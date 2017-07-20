package com.maryun.utils;

import com.maryun.model.PageData;
import com.maryun.utils.WebResult;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * 朋友圈的图片缩放和裁剪。
 * Created by SR on 2017/4/17.
 */
@Component
public class ImgChange {
    //设置改变到的'高'
    private int changeHeight;
    //设置改变到的'宽'
    private int changeWeight;
    //长图的高，如果不设置或者设置为0的话，则默认没有长图设置
    private int longImageHeigh = 0;

   
    private String waterImagePath;

    //水印文件
    private File waterImageFile;

    /**
     * 构造水印文件位置 
     */
    public ImgChange() {
        try {
            if (waterImagePath==null || waterImagePath.equals("") || waterImagePath.equals("default")){
                this.waterImageFile = ResourceUtils.getFile("classpath:longImage.png");
            }else {
                this.waterImageFile = new File(waterImagePath);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置缩略图的长或者高
     * @param changeHeight
     * @param changeWeight
     */
    public void setChangeHeightAndWeight(int changeHeight, int changeWeight) {
        this.changeHeight = changeHeight;
        this.changeWeight = changeWeight;
    }

    /**
     * 设置长图判定的长度（不设置或设置为0则认为不设置长图)
     * @param longImageHeigh
     */
    public void setLongImage(int longImageHeigh) {
        this.longImageHeigh = longImageHeigh;
    }
    
    /**
     * 设置水印路径
     * @param waterImagePath
     */
    public void setWaterImagePath(String waterImagePath){
    	this.waterImagePath=waterImagePath;
    }

    /**
     * 缩放并裁剪图片，输出缩放后的图片和缩放后裁剪的图片。
     * @param inPath   输入图片地址
     * @param outPath  缩放输出地址
     * @param outPath2 裁剪输出地址
     */
    public PageData generateThumImage(String inPath, String outPath, String outPath2) {
        try {
            BufferedImage read = ImageIO.read(new File(inPath));
            int height = read.getHeight();
            int width = read.getWidth();
            if (height > width) {
                Thumbnails.of(inPath).width(this.changeWeight)
                        .toFile(outPath);
            }
            if (height < width) {
                Thumbnails.of(inPath).height(this.changeHeight)
                        .toFile(outPath);
            }
            if (height == width) {
                Thumbnails.of(inPath).forceSize(this.changeHeight, this.changeWeight)
                        .toFile(outPath);
            }
            CutImg(outPath, outPath2, height);
        } catch (IOException e) {
            e.printStackTrace();
            return WebResult.requestFailed(400, "图片压缩失败", null);
        }
        return WebResult.requestSuccess();
    }

    /**
     * 缩放并裁剪图片，输出缩放后的图片和缩放后裁剪的图片。
     *
     * @param inputStream 图片的输入流
     * @param outPath     缩放输出地址
     * @param outPath2    裁剪输出地址
     */
    public PageData generateThumImage(InputStream inputStream, String outPath, String outPath2) {
        try {
            BufferedImage read = ImageIO.read(inputStream);
            int height = read.getHeight();
            int width = read.getWidth();
            if (height > width) {
                Thumbnails.of(inputStream).width(this.changeWeight)
                        .outputQuality(1d)
                        .toFile(outPath);
            }
            if (height < width) {
                Thumbnails.of(inputStream).height(this.changeHeight)
                        .outputQuality(1d)
                        .toFile(outPath);
            }
            if (height == width) {
                Thumbnails.of(inputStream).forceSize(this.changeHeight, this.changeWeight)
                        .outputQuality(1d)
                        .toFile(outPath);
            }
            CutImg(outPath, outPath2, height);
        } catch (IOException e) {
            e.printStackTrace();
            return WebResult.requestFailed(400, "图片压缩失败", null);
        }
        return WebResult.requestSuccess();
    }

    /**
     * 如果设置长图的长度不为0而且传入的图片高度大于设置长图的长度。就认为是长图，裁剪中心顶部，添加长图的水印。
     * @param outPath  输入路径
     * @param outPath2 输出路径
     * @param height   传入图片的高度
     */
    private void CutImg(String outPath, String outPath2, int height) {
        try {
            if (this.longImageHeigh != 0 && height >= this.longImageHeigh) {
                Thumbnails.of(outPath).sourceRegion(Positions.TOP_CENTER, this.changeHeight, this.changeWeight)
                        .size(this.changeHeight, this.changeWeight)
                        .outputQuality(1d).watermark(Positions.BOTTOM_RIGHT, ImageIO.read(this.waterImageFile), 1f)
                        .toFile(outPath2);
            } else {
                Thumbnails.of(outPath).sourceRegion(Positions.CENTER, this.changeHeight, this.changeWeight)
                        .size(this.changeHeight, this.changeWeight)
                        .outputQuality(1d)
                        .toFile(outPath2);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//    public static void main(String[] args) {
//        appFriendImg img = new appFriendImg();
//        img.setChangeHeight(150);
//        img.setChangeWeight(150);
//        img.generateThumImage("D:/timg.jpg", "D:/timg12.jpg", "D:/timg13.jpg");
//    }
}
