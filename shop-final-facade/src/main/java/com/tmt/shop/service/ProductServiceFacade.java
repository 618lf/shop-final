package com.tmt.shop.service;

import java.util.List;
import java.util.Map;

import com.tmt.core.service.BaseServiceFacade;
import com.tmt.shop.entity.Goods;
import com.tmt.shop.entity.Product;
import com.tmt.system.entity.User;

/**
 * 商品库存 管理
 * @author 超级管理员
 * @date 2016-01-21
 */
public interface ProductServiceFacade extends BaseServiceFacade<Product,Long> {
	
	/**
	 * 修改基本信息时
	 */
	public void add(Goods goods);
	
	/**
	 * 单个保存
	 */
	public void save(Product product);
	
	/**
	 * 批量保存
	 */
	public void save(Goods goods);
	
	/**
	 * 根据商品Id查询库存的集合
	 * @param goodsId
	 * @return
	 */
	public List<Product> queryProductsByGoodsId(Long goodsId);
	
	/**
	 * 入库
	 * @param product
	 */
	public void inStroe(Product product);
	
	/**
	 * 出库
	 * @param product
	 */
	public void outStroe(Product product);
	
	/**
	 * 冻结库存
	 * @param product
	 */
	public void freeStroe(Product product);
	
	/**
	 * 根据编号获取唯一的商品
	 * @param sn
	 * @return
	 */
	public Product getBySn(String sn);
	
	/**
	 * 清空快照地址
	 * @param product
	 */
	public void clearSnapshot(List<Product> products);
	
	/**
	 * 修改快照地址
	 * @param product
	 */
	public void updateSnapshot(List<Product> products);
	
	/**
	 * 根据商品Id查询库存的集合
	 * @param goodsId
	 * @return
	 */
	public List<Product> queryUseAbleProductsByGoodsId(Long goodsId);
	
	/**
	 * 清除商品限购
	 */
	public void clearGoodsLimit();
	
	/**
	 * 商品统计
	 * @return
	 */
	public Map<String, Integer> statProduct();
	
	/**
	 * 校验限购
	 * @param product
	 * @param user
	 * @param quantity
	 * @return
	 */
	public Boolean checkGoodsLimit(Long goodsId, User user, int quantity);
	
	/**
	 * 锁定此商品
	 * @param order
	 */
	public Product lockStoreProduct(Long id);
	
	/**
	 * 锁定此商品
	 * @param order
	 */
	public Product getStore(Long id);
}
