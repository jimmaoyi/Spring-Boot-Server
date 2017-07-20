package com.maryun.lb.restful.jyb.videos;

import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.maryun.lb.mapper.jyb.videos.VideosMapper;
import com.maryun.lb.restful.jyb.datainterface.ByTueDataSync;
import com.maryun.lb.restful.jyb.datainterface.TongzhouDataSync;
import com.maryun.lb.restful.jyb.datainterface.XiWeiErDataSync;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.ThreeDes;
import com.maryun.utils.UuidUtil;
import com.maryun.utils.WebResult;

/**
 * 类名称：VideosRestful 创建人：ChuMingZe 创建时间：2017年2月27日
 * 
 * @version
 */
@RestController
@RequestMapping(value = "/video")
public class VideosRestful extends BaseRestful {
	@Autowired
	private VideosMapper videosMapper;
	
	//同洲数据接口-科室
	@Value("${lbaner.sync.tongzhou.url.dept}")
	public String syncUrlDeptTongZhou;
	
	//同洲数据接口-专家
	@Value("${lbaner.sync.tongzhou.url.expert}")
	public String syncUrlExpertTongZhou;
	
	//同洲数据接口-活动
	@Value("${lbaner.sync.tongzhou.url.active}")
	public String syncUrlActiveTongZhou;
	
	//同洲数据接口-资源
	@Value("${lbaner.sync.tongzhou.url.resource}")
	public String syncUrlResourceTongZhou;
	
	//同洲数据接口-播放串
	@Value("${lbaner.sync.tongzhou.url.play}")
	public String syncUrlPlayTongZhou;
	
	//百途数据接口-科室
	@Value("${lbaner.sync.bytue.url.dept}")
	public String syncUrlDeptByTue;
	
	//百途数据接口-专家
	@Value("${lbaner.sync.bytue.url.expert}")
	public String syncUrlExpertByTue;
	
	//百途数据接口-活动
	@Value("${lbaner.sync.bytue.url.active}")
	public String syncUrlActiveByTue;
	
	//百途数据接口-资源
	@Value("${lbaner.sync.bytue.url.resource}")
	public String syncUrlResourceByTue;
	
	//百途数据接口-播放串
	@Value("${lbaner.sync.bytue.url.play}")
	public String syncUrlPlayByTue;
	
	//西维尔数据参数-client
	@Value("${lbaner.sync.xiweier.client}")
	public String syncUrlClientXiWeiEr;
	
	//西维尔数据参数-pw
	@Value("${lbaner.sync.xiweier.pw}")
	public String syncUrlPasswordXiWeiEr;
	
	//西维尔数据参数-ip
	@Value("${lbaner.sync.xiweier.ip}")
	public String syncUrlIPXiWeiEr;
		
	//西维尔数据参数-mac
	@Value("${lbaner.sync.xiweier.mac}")
	public String syncUrlMacXiWeiEr;
		
	//西维尔数据接口-EncryToken
	@Value("${lbaner.sync.xiweier.encryToken}")
	public String syncUrlEncryTokenXiWeiEr;
	
	//西维尔数据接口-Auth
	@Value("${lbaner.sync.xiweier.auth}")
	public String syncUrlAuthXiWeiEr;
	
	//西维尔数据接口-科室
	@Value("${lbaner.sync.xiweier.url.dept}")
	public String syncUrlDeptXiWeiEr;
	
	//西维尔数据接口-专家
	@Value("${lbaner.sync.xiweier.url.expert}")
	public String syncUrlExpertXiWeiEr;
	
	//西维尔数据接口-活动
	@Value("${lbaner.sync.xiweier.url.active}")
	public String syncUrlActiveXiWeiEr;
	
	//西维尔数据接口-资源
	@Value("${lbaner.sync.xiweier.url.resource}")
	public String syncUrlResourceXiWeiEr;
	
	//西维尔数据接口-播放串
	@Value("${lbaner.sync.xiweier.url.play}")
	public String syncUrlPlayXiWeiEr;
	
