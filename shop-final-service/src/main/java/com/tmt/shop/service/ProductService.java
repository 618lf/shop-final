package com.tmt.shop.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.core.persistence.QueryCondition;
import com.tmt.core.persistence.QueryCondition.Criteria;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.service.BaseService;
import com.tmt.core.utils.Lists;
import com.tmt.core.utils.Maps;
import com.tmt.core.utils.StringUtils;
import com.tmt.core.utils.time.DateUtils;
import com.tmt.shop.dao.GoodsLimitLogDao;
import com.tmt.shop.dao.GoodsSpecificationDao;
import com.tmt.shop.dao.ProductDao;
import com.tmt.shop.entity.Goods;
import com.tmt.shop.entity.GoodsLimitLog;
import com.tmt.shop.entity.Product;
import com.tmt.shop.update.ShopModule;
import com.tmt.system.entity.UpdateData;
import com.tmt.system.entity.User;
import com.tmt.update.UpdateServiceFacade;

/**
 * 商品库存 管理
 * @author 超级管理员
 * @date 2016-01-21
 */
@Service("shopProductService")
public class ProductService extends BaseService<Product,Long> implements ProductServiceFacade{
	
	@Autowired
	private ProductDao productDao;
	@Autowired
	private GoodsLimitLogDao limitDao;
	@Autowired
	private GoodsSpecificationDao specificationDao;
	
	// 数据更新
	@Autowired
	private UpdateServiceFacade updateDataService;
	
	@Override
	protected BaseDaoImpl<Product, Long> getBaseDao() {
		return productDao;
	}
	
	/**
	 * 修改基本信息时
	 */
	@Transactional
	public void add(Goods goods) {
		Boolean hasSpecification = this.specificationDao.hasSpecification(goods.getId());
		if (!hasSpecification) {
			Product product = Product.newProduct(goods);
		    product.setId(goods.getId());
		    this.insert(product);
		}
	}
	
	/**
	 * 单个保存(只保存基本信息)
	 */
	@Override
	public void save(Product product) {
		if (product.getIsSalestate() == null) {
			product.setIsSalestate(Product.NO);
		}
		this.update("updateSimple", product);
		this._update(product, (byte)0);
	}
	
	/**
	 * 批量保存
	 */
	@Transactional
	public void save(Goods goods) {
		List<Product> products = goods.getProducts();
		List<Product> olds = this.queryProductsByGoodsId(goods.getId());
		List<Product> inserts = Lists.newArrayList();
		List<Product> updates = Lists.newArrayList();
		int _sn = 0;
		try{
			_sn = olds != null&&olds.size()!=0?Integer.parseInt((StringUtils.remove((olds.get(olds.size() - 1)).getSn(), goods.getSn() + "_"))):0;
		}catch(Exception e){ _sn = 0;}
		//新增的
		for(Product product: products) {
			if(!IdGen.isInvalidId(product.getId()) && Product.YES == product.getIsSalestate()) {
			   product.initSpecifications();
			   Product _product = this.get(product.getId());
			   _product.setTip(product.getTip());
			   _product.setCost(product.getCost());
			   _product.setMarketPrice(product.getMarketPrice());
			   _product.setPrice(product.getPrice());
			   _product.setRewardPoint(product.getRewardPoint());
			   _product.setSpecificationsId(product.getSpecificationsId());
			   _product.setSpecificationsValue(product.getSpecificationsValue());
			   _product.setIsSalestate(Product.YES);
			   _product.setIsDefault(product.getIsDefault());
			   updates.add(_product);
			} else if(Product.YES == product.getIsSalestate()){
			   inserts.add(product);
			   product.setInStore(product.getStore());
			   product.setImage(goods.getImage());
			   product.setUnit(goods.getUnit());
			   product.setWeight(goods.getWeight());
			   product.setSn(StringUtils.format("%s_%s", goods.getSn(), ++_sn));
			   product.setGoodsId(goods.getId());
			   product.setName(goods.getName());
			   product.initSpecifications();
			}
		}
		//修改为禁用
		for(Product old: olds) {
			Boolean found = Boolean.FALSE;
			for(Product product: products) {
				if(old.getId().equals(product.getId()) ) {
				   if(!(Product.YES == product.getIsSalestate())) {
					  old.setIsSalestate(Product.NO);
					  old.setIsDefault(Product.NO);
					  updates.add(old);
				   }
				   found = Boolean.TRUE; 
				   break;
				}
			}
			
			if(!found) {
			   old.setIsSalestate(Product.NO);
			   old.setIsDefault(Product.NO);
			   updates.add(old);
			}
		}
		this.batchInsert(inserts);
		this.batchUpdate(updates);
	}
	
	/**
	 * 删除
	 * @param products
	 */
	@Transactional
	public void delete(List<Product> products){
		this.batchDelete(products);
	}
	
	/**
	 * 根据商品Id查询库存的集合
	 * @param goodsId
	 * @return
	 */
	public List<Product> queryProductIdsByGoodsId(Long goodsId) {
		return this.queryForList("queryProductIdsByGoodsId", goodsId);
	}
	
	/**
	 * 根据商品Id查询库存的集合
	 * @param goodsId
	 * @return
	 */
	public List<Product> queryProductsByGoodsId(Long goodsId) {
		return this.queryForList("queryProductsByGoodsId", goodsId);
	}
	
	/**
	 * 入库
	 * @param product
	 */
	@Transactional
	public void inStroe(Product product) {
		this.update("updateInStroe", product);
		this._update(product, (byte)0);
	}
	
