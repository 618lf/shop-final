package com.tmt.shop.front;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmt.core.entity.AjaxResult;
import com.tmt.core.persistence.QueryCondition;
import com.tmt.core.persistence.QueryCondition.Criteria;
import com.tmt.core.persistence.incrementer.UUIdGenerator;
import com.tmt.core.utils.CacheUtils;
import com.tmt.core.utils.CookieUtils;
import com.tmt.core.utils.Maps;
import com.tmt.core.utils.StringUtils;
import com.tmt.core.utils.WebUtils;
import com.tmt.shop.entity.Rank;
import com.tmt.shop.entity.Receiver;
import com.tmt.shop.entity.ShopConstant;
import com.tmt.shop.entity.UserRank;
import com.tmt.shop.service.CartServiceFacade;
import com.tmt.shop.service.CouponCodeServiceFacade;
import com.tmt.shop.service.OrderAppraiseServiceFacade;
import com.tmt.shop.service.OrderServiceFacade;
import com.tmt.shop.service.ReceiverServiceFacade;
import com.tmt.shop.service.UserRankServiceFacade;
import com.tmt.shop.utils.RankUtils;
import com.tmt.system.entity.User;
import com.tmt.system.service.MessageServiceFacade;
import com.tmt.system.utils.UserUtils;

/**
 * 将前端零散的状态查询集合在一起
 * @author lifeng
 */
@Controller("frontMutilAboutController")
@RequestMapping(value = "${frontPath}/shop/mutil")
public class MutilAboutController {

	@Autowired
	private CartServiceFacade cartService;
	@Autowired
	private OrderServiceFacade orderService;
	@Autowired
	private CouponCodeServiceFacade couponCodeService;
	@Autowired
	private MessageServiceFacade messageService;
	@Autowired
	private OrderAppraiseServiceFacade appraiseService;
	@Autowired
	private UserRankServiceFacade userRankService;
	@Autowired
	private ReceiverServiceFacade receiverService;
	
	/**
	 * 用户相关的
	 * @return
	 */
	@ResponseBody
	@RequestMapping("user.json")
	public AjaxResult user(HttpServletRequest request, HttpServletResponse response) {
		
		// 订单
		Map<String, Object> states = this.countOrderState();
		
		// 购物车
		int cartQuantity = this.cartQuantity(request, response);
		states.put("cartQuantity", cartQuantity);
		
		// 其他自定义
		String mutils = WebUtils.getCleanParam("mutils");
		
		// 未读的消息
		if (StringUtils.contains(mutils, "message")) {
			int unreadMessage = this.unreadMessage();
			states.put("unreadMessage", unreadMessage);
		}
		
		// 订单评价数量
		if (StringUtils.contains(mutils, "appraise")) {
			states.putAll(this.count_appraise_state());
		}
		
		// 用户等级
		if (StringUtils.contains(mutils, "rank")) {
			UserRank rank = this.userRank();
			states.put("rank", rank);
		}
		
		// 用户默认地址
		if (StringUtils.contains(mutils, "receiver")) {
			Receiver receiver = this.userReceiver();
			states.put("receiver", receiver);
		}
		return AjaxResult.success(states);
	}
	
	// 购物车数量
	private int cartQuantity(HttpServletRequest request, HttpServletResponse response) {
		String cartKey = CookieUtils.getCookie(request, ShopConstant.CART_KEY);
		if (UserUtils.isGuest()) {
		    if (StringUtils.isBlank(cartKey)) {
			    cartKey = UUIdGenerator.uuid();
			    CookieUtils.setCookie(response, ShopConstant.CART_KEY, cartKey, 60*60*24*30);
		    } else {
		    	Integer count = CacheUtils.getSessCache().get(cartKey);
		    	if (count == null) {
		    		count = cartService.countQuantityByCartKey(cartKey);
		    		CacheUtils.getSessCache().put(cartKey, count);
		    		return count;
		    	}
		    	return count;
		    }
		} else {
		    User user = UserUtils.getUser();
		    if (StringUtils.isNotBlank(cartKey)) {
			    this.cartService.merge(cartKey, user);
			    CookieUtils.remove(request, response, ShopConstant.CART_KEY);
			    CacheUtils.getSessCache().delete(cartKey);
		    }
		    Integer count = UserUtils.getAttribute(ShopConstant.CART_QUANTITY);
		    if (count == null) {
		    	count = cartService.countQuantityByUserId(user);
		    	UserUtils.setAttribute(ShopConstant.CART_QUANTITY, count);
		    	return count;
		    }
		    return count;
		}
		return 0;
	}
	
