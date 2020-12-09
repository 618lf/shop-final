package com.tmt.shop.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.core.entity.BaseEntity;
import com.tmt.core.persistence.BaseDao;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.service.BaseService;
import com.tmt.core.utils.Ints;
import com.tmt.core.utils.Lists;
import com.tmt.core.utils.Maps;
import com.tmt.core.utils.StringUtils;
import com.tmt.shop.dao.CartDao;
import com.tmt.shop.dao.CartItemDao;
import com.tmt.shop.entity.Cart;
import com.tmt.shop.entity.CartItem;
import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.OrderItem;
import com.tmt.shop.entity.Promotion;
import com.tmt.shop.utils.CartSort;
import com.tmt.shop.utils.ComplexUtils;
import com.tmt.shop.utils.ProductUtils;
import com.tmt.shop.utils.PromotionUtils;
import com.tmt.system.entity.User;

/**
 * 购物车 管理
 * @author 超级管理员
 * @date 2016-01-21
 */
@Service("shopCartService")
public class CartService extends BaseService<Cart,Long> implements CartServiceFacade{
	
	@Autowired
	private CartDao cartDao;
	@Autowired
	private CartItemDao itemDao;
	
	@Override
	protected BaseDao<Cart, Long> getBaseDao() {
		return cartDao;
	}
	
	/**
	 * 添加一个商品到指定的购物车
	 * @param productId
	 */
	@Transactional
	public void add(Cart cart, Byte type, Long productId, int quantity) {
		this.save(cart);
		Map<String, Object> params = Maps.newHashMap();
		params.put("PRODUCT_ID", productId);
		params.put("CART_ID", cart.getId());
		CartItem item = this.itemDao.queryForObject("findByProductId", params);
		if (item != null) {
		    item.setQuantity(Ints.addI(item.getQuantity(), quantity));
		    this.itemDao.update(item);
		} else {
		   item = new CartItem();
		   item.setProductId(productId);
		   item.setCartId(cart.getId());
		   item.setQuantity(quantity);
		   item.setChecked((byte)1);
		   item.setType(type == null?BaseEntity.NO:type); // 暂时只有两种类型
		   this.itemDao.insert(item);
		}
	}
	
	/**
	 * 减少商品
	 */
	@Override
	public void reduce(Cart cart, Long productId) {
		this.save(cart);
		Map<String, Object> params = Maps.newHashMap();
		params.put("PRODUCT_ID", productId);
		params.put("CART_ID", cart.getId());
		CartItem item = this.itemDao.queryForObject("findByProductId", params);
		if (item == null) {
		    return;
		}
		
		// 如果减少到0 则删除
		item.setQuantity(Ints.addI(item.getQuantity(), -1));
		if (item.getQuantity() <= 0) {
			this.itemDao.delete(item);
		} else {
			this.itemDao.update(item);
		}
	}
	
	/**
	 * 删除一件商品
	 * @param cartItem
	 */
	@Transactional
	public void delete(CartItem cartItem) {
		this.itemDao.delete(cartItem);
	}
	
	/**
	 * 编辑一件商品
	 * @param cartItem
	 */
	@Transactional
	public void edit(CartItem cartItem) {
		if (cartItem.getQuantity() <= 0) {
			this.itemDao.delete(cartItem);
		} else {
			this.itemDao.update("updateQuantity", cartItem);
		}
	}
	
	/**
	 * 编辑一件商品
	 * @param cartItem
	 */
	@Transactional
	public void checked(CartItem cartItem) {
		this.itemDao.update("updateChecked", cartItem);
	}
	
	/**
	 * 切换优惠
	 */
	@Override
	@Transactional
	public void changePm(CartItem cartItem) {
		if (cartItem.getPromotions() == null) {
			cartItem.setPromotions(IdGen.INVALID_ID);
		}
		this.itemDao.update("updatePromotions", cartItem);
	}

	/**
	 * 全选择
	 * @param cartItem
	 */
	@Transactional
	public void allSelected(Cart cart) {
		this.update("updateAllSelected", cart);
	}
	
	/**
	 * 全取消
	 * @param cartItem
	 */
	@Transactional
	public void allCanceled(Cart cart) {
		this.update("updateAllCanceled", cart);
	}
	
