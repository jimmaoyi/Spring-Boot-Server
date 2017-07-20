package com.maryun.lb.mapper.jyb.hospitalExpertLibs;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.maryun.model.PageData;

/**
 * 类名称：HospitalExpertLibsMapper 创建人：ChuMingZe 创建时间：2017年2月15日
 * 
 * @version
 */
@Mapper
public interface HospitalExpertLibsMapper {
	/**
	 * 查询
	 * @param map 查询条件
	 * @return
	 */
	List<PageData> listPage(PageData map);
	/**
	 * 代理商查询
	 * @param map 查询条件
	 * @return
	 */
	List<PageData> listPageBySP(PageData map);
	/**
	 * 按主键Id查询
	 * @param map 主键Id
	 * @return
	 */
	PageData findById(PageData map);
	/**
	 * 获取OMS重复最大编号
	 * @param map 查询条件
	 * @return
	 */
	String getMaxRepeatOMSNo(PageData map);
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
	void insert(PageData map);
	/**
	 * 修改专家为模板状态
	 * @param map 数据集合
	 */
	void setExpertTemplate(PageData map);
	/**
	 * 修改其他专家为非模板状态
	 * @param map 数据集合
	 */
	void setOtherExpertTemplate(PageData map);
	/**
	 * 修改专家为合并状态
	 * @param map 数据集合
	 */
	void setExpertMerge(PageData map);
	/**
	 * 物理删除专家库专家信息
	 * @param map 数据集合
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
	 * @param map 数据集合
	 */
	void saveMergeEdit(PageData map);
	/**
	 * 保存设置排序信息
	 * @param map 数据集合
	 */
	void saveSetOrder(PageData map);
	/**
	 * 更新绑定专家系统用户
	 * @param map 数据集合
	 */
	void saveSysUserID(PageData map);
	/**
	 * 保存其他已设置排序信息
	 * @param map 数据集合
	 */
	void saveSetOtherOrder(PageData map);
	/**
	 * 审核
	 * @param map 数据集合
	 */
	void check(PageData map);
	/**
	 * 合并状态
	 * @param map 数据集合
	 */
	void toMergeEnd(PageData map);
	/**
	 * 显示状态
	 * @param map 数据集合
	 */
	void toShow(PageData map);
	/**
	 * 按主键获取审核日志的退回原因
	 * @param map 数据集合
	 * @return
	 */
	PageData findCheckLogById(PageData map);
	/**
	 * 记录审核日志
	 * @param map 数据集合
	 */
	void checkLog(List<PageData> map);
	/**
	 * 查询专家库专家绑定代理商
	 * @param map 数据集合
	 */
	List<PageData> selectSP(PageData map);
	/**
	 * 根据手机号查询用户
	 * @param map 数据集合
	 */
	PageData selectUserName(PageData pd);
	/**
	 * 更改之前的专家提交状态
	 * @param map 数据集合
	 */
	void toHeMergeEnd(PageData pd);
	/**
	 * c查询专家信息
	 * @param map 数据集合
	 */
	PageData findByIdForEdit(PageData pd);
	/**
	 * c查询专家信息
	 * @param map 数据集合
	 */
	PageData findByUId(PageData pd);
	/**
	 * 往sys表添加数据
	 * @param map 数据集合
	 */
	void saveUser(PageData pd);
	/**
	 * 专家库修改数据
	 * @param map 数据集合
	 */
	void saveEditForEdit(PageData pd);
	/**
	 * 专家库专家所属代理商
	 * @param map 数据集合
	 */
	List<PageData> selectBindSp(PageData pd);
	/**
	 * 查询专家拒单理由
	 * @param map 数据集合
	 */
	PageData selectREJECT_REASON(PageData pd);
	/**
	 * 查询专家排序
	 * @param map 数据集合
	 */
	PageData selectSort(PageData pd);
}
