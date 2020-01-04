package com.tmt.system.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.tmt.core.entity.TreeVO;
import com.tmt.core.service.BaseServiceFacade;
import com.tmt.system.entity.AttachmentDir;
import com.tmt.system.entity.User;

/**
 * 附件目录 管理
 * @author 超级管理员
 * @date 2015-07-30
 */
public interface AttachmentDirServiceFacade extends BaseServiceFacade<AttachmentDir,Long> {
	
	public static String DIR_HOME = "D_HOME";
		
	/**
	 * 保存
	 */
	@Transactional
	public void save(AttachmentDir dir);
	
	/**
	 * 删除
	 */
	@Transactional
	public boolean delete(List<AttachmentDir> attachmentDirs);
	
	/**
	 * 删除
	 */
	@Transactional
	public void rename(AttachmentDir dir, String name);
	
	/**
	 * 删除
	 */
	@Transactional
	public void moveto(AttachmentDir source, AttachmentDir dir);
	
	/**
	 * 通过父区域查询所有的子区域
	 * @param parent
	 * @return
	 */
	public List<AttachmentDir> findByParent(AttachmentDir parent);
	
	/**
	 * 得到所有的父级目录
	 * @return
	 */
	public List<AttachmentDir> queryParents(User user, AttachmentDir dir);
	
	/**
	 * 通过查询条件查询
	 * @param params
	 * @return
	 */
	public List<TreeVO> findTreeList(User user);
	
	/**
	 * target 到用户空间的目录
	 * @param target
	 * @return
	 */
	public List<AttachmentDir> getUserPaths(User user, AttachmentDir dir);
	
	/**
	 * 得到用户空间的根目录
	 * @return
	 */
	@Transactional
	public AttachmentDir getUserSpaceDir(User user);
}
