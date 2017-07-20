package com.maryun.utils;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.maryun.model.PageData;

/**
 * @ClassName: CommonMongo
 * @Description: 公共的Mongo方法
 * @author SR
 * @date 2017年4月6日
 */
@Service
public class PushMongoUtil {

	@Resource
	MongoTemplate mongoTemplate;

	/**
	 * 查询
	 * @param pd
	 * @param collectionName 表名
	 * @return
	 */
	public List<PageData> find(PageData pd, String collectionName, int limit, int skip, String... order) {
		Query query = new Query();
		for (Object key : pd.keySet()) {
			if (key.equals("M_GOAL")) {
				query.addCriteria(new Criteria().orOperator(Criteria.where("M_GOAL").is(pd.getString("M_GOAL")), Criteria.where((String) key).is("all")));
			}
			else {
				query.addCriteria(Criteria.where((String) key).is(pd.getString(key)));
			}

		}
		for (String od : order) {
			query.with(new Sort(Sort.Direction.DESC, od));
		}
		query.limit(limit);
		query.skip(skip);
		return mongoTemplate.find(query, PageData.class, collectionName);
	}

	/**
	 * 查询
	 * @param pd
	 * @param collectionName 表名
	 * @return
	 */
	public List<PageData> findNoPage(PageData pd, String collectionName) {
		Query query = new Query();
		for (Object key : pd.keySet()) {
			if (key.equals("M_GOAL")) {
				query.addCriteria(new Criteria().orOperator(Criteria.where("M_GOAL").is(pd.getString("M_GOAL")), Criteria.where((String) key).is("all")));
			}
			else {
				query.addCriteria(Criteria.where((String) key).is(pd.getString(key)));
			}
		}
		return mongoTemplate.find(query, PageData.class, collectionName);
	}

	/**
	 * 更新
	 * @param pd 查询条件
	 * @param updatePd 更新条件
	 * @param collectionName 表名
	 */
	public void update(PageData pd, PageData updatePd, String collectionName) {
		Update update = new Update();
		for (Object key : updatePd.keySet()) {
			update.set((String) key, updatePd.getString(key));
		}
		mongoTemplate.updateMulti(setQuery(pd), update, PageData.class, collectionName);
	}

	/**
	 * 删除
	 * @param pd
	 * @param collectionName
	 */
	public void remove(PageData pd, String collectionName) {
		Query query = new Query();
		for (Object key : pd.keySet()) {
			query.addCriteria(Criteria.where((String) key).in(pd.getString(key)));
		}
		mongoTemplate.remove(query, collectionName);
	}

	/**
	 * 插入
	 * @param pd
	 * @param collectionName
	 */
	public void insert(PageData pd, String collectionName) {
		mongoTemplate.insert(pd, collectionName);
	}

	/**
	 * 公用方法，查询条件语句构建
	 * @param pd
	 * @return
	 */
	private Query setQuery(PageData pd) {
		Query query = new Query();
		for (Object key : pd.keySet()) {
			query.addCriteria(Criteria.where((String) key).is(pd.getString(key)));
		}

		return query;
	}
}
