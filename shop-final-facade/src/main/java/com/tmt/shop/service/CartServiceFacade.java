package com.tmt.shop.service;

import java.util.List;

import com.tmt.core.service.BaseServiceFacade;
import com.tmt.shop.entity.Cart;
import com.tmt.shop.entity.CartItem;
import com.tmt.shop.entity.Order;
import com.tmt.system.entity.User;

/**
 * 购物车 管理
 * @author 超级管理员
 * @date 2016-01-21
 */
public interface CartServiceFacade extends BaseServiceFacade<Cart,Long> {
	
	/**
	 * 添加一个商品到指定的购物车
	 * @param productId
	 */
	public void add(Cart cart, Byte type, Long productId, int quantity);
	
	/**
	 * 减少商品
	 * @param productId
	 */
	public void reduce(Cart cart, Long productId);
	
	/**
	 * 删除一件商品
	 * @param cartItem
	 */
	public void delete(CartItem cartItem);
	
	/**
	 * 编辑一件商品
	 * @param cartItem
	 */
	public void edit(CartItem cartItem);
	
	/**
	 * 选中一件商品
	 * @param cartItem
	 */
	public void checked(CartItem cartItem);
	
	/**
	 * 切换促销
	 * @param cartItem
	 */
	public void changePm(CartItem cartItem);
	
	/**
	 * 全选择
	 * @param cartItem
	 */
	public void allSelected(Cart cart);
	
	/**
	 * 全取消
	 * @param cartItem
	 */
	public void allCanceled(Cart cart);
	
	/**
	 * 将cartKey 中的数据合并到用户信息中
	 * @param cartKey
	 * @return
	 */
	public void merge(String cartKey, User user);
	
	/**
	 * 保存
	 */
	public void save(Cart cart);
	
	/**
	 * 删除
	 */
	public void delete(List<Cart> carts);
	
	/**
	 * 下单
	 * @param order
	 */
	public void book(Order order);
	
	/**
	 * 根据KEY得到购物车
	 * @param cartKey
	 * @return
	 */
	public Cart getByKey(String cartKey);
	
	/**
	 * 得到用户的购物车
	 * @return
	 */
	public Cart getByUserId(User user);
	
	
	/**
	 * 得到购物车明细
	 * @param cartId
	 * @return
	 */
	public List<CartItem> queryProductsByCartId(Long cartId);
	
	/**
	 * 得到购物明细
	 * @param cartItemId
	 * @return
	 */
	public CartItem getProductByCartItemId(Long cartItemId);
	
	
	/**
	 * 根据key获取购物车的数量
	 * @param cartkey
	 * @return
	 */
	public int countQuantityByCartKey(String cartkey);
	
	/**
	 * 根据用户ID获取购物车的数量
	 * @param cartkey
	 * @return
	 */
	public int countQuantityByUserId(User user);
}