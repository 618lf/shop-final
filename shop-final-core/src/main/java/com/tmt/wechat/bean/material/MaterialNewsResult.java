package com.tmt.wechat.bean.material;

import java.util.List;

import com.tmt.common.utils.Lists;
import com.tmt.wechat.bean.massmsg.MassNews.MassNewsArticle;

public class MaterialNewsResult {

	private List<MassNewsArticle> news_item = Lists.newArrayList();

	public List<MassNewsArticle> getNews_item() {
		return news_item;
	}

	public void setNews_item(List<MassNewsArticle> news_item) {
		this.news_item = news_item;
	}
}
