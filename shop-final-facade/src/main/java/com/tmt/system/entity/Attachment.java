package com.tmt.system.entity;

import java.io.Serializable;

import com.tmt.common.entity.BaseEntity;

/**
 * 附件 管理
 * @author 超级管理员
 * @date 2015-07-30
 */
public class Attachment extends BaseEntity<Long> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String unique;//唯一码
	private Long dirId; // 父级编号
	private Long size; // 附件大小KB
	private String type; // 附件类型
	private String storageName; // 存储的名称
	private String storageUrl; // 存储的URL(直接用于显示的Url)
	
    public String getUnique() {
		return unique;
	}
	public void setUnique(String unique) {
		this.unique = unique;
	}
	public Long getDirId() {
		return dirId;
	}
	public void setDirId(Long dirId) {
		this.dirId = dirId;
	}
    public Long getSize() {
		return size;
	}
	public void setSize(Long size) {
		this.size = size;
	}
    public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
    public String getStorageName() {
		return storageName;
	}
	public void setStorageName(String storageName) {
		this.storageName = storageName;
	}
    public String getStorageUrl() {
		return storageUrl;
	}
	public void setStorageUrl(String storageUrl) {
		this.storageUrl = storageUrl;
	}
}