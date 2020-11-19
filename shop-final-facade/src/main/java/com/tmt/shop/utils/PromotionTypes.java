package com.tmt.shop.utils;

import java.util.List;

import com.tmt.common.utils.Ints;
import com.tmt.common.utils.StringUtil3;
import com.tmt.shop.entity.Promotion;
import com.tmt.shop.entity.PromotionCoupon;
import com.tmt.shop.entity.PromotionExt;
import com.tmt.shop.entity.PromotionProduct;

/**
 * 返回类型的定义名称
 * @author root
 *
 */
public class PromotionTypes {

	
	/**
	 * 返回type 定义的名称
	 * @param type
	 * @return
	 */
	public static String getTypeName(Byte type) {
		if (type == null) {
			return null;
		}
		if (type == 1) {
			return "直减";
		} else if(type == 2) {
			return "满减";
		} else if(type == 3) {
			return "满折";
		} else if(type == 4) {
			return "折扣";
		} else if(type == 5) {
			return "包邮";
		} else if(type == 6) {
			return "团购";
		} else if(type == 7) {
			return "抢购";
		} else if(type == 8) {
			return "满赠";
		} else if(type == 9) {
			return "新人礼包";
		} else if(type == 10) {
			return "邀请有礼";
		} else if(type == 11) {
			return "套装";
		}
		return null;
	}
	
	/**
	 * 促销的描述信息
	 * @param promotion
	 * @return
	 */
	public static String getRemarks(Promotion promotion) {
		String remarks = ""; Byte type = promotion.getType();
		if (type == 1) {
			remarks = StringUtil3.format("直减%s元", promotion.getReduce());
		} else if(type == 2) {
			StringBuilder describe = new StringBuilder();
			describe.append("满").append(promotion.getOrderPrice()).append("元减").append(promotion.getReduce()).append("元;");
			PromotionExt ext = promotion.getExt();
			if (ext != null && ext.getOrderPrice1() != null) {
				describe.append("满").append(ext.getOrderPrice1()).append("元减").append(ext.getReduce1()).append("元;");
			}
			if (ext != null && ext.getOrderPrice2() != null) {
				describe.append("满").append(ext.getOrderPrice2()).append("元减").append(ext.getReduce2()).append("元;");
			}
			remarks = describe.toString();
		} else if(type == 3) {
			StringBuilder describe = new StringBuilder();
			describe.append("满").append(promotion.getOrderPrice()).append("元打").append(promotion.getDiscount()* 10).append("折;");
			PromotionExt ext = promotion.getExt();
			if (ext != null && ext.getOrderPrice1() != null) {
				describe.append("满").append(ext.getOrderPrice1()).append("元打").append(ext.getDiscount1()* 10).append("折;");
			}
			if (ext != null && ext.getOrderPrice2() != null) {
				describe.append("满").append(ext.getOrderPrice2()).append("元打").append(ext.getDiscount2()* 10).append("折;");
			}
			remarks = describe.toString();
		} else if(type == 4) {
			StringBuilder describe = new StringBuilder();
			List<PromotionProduct> products = promotion.getProducts();
		    if (products != null) {
		    	for(PromotionProduct product: products) {
		    		describe.append(product.getProductName()).append("打").append(product.getDiscount()* 10).append("折;");
		    	}
		    }
			remarks = StringUtil3.abbr(describe.toString(), 255);
		} else if(type == 5) {
			remarks = StringUtil3.format("满%s元包邮", promotion.getOrderPrice());
		} else if(type == 6) {
			remarks = StringUtil3.format("打%s折", promotion.getDiscount() * 10);
		} else if(type == 7) {
			remarks = StringUtil3.format("打%s折,限购%s件", promotion.getDiscount() * 10, promotion.getGetno());
		} else if(type == 8) {
			int val = 0;
			List<PromotionCoupon> coupons = promotion.getCoupons();
			if (coupons != null) {
		    	for(PromotionCoupon coupon: coupons) {
		    		val = Ints.add(val, coupon.getVal());
		    	}
			}
			if (promotion.getIsPrice() == 1) {
				remarks = StringUtil3.format("满%s元赠送%s元优惠券", promotion.getOrderPrice(), val);
			}
			if (promotion.getIsQuantity() == 1) {
				remarks = StringUtil3.format("满%s件赠送%s元优惠券", promotion.getOrderQuantity(), val);
			}
		}
		return remarks;
	}
}
