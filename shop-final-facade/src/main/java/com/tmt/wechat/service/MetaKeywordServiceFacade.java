package com.tmt.wechat.service;

import java.util.List;

import com.tmt.wechat.entity.MetaKeyword;

/**
 * meta 关键词
 * @author root
 */
public interface MetaKeywordServiceFacade {

	/**
	 * 查询一个匹配到的关键
	 * @param keyword
	 * @return
	 */
	public MetaKeyword searchOne(String keyword, String appId);
	
	/**
	 * 查询前10个
	 * @param keyword
	 * @return
	 */
	public List<MetaKeyword> searchList(String keyword, String appId);
	
	/**
	 * 删除给定的值
	 * @param deletes
	 */
	public void deleteMetas(List<Long> deletes);
	
	/**
	 * 刷新这部分值
	 * @param updates
	 */
	public void refresh_rich(List<Long> updates);
	
	/**
	 * 刷新这部分值
	 * @param updates
	 */
	public void refresh_text(List<Long> updates);
	
	/**
	 * 刷新这部分值
	 * @param updates
	 */
	public void refresh_image(List<Long> updates);
	
	/**
	 * 删除给定的值
	 * @param deletes
	 */
	public void deleteMetas(Long id);
	
	/**
	 * 刷新这部分值
	 * @param updates
	 */
	public void refresh_rich(Long id);
	
	/**
	 * 刷新这部分值
	 * @param updates
	 */
	public void refresh_text(Long id);
	
	/**
	 * 刷新这部分值
	 * @param updates
	 */
	public void refresh_image(Long id);
}