	/**
	 * 分页查询
	 * 
	 * @param pd
	 *            查询、分页条件
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/pageSearch")
	public PageData pageSearch(@RequestBody PageData pd) throws Exception {
		List<PageData> lists = null;
		if (pd.getPageNumber() != 0) {
			PageHelper.startPage(pd.getPageNumber(), pd.getPageSize());
		}
		lists = videosMapper.listPage(pd);
		// 分页
		pd = this.getPagingPd(lists);
		// 结果集封装
		return WebResult.requestSuccess(pd);
	}
	
	/**
	 * 保存选取视频数据
	 * 
	 * @param pd
	 *            参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/saveSel")
	public PageData saveSelect(@RequestBody PageData pd) throws Exception {
		String s_IDS = pd.getString("A_ID");
		String s_OMS_RES_CODE = pd.getString("OMS_RES_CODE");
		if (null != s_IDS && !"".equals(s_IDS)) {
			pd.put("KeyIDSs", s_IDS.split(","));
			videosMapper.deleteVideoRelate(pd);
			
			if(null != s_OMS_RES_CODE && !"".equals(s_OMS_RES_CODE)){
				List<PageData> lists_vides_relate = new ArrayList<PageData>();
				String[] sa_OMS_RES_CODE = s_OMS_RES_CODE.split(",");
				if(null != sa_OMS_RES_CODE && sa_OMS_RES_CODE.length > 0){
					PageData pd_tmp = null;
					for (String s_tmp : sa_OMS_RES_CODE) {
						pd_tmp = new PageData();
						pd_tmp.put("UUID", UuidUtil.get32UUID());
						pd_tmp.put("RELATE_ID", s_IDS);
						pd_tmp.put("OMS_RES_CODE", s_tmp);
						lists_vides_relate.add(pd_tmp);
					}
					videosMapper.addVideoRelate(lists_vides_relate);
				}
			}
			pd.put("msg", "success");
		}else{
			pd.put("msg", "failed");
		}
		return WebResult.requestSuccess(pd);
	}
	
	/**
	 * 同步视频-同洲
	 * 
	 * @param pd
	 *            参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/toSyncVideoByTongZhou")
	public PageData toSyncVideoByTongZhou(@RequestBody PageData pd) throws Exception {
		//返回结果集合
		List<PageData> lists_res = new ArrayList<PageData>();
		//获取同洲数据通用类
		TongzhouDataSync tzDataSync = new TongzhouDataSync();
		//对应关联主键表数据
		boolean b_mapping = Boolean.parseBoolean(pd.getString("Mapping"));
		//存储视频与关联主键对应关系表
		boolean b_relate = Boolean.parseBoolean(pd.getString("Relate"));
		//同步URL地址
		String s_url = "";
		//同步视频类型
		String s_syncType = pd.getString("SyncType");
		//存储视频类型
		pd.put("V_TYPE", pd.getString("TYPE"));
		if("hospExpert".equals(s_syncType)){
			s_url = syncUrlExpertTongZhou;
			videosMapper.deleteVideosByType(pd);
		}else if("hospDept".equals(s_syncType)){
			s_url = syncUrlDeptTongZhou;
			videosMapper.deleteVideosByType(pd);
		}else if("actives".equals(s_syncType)){
			s_url = syncUrlActiveTongZhou;
			videosMapper.deleteVideosByType(pd);
		}
		if(b_mapping){
			//获取同步数据集合
			List<PageData> lists = JSONObject.parseArray(JSON.parseObject(pd.getString("lists")).getString("rows"), PageData.class);
			lists_res = getCatalogDatasByTongZhou(tzDataSync, lists, s_url);
		}else{
			lists_res = getAssetDatasByTongZhou(tzDataSync, null);
		}
		//生成SQL进行操作
		if(null != lists_res && lists_res.size() > 0){
			//添加视频库SQL参数
			PageData pd_video = null;
			//添加视频对应关系SQL参数
			PageData pd_video_relate = null;
			//添加视频库SQL集合
			List<PageData> lists_videos = new ArrayList<PageData>();
			//添加视频对应关系SQL集合
			List<PageData> lists_video_relate = new ArrayList<PageData>();
			PageData pd_video_tmp = new PageData();
			for (PageData pd_tmp : lists_res) {
				pd_video = new PageData();
				pd_video.put("V_ID", UuidUtil.get32UUID());
				pd_video.put("V_TYPE", pd.getString("TYPE"));
				pd_video.put("V_NAME", pd_tmp.getString("V_NAME"));
				pd_video.put("V_SOURCE", pd_tmp.getString("V_SOURCE"));
				pd_video.put("THUMBNAIL1", pd_tmp.getString("THUMBNAIL1"));
				pd_video.put("THUMBNAIL2", pd_tmp.getString("THUMBNAIL2"));
				pd_video.put("THUMBNAIL3", pd_tmp.getString("THUMBNAIL3"));
				pd_video.put("V_KEY_ID", pd_tmp.getString("ID"));
				//pd_video.put("OMS_RES_CODE", pd_tmp.getString("OMS_RES_CODE"));
				pd_video.put("OMS_RES_CODE", pd_tmp.getString("V_RESOURCECODE"));
				pd_video.put("CREATE_UID", pd.getString("CREATE_UID"));
				pd_video.put("CHANGE_UID", pd.getString("CHANGE_UID"));
				pd_video.put("DEL_STATE", pd.getString("DEL_STATE"));
				lists_videos.add(pd_video);
				
				if(b_relate){
					pd_video_relate = new PageData();
					pd_video_relate.put("UUID", UuidUtil.get32UUID());
					pd_video_relate.put("RELATE_ID", pd_tmp.getString("ID"));
					pd_video_relate.put("OMS_RES_CODE", pd_tmp.getString("V_RESOURCECODE"));
					lists_video_relate.add(pd_video_relate);
				}
			}
			pd_video_tmp.put("V_TYPE", pd.getString("TYPE"));
			pd_video_tmp.put("IDSs", lists_res.get(0).getString("OMS_ID").split(","));
			pd_video_tmp.put("KeyIDSs", lists_res.get(0).getString("KEY_ID").split(","));
			videosMapper.deleteVideos(pd_video_tmp);
			videosMapper.addVideos(lists_videos);
			if(b_relate){
				videosMapper.deleteVideoRelate(pd_video_tmp);
				videosMapper.addVideoRelate(lists_video_relate);
			}
			pd.put("msg", "success");
			pd.put("size", lists_videos.size());
		}else{
			pd.put("msg", "failed");
			pd.put("size", 0);
		}
		return WebResult.requestSuccess(pd);
	}
	
	/**
	 * 同洲-根据选择数据进行同步数据
	 * @param tjDataSync 同洲同步类
	 * @param lists_catalog 选择数据
	 * @param s_syncUrl 同步Url地址
	 * @return
	 * @throws Exception
	 */
	private List<PageData> getCatalogDatasByTongZhou(TongzhouDataSync tjDataSync, List<PageData> lists_catalog, String s_syncUrl) throws Exception {
		//返回结果集合
		List<PageData> lists_res = null;
		//返回结果参数
		PageData pd_res = null;
		//OMS_ID集合
		StringBuffer sb_OMS_ID = new StringBuffer();
		//关联主键ID集合
		StringBuffer sb_KEY_ID = new StringBuffer();
		//树目录列编号
		String s_columnID = "";
		//获取同步类型数据
		List<PageData> lists_catalog_sync = tjDataSync.getCatalogDatas(s_syncUrl);
		if(null != lists_catalog && lists_catalog.size() > 0){
			lists_res = new ArrayList<PageData>();
			for (PageData pd_tmp : lists_catalog) {
				String s_OMS_ID = pd_tmp.getString("OMS_ID");
				if(null != lists_catalog_sync && lists_catalog_sync.size() > 0){
					for (PageData pd_catalog_tmp : lists_catalog_sync) {
						s_columnID = pd_catalog_tmp.getString("columnID");
						if(s_OMS_ID.equals(pd_catalog_tmp.getString("describe"))){
//							sb_OMS_ID.append(s_OMS_ID).append(",");
							sb_KEY_ID.append(pd_tmp.getString("ID")).append(",");
							List<PageData> lists_tmp = getAssetDatasByTongZhou(tjDataSync, s_columnID);
							if(null != lists_tmp && lists_tmp.size() > 0){
								for (PageData pd_asset_tmp : lists_tmp) {
									pd_res = new PageData();
									if(s_columnID.equals(pd_asset_tmp.getString("V_COLUMNID"))){
										pd_res.put("ID", pd_tmp.getString("ID"));
										pd_res.put("V_NAME", pd_asset_tmp.getString("V_NAME"));
										pd_res.put("V_SOURCE", pd_asset_tmp.getString("V_SOURCE"));
										pd_res.put("THUMBNAIL1", pd_asset_tmp.getString("THUMBNAIL1"));
										pd_res.put("THUMBNAIL2", pd_asset_tmp.getString("THUMBNAIL2"));
										pd_res.put("THUMBNAIL3", pd_asset_tmp.getString("THUMBNAIL3"));
										pd_res.put("OMS_RES_CODE", s_OMS_ID);
										pd_res.put("V_COLUMNID", pd_asset_tmp.getString("V_COLUMNID"));
										pd_res.put("V_RESOURCECODE", pd_asset_tmp.getString("V_RESOURCECODE"));
										sb_OMS_ID.append(pd_asset_tmp.getString("V_RESOURCECODE")).append(",");
										pd_res.put("OMS_ID", sb_OMS_ID);
										pd_res.put("KEY_ID", sb_KEY_ID);
										lists_res.add(pd_res);
									}
								}
							}
						}
					}
				}
			}
		}
		return lists_res;
	}
	
