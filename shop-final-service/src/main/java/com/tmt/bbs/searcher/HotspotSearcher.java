package com.tmt.bbs.searcher;

import java.util.List;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmt.bbs.entity.HotSpot;
import com.tmt.bbs.entity.ReplyHits;
import com.tmt.bbs.entity.Topic;
import com.tmt.bbs.entity.TopicHot;
import com.tmt.bbs.service.HotspotSearcherFacade;
import com.tmt.bbs.service.HotspotServiceFacade;
import com.tmt.bbs.service.TopicServiceFacade;
import com.tmt.bbs.update.BbsModule;
import com.tmt.bbs.utils.BbsUtils;
import com.tmt.core.persistence.Page;
import com.tmt.core.persistence.PageParameters;
import com.tmt.core.persistence.QueryCondition;
import com.tmt.core.persistence.QueryCondition.Criteria;
import com.tmt.core.persistence.ScorePage;
import com.tmt.core.searcher.BaseSearcher;
import com.tmt.core.utils.Ints;
import com.tmt.core.utils.Lists;

/**
 * 动态热点
 * @author lifeng
 */
@Service
public class HotspotSearcher extends BaseSearcher<HotSpot> implements HotspotSearcherFacade{

	@Autowired
	private HotspotServiceFacade hotspotService;
	@Autowired
	private TopicServiceFacade topicService;
	
	@Override
	protected String getModule() {
		return "hotspot";
	}

	@Override
	protected Document createDocument(HotSpot t) {
		Document document = new Document();
		document.add(new Field(ID, t.getId().toString(), StringField.TYPE_STORED));
		document.add(new Field("type", t.getType().toString(), StringField.TYPE_NOT_STORED));//类型
		document.add(new IntField("hits", t.getHits(), IntField.TYPE_STORED));//点赞
		document.add(new IntField("replys", t.getReplys(), IntField.TYPE_STORED));//回复
		document.add(new Field("isShow", t.getIsShow().toString(), StringField.TYPE_NOT_STORED));//是否上架
		document.add(new NumericDocValuesField("sort", Ints.addI(t.getHits(), t.getReplys())));
		return document;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected <E> E render(Document doc, Map<String, String> fastValues, Byte scene) {
		HotSpot hotSpot = new HotSpot();
		hotSpot.setId(Long.parseLong(doc.get(ID)));
		hotSpot.setHits(Integer.parseInt(doc.get("hits")));
		hotSpot.setReplys(Integer.parseInt(doc.get("replys")));
		return (E)hotSpot;
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
	 * 只显示上架的商品
	 * @param query
	 * @return
	 */
	private Query searcherTopics() {
		return new TermQuery(new Term("type", String.valueOf(Topic.YES)));
	}
	
	/**
	 * 分页查询
	 */
	@Override
	public Page page(ScorePage page) {
		BooleanQuery.Builder builder = new BooleanQuery.Builder();
		builder.add(searcherTopics(), Occur.MUST);
		builder.add(this.searcherIsShow(), Occur.MUST);
		Sort sort = new Sort(new SortField[]{ new SortField("sort", SortField.Type.INT, true), SortField.FIELD_DOC});
		return this.searchPage(builder, sort, page, null, BaseSearcher.scene_ONE);
	}
	
	/**
	 * 刷新索引
	 */
	@Override
	public void refresh(List<Long> updates, Byte module) {
		List<HotSpot> hots = Lists.newArrayList();
		if (module.compareTo(BbsModule.TOPIC_HOTSPOT) == 0) {
			QueryCondition qc = new QueryCondition();
			Criteria c = qc.getCriteria(); c.andIn("ID", updates);
			List<TopicHot> hits = this.hotspotService.queryForTopics(qc);
			for(TopicHot hit: hits) {
				HotSpot hot = new HotSpot();
				hot.setId(hit.getId());
				hot.setType(Topic.YES);
				hot.setHits(hit.getHits());
				hot.setReplys(hit.getReplys());
				hot.setIsShow(this.topicService.isShow(hit.getId()));
				hots.add(hot);
			}
		} else {
			QueryCondition qc = new QueryCondition();
			Criteria c = qc.getCriteria(); c.andIn("ID", updates);
			List<ReplyHits> hits = this.hotspotService.queryForReplys(qc);
			for(ReplyHits hit: hits) {
				HotSpot hot = new HotSpot();
				hot.setId(hit.getId());
				hot.setType(Topic.NO);
				hot.setHits(hit.getHits());
				hot.setReplys(0);
				hot.setIsShow(Topic.YES);
				hots.add(hot);
			}
		}
		this.save(hots);
	}
	
	/**
	 * 索引重建
	 */
	@Override
	public void hotspots_build() {
		// 删除所有的
		this.destory();
		
		// 动态的热度
    	QueryCondition qc = new QueryCondition();
    	int iCount = this.hotspotService.countForTopics(qc);
    	PageParameters param = new PageParameters(1, 500, iCount);
		int iPage = param.getPageCount();
		for(int i=1; i<= iPage; i++) {
			param.setPageIndex(i);
			Page page = this.hotspotService.pageForTopics(qc, param);
			List<TopicHot> hits = page.getData();
			List<HotSpot> hots = Lists.newArrayList();
			for(TopicHot hit: hits) {
				HotSpot hot = new HotSpot();
				hot.setId(hit.getId());
				hot.setType(Topic.YES);
				hot.setHits(hit.getHits());
				hot.setReplys(hit.getReplys());
				hot.setIsShow(this.topicService.isShow(hit.getId()));
				hots.add(hot);
			}
			this.save(hots);
			hots.clear();
		}
		
		// 回复的热度
		qc = new QueryCondition();
    	iCount = this.hotspotService.countForReplys(qc);
    	param = new PageParameters(1, 500, iCount);
		iPage = param.getPageCount();
		for(int i=1; i<= iPage; i++) {
			param.setPageIndex(i);
			Page page = this.hotspotService.pageForReplys(qc, param);
			List<ReplyHits> hits = page.getData();
			List<HotSpot> hots = Lists.newArrayList();
			for(ReplyHits hit: hits) {
				HotSpot hot = new HotSpot();
				hot.setId(hit.getId());
				hot.setType(Topic.NO);
				hot.setHits(hit.getHits());
				hot.setReplys(0);
				hot.setIsShow(Topic.YES);
				hots.add(hot);
			}
			this.save(hots);
			hots.clear();
		}
		
		// 清除所有的相关缓存
		BbsUtils.clearStats();
	}

	/**
	 * 什么也不做
	 */
	@Override
	protected void refresh(List<Long> updates) {}

	/**
	 * 获得单个数据
	 */
	@Override
	public HotSpot get(Long id) {
		return this.get(id.toString(), BaseSearcher.scene_ONE);
	}
}
