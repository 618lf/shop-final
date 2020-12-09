package com.tmt.bbs.searcher;

import java.util.List;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.SortedDocValuesField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.util.BytesRef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmt.bbs.entity.TopicReply;
import com.tmt.bbs.service.ReplySearcherFacade;
import com.tmt.bbs.service.ReplyServiceFacade;
import com.tmt.core.persistence.Page;
import com.tmt.core.persistence.PageParameters;
import com.tmt.core.persistence.QueryCondition;
import com.tmt.core.persistence.QueryCondition.Criteria;
import com.tmt.core.persistence.ScorePage;
import com.tmt.core.searcher.BaseSearcher;
import com.tmt.core.utils.StringUtils;
import com.tmt.core.utils.time.DateUtils;

/**
 * 回复的查询
 * @author lifeng
 */
@Service
public class ReplySearcher extends BaseSearcher<TopicReply> implements ReplySearcherFacade{

	@Autowired
	private ReplyServiceFacade replyService;
	
	@Override
	public Page page(Long topicId, ScorePage page) {
		BooleanQuery.Builder builder = new BooleanQuery.Builder();
		builder.add(searcherTopics(topicId), Occur.MUST);
		Sort sort = new Sort(new SortField[]{new SortField(CREATE_DATE, SortField.Type.STRING, true), SortField.FIELD_DOC});
		return this.searchPage(builder, sort, page, null, BaseSearcher.scene_ONE);
	}

	@Override
	public List<TopicReply> top3(Long topicId) {
		BooleanQuery.Builder builder = new BooleanQuery.Builder();
		builder.add(searcherTopics(topicId), Occur.MUST);
		Sort sort = new Sort(new SortField[]{new SortField(CREATE_DATE, SortField.Type.STRING, true), SortField.FIELD_DOC});
		return this.searchList(builder, sort, 3, null, BaseSearcher.scene_ONE);
	}

	@Override
	protected String getModule() {
		return "reply";
	}

	@Override
	protected Document createDocument(TopicReply topic) {
		Document document = new Document();
		document.add(new Field(ID, topic.getId().toString(), StringField.TYPE_STORED));
		document.add(new Field("topicId", topic.getTopicId().toString(), StringField.TYPE_STORED));
		document.add(new Field("createImage", StringUtils.defaultIfBlank(topic.getCreateImage(), ""), StringField.TYPE_STORED));
		document.add(new Field("createName", topic.getCreateName(), StringField.TYPE_STORED));
		document.add(new Field("content", topic.getContent(), StringField.TYPE_STORED));
		document.add(new Field("reply", StringUtils.defaultIfBlank(topic.getReplyUser(), ""), StringField.TYPE_STORED));
		document.add(new Field("createDate", DateUtils.getFormatDate(topic.getCreateDate(), "yyyy-MM-dd HH:mm:ss"), StringField.TYPE_STORED));
		document.add(new SortedDocValuesField(CREATE_DATE, new BytesRef(DateUtils.getFormatDate(topic.getCreateDate(), "yyyy-MM-dd HH:mm:ss"))));
		return document;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected <E> E render(Document doc, Map<String, String> fastValues,
			Byte scene) {
		TopicReply topic = new TopicReply();
		topic.setId(Long.parseLong(doc.get(ID)));
		topic.setContent(doc.get("content"));
		topic.setCreateDate(DateUtils.getFormatDate(doc.get("createDate"), "yyyy-MM-dd HH:mm:ss"));
		topic.setCreateName(doc.get("createName"));
		topic.setCreateImage(doc.get("createImage"));
		topic.setReplyUser(doc.get("reply"));
		return (E)topic;
	}

	@Override
	public void refresh(List<Long> updates) {
		QueryCondition qc = new QueryCondition();
		Criteria c = qc.getCriteria();
		c.andIn("ID", updates);
		List<TopicReply> goodss = this.replyService.queryByCondition(qc);
		this.save(goodss);
		goodss.clear();
	}
	
	/**
	 * 索引重建
	 */
	@Override
	public void replys_build() {
		// 删除所有的
		this.destory();
		// 更新数据
    	QueryCondition qc = new QueryCondition();
    	int iCount = this.replyService.countByCondition(qc);
    	PageParameters param = new PageParameters(1, 500, iCount);
		int iPage = param.getPageCount();
		for(int i=1; i<= iPage; i++) {
			param.setPageIndex(i);
			Page page = this.replyService.queryForPage(qc, param);
			List<TopicReply> goodss = page.getData();
			this.save(goodss);
			goodss.clear();
		}
	}
	
	/**
	 * 只显示上架的商品
	 * @param query
	 * @return
	 */
	private Query searcherTopics(Long topicId) {
		return new TermQuery(new Term("topicId", String.valueOf(topicId)));
	}

	/**
	 * 得到单个数据
	 */
	@Override
	public TopicReply get(Long id) {
		return this.get(id.toString(), BaseSearcher.scene_ONE);
	}
}