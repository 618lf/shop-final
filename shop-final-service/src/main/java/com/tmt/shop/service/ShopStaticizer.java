package com.tmt.shop.service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmt.cms.entity.Mpage;
import com.tmt.cms.entity.MpageField;
import com.tmt.cms.service.MpageServiceFacade;
import com.tmt.core.persistence.Page;
import com.tmt.core.persistence.PageParameters;
import com.tmt.core.persistence.QueryCondition;
import com.tmt.core.staticize.StaticUtils;
import com.tmt.core.utils.FreemarkerUtils;
import com.tmt.core.utils.JsonMapper;
import com.tmt.core.utils.Lists;
import com.tmt.core.utils.Maps;
import com.tmt.core.utils.StringUtils;
import com.tmt.shop.entity.Category;
import com.tmt.shop.entity.Goods;
import com.tmt.shop.entity.Product;
import com.tmt.shop.entity.Promotion;
import com.tmt.shop.entity.Store;
import com.tmt.shop.utils.StoreUtils;
import com.tmt.wechat.entity.App;
import com.tmt.wechat.entity.MetaImage;
import com.tmt.wechat.entity.MetaRich;
import com.tmt.wechat.entity.MetaText;
import com.tmt.wechat.service.MetaImageServiceFacade;
import com.tmt.wechat.service.MetaKeywordServiceFacade;
import com.tmt.wechat.service.MetaRichServiceFacade;
import com.tmt.wechat.service.MetaTextServiceFacade;
import com.tmt.wechat.utils.WechatUtils;

/**
 * 商店静态化服务
 * @author root
 */
@Service
public class ShopStaticizer implements ShopStaticizerFacade{

	@Autowired
	private GoodsServiceFacade goodsService;
	@Autowired
	private CategoryServiceFacade categoryService;
	@Autowired
	private MetaRichServiceFacade metaRichService;
	@Autowired
	private MetaTextServiceFacade metaTextService;
	@Autowired
	private MetaImageServiceFacade metaImageService;
	@Autowired
	private MetaKeywordServiceFacade keywordService;
	@Autowired
	private MpageServiceFacade mpageService;
	@Autowired
	private PromotionServiceFacade promotionService;
	
    /**
     * 首页静态化
     * -- 修改分类需要修改首页
     */
	@Override
    public void index_build() {
    	Store store = StoreUtils.getDefaultStore();
    	Map<String, Object> modelMap = Maps.newHashMap();
    	List<Category> categorys = categoryService.queryUseAbleTopCategorys();
    	modelMap.put("categorys", categorys);
    	StaticUtils.staticSinglePage(store, "index", modelMap);
    }
    
    /**
     * 搜索页面静态化
     * -- 一般不许要修改
     */
	@Override
    public void search_build() {
    	Store store = StoreUtils.getDefaultStore();
    	Map<String, Object> modelMap = Maps.newHashMap();
    	StaticUtils.staticSinglePage(store, "search", modelMap);
    }
	
	/**
	 * 分类页静态化
	 * -- 所有的店铺
	 */
    @Override
	public void categorys_build() {
    	Store store = StoreUtils.getDefaultStore();
    	List<Category> categorys = categoryService.queryUseAbleTopCategorys();
    	Map<String, Object> modelMap = Maps.newHashMap();
    	modelMap.put("categorys", categorys);
    	StaticUtils.staticSinglePage(store, "category", modelMap);
	}

    /**
     * 分类页静态化
     * -- 只有修改单个店铺
     */
	@Override
	public void categorys_build(Long categoryId) {
		Store store = StoreUtils.getDefaultStore();
    	List<Category> categorys = categoryService.queryUseAbleTopCategorys();
    	Map<String, Object> modelMap = Maps.newHashMap();
    	modelMap.put("categorys", categorys);
    	StaticUtils.staticSinglePage(store, "category", modelMap);
	}

