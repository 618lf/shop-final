package com.tmt.shop.service;

import com.tmt.shop.entity.Product;

/**
 * 商店静态化服务
 * @author root
 */
public interface ShopStaticizerFacade {

    /**
     * 首页静态化
     * -- 修改分类需要修改首页
     */
    public void index_build();
    
    /**
     * 搜索页面静态化
     * -- 一般不许要修改
     */
    public void search_build();
    
    /**
     * 分类静态化
     * -- 修改分类需要修改首页
     */
    public void categorys_build();
    
    /**
     * 分类静态化
     * -- 修改分类需要修改首页
     */
    public void categorys_build(Long categoryId);
    
    /**
	 * 商品静态化
	 * @param goods
	 */
	public Product goods_snapshot_build(Long productId);
    
	/**
	 * 商品静态化
	 * @param goods
	 */
	public void goods_build(Long goodsId);
	
	/**
	 * 删除静态化内容
	 * @param goods
	 */
    public void goods_delete(Long goodsId);
    
    /**
     * 所有商品
     */
    public void goodss_build();
    
	/**
	 * 删除静态化内容
	 * @param goods
	 */
    public void meta_delete(Long metaId);
    
    /**
     * 所有分类
     * @param categoryId
     */
    public void meta_build(Long metaId);
    
    /**
     * 所有素材 -- 图文
     */
    public void metas_rich_build();
    
    /**
     *  所有素材 -- 文本
     */
    public void metas_text_build();
    
    /**
     *  所有素材 -- 图片
     */
    public void metas_image_build();
    
	/**
	 * 微页面静态化
	 * @param goods
	 */
	public void mpage_build(Long mpageId);
	
	/**
	 * 删除静态化内容
	 * @param goods
	 */
    public void mpage_delete(Long mpageId);
    
    /**
	 * 促销静态化
	 * @param goods
	 */
	public void promotion_build(Long promotionId);
	
	/**
	 * 删除静态化内容
	 * @param goods
	 */
    public void promotion_delete(Long promotionId);
    
    /**
     * 所有微页面
     */
    public void mpages_build();
    
    /**
     *  所有促销
     */
    public void promotions_build();
}