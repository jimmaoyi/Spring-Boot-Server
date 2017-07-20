package com.maryun.restful.system.fileuploads;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.maryun.common.service.FileUploadsService;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.FilesSaveLocalUtils;
import com.maryun.utils.UuidUtil;
import com.maryun.utils.WebResult;

import net.coobird.thumbnailator.Thumbnails;

/**
 * 
 * @ClassName: HeadPortrait 
 * @Description: 头像上传接口
 * @author SR 
 * @date 2017年3月3日
 */
@Deprecated
@RestController
@RequestMapping(value="HeadPortraitUploadsRestful")
public class HeadPortraitUploadsRestful extends BaseRestful{
	
	@Resource
	private FileUploadsService fileUploadsService;
	@Value("${system.file.uploads.basePath}")
	private  String BASE_PATH;
	@Value("${system.imageServer.uploads.basePath}")
	private  String ImageServer;
	@Resource
	private FilesSaveLocalUtils filesSaveLocalUtils;
	//临时路径
	private static final String iconPath="D:/img/icon.png";  
	@ResponseBody
	@RequestMapping(value = "/fileUpds")
	public Object fileUpd(MultipartFile[] file) throws Exception {
			List list=new ArrayList();
			try {
				//获得CREATE_UID
				PageData savePage = this.savePage(getPageData());
				String CREATE_UID = savePage.getString("CREATE_UID");
				
				String MASTER_ID=this.getPageData().get("MASTER_ID")+"";
				
				//String yyydd=DateUtil.getMonth();
				 SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
				String yyydd=simpleDateFormat.format(new Date());
				
		              
		                	for (MultipartFile multipartFile : file) {
		                		//取得当前上传文件的文件名称  
		                        String fileName = multipartFile.getOriginalFilename();
		                        	String id=UuidUtil.get32UUID();
		                            //重命名上传后的文件名  
		                            //String filename = fileName.substring(0,fileName.lastIndexOf("."));
		                            String fileType = fileName.substring(fileName.lastIndexOf("."));
		                            
		                            String newFileName = id+fileType;  
		                            String path=File.separator+yyydd+File.separator+newFileName;
		                            
		                            //定义缩略图路径
		                            String newFileNameslt = id+"slt"+".jpg";
		                            String pathslt=File.separator+yyydd+"slt"+File.separator+newFileNameslt;
		                            
		                            
		                            //硬盘路径是否存在，如果不存在则创建
		                            String dstPath=BASE_PATH +File.separator+yyydd;
		                            File dstFile = new File(dstPath);
		                            if(!dstFile.exists()){
		                                dstFile.mkdirs();
		                            }
		                            //缩略图硬盘路径是否存在，如果不存在则创建
		                            String dstPathslt=BASE_PATH +File.separator+yyydd+"slt";
		                            File dstFileslt = new File(dstPathslt);
		                            if(!dstFileslt.exists()){
		                                dstFileslt.mkdirs();
		                            }
		                            //定义上传路径  
		                            String filePath = BASE_PATH + path;  
		            				//储存图片
		            				if(!filesSaveLocalUtils.savePhotoToPath(multipartFile,filePath)){
		            					return WebResult.requestFailed(501, "存入文件失败", null);
		            				}
									
									Thumbnails.of(BASE_PATH+path)   
							        .size(100, 100)
							        .keepAspectRatio(false)
							        .toFile(BASE_PATH+pathslt);  
//									//缩略图的outputstream
//									FileOutputStream os=new FileOutputStream(BASE_PATH+pathslt);
//									//原图的inputstream
//		                            FileInputStream fileInputStream = new FileInputStream(BASE_PATH+path);
//									UploadChange.resizeImage(fileInputStream, os, "jpg", 100, 100);
		                            //"/"符号
		                            String replaceBasePath=BASE_PATH.replaceAll("\\\\", "/");
		                            String replaceSltPath=pathslt.replaceAll("\\\\", "/");
		                            String replacePath=path.replaceAll("\\\\", "/");
		                            //保存数据库
		                            PageData insertRes = fileUploadsService.insert(id, fileName, replaceBasePath, 
											replacePath, replaceSltPath,
											String.valueOf(multipartFile.getSize()), String.valueOf(multipartFile.getContentType()), 
											MASTER_ID, "0", CREATE_UID,new Date(),null,null,"1");
		                            if(!insertRes.getString("state").equals("200"))
			                			return WebResult.requestFailed(501, "存入数据库失败", insertRes);
									Map map=new HashMap();
									map.put("id", id);
									map.put("path",ImageServer+replacePath);
									map.put("sltpath",ImageServer+replaceSltPath);
									list.add(map);
							}
			} catch (IllegalStateException e) {
				e.printStackTrace();
				System.out.println(e);
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println(e);
			}  
			return WebResult.requestSuccess(list);
		}
}
