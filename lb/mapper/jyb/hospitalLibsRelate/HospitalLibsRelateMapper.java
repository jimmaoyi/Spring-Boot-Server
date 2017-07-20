package com.maryun.lb.mapper.jyb.hospitalLibsRelate;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.maryun.model.PageData;

/**
 * 类名称：HospitalLibsRelateMapper 创建人：ChuMingZe 创建时间：2017年2月17日
 * 
 * @version
 */
@Mapper
public interface HospitalLibsRelateMapper {
	/**
	 * 查询
	 * @param map 查询条件
	 * @return
	 */
	List<PageData> listPage(PageData map);
	/**
	 * 查询非绑定的专家库审核通过数据
	 * @param map 查询条件
	 * @return
	 */
	List<PageData> expertsUnbingListPage(PageData map);
	/**
	 * 查询已绑定的专家库审核通过数据
	 * @param map 查询条件
	 * @return
	 */
	List<PageData> expertsBingListPage(PageData map);
	/**
	 * 查询从专家库临时表查询代理与专家对应关系
	 * @param map
	 * @return
	 */
	List<PageData> expertsInitListPage(PageData map);
	/**
	 * 获取代理商绑定专家数量
	 * @param map
	 * @return
	 */
	PageData getAgentBingExpSize(PageData map);
	/**
	 * 第一次绑定专家，默认选取代理商提交的专家
	 * @param map
	 * @return
	 */
	List<PageData> getAgentSubmitExpert(PageData map);
	/**
	 * 第一次以后，默认选中之前选取绑定专家信息
	 * @param map
	 * @return
	 */
	List<PageData> getAgentBingedExpert(PageData map);
	/**
	 * 获取需绑定专家是否重新
	 * @param map
	 * @return
	 */
	List<PageData> getAgentBingExpInfo(PageData map);
	/**
	 * 绑定
	 * @param map 数据集合
	 */
	void bing(List<PageData> map);
	/**
	 * 取消绑定
	 * @param map 数据集合
	 */
	void cancelBing(PageData map);
	/**
	 * 删除
	 * @param map 数据集合
	 */
	void delete(PageData map);
}