	/**
	 * 同洲-根据树目录列编号获取类型资源
	 * @param tjDataSync 同洲同步类
	 * @param s_columnID 树目录列编号
	 * @return
	 * @throws Exception
	 */
	private List<PageData> getAssetDatasByTongZhou(TongzhouDataSync tjDataSync, String s_columnID) throws Exception {
		//返回结果集合
		List<PageData> lists_res = null;
		//返回结果参数
		PageData pd_res = null;
		//资源代码
		String s_resourceCode = "";
		//同步资源URL地址
		String s_syncUrl = "";
		if(null != s_columnID && !"".equals(s_columnID)){
			s_syncUrl = syncUrlResourceTongZhou + s_columnID;
		}else{
			s_syncUrl = syncUrlActiveTongZhou;
		}
		//获取同步资源数据
		List<PageData> lists_Assets = tjDataSync.getAssetDatas(s_syncUrl);
		if(null != lists_Assets && lists_Assets.size() > 0){
			lists_res = new ArrayList<PageData>();
			for (PageData pd_tmp : lists_Assets) {
				s_resourceCode = pd_tmp.getString("resourceCode");
				List<PageData> lists_tmp = getVideoDatasByTongZhou(tjDataSync, s_resourceCode);
				if(null != lists_tmp && lists_tmp.size() > 0){
					for (PageData pd_video_tmp : lists_tmp) {
						pd_res = new PageData();
						if(s_resourceCode.equals(pd_video_tmp.getString("V_RESOURCECODE"))){
							pd_res.put("V_COLUMNID", s_columnID);
							pd_res.put("V_RESOURCECODE", s_resourceCode);
							pd_res.put("V_NAME", pd_tmp.getString("assetName"));
							pd_res.put("V_SOURCE", pd_video_tmp.getString("V_SOURCE"));
							List<PageData> lists_thumbnails = JSON.parseArray(pd_tmp.getString("posterInfo"), PageData.class);
							if(null != lists_thumbnails && lists_thumbnails.size() > 0){
								int i_tmp = 1;
								for (PageData pd_thumbnail_tmp : lists_thumbnails) {
									if(i_tmp == 1){
										pd_res.put("THUMBNAIL1", pd_thumbnail_tmp.getString("serverLocalPath"));
									}else if(i_tmp == 2){
										pd_res.put("THUMBNAIL2", pd_thumbnail_tmp.getString("serverLocalPath"));
									}else if(i_tmp == 3){
										pd_res.put("THUMBNAIL3", pd_thumbnail_tmp.getString("serverLocalPath"));
									}
									i_tmp++;
								}
							}
							lists_res.add(pd_res);
						}
					}
				}
			}
		}
		return lists_res;
	}
	
