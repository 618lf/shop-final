package com.tmt.shop.service;

import java.util.List;

import com.tmt.core.service.BaseServiceFacade;
import com.tmt.shop.entity.Category;
import com.tmt.shop.entity.Goods;
import com.tmt.shop.entity.GoodsDelivery;
import com.tmt.shop.entity.GoodsEvaluate;
import com.tmt.shop.entity.GoodsLimit;
import com.tmt.shop.entity.GoodsSpecification;
import com.tmt.shop.entity.GoodsTag;
import com.tmt.shop.entity.Product;

/**
 * 商品管理 管理
 * @author 超级管理员
 * @date 2016-01-21
 */
public interface GoodsServiceFacade extends BaseServiceFacade<Goods,Long> {
	
	/**
	 * 初始化向导
	 * @param category
	 * @return
	 */
	public Goods initAdd(Category category);
	
	/**
	 * 新增
	 */
	public void add(Goods goods);
	
	/**
	 * 初始化向导
	 * @param category
	 * @return
	 */
	public Goods initModify(Long id);
	
	/**
	 * 修改
	 */
	public void modify(Goods goods);
	
	/**
	 * 推荐信息
	 * @param goods
	 */
	public void saveEvaluate(GoodsEvaluate evaluate);
	
	/**
	 * 规格信息
	 * @param goods
	 */
	public void saveSpecification(Goods goods);
	
	/**
	 * 删除
	 */
	public void delete(List<Goods> goodss);
	
	/**
	 * 查询商品评价（现阶段只有一个）
	 * @param id
	 * @return
	 */
	public Goods getWithEvaluate(Long id);
	
	/**
	 * 取消限购
	 * @param id
	 */
	public void cancelLimit(Long id);
	
	/**
	 * 设置限购
	 * @param id
	 */
	public void openLimit(GoodsLimit limit);
	
	/**
	 * 商品的标签
	 * @param id
	 * @return
	 */
	public List<GoodsTag> queryRealTags(Long id);
	
	/**
	 * 商品的规格
	 * @return
	 */
	public List<GoodsSpecification> querySpecifications(Long id);
	
	//前端需要的方法
	/**
	 * 商品配送方案
	 * @param id
	 * @return
	 */
	public GoodsDelivery getGoodsDelivery(Long id);
	
	/**
	 * 保存最新的商品快照
	 */
	public void clearSnapshot(Goods goods);
	
	/**
	 * 得到前端可显示的商品 - 快照
	 * @param id
	 * @return
	 */
	public Product getSnapshotProductId(Long productId);
	
	/**
	 * 得到前端可显示的商品
	 * @param id
	 * @return
	 */
	public Goods getShowGoods(Long id);
}