package com.tmt.system.entity;

import java.io.Serializable;

import com.tmt.core.entity.BaseParentEntity;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.utils.StringUtils;

/**
 * 附件目录 管理
 * @author 超级管理员
 * @date 2015-07-30
 */
public class AttachmentDir extends BaseParentEntity<Long> implements Serializable{
	
	private static AttachmentDir ROOT = null;
	private static AttachmentDir HOME = null;
	private static AttachmentDir TEMP = null;
	private static final long serialVersionUID = 1L;
	
	private String code; // 附件目录编码
	private String path; // 附件目录路径
	
    public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
    public String getParentIds() {
		return parentIds;
	}
	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}
    public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
    public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	/**
	 * 是否系统目录
	 * @return
	 */
	public Boolean isSystemDir() {
		return StringUtils.isNotBlank(this.getCode()) && StringUtils.startsWith(this.getCode(), "S");
	}
	
	/**
	 * 得到系统根目录
	 * @return
	 */
	public static AttachmentDir getRootDir() {
		if( ROOT == null ) {
			ROOT = new AttachmentDir();
			ROOT.setId(IdGen.ROOT_ID);
			ROOT.setParentId(IdGen.INVALID_ID);
			ROOT.setParentIds("-1,");
			ROOT.setLevel(0);
			ROOT.setPath("");
			ROOT.setCode("S0");
			ROOT.setName("全部文件");
		}
		return ROOT;
	}
	
	/**
	 * 得到用户根目录
	 * @return
	 */
	public static AttachmentDir getHomeDir() {
		if( HOME == null ) {
			HOME = AttachmentDir.newDefaultUserSpace("S1", "用户目录", AttachmentDir.getRootDir());
			HOME.setId(1L);
		}
		return HOME;
	}
	
	/**
	 * 得到临时目录
	 * @return
	 */
	public static AttachmentDir getTempDir() {
		if( TEMP == null ) {
			TEMP = AttachmentDir.newDefaultUserSpace("S3", "临时目录", AttachmentDir.getRootDir());
			TEMP.setId(2L);
		}
		return TEMP;
	}
	
	/**
	 * 创建一个默认的用户空间
	 * @param code
	 * @param name
	 * @return
	 */
	public static AttachmentDir newDefaultUserSpace(String code, String name, AttachmentDir root) {
		AttachmentDir dir = new AttachmentDir();
		dir.setParentId(root.getId());
		dir.setParentIds(StringUtils.format("%s%s,", root.getParentIds(), root.getId()));
		dir.setLevel(root.getLevel() + 1);
		dir.setPath(StringUtils.format("%s/%s", root.getPath(), name));
		dir.setCode(code);
		dir.setName(name);
		return dir;
	}
}