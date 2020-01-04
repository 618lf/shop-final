package com.tmt.system.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tmt.core.service.BaseServiceFacade;
import com.tmt.system.entity.Attachment;
import com.tmt.system.entity.AttachmentDir;
import com.tmt.system.entity.User;

/**
 * 附件 管理
 * @author 超级管理员
 * @date 2015-07-30
 */
@Service
public interface AttachmentServiceFacade extends BaseServiceFacade<Attachment,Long> {
	
	/**
	 * 保存
	 */
	public void save(User user, Attachment attachment, AttachmentDir dir);
	
	/**
	 * 删除
	 */
	public void delete(List<Attachment> attachments);
	
	/**
	 * 删除
	 */
	public void rename(Attachment attachment);
	
	/**
	 * 移动
	 */
	public void moveto(Attachment attachment, AttachmentDir dir);
	
	/**
	 * 存储文件，返回真是的访问地址
	 * @return
	 */
	public String storage(byte[] data, String filePath, AttachmentDir dir, String originFileName, long length, User user);
	
	/**
	 * 存储分片
	 * @param data
	 * @param filePath
	 * @param originFileName
	 * @param uuid
	 * @return
	 */
	public String storageChunk(byte[] data, String filePath, String chunk, String uuid);
	
	/**
	 * 合并分片
	 * @param data
	 * @param filePath
	 * @param originFileName
	 * @param uuid
	 * @return
	 */
	public String mergeChunks(String uuid, String filePath, AttachmentDir dir, String originFileName, long length, User user);
}
