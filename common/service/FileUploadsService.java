package com.maryun.common.service;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.maryun.mapper.system.fileuploads.FileUploadsMapper;
import com.maryun.model.PageData;
import com.maryun.utils.WebResult;

/**
 * 
 * @ClassName: FileUploadsService 
 * @Description: 文件上传服务类。
 * @author SR 
 * @date 2017年3月7日
 */
@Service
public class FileUploadsService {
	
	@Resource
	private FileUploadsMapper fileUploadsMapper;
	/**
	 * 
	 * @Description: 储存文件信息到数据库
	 * @param id
	 * @param fileName
	 * @param replaceBasePath
	 * @param replacePath
	 * @param replaceSltPath
	 * @param fileSize
	 * @param fileType
	 * @param masterId
	 * @param status
	 * @param CreateId
	 * @return
	 */
	public PageData insert(String id,String fileName,String replaceBasePath,String replacePath,String replaceSltPath,
			String fileSize,String fileType,String masterId,String status,String CreateId,Date date,String createOrder
			,String location,String delState){
		PageData pd = new PageData();
		pd.put("ID", id);
		pd.put("FILE_NAME",fileName);
		pd.put("BASE_PATH", replaceBasePath); 
		pd.put("PATH", replacePath);
		pd.put("THUMBNAILS", replaceSltPath);
		pd.put("FILE_SIZE", fileSize);
		pd.put("FILE_TYPE", fileType);
		pd.put("MASTER_ID", masterId);
		pd.put("STATUS", status);
		pd.put("CREATE_ID", CreateId);
		pd.put("CREATE_TIME", date);
		pd.put("CREATE_ORDER", createOrder);
		pd.put("LOCATION", location);
		pd.put("DEL_STATE", delState);
		fileUploadsMapper.insert(pd);
		return WebResult.requestSuccess();
	}
}
