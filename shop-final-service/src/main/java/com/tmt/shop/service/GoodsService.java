package com.tmt.shop.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.service.BaseService;
import com.tmt.core.utils.Lists;
import com.tmt.core.utils.Maps;
import com.tmt.core.utils.StringUtils;
import com.tmt.shop.dao.GoodsAttributeDao;
import com.tmt.shop.dao.GoodsDao;
import com.tmt.shop.dao.GoodsDeliveryDao;
import com.tmt.shop.dao.GoodsImageDao;
import com.tmt.shop.dao.GoodsLimitDao;
import com.tmt.shop.dao.GoodsParameterGroupDao;
import com.tmt.shop.dao.GoodsSpecificationDao;
import com.tmt.shop.dao.GoodsTagDao;
import com.tmt.shop.entity.Attribute;
import com.tmt.shop.entity.Category;
import com.tmt.shop.entity.Goods;
import com.tmt.shop.entity.GoodsAttribute;
import com.tmt.shop.entity.GoodsDelivery;
import com.tmt.shop.entity.GoodsEvaluate;
import com.tmt.shop.entity.GoodsImage;
import com.tmt.shop.entity.GoodsLimit;
import com.tmt.shop.entity.GoodsParameterGroup;
import com.tmt.shop.entity.GoodsSpecification;
import com.tmt.shop.entity.GoodsTag;
import com.tmt.shop.entity.Parameter;
import com.tmt.shop.entity.Product;
import com.tmt.shop.update.ShopModule;
import com.tmt.shop.utils.GoodsUtils;
import com.tmt.shop.utils.SnUtils;
import com.tmt.system.entity.UpdateData;
import com.tmt.update.UpdateServiceFacade;

/**
 * 商品管理 管理
 * @author 超级管理员
 * @date 2016-01-21
 */
@Service("shopGoodsService")
public class GoodsService extends BaseService<Goods,Long> implements GoodsServiceFacade {
	
	@Autowired
	private GoodsDao goodsDao;
	@Autowired
	private GoodsImageDao imageDao;
	@Autowired
	private GoodsParameterGroupDao parameterGroupDao;
	@Autowired
	private GoodsSpecificationDao  specificationDao;
	@Autowired
	private GoodsTagDao tagDao;
	@Autowired
	private GoodsAttributeDao attributeDao;
	@Autowired
	private GoodsLimitDao limitDao;
	@Autowired
	private GoodsDeliveryDao deliveryDao;
	@Autowired
	private AttributeService attributeService;
	@Autowired
	private ParameterService parameterService;
	@Autowired
	private ProductService productService;
	@Autowired
	private GoodsEvaluateService evaluateService;
	
	// 数据更新
	@Autowired
	private UpdateServiceFacade updateService;
	
	@Override
	protected BaseDaoImpl<Goods, Long> getBaseDao() {
		return goodsDao;
	}
	
	/**
	 * 初始化向导
	 */
	@Override
	public Goods initAdd(Category category) {
		Goods goods = new Goods();
	    goods.setId(IdGen.INVALID_ID);
	    goods.setSweight(0);
	    goods.setCategoryId(category.getId());
	    goods.setCategoryName(category.getName());
	    goods.setTags(this.queryTags(goods.getId()));
	    goods.setAttributes(this.queryAttrs(goods));
	    goods.setParameterGroups(this.getDefaultParameters(goods.getCategoryId()));
	    goods.setSpecifications(this.specificationDao.queryByCategoryId(goods.getCategoryId()));
	    return goods;
	}
	
	/**
	 * 初始化修改
	 * @param id
	 * @return
	 */
	@Override
	public Goods initModify(Long id) {
		Goods goods = this.get(id);
		GoodsLimit limit = this.limitDao.get(id);
	    goods.setLimit(limit);
	    GoodsDelivery delivery = deliveryDao.get(id);
	    goods.setDelivery(delivery);
	    goods.setTags(this.queryTags(goods.getId()));
	    goods.setImages(this.queryImages(goods));
	    goods.setAttributes(this.queryAttrs(goods));
	    goods.setParameterGroups(this.queryParameters(goods));
	    goods.setHasSpecification(this.specificationDao.hasSpecification(goods.getId()));
	    return goods;
	}
	
