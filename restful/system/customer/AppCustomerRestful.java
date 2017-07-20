package com.maryun.restful.system.customer;

import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.UuidUtil;
import com.maryun.utils.WebResult;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @ClassName: AppCustomerRestful 
 * @Description: app巡检人员发起客服请求
 * @author lh 
 * @date 2017年2月28日
 */
@RestController
@RequestMapping(value = "/app/im")
public class AppCustomerRestful  extends BaseRestful {
	@Value("${system.redis.customer.counter}")
	private String customerCounter;

	@Value("${system.imageServer.uploads.basePath}")
	private String imageServerUrl;

	@Resource
	private RedisTemplate<String, Object> redisTemplate;

	@Resource
	private MongoTemplate mongoTemplate;

	//title超时时间
	private int titleIdTimeOut=60*10;//十分钟
	private int handlerTimeOut=60*20;//二十分钟


	/**
	 *
	 * @Description:
	 * @param pd
	 * @return
	 * @return PageData
	 * @throws
	 */
	@RequestMapping(value = "/createTitle")
	public PageData createTitle(@RequestBody PageData pd) {

		String titleId=UuidUtil.get32UUID();
		pd.put("TITLE_ID", titleId);
		PageData customerPd=distributionCustomer();
		if(customerPd!=null){
			//处理人员
			pd.put("HANDLER_ID", customerPd.get("HANDLER_ID"));
			pd.put("STATUS", "1");//1、未结束  0、结束
			this.savePage(pd);
			mongoTemplate.insert(pd, "IM_TITLE");
			redisTemplate.opsForValue().set(String.join("_",customerCounter,titleId), new Date(), titleIdTimeOut, TimeUnit.SECONDS);
			redisTemplate.opsForValue().set(String.join("_",customerCounter,"CUSTOMER",titleId), new Date(), titleIdTimeOut, TimeUnit.SECONDS);
			PageData retPd=new PageData();
			retPd.put("TITLE_ID",titleId);
			retPd.put("HANDLER",customerPd.get("USER_NAME"));
			retPd.put("HANDLER_ID", customerPd.get("HANDLER_ID"));
			retPd.put("WORKTIME","周一到周五 8：30-17：30");
			retPd.put("DESCRIPTION","请提前准备好病例然后按照约定的时间来医院进行检查");
			System.out.println("回话ID:"+titleId);
			return WebResult.requestSuccess(retPd);
		}else{
			return WebResult.requestFailed(505,"客服人员暂无上班，请稍后操作",null);
		}

	}







	/**
	 *
	 * @Description:
	 * @param pd
	 * @return
	 * @return PageData
	 * @throws
	 */
	@RequestMapping(value = "/checkTitle")
	public PageData checkTitle(@RequestBody PageData pd) {
		String titleId=pd.getString("TITLE_ID");
		if(StringUtils.isNotBlank(titleId)){
			String redisTitleId=String.join("_",customerCounter,titleId);
			if(redisTemplate.hasKey(redisTitleId)){
				return WebResult.requestSuccess();
			}else{
				return WebResult.requestFailed(401,"回话已结束",null);
			}
		}else{
			return WebResult.requestFailed(504,"参数不正确",null);
		}
	}



	/**
	 *
	 * @Description:发送消息 user
	 * @param pd
	 * @return
	 * @return PageData
	 * @throws
	 */
	@RequestMapping(value = "/userSendMsg")
	public PageData userSendMsg(@RequestBody PageData pd){
		pd.put("TITLE_TYPE", "1");//1表示用户  2表示客服人员
		return sendMsg(pd);
	}



