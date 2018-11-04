package com.tmt.wechat.entity;

import java.io.Serializable;
import java.util.List;

import com.tmt.common.entity.BaseEntity;
import com.tmt.common.utils.Lists;
import com.tmt.common.utils.StringUtil3;
import com.tmt.wechat.bean.base.WechatConstants;
/**
 * 图文回复 管理
 * @author 超级管理员
 * @date 2016-09-30
 */
public class MetaRich extends BaseEntity<Long> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String appId; // 对应公众号APP_ID
	private String keyword; // 关键词
	private String title; // 标题
	private String image; // 图片
	private Byte topImage; // 图片显示到正文中
	private String content; // 正文
	private String author; // 作者
	private String source; // 原文链接
	private List<MetaRichRela> relas; // 相关
	private App app; // 默认的微信公众号
    
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public App getApp() {
		return app;
	}
	public void setApp(App app) {
		this.app = app;
	}
	public List<MetaRichRela> getRelas() {
		return relas;
	}
	public void setRelas(List<MetaRichRela> relas) {
		this.relas = relas;
	}
	public Byte getTopImage() {
		return topImage;
	}
	public void setTopImage(Byte topImage) {
		this.topImage = topImage;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
    public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
    public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
    public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
    public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
    public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
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
			keyword.setType(WechatConstants.HANDLER_rich.intValue());
			keyword.setConfig(this.getId().toString());
			keyword.setMetaId(this.getId().toString());
			keywords.add(keyword);
		}
		return keywords;
	}
}