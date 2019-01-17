package com.tmt.wechat.searcher;

import java.util.List;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.WildcardQuery;
import org.springframework.beans.factory.annotation.Autowired;

import com.tmt.common.searcher.BaseSearcher;
import com.tmt.common.utils.Lists;
import com.tmt.common.utils.StringUtil3;
import com.tmt.wechat.entity.MetaImage;
import com.tmt.wechat.entity.MetaKeyword;
import com.tmt.wechat.entity.MetaRich;
import com.tmt.wechat.entity.MetaText;
import com.tmt.wechat.service.MetaImageServiceFacade;
import com.tmt.wechat.service.MetaKeywordServiceFacade;
import com.tmt.wechat.service.MetaRichServiceFacade;
import com.tmt.wechat.service.MetaTextServiceFacade;

/**
 * 关键词查找
 * 
 * @author root
 */
public class MetaKeywordSearcher extends BaseSearcher<MetaKeyword> implements MetaKeywordServiceFacade {

	@Autowired
	private MetaRichServiceFacade metaRichService;
	@Autowired
	private MetaTextServiceFacade metaTextService;
	@Autowired
	private MetaImageServiceFacade metaImageService;

	@Override
	protected String getModule() {
		return "meta_keyword";
	}

	@Override
	protected Document createDocument(MetaKeyword t) {
		Document document = new Document();
		document.add(new Field(ID, t.getId().toString(), StringField.TYPE_STORED));
		document.add(new Field("appId", t.getAppId(), StringField.TYPE_STORED));
		document.add(new Field("keyword", t.getKeyword(), StringField.TYPE_STORED));
		document.add(new Field("hkeyword", t.getKeyword(), this.vectorField(TextField.TYPE_STORED)));
		document.add(new IntField("type", t.getType(), IntField.TYPE_STORED));
		document.add(new Field("config", t.getConfig(), StringField.TYPE_STORED));
		document.add(new Field("metaId", t.getMetaId(), StringField.TYPE_STORED));

		// 专用排序字段 按照关键词的长度排序
		int size = StringUtil3.length(t.getKeyword());
		document.add(new IntField("size", size, IntField.TYPE_NOT_STORED));
		document.add(new NumericDocValuesField("size", size));
		return document;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected <E> E render(Document doc, Map<String, String> fastValues, Byte scene) {
		MetaKeyword meta = new MetaKeyword();
		meta.setId(Long.parseLong(doc.get(ID)));
		meta.setAppId(doc.get("appId"));
		meta.setType(Integer.parseInt(doc.get("type")));
		meta.setConfig(doc.get("config"));
		meta.setKeyword(doc.get("keyword"));
		meta.setHkeyword(meta.getKeyword());
		if (fastValues != null) {
			String keyword = fastValues.get("hkeyword");
			if (StringUtil3.isNotBlank(keyword)) {
				meta.setHkeyword(keyword);
			}
		}
		return (E) meta;
	}

	@Override
	protected void refresh(List<Long> updates) {
	}

	// 默认的排序方式
	Sort sort = new Sort(new SortField[] { new SortField("size", SortField.Type.INT, false), SortField.FIELD_SCORE });
	String[] hfields = new String[] { "hkeyword" };

	/**
	 * 匹配一个关键词 如果多APP，则要区分关键词是哪个APP的
	 */
	@Override
	public MetaKeyword searchOne(String keyword, String appId) {
		BooleanQuery.Builder builder = new BooleanQuery.Builder();
		builder.add(
				new WildcardQuery(new Term("keyword", new StringBuilder("*").append(keyword).append("*").toString())),
				Occur.MUST);
		builder.add(new TermQuery(new Term("appId", appId)), Occur.MUST);
		List<MetaKeyword> metas = this.searchList(builder, sort, 1, null, BaseSearcher.scene_ONE);
		return metas != null && metas.size() != 0 ? metas.get(0) : null;
	}

	/**
	 * 给定的并不是主键是 metaId
	 */
	@Override
	public void deleteMetas(List<Long> deletes) {
		List<MetaKeyword> metas = Lists.newArrayList();
		for (Long metaId : deletes) {
			BooleanQuery.Builder builder = new BooleanQuery.Builder();
			TermQuery q = new TermQuery(new Term("metaId", metaId.toString()));
			builder.add(q, BooleanClause.Occur.MUST);
			List<MetaKeyword> keys = this.searchList(builder, Sort.INDEXORDER, 255, null, BaseSearcher.scene_ONE);
			metas.addAll(keys);
		}
		this.delete(metas);

		// ID删除
		// this._delete(deletes);
	}

	@Override
	public void refresh_rich(List<Long> updates) {
		this.deleteMetas(updates);
		// 保存为索引
		List<MetaKeyword> metas = Lists.newArrayList();
		for (Long metaId : updates) {
			MetaRich rich = metaRichService.get(metaId);
			metas.addAll(rich.fetchMetaKeywords());
		}
		this.save(metas);
	}

	@Override
	public void refresh_text(List<Long> updates) {
		this.deleteMetas(updates);
		// 保存为索引
		List<MetaKeyword> metas = Lists.newArrayList();
		for (Long metaId : updates) {
			MetaText rich = metaTextService.get(metaId);
			metas.addAll(rich.fetchMetaKeywords());
		}
		this.save(metas);
	}

	@Override
	public void refresh_image(List<Long> updates) {
		this.deleteMetas(updates);
		// 保存为索引
		List<MetaKeyword> metas = Lists.newArrayList();
		for (Long metaId : updates) {
			MetaImage rich = metaImageService.get(metaId);
			metas.addAll(rich.fetchMetaKeywords());
		}
		this.save(metas);
	}

	/**
	 * 列表查询
	 */
	@Override
	public List<MetaKeyword> searchList(String keyword, String appId) {
		BooleanQuery.Builder builder = new BooleanQuery.Builder();

		// 只能通过这种方式
		builder.add(
				new WildcardQuery(new Term("keyword", new StringBuilder("*").append(keyword).append("*").toString())),
				Occur.MUST);
		builder.add(new TermQuery(new Term("appId", appId)), Occur.MUST);
		return this.searchList(builder, sort, 10, hfields, BaseSearcher.scene_ONE);
	}

	@Override
	protected boolean supportFastHighlight() {
		return true;
	}

	@Override
	public void deleteMetas(Long id) {
		BooleanQuery.Builder builder = new BooleanQuery.Builder();
		TermQuery q = new TermQuery(new Term("metaId", id.toString()));
		builder.add(q, BooleanClause.Occur.MUST);
		List<MetaKeyword> keys = this.searchList(builder, Sort.INDEXORDER, 255, null, BaseSearcher.scene_ONE);
		this.delete(keys);
	}

	@Override
	public void refresh_rich(Long id) {
		this.deleteMetas(id);
		MetaRich rich = metaRichService.get(id);
		this.save(rich.fetchMetaKeywords());
	}

	@Override
	public void refresh_text(Long id) {
		this.deleteMetas(id);
		MetaText rich = metaTextService.get(id);
		this.save(rich.fetchMetaKeywords());
	}

	@Override
	public void refresh_image(Long id) {
		this.deleteMetas(id);
		MetaImage rich = metaImageService.get(id);
		this.save(rich.fetchMetaKeywords());
	}
}