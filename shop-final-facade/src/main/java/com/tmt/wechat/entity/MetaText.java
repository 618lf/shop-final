package com.tmt.wechat.entity;

import java.io.Serializable;
import java.util.List;

import com.tmt.common.entity.BaseEntity;
import com.tmt.common.utils.Lists;
import com.tmt.common.utils.StringUtil3;
import com.tmt.wechat.bean.base.WechatConstants;
/**
 * 文本回复 管理
 * @author 超级管理员
 * @date 2016-09-30
 */
public class MetaText extends BaseEntity<Long> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String appId; // 对应公众号APP_ID
	private String keyword; // 关键词
	private String content; // 内容
    
    public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
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
		String[] keys = StringUtil3.split(this.getKeyword(), ",");
		for(String key: keys) {
			MetaKeyword keyword = new MetaKeyword();
			keyword.prePersist();
			keyword.setAppId(this.getAppId());
			keyword.setKeyword(key);
			keyword.setType(WechatConstants.HANDLER_text.intValue());
			keyword.setConfig(this.getContent());
			keyword.setMetaId(this.getId().toString());
			keywords.add(keyword);
		}
		return keywords;
	}
}