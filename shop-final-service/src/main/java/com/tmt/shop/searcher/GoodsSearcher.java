package com.tmt.shop.searcher;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FloatDocValuesField;
import org.apache.lucene.document.FloatField;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.SortedDocValuesField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
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

import com.tmt.core.persistence.Page;
import com.tmt.core.persistence.PageParameters;
import com.tmt.core.persistence.QueryCondition;
import com.tmt.core.persistence.QueryCondition.Criteria;
import com.tmt.core.persistence.ScorePage;
import com.tmt.core.searcher.BaseSearcher;
import com.tmt.core.utils.BigDecimalUtil;
import com.tmt.core.utils.Lists;
import com.tmt.core.utils.StringUtils;
import com.tmt.core.utils.time.DateUtils;
import com.tmt.shop.entity.Category;
import com.tmt.shop.entity.Goods;
import com.tmt.shop.entity.GoodsMini;
import com.tmt.shop.entity.GoodsTag;
import com.tmt.shop.entity.Product;
import com.tmt.shop.service.CategoryServiceFacade;
import com.tmt.shop.service.GoodsSearcherFacade;
import com.tmt.shop.service.GoodsServiceFacade;
import com.tmt.shop.service.ProductServiceFacade;

/**
 * 商品查找
 * @author root
 */
@Service
public class GoodsSearcher extends BaseSearcher<Product> implements GoodsSearcherFacade{

	@Autowired
	private GoodsServiceFacade goodsService;
	@Autowired
	private ProductServiceFacade productService;
	@Autowired
	private CategoryServiceFacade categoryService;
	
	@Override
	protected String getModule() {
		return "goods";
	}

