package com.maryun.restful.app.sp;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.maryun.utils.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.maryun.mapper.app.user.friend.AppUserFriendMapper;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.restful.system.fileuploads.UploadChange;

@RestController
@RequestMapping(value = "appUserFriendFilesUploads")
public class AppFriendUploads extends BaseRestful {
	@Resource
	private AppUserFriendMapper appUserFriendMapper;

	@Value("${system.file.uploads.basePath}")
	private String BASE_PATH;

	@Value("${server.port}")
	private String port;
	//ffmpeg裁剪图片高宽
	@Value("${ffmpeg.cut.height}")
	private int cutHeight;
	
	@Value("${ffmpeg.cut.weight}")
	private int cutWeight;
	//缩略图片高宽
	@Value("${slt.height}")
	private int sltHeight;
	
	@Value("${slt.weight}")
	private int sltWeight;

	@Value("${appfriend.setLongImage}")
	private int setLongImage;
	
	@Value("${appfriend.setLongImage.waterImagePath}")
	private String waterImagePath;
	
	@Resource
	private ImgChange imgChange;

	@Resource
	private FilesSaveLocalUtils filesSaveLocalUtils;
	
	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");

	@ResponseBody
	@RequestMapping(value = "/Uploads")
	public Object fileUpd(MultipartFile[] file) throws Exception {
		long time = new Date().getTime();
		System.out.println("开始时间"+new Date());
		List list = new ArrayList();
		try {
			PageData pd=this.getPageData();
			String MASTER_ID = pd.getString("MASTER_ID");

			String yyydd = simpleDateFormat.format(new Date());

			for (MultipartFile multipartFile : file) {
				
				// 取得当前上传文件的文件名称
				String fileName = multipartFile.getOriginalFilename();
				String id = UuidUtil.get32UUID();
				// 重命名上传后的文件名
				String fileType = fileName.substring(fileName.lastIndexOf("."));
				

				String newFileName = id + fileType;
				String path=String.join( File.separator, yyydd,newFileName);

				// 定义缩略图路径
				String newFileNameslt =String.join("", id,"slt.jpg");
				String pathslt = String.join(File.separator,yyydd+"slt",newFileNameslt);
				// 硬盘路径是否存在，如果不存在则创建
				String dstPath =String.join(File.separator, BASE_PATH,yyydd);
				File dstFile = new File(dstPath);
				if (!dstFile.exists()) {
					dstFile.mkdirs();
				}
				// 缩略图硬盘路径是否存在，如果不存在则创建
				String dstPathslt = BASE_PATH + File.separator + yyydd + "slt";
				File dstFileslt = new File(dstPathslt);
				if (!dstFileslt.exists()) {
					dstFileslt.mkdirs();
				}
				// 定义上传路径
				String filePath = BASE_PATH + path;
				//储存图片
				if(!filesSaveLocalUtils.savePhotoToPath(multipartFile,filePath)){
					return WebResult.requestFailed(501, "存入文件失败", pd);
				}
				//multipartFile.transferTo(localFile);
				// "/"符号
				String sltPathChange = pathslt.replaceAll("\\\\", "/");
				String Path = path.replaceAll("\\\\", "/");
				Map map = new HashMap();
				if (fileType.equals(".jpg") || fileType.equals(".JPG") || fileType.equals(".bmp") || fileType.equals(".BMP") || fileType.equals(".png") || fileType.equals(".PNG") || fileType.equals(".jpeg") || fileType.equals(".JPEG") ||  fileType.equals(".gif") || fileType.equals(".GIF")) {
					//图片生成缩略图
                    imgChange.setChangeHeightAndWeight(sltHeight,sltWeight);
					imgChange.setLongImage(setLongImage);
					imgChange.setWaterImagePath(waterImagePath);
                    PageData generateThumImage = imgChange.generateThumImage(BASE_PATH + path, BASE_PATH + pathslt, BASE_PATH + pathslt);
                    //PageData generateThum = UploadChange.generateHeightFixationThum(BASE_PATH + path,BASE_PATH + pathslt,sltHeight,sltWeight);

			    	if(!generateThumImage.getString("state").equals("200")){
			    		return WebResult.requestFailed(501, "图片缩略图压缩失败", generateThumImage);
			    	}
					pd.put("PIC_ID", id);
					pd.put("PIC_URL","/"+ Path);
					pd.put("PIC_THUM_URL","/"+ sltPathChange);
					map.put("id", id);
					map.put("path","/"+ Path);
					map.put("sltpath","/"+ sltPathChange);
					list.add(map);
					appUserFriendMapper.savePic(pd);
				}
				else if (fileType.equals(".mov") || fileType.equals(".MOV") || fileType.equals(".mp4") || fileType.equals(".MP4")) {
					UploadChange.resizeVideo(BASE_PATH + path, BASE_PATH + pathslt, cutHeight, cutWeight);
					pd.put("VIDEO_ID", id);
					pd.put("VIDEO_URL","/"+ Path);
					pd.put("VIDEO_THUM_URL","/"+ sltPathChange);
					map.put("id", id);
					map.put("path", "/"+Path);
					map.put("sltpath","/"+sltPathChange);
					list.add(map);
					appUserFriendMapper.saveVideo(pd);
				}
			}

		}
		catch (IllegalStateException e) {
			e.printStackTrace();
			System.out.println(e);
		}
		Date date2 = new Date();
		System.out.println("结束时间"+date2);
		System.out.println("");
		System.err.print("统计时间");
		System.err.print(date2.getTime()-time);
		return WebResult.requestSuccess(list);
	}

	@RequestMapping("picBackFile")
	public ResponseEntity<byte[]> picBackFile() throws Exception {
		String id = getPageData().getString("PIC_ID");

		PageData pd = new PageData();
		pd.put("PIC_ID", id.replace("slt", ""));

		PageData findById = appUserFriendMapper.findPic(pd);
		String PIC_THUM_URL = findById.getString("PIC_THUM_URL");
		String PIC_URL = findById.getString("PIC_URL");
		String path = "";

		// 如果是缩略图
		if (id.contains("slt")) {
			path = PIC_THUM_URL;
		}
		// 如果是图片
		else {
			path = PIC_URL;
		}
		byte[] file2byte = IOUtils.File2byte(path);
		return ResponseEntity.ok().contentLength(file2byte.length).contentType(MediaType.IMAGE_JPEG)
				.header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + id + ".jpg\"").body(file2byte);
	}

	// 视频返回流
	@RequestMapping("VideoBackFile")
	public ResponseEntity<byte[]> VideoBackFile() {
		String id = getPageData().getString("VIDEO_ID");

		PageData pd = new PageData();
		pd.put("VIDEO_ID", id.replace("slt", ""));

		PageData findById = appUserFriendMapper.findVideo(pd);
		String VIDEO_THUM_URL = findById.getString("VIDEO_URL");
		String VIDEO_URL = findById.getString("VIDEO_URL");
		String path = "";

		// 如果是缩略图
		if (id.contains("slt")) {
			path = VIDEO_THUM_URL;
			byte[] file2byte = IOUtils.File2byte(path);
			return ResponseEntity.ok().contentLength(file2byte.length).contentType(MediaType.IMAGE_JPEG).contentLength(file2byte.length)
					.header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + id + ".jpg\"").body(file2byte);
		}
		// 如果是视频
		path = VIDEO_URL;

		byte[] file2byte = IOUtils.File2byte(path);
		return ResponseEntity.ok().contentLength(file2byte.length).contentType(MediaType.APPLICATION_OCTET_STREAM).contentLength(file2byte.length)
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + id + ".mp4\"").body(file2byte);
	}
	
	
}