	/**
	 * 出库
	 * @param product
	 */
	@Transactional
	public void outStroe(Product product) {
		this.update("updateOutStroe", product);
		this._update(product, (byte)0);
	}
	
	/**
	 * 出库(反)
	 * @param product
	 */
	@Transactional
	public void unOutStroe(Product product) {
		this.update("updateUnOutStroe", product);
		this._update(product, (byte)0);
	}
	
	/**
	 * 冻结库存
	 * @param product
	 */
	@Transactional
	public void freeStroe(Product product) {
		this.update("updateFreeStroe", product);
		this._update(product, (byte)0);
	}
	
	// 数据修改
	private void _update(Product product, byte opt) {
		UpdateData updateData = new UpdateData();
		updateData.setId(product.getGoodsId());
		updateData.setModule(ShopModule.GOODS);
		updateData.setOpt(opt);
		updateDataService.save(updateData);
	}
	
	/**
	 * 根据编号获取唯一的商品
	 * @param sn
	 * @return
	 */
	public Product getBySn(String sn) {
		return this.queryForObject("getBySn", sn);
	}
	
	/**
	 * 修改快照地址
	 * @param product
	 */
	@Transactional
	public void clearSnapshot(List<Product> products) {
		this.batchUpdate("clearSnapshot", products);
	}
	
	/**
	 * 修改快照地址
	 * @param product
	 */
	@Transactional
	public void updateSnapshot(List<Product> products) {
		this.batchUpdate("updateSnapshot", products);
	}
	
	/**
	 * 添加限购记录 
	 * 限购商品才设置
	 */
	@Transactional
	public void addGoodsLimit(Long goodsId, Long userId, int quantity) {
		Integer limit = this.countByCondition("getGoodsLimitNum", goodsId);
		if (limit != null) {
			GoodsLimitLog log = new GoodsLimitLog();
			log.setGoodsId(goodsId);
			log.setUserId(userId);
			log.setBuyDate(DateUtils.getTodayDate());
			log.setBuyNum(quantity);
			limitDao.insert(log);
		}
	}
	
	/**
	 * 删除限购记录 
	 * 限购商品才设置
	 */
	@Transactional
	public void reduceGoodsLimit(Long goodsId, Long userId, int quantity) {
		Integer limit = this.countByCondition("getGoodsLimitNum", goodsId);
		if (limit != null) {
			GoodsLimitLog log = new GoodsLimitLog();
			log.setGoodsId(goodsId);
			log.setUserId(userId);
			log.setBuyDate(DateUtils.getTodayDate());
			log.setBuyNum(quantity);
			limitDao.update(log);
		}
	}
	
	/**
	 * 清除商品限购
	 * 清除今天之前的数据
	 */
	@Transactional
	@Override
	public void clearGoodsLimit() {
		GoodsLimitLog log = new GoodsLimitLog();
		log.setBuyDate(DateUtils.getDateByOffset(DateUtils.getTodayDate(), -1));
		this.limitDao.delete(log);
	}

	//前端需要用的方法
	/**
	 * 根据商品Id查询库存的集合
	 * @param goodsId
	 * @return
	 */
	public List<Product> queryUseAbleProductsByGoodsId(Long goodsId) {
		return this.queryForList("queryUseAbleProductsByGoodsId", goodsId);
	}
	
	/**
	 * 校验商品是否限购
	 * @param product
	 * @return
	 */
	@Override
	public Boolean checkGoodsLimit(Long goodsId, User user, int quantity) {
		
		// 是否限购
		Integer limit = this.countByCondition("getGoodsLimitNum", goodsId);
		if (limit == null) {
			return Boolean.TRUE;
		}
		
		// 已超过购买数量
		if (limit < quantity) {
			return Boolean.FALSE;
		}
		
		// 校验历史购买
		Map<String, Object> param = Maps.newHashMap();
		param.put("goodsId", goodsId);
		param.put("userId", user.getId());
		param.put("quantity", quantity);
		param.put("day", DateUtils.getTodayDate());
		// 如果count == 0则没限购或没超过限购，否则已超过限购
		int count = this.countByCondition("checkGoodsLimit", param);
		if (count == 0) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/**
	 * 商品统计
	 */
	@Override
	public Map<String, Integer> statProduct() {
		Map<String, Integer> stats = Maps.newHashMap();
		QueryCondition qc = new QueryCondition();
		Criteria c = qc.getCriteria();
		c.andEqualTo("IS_SALESTATE", 1);
		
		// 上架
		int count = this.countByCondition(qc);
		stats.put("CS", count);
		
		// 未上架
		qc.clear(); c = qc.getCriteria();
		c.andEqualTo("IS_SALESTATE", 0);
		count = this.countByCondition(qc);
		stats.put("XJ", count);
		
		// 库存 < 100
		qc.clear(); c = qc.getCriteria();
		c.andLessThan("STORE", 100);
		count = this.countByCondition(qc);
		stats.put("KC100", count);
		
		// 库存 < 10
		qc.clear(); c = qc.getCriteria();
		c.andLessThan("STORE", 10);
		count = this.countByCondition(qc);
		stats.put("KC10", count);
		
		return stats;
	}
	
	/**
	 * 锁定此商品
	 * @param order
	 */
	@Transactional
	public Product lockStoreProduct(Long id) {
		return this.queryForObject("lockStoreProduct", id);
	}

	/**
	 * 获取库存
	 */
	@Override
	public Product getStore(Long id) {
		return this.queryForObject("getStore", id);
	}
}