	/**
	 * 同洲-根据资源代码获取视频信息
	 * @param tjDataSync 同洲同步类
	 * @param s_resourceCode 资源代码
	 * @return
	 * @throws Exception
	 */
	private List<PageData> getVideoDatasByTongZhou(TongzhouDataSync tjDataSync, String s_resourceCode) throws Exception {
		//返回结果集合
		List<PageData> lists_res = null;
		//获取同步视频数据
		List<String> lists_videos = tjDataSync.getVideosData(syncUrlPlayTongZhou + s_resourceCode);
		if(null != lists_videos && lists_videos.size() > 0){
			lists_res = new ArrayList<PageData>();
			for(String s_tmp : lists_videos){
				PageData pd_tmp = new PageData();
				pd_tmp.put("V_SOURCE", s_tmp);
				pd_tmp.put("V_RESOURCECODE", s_resourceCode);
				lists_res.add(pd_tmp);
			}
		}
		return lists_res;
	}
	
	/**
	 * 同步视频-百途
	 * 
	 * @param pd
	 *            参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/toSyncVideoByByTue")
	public PageData toSyncVideoByByTue(@RequestBody PageData pd) throws Exception {
		//返回结果集合
		List<PageData> lists_res = new ArrayList<PageData>();
		//获取百途数据通用类
		ByTueDataSync btDataSync = new ByTueDataSync();
		//对应关联主键表数据
		boolean b_mapping = Boolean.parseBoolean(pd.getString("Mapping"));
		//存储视频与关联主键对应关系表
		boolean b_relate = Boolean.parseBoolean(pd.getString("Relate"));
		//同步URL地址
		String s_url = "";
		//同步视频类型
		String s_syncType = pd.getString("SyncType");
		//存储视频类型
		pd.put("V_TYPE", pd.getString("TYPE"));
		if("hospExpert".equals(s_syncType)){
			s_url = syncUrlExpertByTue;
			videosMapper.deleteVideosByType(pd);
		}else if("hospDept".equals(s_syncType)){
			s_url = syncUrlDeptByTue;
			videosMapper.deleteVideosByType(pd);
		}else if("actives".equals(s_syncType)){
			s_url = syncUrlActiveByTue;
			videosMapper.deleteVideosByType(pd);
		}
		if(b_mapping){
			//获取同步数据集合
			List<PageData> lists = JSONObject.parseArray(JSON.parseObject(pd.getString("lists")).getString("rows"), PageData.class);
			lists_res = getCatalogDatasByByTue(btDataSync, lists, s_url);
		}else{
			lists_res = getAssetDatasByByTue(btDataSync, null);
		}
		//生成SQL进行操作
		if(null != lists_res && lists_res.size() > 0){
			//添加视频库SQL参数
			PageData pd_video = null;
			//添加视频对应关系SQL参数
			PageData pd_video_relate = null;
			//添加视频库SQL集合
			List<PageData> lists_videos = new ArrayList<PageData>();
			//添加视频对应关系SQL集合
			List<PageData> lists_video_relate = new ArrayList<PageData>();
			PageData pd_video_tmp = new PageData();
			for (PageData pd_tmp : lists_res) {
				pd_video = new PageData();
				pd_video.put("V_ID", UuidUtil.get32UUID());
				pd_video.put("V_TYPE", pd.getString("TYPE"));
				pd_video.put("V_NAME", pd_tmp.getString("V_NAME"));
				pd_video.put("V_SOURCE", pd_tmp.getString("V_SOURCE"));
				pd_video.put("THUMBNAIL1", pd_tmp.getString("THUMBNAIL1"));
				pd_video.put("THUMBNAIL2", pd_tmp.getString("THUMBNAIL2"));
				pd_video.put("THUMBNAIL3", pd_tmp.getString("THUMBNAIL3"));
				pd_video.put("V_KEY_ID", pd_tmp.getString("ID"));
				//pd_video.put("OMS_RES_CODE", pd_tmp.getString("OMS_RES_CODE"));
				pd_video.put("OMS_RES_CODE", pd_tmp.getString("V_RESOURCECODE"));
				pd_video.put("CREATE_UID", pd.getString("CREATE_UID"));
				pd_video.put("CHANGE_UID", pd.getString("CHANGE_UID"));
				pd_video.put("DEL_STATE", pd.getString("DEL_STATE"));
				lists_videos.add(pd_video);
				
				if(b_relate){
					pd_video_relate = new PageData();
					pd_video_relate.put("UUID", UuidUtil.get32UUID());
					pd_video_relate.put("RELATE_ID", pd_tmp.getString("ID"));
					pd_video_relate.put("OMS_RES_CODE", pd_tmp.getString("V_RESOURCECODE"));
					lists_video_relate.add(pd_video_relate);
				}
			}
			pd_video_tmp.put("V_TYPE", pd.getString("TYPE"));
			pd_video_tmp.put("IDSs", lists_res.get(0).getString("OMS_ID").split(","));
			pd_video_tmp.put("KeyIDSs", lists_res.get(0).getString("KEY_ID").split(","));
			videosMapper.deleteVideos(pd_video_tmp);
			videosMapper.addVideos(lists_videos);
			if(b_relate){
				videosMapper.deleteVideoRelate(pd_video_tmp);
				videosMapper.addVideoRelate(lists_video_relate);
			}
			pd.put("msg", "success");
			pd.put("size", lists_videos.size());
		}else{
			pd.put("msg", "failed");
			pd.put("size", 0);
		}
		return WebResult.requestSuccess(pd);
	}
	
	/**
	 * 百途-根据选择数据进行同步数据
	 * @param tjDataSync 百途同步类
	 * @param lists_catalog 选择数据
	 * @param s_syncUrl 同步Url地址
	 * @return
	 * @throws Exception
	 */
	private List<PageData> getCatalogDatasByByTue(ByTueDataSync btDataSync, List<PageData> lists_catalog, String s_syncUrl) throws Exception {
		//返回结果集合
		List<PageData> lists_res = null;
		//返回结果参数
		PageData pd_res = null;
		//OMS_ID集合
		StringBuffer sb_OMS_ID = new StringBuffer();
		//关联主键ID集合
		StringBuffer sb_KEY_ID = new StringBuffer();
		//树目录列编号
		String s_primaryID = "";
		if(null != lists_catalog && lists_catalog.size() > 0){
			lists_res = new ArrayList<PageData>();
			//获取同步类型数据
			List<PageData> lists_catalog_sync = btDataSync.getCatalogDatas(s_syncUrl);
			for (PageData pd_tmp : lists_catalog) {
				String s_OMS_ID = pd_tmp.getString("OMS_ID");
				if(null != lists_catalog_sync && lists_catalog_sync.size() > 0){
					for (PageData pd_catalog_tmp : lists_catalog_sync) {
						s_primaryID = pd_catalog_tmp.getString("primaryid");
						
						if(s_OMS_ID.equalsIgnoreCase(pd_catalog_tmp.getString("description"))){
							if("zhangliqiang".equals(pd_catalog_tmp.getString("description"))){
								
							}
							sb_KEY_ID.append(pd_tmp.getString("ID")).append(",");
							List<PageData> lists_tmp = getAssetDatasByByTue(btDataSync, s_primaryID);
							if(null != lists_tmp && lists_tmp.size() > 0){
								for (PageData pd_asset_tmp : lists_tmp) {
									pd_res = new PageData();
									if(s_primaryID.equals(pd_asset_tmp.getString("V_COLUMNID"))){
										pd_res.put("ID", pd_tmp.getString("ID"));
										pd_res.put("V_NAME", pd_asset_tmp.getString("V_NAME"));
										pd_res.put("V_SOURCE", pd_asset_tmp.getString("V_SOURCE"));
										pd_res.put("THUMBNAIL1", pd_asset_tmp.getString("THUMBNAIL1"));
										pd_res.put("THUMBNAIL2", pd_asset_tmp.getString("THUMBNAIL2"));
										pd_res.put("THUMBNAIL3", pd_asset_tmp.getString("THUMBNAIL3"));
										pd_res.put("OMS_RES_CODE", s_OMS_ID);
										pd_res.put("V_COLUMNID", pd_asset_tmp.getString("V_COLUMNID"));
										pd_res.put("V_RESOURCECODE", pd_asset_tmp.getString("V_RESOURCECODE"));
										sb_OMS_ID.append(pd_asset_tmp.getString("V_RESOURCECODE")).append(",");
										pd_res.put("OMS_ID", sb_OMS_ID);
										pd_res.put("KEY_ID", sb_KEY_ID);
										lists_res.add(pd_res);
									}
								}
							}
						}
					}
				}
			}
		}
		return lists_res;
	}
	
