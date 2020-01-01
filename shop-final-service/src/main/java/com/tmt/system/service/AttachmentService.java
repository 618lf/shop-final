package com.tmt.system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.common.persistence.BaseDao;
import com.tmt.common.service.BaseService;
import com.tmt.common.utils.StorageUtils;
import com.tmt.common.utils.StringUtils;
import com.tmt.system.dao.AttachmentDao;
import com.tmt.system.entity.Attachment;
import com.tmt.system.entity.AttachmentDir;
import com.tmt.system.entity.User;
import com.tmt.system.service.AttachmentServiceFacade;

/**
 * 附件 管理
 * 
 * @author 超级管理员
 * @date 2015-07-30
 */
@Service
public class AttachmentService extends BaseService<Attachment, Long> implements AttachmentServiceFacade {

	@Autowired
	private AttachmentDao attachmentDao;
	@Autowired
	private AttachmentDirService dirService;

	@Override
	protected BaseDao<Attachment, Long> getBaseDao() {
		return attachmentDao;
	}

	/**
	 * 保存
	 */
	@Transactional
	public void save(User user, Attachment attachment, AttachmentDir dir) {
		attachment.setDirId(dir.getId());
		this.insert(attachment);
	}

	/**
	 * 删除
	 */
	@Transactional
	public void delete(List<Attachment> attachments) {
		this.batchDelete(attachments);
		for (Attachment attachment : attachments) {
			StorageUtils.delete(null, attachment.getStorageUrl());
		}
	}

	/**
	 * 删除
	 */
	@Transactional
	public void rename(Attachment attachment) {
		this.update("updateRename", attachment);
	}

	/**
	 * 移动
	 */
	@Transactional
	public void moveto(Attachment attachment, AttachmentDir dir) {
		attachment.setDirId(dir.getId());
		this.update("updateMovetoDir", attachment);
	}

	/**
	 * 根据唯一码获取附件
	 * 
	 * @param unique
	 * @return
	 */
	public Attachment getUniqueAttachment(String unique) {
		return this.queryForObject("getUniqueAttachment", unique);
	}

	/**
	 * 存储服务
	 */
	@Transactional
	public String storage(byte[] data, String filePath, AttachmentDir dir, String originFileName, long length,
			User user) {
		String url = StorageUtils.upload(data, null, filePath);
		if (StringUtils.isNotBlank(url)) {
			Attachment attachment = new Attachment();
			attachment.setName(originFileName);
			attachment.setSize(length);
			attachment.setType(StorageUtils.getFileSuffix(url));
			attachment.setStorageName(StorageUtils.getFileName(url));
			attachment.setStorageUrl(url);
			this.save(user, attachment, this.getSafeDir(dir, user));
		}
		return url;
	}

	// 得到安全的存储目录(只能操作自己的存储空间)
	private AttachmentDir getSafeDir(AttachmentDir dir, User user) {
		AttachmentDir home = dirService.getUserSpaceDir(user);
		if (!user.isRoot() && !StringUtils.contains(dir.getParentIds(), home.getId().toString())) {
			return home;
		}
		return dir;
	}

	/**
	 * 分片上传
	 */
	@Override
	public String storageChunk(byte[] data, String filePath, String chunk, String uuid) {
		return StorageUtils.uploadChunk(data, null, chunk, uuid);
	}

	/**
	 * 合并分片
	 */
	@Transactional
	public String mergeChunks(String uuid, String filePath, AttachmentDir dir, String originFileName, long length,
			User user) {
		String url = StorageUtils.mergeChunks(uuid, null, filePath);
		if (StringUtils.isNotBlank(url)) {
			Attachment attachment = new Attachment();
			attachment.setName(originFileName);
			attachment.setSize(length);
			attachment.setType(StorageUtils.getFileSuffix(url));
			attachment.setStorageName(StorageUtils.getFileName(url));
			attachment.setStorageUrl(url);
			this.save(user, attachment, this.getSafeDir(dir, user));
		}
		return url;
	}
}