	/**
	 *
	 * @Description:用户查询信息
	 * @param pd
	 * @return
	 * @return PageData
	 * @throws
	 */
	@RequestMapping(value = "/userQueryMsg")
	public PageData userQueryMsg(@RequestBody PageData pd){
		String titleId=pd.getString("TITLE_ID");
		if(StringUtils.isNotBlank(titleId)){
			String redisTitleId=String.join("_",customerCounter,titleId);
			if(redisTemplate.hasKey(redisTitleId)){
				Date startDate=(Date) redisTemplate.opsForValue().get(redisTitleId);
				Date now=new Date();
				//查询数据
				pd.put("STARTTIME",startDate);
				pd.put("ENDTIME",now);
				pd.put("TITLE_TYPE","2");
				List<PageData> contentList=loopContent(pd);
				redisTemplate.opsForValue().set(redisTitleId, now, titleIdTimeOut, TimeUnit.SECONDS);
				return WebResult.requestSuccess(contentList);
			}else{
				return WebResult.requestFailed(401,"回话超时",null);
			}
		}else{
			return WebResult.requestFailed(504,"参数不正确",null);
		}
	}



	/**
	 *
	 * @Description:关闭回话
	 * @param pd
	 * @return
	 * @return PageData
	 * @throws
	 */
	@RequestMapping(value = "/closeTitle")
	public PageData closeTitle(@RequestBody PageData pd){
		String titleId=pd.getString("TITLE_ID");
		if(StringUtils.isNotBlank(titleId)){
			Criteria criteria = new Criteria();
			criteria.orOperator(Criteria.where("TITLE_ID").is(pd.get("TITLE_ID")));
			mongoTemplate.updateFirst(new Query(criteria), Update.update("STATUS","0"),"IM_TITLE");
			redisTemplate.delete(String.join("_",customerCounter,titleId));
			return WebResult.requestSuccess();
		}else{
			return WebResult.requestFailed(504,"参数不正确",null);
		}
	}


	/********************客服人员***************************/

	/**
	 *
	 * @Description: 客服人员上线
	 * @param pd----->USER_ID
	 * @return
	 * @return PageData
	 * @throws
	 */
	@RequestMapping(value = "/customerOnline")
	public PageData customerOnline(@RequestBody PageData pd){
		String handlerId=pd.getString("HANDLER_ID");
		if(StringUtils.isNotBlank(handlerId)){
			this.savePage(pd);
			pd.put("_id",false);
			pd.put("_id",handlerId);
			mongoTemplate.save(pd,"IM_INCUSTOMER");
			redisTemplate.opsForValue().set(String.join("_",customerCounter,"HANDLER",handlerId), new Date(), handlerTimeOut, TimeUnit.SECONDS);
			return WebResult.requestSuccess();
		}else{
			return WebResult.requestFailed(504,"参数不正确",null);
		}
	}


	/**
	 *
	 * @Description: 客服人员下线
	 * @param pd----->USER_ID
	 * @return
	 * @return PageData
	 * @throws
	 */
	@RequestMapping(value = "/customerOffline")
	public PageData customerOffline(@RequestBody PageData pd){
		String handlerId=pd.getString("HANDLER_ID");
		if(StringUtils.isNotBlank(handlerId)){
			Criteria criteria = new Criteria();
			criteria.orOperator(Criteria.where("_id").is(handlerId));
			mongoTemplate.remove(new Query(criteria),"IM_INCUSTOMER");
			redisTemplate.delete(String.join("_",customerCounter,"HANDLER",handlerId));
			return WebResult.requestSuccess();
		}else{
			return WebResult.requestFailed(504,"参数不正确",null);
		}
	}