	/**
	 * 百途-根据树目录列编号获取类型资源
	 * @param tjDataSync 百途同步类
	 * @param s_categoryID 树目录列编号
	 * @return
	 * @throws Exception
	 */
	private List<PageData> getAssetDatasByByTue(ByTueDataSync btDataSync, String s_primaryID) throws Exception {
		//返回结果集合
		List<PageData> lists_res = null;
		//返回结果参数
		PageData pd_res = null;
		//资源代码
		String s_resourceCode = "";
		//图片地址
		String s_imgUrl = "";
		//同步资源URL地址
		String s_syncUrl = "";
		if(null != s_primaryID && !"".equals(s_primaryID)){
			s_syncUrl = syncUrlResourceByTue + s_primaryID;
		}else{
			s_syncUrl = syncUrlActiveByTue;
		}
		//获取同步资源数据
		List<PageData> lists_Assets = btDataSync.getAssetDatas(s_syncUrl);
		if(null != lists_Assets && lists_Assets.size() > 0){
			lists_res = new ArrayList<PageData>();
			for (PageData pd_tmp : lists_Assets) {
				s_resourceCode = pd_tmp.getString("code");
				s_imgUrl = pd_tmp.getString("fileurl");
				List<PageData> lists_tmp = getVideoDatasByByTue(btDataSync, s_resourceCode);
				if(null != lists_tmp && lists_tmp.size() > 0){
					for (PageData pd_video_tmp : lists_tmp) {
						pd_res = new PageData();
						if(s_resourceCode.equals(pd_video_tmp.getString("V_RESOURCECODE"))){
							pd_res.put("V_COLUMNID", s_primaryID);
							pd_res.put("V_RESOURCECODE", s_resourceCode);
							pd_res.put("V_NAME", pd_tmp.getString("name"));
							pd_res.put("V_SOURCE", pd_video_tmp.getString("V_SOURCE"));
							pd_res.put("THUMBNAIL1", s_imgUrl);
							pd_res.put("THUMBNAIL2", "");
							pd_res.put("THUMBNAIL3", "");
							lists_res.add(pd_res);
						}
					}
				}
			}
		}
		return lists_res;
	}
	