	/**
	 * 查询商品的介绍信息
	 * @param id
	 * @return
	 */
	private List<GoodsTag> queryTags(Long id) {
		return this.tagDao.queryForList("queryByGoodsId", id);
	}
	
	/**
	 * 查询商品图片
	 * @param id
	 * @return
	 */
	private List<GoodsImage> queryImages(Goods goods){
		return this.imageDao.queryForList("queryImagesById", goods.getId());
	}
	
	/**
	 * 查询商品参数
	 * @param id
	 * @return
	 */
	private List<GoodsParameterGroup> queryParameters(Goods goods) {
		return this.parameterGroupDao.queryParameterGroupsByGoodsId(goods.getId());
	}
	
	/**
	 * 获得默认的参数
	 * @param categoryId
	 * @return
	 */
	private List<GoodsParameterGroup> getDefaultParameters(Long categoryId) {
		List<Parameter> parameters = parameterService.queryByCategoryId(categoryId);
		List<GoodsParameterGroup> groups = Lists.newArrayList();
		for(Parameter parameter: parameters) {
			parameter = this.parameterService.getWithOptions(parameter.getId());
			GoodsParameterGroup group = new GoodsParameterGroup();
			group.setGroupName(parameter.getName());
			group.fillParameters(parameter.getOptions());
			groups.add(group);
		}
		return groups;
	}
	
	/**
	 * 查询商品参数
	 * @param id
	 * @return
	 */
	private List<GoodsAttribute> queryAttrs(Goods goods) {
		Map<String, Object> param = Maps.newHashMap();
	    param.put("GOODS_ID", goods.getId());
	    param.put("CATEGORY_ID", goods.getCategoryId());
	    List<GoodsAttribute> attributes = this.attributeDao.queryForList("queryByCategoryId", param);
	    for(GoodsAttribute attribute: attributes) {
		    Attribute _attribute = attributeService.getWithOptions(attribute.getAttributeId());
		    attribute.setAttribute(_attribute);
	    }
	    return attributes;
	}
	
	/**
	 * 保存
	 */
	@Override
	@Transactional
	public void saveEvaluate(GoodsEvaluate evaluate) {
		if(!IdGen.isInvalidId(evaluate.getGoodsId())) {
		   
		   Goods goods = new Goods();
		   goods.setId(evaluate.getGoodsId());
		   
		   // 保存评价信息
		   this.evaluateService.save(evaluate);
		   
		   // 清除快照信息
		   List<Product> products = this.productService.queryProductsByGoodsId(goods.getId());
		   this.productService.clearSnapshot(products);
		   
		   // 记录数据更新
			_update(goods, (byte)0);
		}
	}
	
	/**
	 * 保存规格 
	 * @param goods
	 */
	@Override
	@Transactional
	public void saveSpecification(Goods goods) {
		Goods _goods = this.get(goods.getId());
		//保存产品
		_goods.setProducts(goods.getProducts());
		this.productService.save(_goods);
		// 记录数据更新
		_update(goods, (byte)0);
	}
	
	/**
	 * 删除
	 */
	@Override
	@Transactional
	public void delete(List<Goods> goodss) {
		this.batchDelete(goodss);
		List<Product> products = Lists.newArrayList();
		for(Goods goods: goodss){
			products.addAll(this.productService.queryProductIdsByGoodsId(goods.getId()));
		}
		// 删除库存
		this.productService.delete(products);
		
		// 记录数据更新
		for(Goods goods: goodss) {
			_update(goods, (byte)1);
		}
	}
	
	/**
	 * 保存最新的商品快照
	 * 1. 每次修改商品信息后把快照信息删除
	 * 2. 下单时候获取商品快照信息，如果为空
	 *    则获取快照，并修改商品快照信息。
	 */
	@Override
	@Transactional
	public void clearSnapshot(Goods goods) {
		List<Product> products = goods.getProducts();
		this.productService.clearSnapshot(products);
	}
	
