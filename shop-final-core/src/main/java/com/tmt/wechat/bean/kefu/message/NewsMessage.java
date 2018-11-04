package com.tmt.wechat.bean.kefu.message;

import java.util.List;

import com.tmt.wechat.bean.base.Article;
import com.tmt.wechat.bean.base.Constants;
import com.tmt.wechat.bean.base.News;

/**
 * 图文消息 图文消息条数限制在10条以内，注意，如果图文数超过10，则将会无响应。
 * @author lifeng
 */
public class NewsMessage extends Message {

	private static final long serialVersionUID = 1L;

	public NewsMessage(String touser, List<Article> articles) {
		this.news = new News();
		this.news.setArticles(articles);
		this.setTouser(touser);
		this.setMsgtype(Constants.KefuType.news.name());
	}

	private News news;

	public News getNews() {
		return news;
	}

	public void setNews(News news) {
		this.news = news;
	}
}