	/**
	 * 商品静态化
	 * @param goods
	 */
	@Override
	public Product goods_snapshot_build(Long productId) {
		Store store = StoreUtils.getDefaultStore();
		Product product = this.goodsService.getSnapshotProductId(productId);
		product.setSnapshot(StaticUtils.touchSnapshotPage(store, "product", product));
		return product;
	}
    
	/**
	 * 商品静态化
	 * @param goods
	 */
	@Override
	public void goods_build(Long goodsId) {
		Store store = StoreUtils.getDefaultStore();
		Goods goods = this.goodsService.getShowGoods(goodsId);
		List<Product> products = goods.getProducts();
		for(Product product: products) {
			product.setGoods(goods);
			StaticUtils.staticSinglePage(store, "product", product);
			String snapshot = StaticUtils.touchSnapshotPage(store, "product", product);
			product.setSnapshot(snapshot);
		}
		this.goodsService.clearSnapshot(goods);
	}
	
	/**
	 * 删除静态化内容(不用删除)
	 * @param goods
	 */
	@Override
    public void goods_delete(Long goodsId) {
    	Goods goods = goodsService.getShowGoods(goodsId);
    	List<Product> products = goods.getProducts();
		for(Product product: products) {
			StaticUtils.deleteStaticPage("product", product);
		}
    }
    
    /**
     * 所有商品
     */
	@Override
    public void goodss_build() {
    	QueryCondition qc = new QueryCondition();
    	qc.getCriteria().andEqualTo("DEL_FLAG", Goods.NO);
    	int iCount = this.goodsService.countByCondition(qc);
    	PageParameters param = new PageParameters(1, 500, iCount);
		int iPage = param.getPageCount();
		for(int i=1; i<= iPage; i++) {
			param.setPageIndex(i);
			Page page = this.goodsService.queryForPage(qc, param);
			List<Goods> goodss = page.getData();
			List<Long> fulls = Lists.newArrayList();
			for(Goods goods: goodss) {
				fulls.add(goods.getId());
				this.goods_build(goods.getId());
			}
			fulls.clear();
		}
    }
    
	/**
	 * 删除静态化内容
	 * @param goods
	 */
	@Override
    public void meta_delete(Long metaId) {
    	MetaRich metaRich = metaRichService.get(metaId);
    	StaticUtils.deleteStaticPage("metaRich", metaRich);
    }
    
    /**
     * 所有分类
     * @param categoryId
     */
	@Override
    public void meta_build(Long metaId) {
    	MetaRich metaRich = metaRichService.getWithRelas(metaId);
    	App app = WechatUtils.get(metaRich.getAppId());
    	metaRich.setApp(app);
    	StaticUtils.staticSinglePage(app, "metaRich", metaRich);
    }
    
    /**
     * 所有素材 -- 图文
     */
	@Override
    public void metas_rich_build() {
    	QueryCondition qc = new QueryCondition();
    	int iCount = this.metaRichService.countByCondition(qc);
    	PageParameters param = new PageParameters(1, 500, iCount);
		int iPage = param.getPageCount();
		for(int i=1; i<= iPage; i++) {
			param.setPageIndex(i);
			Page page = this.metaRichService.queryForPage(qc, param);
			List<MetaRich> metas = page.getData();
			List<Long> fulls = Lists.newArrayList();
			for(MetaRich goods: metas) {
				fulls.add(goods.getId());
				this.meta_build(goods.getId());
			}
			// 刷新索引
			keywordService.refresh_rich(fulls);
			fulls.clear();
		}
    }
    
    /**
     * 所有素材 -- 文本
     */
	@Override
    public void metas_text_build() {
    	QueryCondition qc = new QueryCondition();
    	int iCount = this.metaTextService.countByCondition(qc);
    	PageParameters param = new PageParameters(1, 500, iCount);
		int iPage = param.getPageCount();
		for(int i=1; i<= iPage; i++) {
			param.setPageIndex(i);
			Page page = this.metaTextService.queryForPage(qc, param);
			List<MetaText> metas = page.getData();
			List<Long> fulls = Lists.newArrayList();
			for(MetaText goods: metas) {
				fulls.add(goods.getId());
			}
			// 刷新索引
			keywordService.refresh_text(fulls);
			fulls.clear();
		}
    }
    