	/**
	 * 查询商品的介绍信息
	 * @param id
	 * @return
	 */
	@Override
	public List<GoodsTag> queryRealTags(Long id) {
		return this.tagDao.queryForList("queryRealsByGoodsId", id);
	}
	
	/**
	 * 查询商品的介绍信息
	 * @param id
	 * @return
	 */
	@Override
	public List<GoodsSpecification> querySpecifications(Long id) {
		return this.specificationDao.queryByGoodsId(id);
	}
	
	
	/**
	 * 查询商品评价（现阶段商品ID就是评价ID）
	 * @param id
	 * @return
	 */
	@Override
	public Goods getWithEvaluate(Long id){
		Goods goods = this.get(id);
		if(goods != null) {
		   GoodsEvaluate evaluate = evaluateService.get(id);
		   goods.setEvaluate(evaluate);
		}
		return goods;
	}
	
	/**
	 * 取消限购
	 */
	@Transactional
	@Override
	public void cancelLimit(Long id) {
		GoodsLimit limit = new GoodsLimit();
		limit.setGoodsId(id);
		this.limitDao.delete(limit);
	}
	
	/**
	 * 开启限购
	 */
	@Transactional
	@Override
	public void openLimit(GoodsLimit limit) {
		this.limitDao.insert(limit);
	}
	
	/**
	 * 保存
	 */
	@Override
	@Transactional
	public void modify(Goods goods) {
		if (StringUtils.isBlank(goods.getSn())) {
		    goods.setSn(SnUtils.createProductSn());
		}
		if (goods.getIsMarketable() == null || !(Goods.YES == goods.getIsMarketable())) {
			goods.setIsMarketable(Goods.NO);
		}
		if (goods.getIsList() == null || !(Goods.YES == goods.getIsList())) {
			goods.setIsList(Goods.NO);
		}
		if (goods.getIsDelivery() == null || !(Goods.YES == goods.getIsDelivery())) {
			goods.setIsDelivery(Goods.NO);
		}
		if (goods.getIsTop() == null || !(Goods.YES == goods.getIsTop())) {
			goods.setIsTop(Goods.NO);
		}
		this.update("updateBase", goods);
		this.saveTags(goods);
		this.saveDelivery(goods);
		this.saveTags(goods);
		this.saveIntroduction(goods);
		this.saveImage(goods);
		this.saveAttr(goods);
		this.saveParameter(goods);
		
		// 记录数据更新
		_update(goods, (byte)0);
	}
	
	/**
	 * 新增
	 */
	@Override
	@Transactional
	public void add(Goods goods) {
		if (StringUtils.isBlank(goods.getSn())) {
		    goods.setSn(SnUtils.createProductSn());
		}
		if (goods.getIsMarketable() == null || !(Goods.YES == goods.getIsMarketable())) {
			goods.setIsMarketable(Goods.NO);
		}
		if (goods.getIsList() == null || !(Goods.YES == goods.getIsList())) {
			goods.setIsList(Goods.NO);
		}
		if (goods.getIsDelivery() == null || !(Goods.YES == goods.getIsDelivery())) {
			goods.setIsDelivery(Goods.NO);
		}
		if (goods.getIsTop() == null || !(Goods.YES == goods.getIsTop())) {
			goods.setIsTop(Goods.NO);
		}
		this.insert(goods);
		this.saveTags(goods);
		this.saveIntroduction(goods);
		this.saveImage(goods);
		this.saveAttr(goods);
		this.saveParameter(goods);
		this.specificationDao.save(goods);
		this.productService.add(goods);
	}
	
	/**
	 * 保存配送方案
	 * @param goods
	 */
	private void saveDelivery(Goods goods) {
		GoodsDelivery delivery = goods.getDelivery();
		if (delivery == null || delivery.getDeliveryId() == null) {
			delivery = new GoodsDelivery(); 
			delivery.setGoodsId(goods.getId());
			this.deliveryDao.delete(delivery);
		} else {
			delivery.setGoodsId(goods.getId());
			this.deliveryDao.insert(delivery);
		}
	}
	
