package com.tmt.wechat.bean.message;

import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.tmt.wechat.bean.base.Article;
import com.tmt.wechat.bean.base.Constants.RespType;

/**
 * 回复图文消息
 * 
 * @author lifeng
 * 
 */
@XmlRootElement(name="xml")
public class RespMsgNews extends RespMsg {

	private static final long serialVersionUID = 1L;
	private int articleCount;
	private List<Article> articles;
	public RespMsgNews(MsgHead req, List<Article> articles) {
		super(req, RespType.news.name());
		setArticles(articles);
		this.articleCount = articles.size();
	}
	public RespMsgNews(MsgHead req, Article... articles) {
		this(req, Arrays.asList(articles));
	}
	public RespMsgNews() {}
	@XmlElementWrapper(name="Articles")
	@XmlElement(name = "item")
	public List<Article> getArticles() {
		return articles;
	}
	public void setArticles(List<Article> articles) {
		this.articles = articles;
		this.articleCount = articles.size();
	}
	@XmlElement(name="ArticleCount")
	public int getArticleCount() {
		return articleCount;
	}
}