	/**
	 * 将cartKey 中的数据合并到用户信息中
	 * @param cartKey
	 * @return
	 */
	@Transactional
	public void merge(String cartKey, User user) {
		Cart cart1 = this.getByKey(cartKey);
		Cart cart2 = this.getByUserId(user);
		if(cart1 != null && cart2 != null) {
		   List<CartItem> items1 = this.itemDao.queryForList("findByCartId",cart1.getId());
		   List<CartItem> items2 = this.itemDao.queryForList("findByCartId",cart2.getId());
		   List<CartItem> updates = Lists.newArrayList();
		   List<CartItem> inserts = Lists.newArrayList();
		   for(CartItem item1: items1) {
			   Boolean bFound = Boolean.FALSE;
			   for(CartItem item2: items2) {
				   if(item1.getProductId().equals(item2.getProductId())) {
					  item2.setQuantity(Ints.addI(item1.getQuantity(), item2.getQuantity()));
					  updates.add(item2);
					  bFound = Boolean.TRUE; break;
				   }
			   }
			   if(!bFound) {
				  CartItem copy = item1.copy();
				  copy.setCartId(cart2.getId());
				  inserts.add(copy);
			   }
		   }
		   this.itemDao.batchUpdate(updates);
		   this.itemDao.batchInsert(inserts);
		   this.itemDao.batchDelete(items1);
		   this.delete(cart1);
		} else if(cart1 != null && cart2 == null) {
		   cart1.setCreateId(user.getId());
		   cart1.setCreateName(user.getName());
		   cart1.setCartKey(null);
		   this.update(cart1);
		}
	}
	
	/**
	 * 保存
	 */
	@Transactional
	public void save(Cart cart) {
		if (IdGen.isInvalidId(cart.getId())) {
		    if (StringUtils.isBlank(cart.getCartKey())) {
		    	cart.setCartKey(null);
		    }
			this.insert(cart);
		}
	}
	
	/**
	 * 删除
	 */
	@Transactional
	public void delete(List<Cart> carts) {
		this.batchDelete(carts);
	}
	
	/**
	 * 下单
	 * @param order
	 */
	@Transactional
	public void book(Order order) {
		List<OrderItem> items = order.getItems();
		List<CartItem> cItems = Lists.newArrayList();
		for(OrderItem item: items) {
			if(item.getCartItemId() != null) {
			   CartItem cartItem = new CartItem();
			   cartItem.setId(item.getCartItemId());
			   cItems.add(cartItem);
			}
		}
		this.itemDao.batchDelete(cItems);
	}
	
	public Cart getByKey(String cartKey) {
		return this.queryForObject("findByKey", cartKey);
	}
	
	public Cart getByUserId(User user) {
		return this.queryForObject("findByUserId", user.getId());
	}
	
	/**
	 * 得到购物车明细，并设置促销
	 */
	@Transactional
	public List<CartItem> queryProductsByCartId(Long cartId) {
		List<CartItem> items = this.itemDao.queryForList("findByCartId",cartId);
		if (items != null && items.size() != 0) {
			List<CartItem> updates = Lists.newArrayList();
			for(CartItem item: items) {
				
				// 填充product
				if (item != null && item.getType() == 0) {
					item.setProduct(ProductUtils.getProduct(item.getProductId()));
				} else if(item != null && item.getType() == 1) {
					item.setComplex(ComplexUtils.getComplex(item.getProductId()));
				}
				
				// 套餐的无促销
				if (item.getType() == 1) {
					item.setPromotion(null);
					continue;
				}
				
				// 促销
				Map<Long, Promotion> _promotions = PromotionUtils.queryProductAllEnabledPromotions(item.getProductId());
				Promotion one = null;
				if (item.getPromotions() != null && IdGen.isInvalidId(item.getPromotions())) {
					item.setPromotion(null);
					item.setHasPromotion(!_promotions.isEmpty());
				} else if (item.getPromotions() != null && _promotions.containsKey(item.getPromotions())
						&& (one = _promotions.get(item.getPromotions())) != null) {
					item.setPromotion(one);
				} else if (!_promotions.isEmpty() && (one = Maps.getFirst(_promotions)) != null) {
					item.setPromotion(one);
					updates.add(item);
				} else if (!_promotions.isEmpty()) {
					item.setPromotion(null);
					updates.add(item);
				} else {
					item.setPromotion(null);
					updates.add(item);
				}
			}
			
			// 更新最新的促销信息
			if (updates != null) {
				this.itemDao.batchUpdate("updatePromotions", updates);
			}
			
			// 排序，按照促销信息排序
			CartSort.sort(items);
		}
		return items;
	}
	
	/**
	 * 得到购物车明细，并设置促销
	 */
	public CartItem getProductByCartItemId(Long cartItemId) {
		CartItem item = this.itemDao.get(cartItemId);
		if (item != null && item.getType() == 0) {
			item.setProduct(ProductUtils.getProduct(item.getProductId()));
		} else if(item != null && item.getType() == 1) {
			item.setComplex(ComplexUtils.getComplex(item.getProductId()));
		}
		return item;
	}
	
	/**
	 * 根据key获取购物车的数量
	 * @param cartkey
	 * @return
	 */
	public int countQuantityByCartKey(String cartkey) {
		Integer count = this.itemDao.countByCondition("countQuantityByCartKey", cartkey);
		return count == null?0:count;
	}
	
	/**
	 * 根据用户ID获取购物车的数量
	 * @param cartkey
	 * @return
	 */
	public int countQuantityByUserId(User user) {
		Integer count = this.itemDao.countByCondition("countQuantityByUserId", user.getId());
		return count == null?0:count;
	}
}