	/**
	 *
	 * @Description: 查询任务
	 * @param
	 * @return  List<PageData>
	 * @throws Exception
	 * @Data: 2017/3/2 下午4:47
	 *
	 */
	@RequestMapping(value = "/customerTitles")
	public PageData customerTitles(@RequestBody PageData pd){
		String handlerId=pd.getString("HANDLER_ID");
		if(StringUtils.isNotBlank(handlerId)){

			Calendar queryTime = Calendar.getInstance();
			queryTime.setTime(new Date());
			queryTime.add(Calendar.SECOND,titleIdTimeOut*-1);


			//查询所有未结束的消息
			Criteria criteria = new Criteria();
			criteria.andOperator(Criteria.where("HANDLER_ID").is(handlerId),Criteria.where("STATUS").is("1"),Criteria.where("CHANGE_TIME").gte(queryTime.getTime()));
			Query query = new Query(criteria);
			List<PageData> allTitle=mongoTemplate.find(query,PageData.class,"IM_TITLE");

			String handlerRedis=String.join("_",customerCounter,"HANDLER",handlerId);
			if(!redisTemplate.hasKey(handlerRedis)){
				return WebResult.requestFailed(401,"本客服以及下线！",null);
			}
			//客服人员上次更新时间
			Date updateDate=(Date) redisTemplate.opsForValue().get(handlerRedis);
			Date now=new Date();

			List<PageData> newTitle=new ArrayList<>();
			List<PageData> oldTitle=new ArrayList<>();
			for(PageData title:allTitle){
				String titleId=title.getString("TITLE_ID");
				if(redisTemplate.hasKey(String.join("_",customerCounter,titleId))){
					//查询新消息
					PageData contentPd=new PageData();
					contentPd.put("TITLE_ID",titleId);
					contentPd.put("STARTTIME",updateDate);
					contentPd.put("ENDTIME",now);
					contentPd.put("TITLE_TYPE","1");
					List<PageData> contentList=loopContent(contentPd);
					title.put("CONTENTLIST",contentList);

					//区分新旧回话
					Date changeTime=(Date)title.get("CHANGE_TIME");
					if(changeTime.compareTo(updateDate)>0){
						newTitle.add(title);
					}else{
						oldTitle.add(title);
					}
				}else{
					//关闭回话
					Criteria updCriteria = new Criteria();
					updCriteria.orOperator(Criteria.where("TITLE_ID").is(pd.get("TITLE_ID")));
					mongoTemplate.updateFirst(new Query(updCriteria), Update.update("STATUS","0"),"IM_TITLE");
				}
			}
			PageData retPd=new PageData();
			retPd.put("NEWTITLE",newTitle);
			retPd.put("OLDTITLE",oldTitle);
			retPd.put("IMGSERVER",imageServerUrl);

			//更新客服时间
			pd.put("_id",false);
			pd.put("_id",handlerId);
			pd.put("CHANGE_TIME",new Date());
			pd.put("CHANGE_UID",pd.getString("HANDLER_ID"));
			mongoTemplate.save(pd,"IM_INCUSTOMER");
			redisTemplate.opsForValue().set(handlerRedis, now, handlerTimeOut, TimeUnit.SECONDS);
			return WebResult.requestSuccess(retPd);
		}else{
			return WebResult.requestFailed(504,"参数错误",null);
		}
	}


	/**
	 *
	 * @Description:客服查询信息
	 * @param pd
	 * @return
	 * @return PageData
	 * @throws
	 */
	@RequestMapping(value = "/customerContents")
	public PageData customerContents(@RequestBody PageData pd){
		String titleId=pd.getString("TITLE_ID");
		if(StringUtils.isNotBlank(titleId)){
			String redisTitleId=String.join("_",customerCounter,"CUSTOMER",titleId);
			if(redisTemplate.hasKey(redisTitleId)){
				Date startDate=(Date) redisTemplate.opsForValue().get(redisTitleId);
				Date now=new Date();
				//查询数据
				pd.put("STARTTIME",startDate);
				pd.put("ENDTIME",now);
				//pd.put("TITLE_TYPE","1");
				List<PageData> contentList=loopContent(pd);
				//redisTemplate.opsForValue().set(redisTitleId, now, titleIdTimeOut, TimeUnit.SECONDS);
				return WebResult.requestSuccess(contentList);
			}else{
				return WebResult.requestSuccess(new ArrayList<>());
			}
		}else{
			return WebResult.requestFailed(504,"参数不正确",null);
		}
	}



	/**
	 *
	 * @Description:发送消息 customer
	 * @param pd
	 * @return
	 * @return PageData
	 * @throws
	 */
	@RequestMapping(value = "/customerSendMsg")
	public PageData customerSendMsg(@RequestBody PageData pd){
		pd.put("TITLE_TYPE", "2");//1表示用户  2表示客服人员
		return sendMsg(pd);
	}

