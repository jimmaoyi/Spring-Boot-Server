package com.maryun.lb.mapper.SensitiveWords;

import com.maryun.model.PageData;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

@Mapper
//@CacheConfig(cacheNames = "SENSITIVEWORDS_BINARYTREE")
public interface SensitiveWordsMapper {
	List<PageData> select(PageData pd);

//	@Cacheable(cacheNames = "SENSITIVEWORDS_BINARYTREE",value = "SENSITIVEWORDS_BINARYTREE")
	List<String> selectSensitiveWords(PageData pd);
//	@CacheEvict(key="SENSITIVEWORDS_BINARYTREE")
	void insert(PageData pd);
//	@CacheEvict(key="SENSITIVEWORDS_BINARYTREE")  
	void update(PageData pd);
//	@CacheEvict(key="SENSITIVEWORDS_BINARYTREE")  
	void delete(String[] pd);
//	@CacheEvict(key="SENSITIVEWORDS_BINARYTREE")  
	void fakeDelete(String[] pd);
	
	List<PageData> sensitiveWordslistPage(PageData pd);
}
