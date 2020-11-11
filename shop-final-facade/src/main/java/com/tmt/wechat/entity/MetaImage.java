package com.tmt.wechat.entity;

import java.io.Serializable;
import java.util.List;

import com.tmt.core.entity.BaseEntity;
import com.tmt.core.utils.Lists;
import com.tmt.core.utils.StringUtils;
import com.tmt.wechat.bean.base.WechatConstants;
import com.tmt.wechat.bean.material.MaterialUploadResult;
/**
 * 图片 管理
 * @author 超级管理员
 * @date 2017-01-12
 */
public class MetaImage extends BaseEntity<Long> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String appId; // 对应公众号APP_ID
	private String keyword; // 关键词
	private String content; // 内容
	private Integer isUpdate; // 是否修改
	private String image; // 选择的图片
	private MaterialUploadResult material;// 实际的内容
    
    public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public MaterialUploadResult getMaterial() {
		return material;
	}
	public void setMaterial(MaterialUploadResult material) {
		this.material = material;
	}
	public Integer getIsUpdate() {
		return isUpdate;
	}
	public void setIsUpdate(Integer isUpdate) {
		this.isUpdate = isUpdate;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
    
    public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	/**
	 * 拆分关键词
	 * @return
	 */
	public List<MetaKeyword> fetchMetaKeywords() {
		List<MetaKeyword> keywords = Lists.newArrayList();
		String[] keys = StringUtils.split(this.getKeyword(), ",");
		for(String key: keys) {
			MetaKeyword keyword = new MetaKeyword();
			keyword.prePersist();
			keyword.setAppId(this.getAppId());
			keyword.setKeyword(key);
			keyword.setType(WechatConstants.HANDLER_pic.intValue());
			keyword.setConfig(this.getContent());
			keyword.setMetaId(this.getId().toString());
			keywords.add(keyword);
		}
		return keywords;
	}
}
