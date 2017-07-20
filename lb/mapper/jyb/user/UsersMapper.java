package com.maryun.lb.mapper.jyb.user;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.maryun.model.PageData;

/**
 * 类名称：UserMapper 创建人：ChuMingZe 创建时间：2017年2月21日
 * 
 * @version
 */
@Mapper
public interface UsersMapper {
	/**
	 * 查询
	 * @param map 查询条件
	 * @return
	 */
	List<PageData> listPage(PageData map);
	/**
	 * 按主键Id查询
	 * @param map 主键Id
	 * @return
	 */
	PageData findById(PageData map);
	/**
	 * 删除
	 * @param map 数据集合
	 */
	void delete(PageData map);
	/**
	 * 修改冻结状态
	 * @param map 数据集合
	 */
	void updateFreezeState(PageData map);
	/**
	 * 修改禁言状态
	 * @param map 数据集合
	 */
	void updateForbidSpeakState(PageData map);
	/**
	 * 记录禁言日志
	 * @param map 数据集合
	 */
	void insertForbidSpeakLog(PageData map);
	/**
	 * 修改禁言日志
	 * @param map 数据集合
	 */
	void updateForbidSpeakLog(PageData map);
	/**
	 * 查询禁言日志
	 * @param map
	 * @return
	 */
	PageData findForbidSpeakLog(PageData map);
	/**
	 * 查询push_code
	 * @param map
	 * @return
	 */
	List<PageData> findPushCodeById(List<String> map);
	/**
	 * 储存推送成功的消息
	 * @Description: TODO
	 * @param map    
	 * @return void    
	 * @throws
	 */
	void saveSuccessPush(PageData map);
}
