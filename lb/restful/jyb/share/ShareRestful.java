package com.maryun.lb.restful.jyb.share;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.maryun.lb.mapper.jyb.share.ShareMapper;
import com.maryun.mapper.system.user.UserMapper;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.UuidUtil;
import com.maryun.utils.WebResult;
@RestController
@RequestMapping(value = "/share")
public class ShareRestful extends BaseRestful {
	@Autowired
	private ShareMapper shareMapper;
	
	@Autowired
	private UserMapper userMapper;
	
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
		PageData pds = pd;
		lists = shareMapper.listPage(pds);
		pd = this.getPagingPd(lists);
		return WebResult.requestSuccess(pd);
	}
	
	/**
	 * 按主键Id查询
	 * 
	 * @param pd
	 *            查询条件
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/findById")
	public PageData findById(@RequestBody PageData pd) throws Exception {
		pd = shareMapper.findById(pd);
		return WebResult.requestSuccess(pd);
	}
	
	/**
	 * 添加
	 * 
	 * @param pd
	 *            参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/saveAdd")
	public PageData saveAdd(@RequestBody PageData pd) throws Exception {
		boolean isHasByUserName = (boolean)pd.get("isHasByUserName");
		pd.put("GI_ID", UuidUtil.get32UUID());
		if(!isHasByUserName){
			PageData pd_sysUser = new PageData();
//			pd_sysUser.put("USER_ID", UuidUtil.get32UUID());
			pd_sysUser.put("USER_ID", pd.getString("USER_ID"));
			pd_sysUser.put("USERNAME", pd.getString("GI_EMP_NUM"));
			pd_sysUser.put("PASSWORD", pd.getString("PASSWORD"));
			pd_sysUser.put("NAME", pd.getString("GI_NAME"));
			pd_sysUser.put("STATUS", "0");
			pd_sysUser.put("SKIN", "default");
			pd_sysUser.put("PHONE", pd.getString("GI_PHONE"));
			pd_sysUser.put("TYPE", "2");
			pd_sysUser.put("CREATE_UID", pd.getString("CREATE_UID"));
			pd_sysUser.put("CHANGE_UID", pd.getString("CHANGE_UID"));
			pd_sysUser.put("DEL_STATE", pd.getString("DEL_STATE"));
			userMapper.saveAddByOtherType(pd_sysUser);
			pd.put("SYS_UI_ID", pd_sysUser.getString("USER_ID"));
			shareMapper.save(pd);
			pd.put("msg", "success");
		}else{
			pd.put("msg", "failed");
		}
		return WebResult.requestSuccess(pd);
	}
	
	/**
	 * 修改
	 * 
	 * @param pd
	 *            参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/saveEdit")
	public PageData saveEdit(@RequestBody PageData pd) throws Exception {
		boolean isHasByUserName = (boolean)pd.get("isHasByUserName");
		if(!isHasByUserName){
			PageData pd_sysUser = new PageData();
			pd_sysUser.put("USER_ID", pd.getString("USER_ID"));
			pd_sysUser.put("USERNAME", pd.getString("GI_EMP_NUM"));
			pd_sysUser.put("NAME", pd.getString("GI_NAME"));
			pd_sysUser.put("PHONE", pd.getString("GI_PHONE"));
			pd_sysUser.put("CHANGE_UID", pd.getString("CHANGE_UID"));
			userMapper.saveEditByOtherType(pd_sysUser);
			shareMapper.edit(pd);
			pd.put("msg", "success");
		}else{
			pd.put("msg", "failed");
		}
		return WebResult.requestSuccess(pd);
	}
	
	/**
	 * 删除
	 * 
	 * @param pd
	 *            参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/toDelete")
	public PageData toDelete(@RequestBody PageData pd) throws Exception {
		String s_IDS = pd.getString("IDS");
		if(null != s_IDS && !"".equals(s_IDS)){
			pd.put("IDSs", s_IDS.split(","));
			shareMapper.delete(pd);
			pd.put("msg", "success");
		}else{
			pd.put("msg", "failed");
		}
		return WebResult.requestSuccess(pd);
	}
	/**
	 * 删除
	 * 
	 * @param pd
	 *            参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/recommend")
	public PageData recommend(@RequestBody PageData pd) throws Exception {
		String s_IDS = pd.getString("IDS");
		if(null != s_IDS && !"".equals(s_IDS)){
			pd.put("IDSs", s_IDS.split(","));
			shareMapper.recommend(pd);
			pd.put("msg", "success");
		}else{
			pd.put("msg", "failed");
		}
		return WebResult.requestSuccess(pd);
	}
	/**
	 * 查看图片信息
	 * 
	 * @param pd
	 *            参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/selectPic")
	public PageData selectPic(@RequestBody PageData pd) throws Exception {
		List<PageData> lists = null;
		PageData pds = pd;
		lists = shareMapper.selectPic(pds);
		pd.put("lists", lists);
		return WebResult.requestSuccess(pd);
	}
	/**
	 * 查看视频信息
	 * 
	 * @param pd
	 *            参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/selectVideo")
	public PageData selectVideo(@RequestBody PageData pd) throws Exception {
		List<PageData> lists = null;
		PageData pds =  shareMapper.selectVideo(pd);
		pd.put("VIDEO_THUM_URL", pds.getString("VIDEO_THUM_URL"));
		return WebResult.requestSuccess(pd);
	}
	/**
	 * 查询评论信息
	 * 
	 * @param pd
	 *            参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/selectDiscussion")
	public PageData selectDiscussion(@RequestBody PageData pd) throws Exception {
		List<PageData> lists = null;
		if (pd.getPageNumber() != 0) {
			PageHelper.startPage(pd.getPageNumber(), pd.getPageSize());
		}
		lists = shareMapper.selectDiscussion(pd);
		pd = this.getPagingPd(lists);
		return WebResult.requestSuccess(pd);
	}
	/**
	 * 查询点赞信息
	 * 
	 * @param pd
	 *            参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/selectLike")
	public PageData selectLike(@RequestBody PageData pd) throws Exception {
		List<PageData> lists = null;
		if (pd.getPageNumber() != 0) {
			PageHelper.startPage(pd.getPageNumber(), pd.getPageSize());
		}
		lists = shareMapper.selectLike(pd);
		pd = this.getPagingPd(lists);
		return WebResult.requestSuccess(pd);
	}
}