	/**
	 *
	 * @Description:发送消息封装
	 * @param pd
	 * @return
	 * @return PageData
	 * @throws
	 */
	private PageData sendMsg(PageData pd){
		String titleId=pd.getString("TITLE_ID");
		if(StringUtils.isNotBlank(titleId)&&StringUtils.isNotBlank(pd.getString("CONTENT"))){
			String messageType=pd.getString("MESSAGETYPE");
			if(StringUtils.isBlank(messageType)){
				messageType="1";
				pd.put("MESSAGETYPE",messageType);
			}
			//将title的有效期延迟十分钟
			String redisKey=String.join("_",customerCounter,titleId);
			if(redisTemplate.hasKey(redisKey)){
				if("1".equals(messageType)){
					redisTemplate.opsForValue().set(redisKey, redisTemplate.opsForValue().get(redisKey), titleIdTimeOut, TimeUnit.SECONDS);
					Criteria criteria = Criteria.where("TITLE_ID").is(pd.getString("TITLE_ID"));
					Query query = new Query(criteria);
					Update update = Update.update("CHANGE_TIME", new Date());
					mongoTemplate.updateFirst(query, update, "IM_TITLE");
				}
			}else{
				return WebResult.requestFailed(401,"回话已结束",null);
			}

			this.savePage(pd);
			pd.put("CONTENT_ID", UuidUtil.get32UUID());
			mongoTemplate.insert(pd, "IM_CONTENT");
			return WebResult.requestSuccess();
		}else{
			return WebResult.requestFailed(504,"参数不正确",null);
		}
	}




	/**
	 *
	 * @Description: 修改客服人员
	 * @param pd----->HANDLER_ID、TITLE_ID
	 * @return
	 * @return PageData
	 * @throws
	 */
	@RequestMapping(value = "/changeCustomer")
	public PageData changeCustomer(@RequestBody PageData pd) {
		if(StringUtils.isNotBlank(pd.getString("TITLE_ID"))&&StringUtils.isNotBlank(pd.getString("HANDLER_ID"))){
			//修改title值
            mongoTemplate.save(pd,"IM_TITLE");
			return WebResult.requestSuccess();
		}else{
			return WebResult.requestFailed(401, "参数不正确",null);
		}
	}




	/**
	 *
	 * @Description: 获取在线的客服人员
	 * @param pd
	 * @return
	 * @return PageData
	 * @throws
	 */
	@RequestMapping(value = "/getContent")
	public PageData getContent(@RequestBody PageData pd) {
		try {
			List<PageData> allCus=getAllCustomer();
			return WebResult.requestSuccess(allCus);
		} catch (Exception e) {
			e.printStackTrace();
			return WebResult.requestFailed(500, "参数不正确", null);
		}
	}

	/**
	 *
	 * @Description: 查询所有在线客服
	 * @param
	 * @return  List<PageData>
	 * @throws Exception
	 * @Data: 2017/3/2 下午4:47
	 *
	*/
	private List<PageData> getAllCustomer(){
		Calendar now = Calendar.getInstance();
		now.add(Calendar.MINUTE,handlerTimeOut*-1);
		List<PageData> custorList=mongoTemplate.find(new Query(Criteria.where("CHANGE_TIME").gte(now.getTime())),PageData.class,"IM_INCUSTOMER");
		return custorList;
	}



	/**
	 *
	 * @Description: 查询所有未处理的任务
	 * @param
	 * @return  List<PageData>
	 * @throws Exception
	 * @Data: 2017/3/2 下午4:47
	 *
	 */
	private List<PageData> getNotDeal(){
		Criteria criteria = new Criteria();
		criteria.orOperator(Criteria.where("HANDLER_ID").is(""),Criteria.where("STATUS").is("1"));
		Query query = new Query(criteria);
		List<PageData> allTitle=mongoTemplate.find(query,PageData.class,"IM_TITLE");
		return allTitle;
	}