	/**
	 * 百途-根据资源代码获取视频信息
	 * @param tjDataSync 百途同步类
	 * @param s_resourceCode 资源代码
	 * @return
	 * @throws Exception
	 */
	private List<PageData> getVideoDatasByByTue(ByTueDataSync btDataSync, String s_resourceCode) throws Exception {
		//返回结果集合
		List<PageData> lists_res = null;
		//获取同步视频数据
		HashMap<String, String> lists_videos = btDataSync.getVideosData(syncUrlPlayByTue + s_resourceCode);
		if(null != lists_videos && lists_videos.size() > 0){
			lists_res = new ArrayList<PageData>();
			PageData pd_tmp = new PageData();
			pd_tmp.put("V_SOURCE", lists_videos.get("playUrl"));
			pd_tmp.put("V_RESOURCECODE", s_resourceCode);
			lists_res.add(pd_tmp);
		}
		return lists_res;
	}
	
	/**
	 * 同步视频-西维尔
	 * 
	 * @param pd
	 *            参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/toSyncVideoByXiWeiEr")
	public PageData toSyncVideoByXiWeiEr(@RequestBody PageData pd) throws Exception {
		//获取西维尔数据通用类
		XiWeiErDataSync xweDataSync = new XiWeiErDataSync();
		//获取EncryToken
		String s_encryToken = getEncryTokenByXiWeiEr(xweDataSync);
		//生成加密串
		String s_strPass = "";
		String s_userID = syncUrlClientXiWeiEr;
		String s_password = syncUrlPasswordXiWeiEr;
		String s_ip = syncUrlIPXiWeiEr;
		String s_mac = syncUrlMacXiWeiEr;
		String s_et = s_encryToken;
		if(s_userID != null && s_password != null && s_et != null){
			Security.addProvider(new com.sun.crypto.provider.SunJCE());
			String authenticator = "69556765$" + s_et + "$" + s_userID + "$001001990070114000034C09B4BE1B65$" + s_ip + "$" + s_mac + "$990070|$CTC";
			s_userID = syncUrlPasswordXiWeiEr;
			int lenth = s_userID.length();
			for (int i = 0; i < 24 - lenth; i++) {
				s_userID = s_userID + "0";
			}
			byte[] keybyte = s_userID.getBytes();
			byte[] source = authenticator.getBytes();
			byte[] data3 = ThreeDes.ecrypt(keybyte, source);
			s_strPass = ThreeDes.byte2hex(data3);
		}
		//获取鉴权
		Map<String, Object> m_auth = getAuthByXiWeiEr(xweDataSync, s_strPass);
		String s_userToken = m_auth.get("userToken").toString();
//		String s_epgDomain = m_auth.get("epgDomain").toString();
		String s_epgDomain = "http://223.99.20.55:5901/ydjinan/";
		//返回结果集合
		List<PageData> lists_res = new ArrayList<PageData>();
		//对应关联主键表数据
		boolean b_mapping = Boolean.parseBoolean(pd.getString("Mapping"));
		//存储视频与关联主键对应关系表
		boolean b_relate = Boolean.parseBoolean(pd.getString("Relate"));
		//同步URL地址
		String s_url = "";
		//同步视频类型
		String s_syncType = pd.getString("SyncType");
		//存储视频类型
		pd.put("V_TYPE", pd.getString("TYPE"));
		if("hospExpert".equals(s_syncType)){
			s_url = s_epgDomain + syncUrlExpertXiWeiEr + s_userToken;
			videosMapper.deleteVideosByType(pd);
		}else if("hospDept".equals(s_syncType)){
			s_url = s_epgDomain + syncUrlDeptXiWeiEr + s_userToken;
			videosMapper.deleteVideosByType(pd);
		}else if("actives".equals(s_syncType)){
			s_url = s_epgDomain + syncUrlActiveXiWeiEr + s_userToken;
			videosMapper.deleteVideosByType(pd);
		}
		if(b_mapping){
			//获取同步数据集合
			List<PageData> lists = JSONObject.parseArray(JSON.parseObject(pd.getString("lists")).getString("rows"), PageData.class);
			lists_res = getCatalogDatasByXiWeiEr(xweDataSync, lists, s_url, s_userToken, s_epgDomain);
		}else{
			lists_res = getAssetDatasByXiWeiEr(xweDataSync, null, s_userToken, s_epgDomain);
		}
		//生成SQL进行操作
		if(null != lists_res && lists_res.size() > 0){
			//添加视频库SQL参数
			PageData pd_video = null;
			//添加视频对应关系SQL参数
			PageData pd_video_relate = null;
			//添加视频库SQL集合
			List<PageData> lists_videos = new ArrayList<PageData>();
			//添加视频对应关系SQL集合
			List<PageData> lists_video_relate = new ArrayList<PageData>();
			PageData pd_video_tmp = new PageData();
			for (PageData pd_tmp : lists_res) {
				pd_video = new PageData();
				pd_video.put("V_ID", UuidUtil.get32UUID());
				pd_video.put("V_TYPE", pd.getString("TYPE"));
				pd_video.put("V_NAME", pd_tmp.getString("V_NAME"));
				pd_video.put("V_SOURCE", pd_tmp.getString("V_SOURCE"));
				pd_video.put("THUMBNAIL1", pd_tmp.getString("THUMBNAIL1"));
				pd_video.put("THUMBNAIL2", pd_tmp.getString("THUMBNAIL2"));
				pd_video.put("THUMBNAIL3", pd_tmp.getString("THUMBNAIL3"));
				pd_video.put("V_KEY_ID", pd_tmp.getString("ID"));
				//pd_video.put("OMS_RES_CODE", pd_tmp.getString("OMS_RES_CODE"));
				pd_video.put("OMS_RES_CODE", pd_tmp.getString("V_RESOURCECODE"));
				pd_video.put("CREATE_UID", pd.getString("CREATE_UID"));
				pd_video.put("CHANGE_UID", pd.getString("CHANGE_UID"));
				pd_video.put("DEL_STATE", pd.getString("DEL_STATE"));
				lists_videos.add(pd_video);
				
				if(b_relate){
					pd_video_relate = new PageData();
					pd_video_relate.put("UUID", UuidUtil.get32UUID());
					pd_video_relate.put("RELATE_ID", pd_tmp.getString("ID"));
					pd_video_relate.put("OMS_RES_CODE", pd_tmp.getString("V_RESOURCECODE"));
					lists_video_relate.add(pd_video_relate);
				}
			}
			pd_video_tmp.put("V_TYPE", pd.getString("TYPE"));
			pd_video_tmp.put("IDSs", lists_res.get(0).getString("OMS_ID").split(","));
			pd_video_tmp.put("KeyIDSs", lists_res.get(0).getString("KEY_ID").split(","));
			videosMapper.deleteVideos(pd_video_tmp);
			videosMapper.addVideos(lists_videos);
			if(b_relate){
				videosMapper.deleteVideoRelate(pd_video_tmp);
				videosMapper.addVideoRelate(lists_video_relate);
			}
			pd.put("msg", "success");
			pd.put("size", lists_videos.size());
		}else{
			pd.put("msg", "failed");
			pd.put("size", 0);
		}
		return WebResult.requestSuccess(pd);
	}
	

	/**
	 * 西维尔-获取EncryToken
	 * @return
	 * @throws Exception
	 */
	private String getEncryTokenByXiWeiEr(XiWeiErDataSync xweDataSync) throws Exception {
		Map<String, Object> m_encryToken = xweDataSync.getEncryTokenDatas(syncUrlEncryTokenXiWeiEr);
		return m_encryToken.get("encryToken").toString();
	}
	