	@Override
	protected Document createDocument(Product product) {
		Document document = new Document(); Goods goods = product.getGoods();
		StringBuilder name = new StringBuilder(product.getName());
		if (StringUtils.isNotBlank(product.getTip())) {name.append(product.getTip());}
		document.add(new Field(ID, product.getId().toString(), StringField.TYPE_STORED));
		document.add(new Field("name", name.toString(), StringField.TYPE_STORED));
		document.add(new Field("fullName", goods.getFullName(), StringField.TYPE_STORED));
		document.add(new Field("sn", product.getSn(), StringField.TYPE_STORED));
		document.add(new Field("keyword", StringUtils.defaultIfBlank(goods.getKeyword(), ""), TextField.TYPE_NOT_STORED));
		document.add(new Field("remarks", StringUtils.defaultIfBlank(goods.getRemarks(), ""), TextField.TYPE_NOT_STORED));
		document.add(new FloatField("price", Float.parseFloat(BigDecimalUtil.toString(product.getPrice(), 2)), FloatField.TYPE_STORED));//销售价
		document.add(new FloatField("marketPrice", null != goods.getMarketPrice() ? Float.parseFloat(BigDecimalUtil.toString(product.getMarketPrice(), 2)) : 0, FloatField.TYPE_STORED));//市场价
		document.add(new Field("unit", product.getUnit(), StringField.TYPE_STORED));//单位
		document.add(new IntField("sweight", goods.getSweight(), IntField.TYPE_NOT_STORED));//权重
		document.add(new Field("weight", product.getWeight(), StringField.TYPE_STORED));//重量
		document.add(new Field("image", StringUtils.defaultIfBlank(product.getImage(), ""), StringField.TYPE_STORED));//图片
		
		// 分类
		Category category = goods.getCategory();
		if (null != category){
			String categoryIds = new StringBuilder(DIVIDE).append(category.getParentIds()).append(category.getId()).append(DIVIDE).toString();
			document.add(new Field("categoryIds", categoryIds, StringField.TYPE_NOT_STORED));//分类
		}
		
		// 标签
		List<GoodsTag> tags = goods.getTags();
		if (tags != null && !tags.isEmpty()) {
			StringBuilder _tags = new StringBuilder();
			for(GoodsTag tag: tags) {
				_tags.append(tag.getTagName()).append(" ");
			}
			document.add(new Field("tags", StringUtils.trim(_tags.toString()), StringField.TYPE_STORED));
		}
		
		// 总体设置的是否上架
		document.add(new Field("isMarketable", goods.getIsMarketable().toString(), StringField.TYPE_NOT_STORED));//是否上架
		
		// 是否有售
		document.add(new Field("isSalestate", product.getIsSalestate().toString(), StringField.TYPE_NOT_STORED));//是否上架
		
		//专用排序字段
		Date docDate = goods.getCreateDate();
		document.add(new SortedDocValuesField(CREATE_DATE, new BytesRef(docDate!= null?DateUtils.getFormatDate(docDate, "yyyyMMdd"):"")));
		document.add(new NumericDocValuesField("sweight", goods.getSweight()));
		document.add(new FloatDocValuesField("price",Float.parseFloat(BigDecimalUtil.toString(product.getPrice(), 2))));
		return document;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected <E> E render(Document doc, Map<String, String> fastValues, Byte scene) {
		// 简单模式
		GoodsMini goods = new GoodsMini();
		goods.setId(Long.parseLong(doc.get(ID)));
		goods.setName(doc.get("name"));
		goods.setFullName(doc.get("fullName"));
		goods.setPrice(BigDecimalUtil.valueOf(doc.get("price")));
		goods.setMarketPrice(BigDecimalUtil.valueOf(doc.get("marketPrice")));
		goods.setUnit(doc.get("unit"));
		goods.setWeight(doc.get("weight"));
		goods.setImage(doc.get("image"));
		goods.setTags(doc.get("tags"));
		return (E) goods;
	}
	
	/**
	 * 关键字查询
	 * @param query
	 * @return
	 */
	public Page searchText(String query, ScorePage page) {
		BooleanQuery.Builder builder = new BooleanQuery.Builder();
		builder.add(this.searcherIsMarketable(), Occur.MUST);
		builder.add(this.searcherIsSalestate(), Occur.MUST);
		builder.add(new WildcardQuery(new Term("name", new StringBuilder("*").append(query).append("*").toString())), Occur.MUST);
		Sort sort = new Sort(new SortField[]{ new SortField("sweight", SortField.Type.INT, true), SortField.FIELD_DOC});
		return this.searchPage(builder, sort, page, null, BaseSearcher.scene_ONE);
	}
	
	/**
	 * 分类查询
	 * @param query
	 * @return
	 */
	public Page searchByCategory(String categoryId, ScorePage page) {
		// 条件
		BooleanQuery.Builder builder = new BooleanQuery.Builder();
		builder.add(this.searcherIsMarketable(), Occur.MUST);
		builder.add(this.searcherIsSalestate(), Occur.MUST);
		if (StringUtils.isNotBlank(categoryId)) { 
			builder.add(new WildcardQuery(new Term("categoryIds", this.idsQuery(categoryId))), Occur.MUST);
		}
		// 排序
		Sort sort = null;
		if (StringUtils.isBlank(page.getParam().getSortName())){
			sort = new Sort(new SortField[]{ new SortField("sweight", SortField.Type.INT, true), SortField.FIELD_DOC});
		}else{
			sort = new Sort(new SortField[]{new SortField("price", SortField.Type.FLOAT, true), new SortField("sweight", SortField.Type.INT, true), SortField.FIELD_DOC});
		}
		return this.searchPage(builder, sort, page, null, BaseSearcher.scene_ONE);
	}

	/**
	 * 刷新索引(批量更新，效率更高)
	 */
	@Override
	public void refresh(List<Long> updates) {
		QueryCondition qc = new QueryCondition();
		Criteria c = qc.getCriteria();
		c.andIn("ID", updates);
		List<Product> all_products = Lists.newArrayList();
		List<Goods> goodss = this.goodsService.queryByCondition(qc);
		for(Goods goods: goodss) {
			Category category = this.categoryService.get(goods.getCategoryId());
			goods.setCategory(category);
			
			// 设置标签
			goods.setTags(this.goodsService.queryRealTags(goods.getId()));
			
			// 全部的货品
			List<Product> products = this.productService.queryUseAbleProductsByGoodsId(goods.getId());
			for(Product product: products) {
				product.setGoods(goods);
			}
			all_products.addAll(products);
		}
		this.save(all_products);
		goodss.clear(); all_products.clear();
	}
	
	/**
	 * 显示首页数据
	 */
	@Override
	public Page index_goods(ScorePage page) {
		BooleanQuery.Builder query = new BooleanQuery.Builder();
		query.add(this.searcherIsMarketable(), Occur.MUST);
		query.add(this.searcherIsSalestate(), Occur.MUST);
		Sort sort = new Sort(new SortField[]{new SortField("sweight", SortField.Type.INT, true), SortField.FIELD_DOC});
		return this.searchPage(query, sort, page, null, BaseSearcher.scene_ONE);
	}
	
	/**
	 * 只显示上架的商品
	 * @param query
	 * @return
	 */
	private Query searcherIsMarketable() {
		return new TermQuery(new Term("isMarketable", String.valueOf(Goods.YES)));
	}
	
	/**
	 * 只显示有售卖的商品
	 * @param query
	 * @return
	 */
	private Query searcherIsSalestate() {
		return new TermQuery(new Term("isSalestate", String.valueOf(Goods.YES)));
	}
	
    /**
     * 所有商品(只需要索引未删除的数据)
     */
	@Override
    public void goodss_build() {
		// 删除所有的
		this.destory();
		// 更新数据
    	QueryCondition qc = new QueryCondition();
    	qc.getCriteria().andEqualTo("DEL_FLAG", Goods.NO);
    	int iCount = this.goodsService.countByCondition(qc);
    	PageParameters param = new PageParameters(1, 500, iCount);
		int iPage = param.getPageCount();
		for(int i=1; i<= iPage; i++) {
			param.setPageIndex(i);
			Page page = this.goodsService.queryForPage(qc, param);
			List<Product> all_products = Lists.newArrayList();
			List<Goods> goodss = page.getData();
			for(Goods goods: goodss) {
				Category category = this.categoryService.get(goods.getCategoryId());
				goods.setCategory(category);
				
				// 设置标签
				goods.setTags(this.goodsService.queryRealTags(goods.getId()));
				
				// 全部的货品
				List<Product> products = this.productService.queryUseAbleProductsByGoodsId(goods.getId());
				for(Product product: products) {
					product.setGoods(goods);
				}
				all_products.addAll(products);
			}
			this.save(all_products);
			goodss.clear();
			all_products.clear();
		}
    }
}