    // 所有素材 -- 图片
	@Override
    public void metas_image_build() {
    	QueryCondition qc = new QueryCondition();
    	int iCount = this.metaImageService.countByCondition(qc);
    	PageParameters param = new PageParameters(1, 500, iCount);
		int iPage = param.getPageCount();
		for(int i=1; i<= iPage; i++) {
			param.setPageIndex(i);
			Page page = this.metaImageService.queryForPage(qc, param);
			List<MetaImage> metas = page.getData();
			List<Long> fulls = Lists.newArrayList();
			for(MetaImage goods: metas) {
				fulls.add(goods.getId());
			}
			// 刷新索引
			keywordService.refresh_image(fulls);
			fulls.clear();
		}
    }
    
    @SuppressWarnings("unchecked")
	@Override
	public void mpage_build(Long mpageId) {
    	Store store = StoreUtils.getDefaultStore();
		Mpage mpage = this.mpageService.getWithFields(mpageId);
		List<MpageField> fields = mpage.getMfields();
		for(MpageField field: fields) {
			Map<String, Object> context = FreemarkerUtils.escape(JsonMapper.fromJson(field.getConfig(), Map.class));
			field.setMconfig(context);
		}
		StaticUtils.staticSinglePage(store, "mpage", mpage);
	}

	@Override
	public void mpage_delete(Long mpageId) {
		Mpage mpage = this.mpageService.get(mpageId);
    	StaticUtils.deleteStaticPage("mpage", mpage);
	}

	@Override
	public void promotion_build(Long promotionId) {
		Store store = StoreUtils.getDefaultStore();
		Promotion promotion = this.promotionService.getShowPromotion(promotionId);
		// 修改 goods 图片 -- 实现异步加载
		String gif = StringUtils.format("/static%s", "/img/loading.gif");
		String content = promotion.getIntroduction();
		if (StringUtils.isNoneBlank(content)) {
			Document _doc = Jsoup.parse(content);
			Elements imgs =_doc.getElementsByTag("img");
			Iterator<Element>  it = imgs.iterator();
			while(it.hasNext()) {
				Element element = it.next();
				String src = element.attr("src");
				element.attr("src", gif);
				element.attr("data-url", src);
			}
			// 重新设置图片
			content = _doc.html();
			promotion.setIntroduction(content);
		}
		StaticUtils.staticSinglePage(store, "promotion", promotion);
	}

	@Override
	public void promotion_delete(Long promotionId) {
		Promotion promotion = this.promotionService.get(promotionId);
    	StaticUtils.deleteStaticPage("promotion", promotion);
	}

	@Override
	public void mpages_build() {
		QueryCondition qc = new QueryCondition();
    	int iCount = this.mpageService.countByCondition(qc);
    	PageParameters param = new PageParameters(1, 500, iCount);
		int iPage = param.getPageCount();
		for(int i=1; i<= iPage; i++) {
			param.setPageIndex(i);
			Page page = this.mpageService.queryForPage(qc, param);
			List<Mpage> metas = page.getData();
			for(Mpage goods: metas) {
				this.mpage_build(goods.getId());
			}
		}
	}

	@Override
	public void promotions_build() {
		QueryCondition qc = new QueryCondition();
    	int iCount = this.promotionService.countByCondition(qc);
    	PageParameters param = new PageParameters(1, 500, iCount);
		int iPage = param.getPageCount();
		for(int i=1; i<= iPage; i++) {
			param.setPageIndex(i);
			Page page = this.promotionService.queryForPage(qc, param);
			List<Promotion> metas = page.getData();
			for(Promotion goods: metas) {
				this.promotion_build(goods.getId());
			}
		}
	}
}