	// 用户未读的消息数量
    private int unreadMessage() {
    	if (UserUtils.isUser()) {
    		User user = UserUtils.getUser();
    		return messageService.getUnreadCount(user);
    	}
    	return 0;
    }
	
	
	// 用户订单的状态
	private Map<String, Object> countOrderState() {
		if (UserUtils.isUser()) {
			User user = UserUtils.getUser();
			QueryCondition qc = new QueryCondition();
			Criteria c = qc.getCriteria();
			this.initQc("2", c);
			c.andEqualTo("CREATE_ID", user.getId());
			int unpay = this.orderService.countByCondition(qc);
			qc.clear();
			c = qc.getCriteria();
			this.initQc("3", c);
			c.andEqualTo("CREATE_ID", user.getId());
			int unshipped = this.orderService.countByCondition(qc);
			qc.clear();
			c = qc.getCriteria();
			this.initQc("5", c);
			c.andEqualTo("CREATE_ID", user.getId());
			int unreceipted = this.orderService.countByCondition(qc);
			
			//可用优惠券数量
			int usableCount = couponCodeService.countUserUsableStat(user);
			
			// 返回相应数据
			Map<String, Object> param = Maps.newHashMap();
			param.put("unpay", unpay);
			param.put("unshipped", unshipped);
			param.put("unreceipted", unreceipted);
			param.put("usableCount", usableCount);
			return param;
		}
		return Maps.newHashMap();
	}
	
	//查询条件
	private void initQc(String queryStatus, Criteria c) {
		// 待付款（单可以付款的）
		if ("2".equals(queryStatus)) {
			c.andIsNotNull("EXPIRE");
			c.andDateGreaterThanOrEqualTo("EXPIRE", new Date());
			c.andEqualTo("FLOW_STATE", "待付款");
		}
		
		// 待发货(是否需要包含申请退款) 4 是申请退款
		else if ("3".equals(queryStatus)) {
			c.andEqualTo("FLOW_STATE", "待发货");
		}
		
		// 待收货
		else if ("5".equals(queryStatus)) {
			c.andEqualTo("FLOW_STATE", "待收货");
		}
	}
	
	// 待评价的订单
	public Map<String, Object> count_appraise_state() {
		if (UserUtils.isUser()) {
			Long userId = UserUtils.getUser().getId();
			int unappraise = this.appraiseService.countUserAppraiseTask(userId);
			int unrappraise = this.appraiseService.countUserRappraiseTask(userId);
			int appraised = this.appraiseService.countUserAppraisedTask(userId);
			Map<String, Object> param = Maps.newHashMap();
			param.put("unappraise", unappraise);
			param.put("unrappraise", unrappraise);
			param.put("appraised", appraised);
			return param;
		}
		return Maps.newHashMap();
	}
	
	// 用户等级
	public UserRank userRank() {
		if (UserUtils.isUser()) {
			User user = UserUtils.getUser();
			UserRank rank = userRankService.touch(user.getId());
			if (rank != null && rank.getRankId() != null) {
				Rank _rank = RankUtils.getRank(rank.getRankId());
				if (_rank != null) {
					rank.setRankImage(_rank.getImage());
					rank.setRankName(_rank.getName());
				}
			}
			return rank;
		}
		return null;
	}
	
	// 用户默认的地址
	public Receiver userReceiver() {
		if (UserUtils.isUser()) {
			User user = UserUtils.getUser();
			return this.receiverService.queryUserDefaultReceiver(user);
		}
		return null;
	}
}