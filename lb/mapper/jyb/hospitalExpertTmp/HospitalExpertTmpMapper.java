package com.maryun.lb.mapper.jyb.hospitalExpertTmp;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.maryun.model.PageData;

/**
 * 类名称：HospitalExpertLibsMapper 创建人：ChuMingZe 创建时间：2017年2月15日
 * 
 * @version
 */
@Mapper
public interface HospitalExpertTmpMapper {
	/**
	 * 查询
	 * @param map 查询条件
	 * @return
	 */
	List<PageData> listPage(PageData map);
	/**
	 * 合并专家查询
	 * @param map 查询条件
	 * @return
	 */
	List<PageData> mergeListsPage(PageData map);
	/**
	 * 重复专家详情查询
	 * @param map 查询条件
	 * @return
	 */
	List<PageData> mergeViewPageSearch(PageData map);
	/**
	 * 设置专家模板
	 * @param map 数据集合
	 */
	void saveAdd(PageData map);
	/**
	 * 修改专家为模板状态
	 * @param map
	 */
	void saveEdit(PageData map);
	/**
	 * 保存用户信息
	 * @param map
	 */
	void saveUser(PageData map);
	/**
	 * 修改专家为合并状态
	 * @param map
	 */
	void setExpertMerge(PageData map);
	/**
	 * 物理删除专家库专家信息
	 * @param map
	 */
	void setExpertTemplateDelete(PageData map);
	/**
	 * 查询对比信息
	 * @param map 数据集合
	 * @return
	 */
	List<PageData> compareLists(PageData map);
	/**
	 * 合并修改专家库信息
	 * @param map
	 */
	void saveMergeEdit(PageData map);
	/**
	 * 审核
	 * @param map 数据集合
	 */
	void check(PageData map);
	/**
	 * 按主键获取审核日志的退回原因
	 * @param map 数据集合
	 * @return
	 */
	PageData findById(PageData map);
	/**
	 * 记录审核日志
	 * @param map 数据集合
	 */
	void checkLog(List<PageData> map);
	/**
	 * 批量删除
	 * @param map 数据集合
	 */
	void toDelete(PageData pd);
	/**
	 * 批量提交
	 * @param map 数据集合
	 */
	void toSubmit(PageData pd);
	/**
	 * 批量更改提交状态
	 * @param map 数据集合
	 */
	void toSubmission(PageData pd);
	/**
	 * 查询手机号是否重复
	 * @param map 数据集合
	 */
	PageData findByUId(PageData pd);
	/**
	 * 查询提交状态
	 * @param map 数据集合
	 */
	PageData selectStatus(PageData pd);
}
