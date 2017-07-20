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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.maryun.common.service.FileUploadsService;
import com.maryun.mapper.system.fileuploads.FileUploadsMapper;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.FilesSaveLocalUtils;
import com.maryun.utils.IOUtils;
import com.maryun.utils.UuidUtil;
import com.maryun.utils.WebResult;
/**
 * 
 * @ClassName: MobelUploads 
 * @Description: 移动端上传图片或者视频
 * @author SR 
 * @date 2017年3月2日
 */
@Deprecated
@RestController
@RequestMapping(value = "mobelFilesUploads")
public class MobelUploads extends BaseRestful{
	@Resource
	private FileUploadsMapper fileUploadsMapper;
	@Resource
	private FileUploadsService fileUploadsService;
	@Value("${system.file.uploads.basePath}")
	private String BASE_PATH;
	@Value("${system.imageServer.uploads.basePath}")
	private String ImageServer;
	@Value("${server.port}")
	private String port;
	@Resource
	private FilesSaveLocalUtils filesSaveLocalUtils;
	
	/**
	 * 
	 * @Description: 视频或者照片上传，保存并生产缩略图
	 * @param file
	 * @return
	 * @throws Exception
	 */

	@ResponseBody
	@RequestMapping(value = "/mobelFilesUploads")
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
		                            if(fileType.equals(".jpg") || fileType.equals(".JPG")){
		                            }else if (fileType.equals(".mov") || fileType.equals(".MOV") ||fileType.equals(".mp4") || fileType.equals(".MP4")) {
		                      ////      	//UploadChange.resizeVideo(BASE_PATH+path, BASE_PATH+pathslt,100, 100);
		                            }
		                            //"/"符号
		                            String replaceBasePath=BASE_PATH.replaceAll("\\\\", "/");
		                            String replaceSltPath=pathslt.replaceAll("\\\\", "/");
		                            String replacePath=path.replaceAll("\\\\", "/");
		                            //保存数据库
									PageData insertRes = fileUploadsService.insert(id, fileName, replaceBasePath, 
											replacePath, replaceSltPath,
											String.valueOf(multipartFile.getSize()), String.valueOf(multipartFile.getContentType()), 
											MASTER_ID, "0", CREATE_UID,new Date(),null,null,"1");
//									
			                		if(!insertRes.getString("state").equals("200"))
			                			return WebResult.requestFailed(501, "存入数据库失败", insertRes);
									Map map=new HashMap();
									map.put("id", id);
									map.put("path",ImageServer+replacePath);
									map.put("sltpath",ImageServer+replaceSltPath);
									list.add(map);
							}
		                
		              
		        
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println(e);
			}  
			return WebResult.requestSuccess(list);
		}
	/**
	 * 
	 * @Description: 返回一个http流。如果是图片展示在浏览器。如果是视频则下载
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("backFile")
	public ResponseEntity<byte[]> backFile() throws Exception{
		String id=getPageData().getString("id");
		
		PageData pd=new PageData();
		pd.put("ID", id.replace("slt", ""));
		
		PageData findById = fileUploadsMapper.get(pd);
		String THUMBNAILSPath = findById.getString("THUMBNAILS");
		String relativePath=findById.getString("PATH");
		String fileName=findById.getString("FILE_NAME");
		String path="";
		
		
		//如果是缩略图
		if(id.contains("slt")){
			path=THUMBNAILSPath;
		}
		//如果是视频
		else if(relativePath.contains("mp4") || relativePath.contains("MP4") || relativePath.contains("mov") || relativePath.contains("MOV")){
			byte[] file2byte = IOUtils.File2byte(path);
			return  ResponseEntity.ok().contentLength(file2byte.length)
					.header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\""+fileName+"\"")
					.body(file2byte);
		}
		//如果是图片
		else{
			path=BASE_PATH+relativePath;
		}
		byte[] file2byte = IOUtils.File2byte(path);
		return  ResponseEntity.ok().contentLength(file2byte.length)
				.contentType(MediaType.IMAGE_JPEG)
				.header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\""+fileName+"\"")
				.body(file2byte);
	}
}