	/**
	 *
	 * @Description: 查询未读消息
	 * @param
	 * @return  List<PageData>
	 * @throws Exception
	 * @Data: 2017/3/2 下午4:47
	 *
	 */
	private List<PageData> loopContent(PageData pd){
		BasicDBObject fieldsObject=new BasicDBObject();
		//指定返回的字段
		fieldsObject.put("CONTENT", true);
		fieldsObject.put("MESSAGETYPE", true);
		fieldsObject.put("CREATE_TIME", true);
		fieldsObject.put("IMAGE_PATH", true);
		fieldsObject.put("_id", false);

//		Criteria criteria = new Criteria();
//		criteria.orOperator(Criteria.where("TITLE_ID").is(pd.get("TITLE_ID")),
//				Criteria.where("CHANGE_TIME").gt(pd.get("STARTTIME")),
//				Criteria.where("CHANGE_TIME").lte(pd.get("EMDTIME")),
//				Criteria.where("TITLE_TYPE").is(pd.get("TITLE_TYPE")));
//				System.out.print(criteria.toString());
		//查询条件
		DBObject dbObject = new BasicDBObject();
		dbObject.put("TITLE_ID",pd.get("TITLE_ID"));
		if(StringUtils.isNotBlank(pd.getString("TITLE_TYPE"))){
			dbObject.put("TITLE_TYPE",pd.get("TITLE_TYPE"));
		}

		dbObject.put("CHANGE_TIME",new BasicDBObject("$lt", pd.get("EMDTIME")));
		dbObject.put("CHANGE_TIME",new BasicDBObject("$gte", pd.get("STARTTIME")));

		Query query = new BasicQuery(dbObject,fieldsObject);
		//排序
		query.with(new Sort(new Sort.Order(Sort.Direction.ASC, "CHANGE_TIME")));
		List<PageData> allContent=mongoTemplate.find(query,PageData.class,"IM_CONTENT");
		return allContent;
	}



	/**
	 *
	 * @Description: 查询所有存活的title
	 * @param
	 * @return  List<PageData>
	 * @throws Exception
	 * @Data: 2017/3/30 下午4:47
	 *
	 */
	private List<PageData> loopTitle(PageData pd){
		BasicDBObject fieldsObject=new BasicDBObject();
		//指定返回的字段
		fieldsObject.put("TITLE_ID", true);
		fieldsObject.put("USER_ID", true);
		fieldsObject.put("USER_NAME", true);
		fieldsObject.put("USER_PHOTO", true);
		fieldsObject.put("CREATE_TIME", true);
		fieldsObject.put("_id", false);

		Criteria criteria = new Criteria();
		criteria.orOperator(Criteria.where("HANDLER_ID").is(pd.getString("HANDLER_ID")),Criteria.where("STATUS").is("1"));
		Query query = new Query(criteria);
		List<PageData> allTitle=mongoTemplate.find(query,PageData.class,"IM_TITLE");
		return allTitle;
	}




	/**
	 *
	 * @Description: 分配客服人员
	 * @return void
	 * @throws
	 */
	private PageData distributionCustomer(){
		List<PageData> custorList=getAllCustomer();
		if(custorList.size()>0){
			Long numLong=redisTemplate.opsForValue().increment(customerCounter, 1);
			int num=numLong.intValue()-1;
			int size=custorList.size();
			if(num>=size){
				num=0;
				redisTemplate.delete(customerCounter);
			}
			PageData pd=custorList.get(num);
			return pd;
		}else{
			return null;
		}
	}



	/**
	 *
	 * @Description: 查询未分配的任务
	 * @return void
	 * @throws
	 */
	//@Scheduled(cron="0 */5 * * * *")
    public void reportCurrentTime() {
		try{
			//查询在线客服
			List<PageData> custorList=getAllCustomer();
			//查询今天的所有未分配的咨询
			List<PageData> allTitle=getNotDeal();
			if(custorList.size()>0&&allTitle.size()>0){
				int size=custorList.size();
				for (PageData pd : allTitle) {
					int num=redisTemplate.opsForValue().increment(customerCounter, 1).intValue();
					if(num>=size){
						num=0;
						redisTemplate.delete(customerCounter);
					}
					PageData cusPd=custorList.get(num);
					pd.put("HANDLER_ID", cusPd.getString("USER_ID"));
					//调用修改处理人员id
					mongoTemplate.save(pd,"IM_TITLE");
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
    }
	
}