	/**
	 * 西维尔-获取鉴权
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> getAuthByXiWeiEr(XiWeiErDataSync xweDataSync, String authenticator) throws Exception {
		return xweDataSync.getAuthDatas(syncUrlAuthXiWeiEr + authenticator);
	}
	
	/**
	 * 西维尔-根据选择数据进行同步数据
	 * @param tjDataSync 西维尔同步类
	 * @param lists_catalog 选择数据
	 * @param s_syncUrl 同步Url地址
	 * @param s_userToken 权限
	 * @param s_epgDomain 同步地址前缀
	 * @return
	 * @throws Exception
	 */
	private List<PageData> getCatalogDatasByXiWeiEr(XiWeiErDataSync xweDataSync, List<PageData> lists_catalog, String s_syncUrl, String s_userToken, String s_epgDomain) throws Exception {
		//返回结果集合
		List<PageData> lists_res = null;
		//返回结果参数
		PageData pd_res = null;
		//OMS_ID集合
		StringBuffer sb_OMS_ID = new StringBuffer();
		//关联主键ID集合
		StringBuffer sb_KEY_ID = new StringBuffer();
		//树目录列编号
		String s_categoryID = "";
		//获取同步类型数据
		List<PageData> lists_catalog_sync = xweDataSync.getCatalogDatas(s_syncUrl);
		if(null != lists_catalog && lists_catalog.size() > 0){
			lists_res = new ArrayList<PageData>();
			for (PageData pd_tmp : lists_catalog) {
				String s_OMS_ID = pd_tmp.getString("OMS_ID");
				if(null != lists_catalog_sync && lists_catalog_sync.size() > 0){
					for (PageData pd_catalog_tmp : lists_catalog_sync) {
						s_categoryID = pd_catalog_tmp.getString("id");
						String getName = "";
						if(pd_catalog_tmp.getString("name").contains("_")){
							getName = pd_catalog_tmp.getString("name").split("_")[1];
						}else{
							getName = pd_catalog_tmp.getString("name");
						}
							if(s_OMS_ID.toLowerCase().equals(getName.toLowerCase())){
//								sb_OMS_ID.append(s_OMS_ID).append(",");
								sb_KEY_ID.append(pd_tmp.getString("ID")).append(",");
								List<PageData> lists_tmp = getAssetDatasByXiWeiEr(xweDataSync, s_categoryID, s_userToken, s_epgDomain);
								if(null != lists_tmp && lists_tmp.size() > 0){
									for (PageData pd_asset_tmp : lists_tmp) {
										pd_res = new PageData();
										if(s_categoryID.equals(pd_asset_tmp.getString("V_COLUMNID"))){
											pd_res.put("ID", pd_tmp.getString("ID"));
											pd_res.put("V_NAME", pd_asset_tmp.getString("V_NAME"));
											pd_res.put("V_SOURCE", pd_asset_tmp.getString("V_SOURCE"));
											pd_res.put("THUMBNAIL1", pd_asset_tmp.getString("THUMBNAIL1"));
											pd_res.put("THUMBNAIL2", pd_asset_tmp.getString("THUMBNAIL2"));
											pd_res.put("THUMBNAIL3", pd_asset_tmp.getString("THUMBNAIL3"));
											pd_res.put("OMS_RES_CODE", s_OMS_ID);
											pd_res.put("V_COLUMNID", pd_asset_tmp.getString("V_COLUMNID"));
											pd_res.put("V_RESOURCECODE", pd_asset_tmp.getString("V_RESOURCECODE"));
											sb_OMS_ID.append(pd_asset_tmp.getString("V_RESOURCECODE")).append(",");
											pd_res.put("OMS_ID", sb_OMS_ID);
											pd_res.put("KEY_ID", sb_KEY_ID);
											lists_res.add(pd_res);
										}
									}
								}
							}
					}
				}
			}
		}
		return lists_res;
	}
	
