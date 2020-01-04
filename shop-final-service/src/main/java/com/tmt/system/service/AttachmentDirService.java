package com.tmt.system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.core.entity.TreeVO;
import com.tmt.core.persistence.BaseDao;
import com.tmt.core.persistence.QueryCondition;
import com.tmt.core.persistence.QueryCondition.Criteria;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.utils.Lists;
import com.tmt.core.utils.StringUtils;
import com.tmt.system.dao.AttachmentDirDao;
import com.tmt.system.entity.AttachmentDir;
import com.tmt.system.entity.User;
import com.tmt.system.service.AttachmentDirServiceFacade;
import com.tmt.system.service.DataScopeService;

/**
 * 附件目录 管理
 * 
 * @author 超级管理员
 * @date 2015-07-30
 */
@Service
public class AttachmentDirService extends DataScopeService<AttachmentDir, Long> implements AttachmentDirServiceFacade {

	@Autowired
	private AttachmentDirDao attachmentDirDao;

	@Override
	protected BaseDao<AttachmentDir, Long> getBaseDao() {
		return attachmentDirDao;
	}

	/**
	 * 保存
	 */
	@Transactional
	public void save(AttachmentDir dir) {
		AttachmentDir parent = null;
		if (IdGen.isInvalidId(dir.getParentId()) || IdGen.ROOT_ID == dir.getParentId()) {
			dir.setParentId(IdGen.ROOT_ID);
			parent = AttachmentDir.getRootDir();
		} else {
			parent = this.get(dir.getParentId());
		}
		dir.fillByParent(parent);
		if (IdGen.isInvalidId(dir.getId())) {
			this.insert(dir);
		} else {
			this.update(dir);
		}
	}

	/**
	 * 删除
	 */
	@Transactional
	public boolean delete(List<AttachmentDir> attachmentDirs) {
		for (AttachmentDir dir : attachmentDirs) {
			int iCount = this.deleteDirCheck(dir);
			if (iCount > 0) {
				return Boolean.FALSE;
			}
		}
		this.batchDelete(attachmentDirs);
		return Boolean.TRUE;
	}

	/**
	 * 删除
	 */
	@Transactional
	public void rename(AttachmentDir dir, String name) {
		name = name.replaceAll(AttachmentDir.PATH_SEPARATE, "");
		AttachmentDir parent = null;
		if (IdGen.isInvalidId(dir.getParentId()) || IdGen.ROOT_ID == dir.getParentId()) {
			dir.setParentId(IdGen.ROOT_ID);
			parent = AttachmentDir.getRootDir();
		} else {
			parent = this.get(dir.getParentId());
		}
		String oldParentIds = dir.getParentIds();
		String oldPath = dir.getPath();
		Integer oldLevel = dir.getLevel();
		dir.setName(name);
		dir.fillByParent(parent);
		List<AttachmentDir> children = this.findByParent(dir);
		for (AttachmentDir o : children) {
			o.updateIdsByParent(dir, oldParentIds, oldPath, oldLevel);
		}
		this.update(dir);
		this.batchUpdate(children);
	}

	/**
	 * 删除
	 */
	@Transactional
	public void moveto(AttachmentDir source, AttachmentDir dir) {
		String oldParentIds = source.getParentIds();
		String oldPath = source.getPath();
		Integer oldLevel = source.getLevel();
		source.setParentId(dir.getId());
		source.fillByParent(dir);
		List<AttachmentDir> children = this.findByParent(source);
		for (AttachmentDir o : children) {
			o.updateIdsByParent(source, oldParentIds, oldPath, oldLevel);
		}
		this.update(source);
		this.batchUpdate(children);
	}

	/**
	 * 通过父区域查询所有的子区域
	 * 
	 * @param parent
	 * @return
	 */
	public List<AttachmentDir> findByParent(AttachmentDir parent) {
		QueryCondition qc = new QueryCondition();
		Criteria c = qc.getCriteria();
		c.andLike("PARENT_IDS", parent.getId());
		return this.queryByCondition(qc);
	}

	/**
	 * 得到所有的父级目录
	 * 
	 * @return
	 */
	public List<AttachmentDir> queryParents(User user, AttachmentDir dir) {
		QueryCondition qc = new QueryCondition();
		Criteria c = qc.getCriteria();
		c.andLikeColumn(dir.getParentIds(), "AD.ID");
		if (user.isRoot()) {
			return this.queryForList("queryParents", qc);
		}
		return this.queryForDataScopeList(user, "queryParents", "admin:attachment:list", "AD", qc);
	}

	private Integer deleteDirCheck(AttachmentDir dir) {
		return this.countByCondition("deleteDirCheck", dir);
	}

	/**
	 * 通过查询条件查询
	 * 
	 * @param params
	 * @return
	 */
	public List<TreeVO> findTreeList(User user) {
		QueryCondition qc = new QueryCondition();
		qc.setOrderByClause("D.LEVEL");
		if (user.isRoot()) {
			return this.queryForGenericsList("findTreeList", qc);
		}
		return this.queryForDataScopeGenericsList(user, "findTreeList", "admin:attachment:list", "D", qc);
	}

	/**
	 * target 到用户空间的目录
	 * 
	 * @param target
	 * @return
	 */
	public List<AttachmentDir> getUserPaths(User user, AttachmentDir target) {
		List<AttachmentDir> parents = this.queryParents(user, target);
		List<AttachmentDir> copys = Lists.newArrayList();
		copys.addAll(parents);// 父级菜单
		copys.add(target);// 自己本身
		copys.get(0).setName("我的空间");
		return copys;
	}

	/**
	 * 得到用户空间的根目录
	 * 
	 * @return
	 */
	@Transactional
	public AttachmentDir getUserSpaceDir(User user) {
		String no = user.getNo();
		String name = StringUtils.format("%s(%s)", no, user.getName());
		AttachmentDir parent = null;
		if (user.isRoot()) {
			no = "S2";
			name = user.getName();
			parent = AttachmentDir.getRootDir();
		} else {
			parent = AttachmentDir.getHomeDir();
		}
		if (StringUtils.isNotBlank(no)) {
			AttachmentDir dir = this.queryForObject("getByCode", no);
			if (dir == null) {
				dir = AttachmentDir.newDefaultUserSpace(no, name, parent);
				this.save(dir);
			}
			return dir;
		}
		return AttachmentDir.getHomeDir();
	}
}