	/**
	 * 保存促销
	 */
	private void saveTags(Goods goods) {
		List<GoodsTag> olds = this.queryRealTags(goods.getId());
		List<GoodsTag> tags = goods.getTags();
		if(!CollectionUtils.isEmpty(olds)){
			this.tagDao.batchDelete(olds);
		}
		if(!CollectionUtils.isEmpty(tags)){
			for(GoodsTag tag: tags) {
				tag.setGoodsId(goods.getId());
			}
			this.tagDao.batchInsert(tags);
		}
	}
	
	private void _update(Goods goods, byte opt) {
		UpdateData updateData = new UpdateData();
		updateData.setId(goods.getId());
		updateData.setModule(ShopModule.GOODS);
		updateData.setOpt(opt);
		updateService.save(updateData);
	}
	
	/**
	 * 保存详情
	 */
	private void saveIntroduction(Goods goods) {
		this.update("updateIntroduction", goods);
	}
	
	/**
	 * 保存图片
	 */
	private void saveImage(Goods goods) {
		List<GoodsImage> olds = this.imageDao.queryForList("queryImagesById", goods.getId());
		List<GoodsImage> images = goods.getImages();
		if (images != null && images.size() > 0) {
			int i = 1;
		    for(GoodsImage image: images) {
			    image.setGoodsId(goods.getId());
			    image.setSort(i++);
		    }
		    this.imageDao.batchInsert(images);
		}
	    this.imageDao.batchDelete(olds);
	}
	
	/**
	 * 保存属性
	 */
	private void saveAttr(Goods goods) {
	    List<GoodsAttribute> attributes = goods.getAttributes();
	    for(GoodsAttribute attribute: attributes) {
		    attribute.setGoodsId(goods.getId());
	    }
	    List<GoodsAttribute> olds = this.attributeDao.queryForList("queryRelasByGoodsId", goods.getId());
	    this.attributeDao.batchDelete(olds);
	    this.attributeDao.batchInsert(attributes);
	}
	
	/**
	 * 保存参数 
	 * @param goods
	 */
	private void saveParameter(Goods goods) {
		this.parameterGroupDao.delete(goods.getId());
		List<GoodsParameterGroup> parameterGroups = goods.getParameterGroups();
		int i = 1;
		for(GoodsParameterGroup group: parameterGroups) {
			group.setGoodsId(goods.getId());
			group.setSort(i++);
		}
		this.parameterGroupDao.save(parameterGroups);
	}
	
	//前端需要的方法
	/**
	 * 得到商品发货配置
	 * --- 如果没有单独指定，则返回默认的配置
	 * @param id
	 * @return
	 */
	@Override
	public GoodsDelivery getGoodsDelivery(Long id) {
		GoodsDelivery delivery = this.deliveryDao.get(id);
		if (delivery == null) {
			return this.deliveryDao.queryForObject("getDefault");
		}
		return delivery;
	}
	
	/**
	 * 得到前端可显示的商品 - 快照
	 * @param id
	 * @return
	 */
	@Override
	public Product getSnapshotProductId(Long productId) {
		Product product = this.productService.get(productId);
		Goods goods = this.get(product.getGoodsId());
		goods.setImages(this.queryImages(goods));
		product.setGoods(goods);
		return product;
	}
	
	/**
	 * 得到前端可显示的商品
	 * @param id
	 * @return
	 */
	@Override
	public Goods getShowGoods(Long id) {
		Goods goods = this.get(id);
		if (goods != null) {
			goods.setImages(this.queryImages(goods));
			List<GoodsSpecification> specifications = this.specificationDao.queryByGoodsId(goods.getId());
			goods.setSpecifications(specifications);
			List<Product> products = this.productService.queryUseAbleProductsByGoodsId(id);
			goods.setProducts(products);
		}
		if (goods != null) {
			GoodsEvaluate evaluate = this.evaluateService.get(id);
			goods.setEvaluate(evaluate);
		}
		if (goods != null) {
			GoodsDelivery delivery = this.getGoodsDelivery(id);
			goods.setDelivery(delivery);
			GoodsUtils.imagesAsync(goods);
		}
		return goods;
	}
}