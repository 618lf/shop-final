package com.tmt.wechat.bean.material;

import java.util.List;

import com.tmt.core.utils.Lists;
import com.tmt.wechat.bean.massmsg.MassNews.MassNewsArticle;

public class MaterialNewsUpdate {

	private String media_id;
	private String index;
	private List<MassNewsArticle> news_item = Lists.newArrayList();
	
	public List<MassNewsArticle> getNews_item() {
		return news_item;
	}
	public void setNews_item(List<MassNewsArticle> news_item) {
		this.news_item = news_item;
	}
	public String getMedia_id() {
		return media_id;
	}
	public void setMedia_id(String media_id) {
		this.media_id = media_id;
	}
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
}
