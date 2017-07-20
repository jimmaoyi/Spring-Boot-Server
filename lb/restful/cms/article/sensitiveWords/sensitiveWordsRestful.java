package com.maryun.lb.restful.cms.article.sensitiveWords;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.maryun.lb.mapper.SensitiveWords.SensitiveWordsMapper;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.UuidUtil;
import com.maryun.utils.WebResult;
/**
 * 
 * @ClassName: sensitiveWordsRestful 
 * @Description: 敏感词接口
 * @author SR 
 * @date 2017年3月4日
 */
@RestController
@RequestMapping("sensitiveWords")
public class sensitiveWordsRestful extends BaseRestful{
	@Resource
	private RedisTemplate<String, Object> redisTemplate;
	@Resource
	private SensitiveWordsMapper sensitiveWordsMapper;
	private String sensitiveKey="SENSITIVEWORDS_BINARYTREE";
	/**
	 * 
	 * @Description: 查找或者显示list,带分页配置
	 * @param pd
	 * @return@RequestBody PageData pd
	 */
	@RequestMapping("sensitiveWordslistPage")
	public PageData sensitiveWordslistPage(@RequestBody PageData pd){
		List<PageData> sensitiveWordslistPage=null;
		if (pd.getPageNumber()!=0) {
			PageHelper.startPage(pd.getPageNumber(), pd.getPageSize());
		}
		sensitiveWordslistPage = 
				this.sensitiveWordsMapper.sensitiveWordslistPage(pd);
		//分页
		pd=this.getPagingPd(sensitiveWordslistPage);
		return WebResult.requestSuccess(pd);
	}
	
	/**
	 * 
	 * @Description: 去修改按钮页面
	 * @param pd
	 * @return
	 * @throws Exception    
	 * @return PageData    
	 * @throws
	 */
	@RequestMapping(value = "/toEdit")
	public PageData toEdit(@RequestBody PageData pd) throws Exception {
			List<PageData> selectRes = sensitiveWordsMapper.select(pd);
			return WebResult.requestSuccess(selectRes.get(0));
	}
	
	/**
	 * 
	 * @Description: 保存修改按钮
	 * @param pd
	 * @return
	 * @throws Exception    
	 * @return Object    
	 * @throws
	 */
	@RequestMapping(value = "/saveEdit")
	public Object saveEdit(@RequestBody PageData pd) throws Exception {
			pd.put("CHANGE_TIME", new Date());
			sensitiveWordsMapper.update(pd);
			checkSensitiveKey();
			pd.put("msg", "success");
			return WebResult.requestSuccess(pd);
	}
	/**
	 * 
	 * @Description: 保存用户
	 * @param pd
	 * @return
	 * @throws Exception    
	 * @return PageData    
	 * @throws
	 */
	@RequestMapping(value = "/saveAdd")
	public PageData saveAdd(@RequestBody PageData pd) throws Exception {

			if (0 == sensitiveWordsMapper.select(pd).size()) {
				pd.put("CREATE_TIME", new Date());
				pd.put("CHANGE_TIME", new Date());
				pd.put("SW_ID", UuidUtil.get32UUID()); // ID
				sensitiveWordsMapper.insert(pd);
				checkSensitiveKey();
				pd.put("msg", "success");
			} else {
				pd.put("msg", "failed");
			}
			return WebResult.requestSuccess(pd);
	}
	
	/**
	 * 
	 * @Description: 批量删除(逻辑删除)
	 * @param pd
	 * @return
	 * @throws Exception    
	 * @return PageData    
	 * @throws
	 */
	@RequestMapping(value = "/delete")
	public PageData fakeDelete(@RequestBody PageData pd)throws Exception {
			String SensitiveWords_IDS = pd.getString("IDS");
			if (null != SensitiveWords_IDS && !"".equals(SensitiveWords_IDS)) {
				String ArraySensitiveWords_IDS[] = SensitiveWords_IDS.split(",");
				sensitiveWordsMapper.fakeDelete(ArraySensitiveWords_IDS);
				checkSensitiveKey();
				pd.put("msg", "ok");
			} else {
				pd.put("msg", "no");
			}
			return WebResult.requestSuccess(pd);
	}
	
	/**
	 * 
	 * @Description: 批量删除(物理删除)
	 * @param pd
	 * @return
	 * @throws Exception    
	 * @return PageData    
	 * @throws
	 */
	@RequestMapping(value = "/rellDelete")
	public PageData Relldelete(@RequestBody PageData pd)throws Exception {
			String SensitiveWords_IDS = pd.getString("IDS");
			if (null != SensitiveWords_IDS && !"".equals(SensitiveWords_IDS)) {
				String ArraySensitiveWords_IDS[] = SensitiveWords_IDS.split(",");
				sensitiveWordsMapper.delete(ArraySensitiveWords_IDS);
				checkSensitiveKey();
				pd.put("msg", "ok");
			} else {
				pd.put("msg", "no");
			}
			return WebResult.requestSuccess(pd);
	}
	
	/**
	 * 
	 * @Description: 检查是否有缓存，有的话删除
	 */
	private void checkSensitiveKey(){
		if(redisTemplate.hasKey(sensitiveKey)){
    		redisTemplate.delete(sensitiveKey);
    	}
	}
}