	/**
	 * 西维尔-根据树目录列编号获取类型资源
	 * @param tjDataSync 百途同步类
	 * @param s_categoryID 树目录列编号
	 * @param s_userToken 权限
	 * @param s_epgDomain 同步地址前缀
	 * @return
	 * @throws Exception
	 */
	private List<PageData> getAssetDatasByXiWeiEr(XiWeiErDataSync xweDataSync, String s_categoryID, String s_userToken, String s_epgDomain) throws Exception {
		//返回结果集合
		List<PageData> lists_res = null;
		//返回结果参数
		PageData pd_res = null;
		//资源代码
		String s_resourceCode = "";
		//同步资源URL地址
		String s_syncUrl = "";
		if(null != s_categoryID && !"".equals(s_categoryID)){
			s_syncUrl = s_epgDomain + syncUrlResourceXiWeiEr + s_userToken + "&categoryId=" + s_categoryID;
		}else{
			s_syncUrl = s_epgDomain + syncUrlActiveXiWeiEr;
		}
		//获取同步资源数据
		List<PageData> lists_Assets = xweDataSync.getAssetDatas(s_syncUrl);
		if(null != lists_Assets && lists_Assets.size() > 0){
			lists_res = new ArrayList<PageData>();
			for (PageData pd_tmp : lists_Assets) {
				s_resourceCode = pd_tmp.getString("resourceCode");
				List<PageData> lists_tmp = getVideoDatasByXiWeiEr(xweDataSync, s_resourceCode, s_userToken, s_epgDomain);
				if(null != lists_tmp && lists_tmp.size() > 0){
					for (PageData pd_video_tmp : lists_tmp) {
						pd_res = new PageData();
						if(s_resourceCode.equals(pd_video_tmp.getString("V_RESOURCECODE"))){
							pd_res.put("V_COLUMNID", s_categoryID);
							pd_res.put("V_RESOURCECODE", s_resourceCode);
							pd_res.put("V_NAME", pd_tmp.getString("name"));
							pd_res.put("V_SOURCE", pd_video_tmp.getString("V_SOURCE"));
							pd_res.put("THUMBNAIL1", pd_tmp.getString("picFileURL"));
							pd_res.put("THUMBNAIL2", "");
							pd_res.put("THUMBNAIL3", "");
							lists_res.add(pd_res);
						}
					}
				}
			}
		}
		return lists_res;
	}
	
	/**
	 * 百途-根据资源代码获取视频信息
	 * @param tjDataSync 百途同步类
	 * @param s_resourceCode 资源代码
	 * @return
	 * @throws Exception
	 */
	private List<PageData> getVideoDatasByXiWeiEr(XiWeiErDataSync xweDataSync, String s_resourceCode, String s_userToken, String s_epgDomain) throws Exception {
		//返回结果集合
		List<PageData> lists_res = null;
		//获取同步视频数据
		Map<String, Object> m_videos = xweDataSync.getVideosData(s_epgDomain + syncUrlPlayXiWeiEr + s_userToken + "&contentID=" + s_resourceCode);
//		Map<String, Object> m_videos = xweDataSync.getVideosData(syncUrlPlayXiWeiEr + s_userToken + "&contentID=" + s_resourceCode);
		if(null != m_videos && m_videos.size() > 0){
			lists_res = new ArrayList<PageData>();
			PageData pd_tmp = new PageData();
			pd_tmp.put("V_SOURCE", m_videos.get("playUrl"));
			pd_tmp.put("V_RESOURCECODE", s_resourceCode);
			lists_res.add(pd_tmp);
		}
		return lists_res;
	}
}