package com.tmt.bbs.searcher;

import java.util.List;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.SortedDocValuesField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.util.BytesRef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmt.bbs.entity.Topic;
import com.tmt.bbs.entity.TopicMin;
import com.tmt.bbs.service.TopicSearcherFacade;
import com.tmt.bbs.service.TopicServiceFacade;
import com.tmt.bbs.utils.BbsUtils;
import com.tmt.core.persistence.Page;
import com.tmt.core.persistence.PageParameters;
import com.tmt.core.persistence.QueryCondition;
import com.tmt.core.persistence.QueryCondition.Criteria;
import com.tmt.core.persistence.ScorePage;
import com.tmt.core.searcher.BaseSearcher;
import com.tmt.core.utils.StringUtils;
import com.tmt.core.utils.time.DateUtils;

/**
 * 动态
 * @author lifeng
 */
@Service
public class TopicSearcher extends BaseSearcher<Topic> implements TopicSearcherFacade{

	@Autowired
	private TopicServiceFacade topicService;
	
	@Override
	protected String getModule() {
		return "topic";
	}

	@Override
	protected Document createDocument(Topic topic) {
		Document document = new Document();
		document.add(new Field(ID, topic.getId().toString(), StringField.TYPE_STORED));
		document.add(new Field("sectionId", topic.getSectionId() == null?"":topic.getSectionId().toString(), StringField.TYPE_STORED));
		document.add(new Field("sectionName",  StringUtils.defaultIfBlank(topic.getSectionName(), ""), StringField.TYPE_STORED));
		document.add(new Field("tags", this.idsField(topic.getTags()), StringField.TYPE_STORED));
		document.add(new Field("createImage", topic.getCreateImage(), StringField.TYPE_STORED));
		document.add(new Field("createName", topic.getCreateName(), StringField.TYPE_STORED));
		document.add(new Field("content", topic.getContent(), StringField.TYPE_STORED));
		document.add(new Field("addContent", StringUtils.defaultIfBlank(topic.getAddContent(), ""), StringField.TYPE_STORED));
		document.add(new Field("remarks", topic.getRemarks(), StringField.TYPE_STORED));
		document.add(new Field("mood", StringUtils.defaultIfBlank(topic.getMood(), ""), StringField.TYPE_STORED));
		document.add(new Field("images", StringUtils.defaultIfBlank(topic.getImages(), ""), StringField.TYPE_STORED));
		document.add(new Field("createDate", DateUtils.getFormatDate(topic.getCreateDate(), "yyyy-MM-dd HH:mm:ss"), StringField.TYPE_STORED));
		document.add(new Field("isTop", topic.getIsTop().toString(), StringField.TYPE_NOT_STORED));//是否置顶
		document.add(new Field("isShow", topic.getIsShow().toString(), StringField.TYPE_NOT_STORED));//是否上架
		document.add(new Field("isProduct", topic.getSectionId() == null?"0":"1", StringField.TYPE_NOT_STORED));//是否商品
		
		// 排序
		document.add(new NumericDocValuesField("isTop", topic.getIsTop()));
		document.add(new SortedDocValuesField(CREATE_DATE, new BytesRef(DateUtils.getFormatDate(topic.getCreateDate(), "yyyy-MM-dd HH:mm:ss"))));
		return document;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected <E> E render(Document doc, Map<String, String> fastValues, Byte scene) {
		TopicMin topic = new TopicMin();
		topic.setId(Long.parseLong(doc.get(ID)));
		topic.setSectionName(doc.get("sectionName"));
		topic.setContent(doc.get("remarks"));
		topic.setAddContent(doc.get("addContent"));
		topic.setCreateDate(DateUtils.getFormatDate(doc.get("createDate"), "yyyy-MM-dd HH:mm:ss"));
		topic.setCreateName(doc.get("createName"));
		topic.setCreateImage(doc.get("createImage"));
		topic.setImages(doc.get("images"));
		topic.setMood(doc.get("mood"));
		
		// 详细页面的使用场景
		if (scene != null && scene == BaseSearcher.scene_TWO) {
			topic.setContent(doc.get("content"));
			String sectionId = doc.get("sectionId");
			if (StringUtils.isNotBlank(sectionId)) {
				topic.setSectionId(Long.parseLong(sectionId));
			}
		}
		return (E)topic;
	}

	@Override
	public void refresh(List<Long> updates) {
		QueryCondition qc = new QueryCondition();
		Criteria c = qc.getCriteria();
		c.andIn("ID", updates);
		List<Topic> goodss = this.topicService.queryByCondition(qc);
		for(Topic topic: goodss) {
			topicService.fillContent(topic);
			
			try {
				// 清除统计项目
				if (topic.getSectionId() != null) {
					BbsUtils.clearSectionStats(topic.getSectionId());
					BbsUtils.clearTagStats(topic.getSectionId());
				}
			}catch(Exception e) {}
		}
		this.save(goodss);
		goodss.clear();
	}
	
	/**
	 * 索引重建
	 */
	@Override
	public void topics_build() {
		// 删除所有的
		this.destory();
		// 更新数据
    	QueryCondition qc = new QueryCondition();
    	int iCount = this.topicService.countByCondition(qc);
    	PageParameters param = new PageParameters(1, 500, iCount);
		int iPage = param.getPageCount();
		for(int i=1; i<= iPage; i++) {
			param.setPageIndex(i);
			Page page = this.topicService.queryForPage(qc, param);
			List<Topic> goodss = page.getData();
			for(Topic topic: goodss) {
				topicService.fillContent(topic);
			}
			this.save(goodss);
			goodss.clear();
		}
		// 清除所有的相关缓存
		BbsUtils.clearStats();
	}
	
	/**
	 * 分页查询
	 */
	@Override
	public Page newestpage(ScorePage page) {
		BooleanQuery.Builder builder = new BooleanQuery.Builder();
		builder.add(this.searcherIsShow(), Occur.MUST);
		Sort sort = new Sort(new SortField[]{new SortField(CREATE_DATE, SortField.Type.STRING, true), SortField.FIELD_DOC});
		return this.searchPage(builder, sort, page, null, BaseSearcher.scene_ONE);
	}
	
	/**
	 * 分页查询
	 */
	@Override
	public Page productPage(ScorePage page) {
		BooleanQuery.Builder builder = new BooleanQuery.Builder();
		builder.add(this.searcherIsShow(), Occur.MUST);
		builder.add(this.searcherIsProduct(), Occur.MUST);
		Sort sort = new Sort(new SortField[]{new SortField(CREATE_DATE, SortField.Type.STRING, true), SortField.FIELD_DOC});
		return this.searchPage(builder, sort, page, null, BaseSearcher.scene_ONE);
	}
	
	/**
	 * 分页查询
	 */
	@Override
	public Page productPage(Long sectionId, ScorePage page) {
		if (sectionId == null) {
			return this.productPage(page);
		}
		BooleanQuery.Builder builder = new BooleanQuery.Builder();
		builder.add(this.searcherIsShow(), Occur.MUST);
		builder.add(this.searcherSection(sectionId), Occur.MUST);
		Sort sort = new Sort(new SortField[]{new SortField(CREATE_DATE, SortField.Type.STRING, true), SortField.FIELD_DOC});
		return this.searchPage(builder, sort, page, null, BaseSearcher.scene_ONE);
	}
	
	/**
	 * 分页查询
	 */
	@Override
	public Page productPage(Long sectionId, String tag, ScorePage page) {
		if (StringUtils.isBlank(tag)) {
			return this.productPage(sectionId, page);
		}
		BooleanQuery.Builder builder = new BooleanQuery.Builder();
		builder.add(this.searcherIsShow(), Occur.MUST);
		builder.add(this.searcherSection(sectionId), Occur.MUST);
		builder.add(this.searcherTag(tag), Occur.MUST);
		Sort sort = new Sort(new SortField[]{new SortField(CREATE_DATE, SortField.Type.STRING, true), SortField.FIELD_DOC});
		return this.searchPage(builder, sort, page, null, BaseSearcher.scene_ONE);
	}
	
	/**
	 * 可显示的数据
	 * @param query
	 * @return
	 */
	private Query searcherIsShow() {
		return new TermQuery(new Term("isShow", String.valueOf(Topic.YES)));
	}
	
	/**
	 * 可显示的数据
	 * @param query
	 * @return
	 */
	private Query searcherIsProduct() {
		return new TermQuery(new Term("isProduct", String.valueOf(Topic.YES)));
	}
	
	/**
	 * 分类查询
	 * @param query
	 * @return
	 */
	private Query searcherSection(Long sectionId) {
		return new TermQuery(new Term("sectionId", sectionId.toString()));
	}
	
	/**
	 * 标签查询数据
	 * @param query
	 * @return
	 */
	private Query searcherTag(String tag) {
		return new WildcardQuery(new Term("tags", this.idsQuery(tag)));
	}

	/**
	 * 通过id获取数据
	 */
	@Override
	public <T> T get(Long id) {
		return super.get(id.toString(), BaseSearcher.scene_ONE);
	}

	/**
	 * 详细的数据
	 */
	@Override
	public <T> T detail(Long id) {
		return super.get(id.toString(), BaseSearcher.scene_TWO);
	}

	/**
	 * 获取产品动态的总数
	 */
	@Override
	public int countProduct() {
		BooleanQuery.Builder builder = new BooleanQuery.Builder();
		builder.add(this.searcherIsShow(), Occur.MUST);
		builder.add(this.searcherIsProduct(), Occur.MUST);
		return this.countByQuery(builder);
	}
	
	/**
	 * 获取指定产品动态的总数
	 */
	@Override
	public int countProduct(Long sectionId) {
		BooleanQuery.Builder builder = new BooleanQuery.Builder();
		builder.add(this.searcherIsShow(), Occur.MUST);
		builder.add(this.searcherSection(sectionId), Occur.MUST);
		return this.countByQuery(builder);
	}
	
	/**
	 * 获取指定产品动态的总数 - 标签
	 */
	@Override
	public int countProduct(Long sectionId, String tag) {
		BooleanQuery.Builder builder = new BooleanQuery.Builder();
		builder.add(this.searcherIsShow(), Occur.MUST);
		builder.add(this.searcherSection(sectionId), Occur.MUST);
		builder.add(this.searcherTag(tag), Occur.MUST);
		return this.countByQuery(builder);
	}

	/**
	 * 最新的产品评价
	 */
	@Override
	public <T> T newestProduct(Long sectionId) {
		BooleanQuery.Builder builder = new BooleanQuery.Builder();
		builder.add(this.searcherIsShow(), Occur.MUST);
		builder.add(this.searcherSection(sectionId), Occur.MUST);
		Sort sort = new Sort(new SortField[]{new SortField(CREATE_DATE, SortField.Type.STRING, true), SortField.FIELD_DOC});
		return this.searchOne(builder, sort, BaseSearcher.scene_ONE);
	}
}