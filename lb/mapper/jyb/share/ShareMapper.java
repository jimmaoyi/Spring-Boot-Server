package com.maryun.lb.mapper.jyb.share;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.maryun.model.PageData;

/**
 * 类名称：GuideMapper 创建人：ChuMingZe 创建时间：2017年2月21日
 * 
 * @version
 */
@Mapper
public interface ShareMapper {
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
	 * 按主键Ids查询
	 * @param map 主键Id集合
	 * @return
	 */
	List<PageData> findByIds(PageData map);
	/**
	 * 添加
	 * @param map 数据集合
	 */
	void save(PageData map);
	/**
	 * 修改
	 * @param map 数据集合
	 */
	void edit(PageData map);
	/**
	 * 删除
	 * @param map 数据集合
	 */
	void delete(PageData map);
	/**
	 * 修改推荐状态
	 * @param map 数据集合
	 */
	void recommend(PageData pd);
	/**
	 * 查看视频信息
	 * @param map 数据集合
	 */
	PageData selectVideo(PageData pd);
	/**
	 * 查看图片信息
	 * @param map 数据集合
	 */
	List<PageData> selectPic(PageData pd);
	/**
	 * 查询评论信息
	 * @param map 数据集合
	 */
	List<PageData> selectDiscussion(PageData pd);
	/**
	 * 查询点赞信息
	 * @param map 数据集合
	 */
	List<PageData> selectLike(PageData pd);
}
