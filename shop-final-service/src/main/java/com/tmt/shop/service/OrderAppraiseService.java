package com.tmt.shop.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.core.persistence.BaseDao;
import com.tmt.core.persistence.Page;
import com.tmt.core.persistence.PageParameters;
import com.tmt.core.service.BaseService;
import com.tmt.core.utils.Maps;
import com.tmt.core.utils.StringUtils;
import com.tmt.shop.dao.OrderAppraiseDao;
import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.OrderAppraise;
import com.tmt.shop.entity.OrderItem;
import com.tmt.shop.entity.ProductAppraise;
import com.tmt.system.entity.User;

/**
 * 订单评价 管理
 * @author 超级管理员
 * @date 2017-04-10
 */
@Service("shopOrderAppraiseService")
public class OrderAppraiseService extends BaseService<OrderAppraise,Long> implements OrderAppraiseServiceFacade{
	
	@Autowired
	private OrderAppraiseDao orderAppraiseDao;
	@Autowired
	private OrderItemService itemService;
	@Autowired
	private ProductAppraiseService pAppraiseService;
	
	@Override
	protected BaseDao<OrderAppraise, Long> getBaseDao() {
		return orderAppraiseDao;
	}
	
	/**
	 *  添加评价任务
	 * @param order
	 */
	@Transactional
	public void addAppraiseTask(Order order) {
		OrderAppraise appraise = new OrderAppraise();
		appraise.setId(order.getId());
		appraise.setSn(order.getSn());
		appraise.setCreateId(order.getCreateId());
		appraise.setCreateName(order.getCreateName());
		appraise.setCreateDate(order.getCreateDate());
		appraise.setState(OrderAppraise.NO);
		this.insert(appraise);
	}

	/**
	 * 用户待评价列表
	 * @param userId
	 * @param param
	 * @return
	 */
	@Override
	public Page queryUserAppraiseTask(Long userId, PageParameters param) {
		Map<String, Object> qc = Maps.newHashMap();
		qc.put("userId", userId);
		qc.put("state", OrderAppraise.DEL_FLAG_NORMAL);
		return this.fillItems(this.queryForPageList("queryUserAppraiseTask", qc, param));
	}

	/**
	 * 用户待追评列表
	 * @param userId
	 * @param param
	 * @return
	 */
	@Override
	public Page queryUserRappraiseTask(Long userId, PageParameters param) {
		Map<String, Object> qc = Maps.newHashMap();
		qc.put("userId", userId);
		qc.put("state", OrderAppraise.DEL_FLAG_DELETE);
		return this.fillItems(this.queryForPageList("queryUserAppraiseTask", qc, param));
	}

	/**
	 * 用户待已评价列表
	 * 待追评的也是已评价的
	 * @param userId
	 * @param param
	 * @return
	 */
	@Override
	public Page queryUserAppraisedTask(Long userId, PageParameters param) {
		Map<String, Object> qc = Maps.newHashMap();
		qc.put("userId", userId);
		qc.put("state", OrderAppraise.DEL_FLAG_AUDIT);
		return this.fillItems(this.queryForPageList("queryUserAppraisedTask", qc, param));
	}
	
	// 填充明细
	private Page fillItems(Page page) {
		List<OrderAppraise> appraises = page.getData();
		for(OrderAppraise appraise: appraises) {
			List<OrderItem> items = this.itemService.querySimpleItemsByOrderId(appraise.getId());
			appraise.setItems(items);
		}
		return page;
	}

	@Override
	public int countUserAppraiseTask(Long userId) {
		Map<String, Object> qc = Maps.newHashMap();
		qc.put("userId", userId);
		qc.put("state", OrderAppraise.DEL_FLAG_NORMAL);
		return this.countByCondition("queryUserAppraiseTaskStat", qc);
	}

	@Override
	public int countUserRappraiseTask(Long userId) {
		Map<String, Object> qc = Maps.newHashMap();
		qc.put("userId", userId);
		qc.put("state", OrderAppraise.DEL_FLAG_DELETE);
		return this.countByCondition("queryUserAppraiseTaskStat", qc);
	}
	
	@Override
	public int countUserAppraisedTask(Long userId) {
		Map<String, Object> qc = Maps.newHashMap();
		qc.put("userId", userId);
		qc.put("state", OrderAppraise.DEL_FLAG_DELETE);
		return this.countByCondition("queryUserAppraisedTaskStat", qc);
	}

	/**
	 * 评价
	 */
	@Override
	@Transactional
	public void appraise(User user, OrderAppraise appraise) {
		appraise.setState(OrderAppraise.DEL_FLAG_DELETE);
		List<ProductAppraise> appraises = appraise.getAppraises();
		for(ProductAppraise pAppraise : appraises) {
			
			// 没有填写内容的不用评价
			if (StringUtils.isBlank(pAppraise.getContent())) {
				continue;
			}
			
			// 填充商品信息
			OrderItem product = itemService.get(pAppraise.getProductId());
			if (product == null) {
				continue;
			}
			pAppraise.setProductId(product.getProductId());
			pAppraise.setProductName(product.getProductName());
			pAppraise.setProductImage(product.getThumbnail());
			
			// 用户信息
			pAppraise.userOptions(user);
			if (StringUtils.isBlank(pAppraise.getCreateName())) {
				pAppraise.setCreateName(user.getNo());
			}
			pAppraise.setCreateImage(user.getHeadimg());
			pAppraiseService.appraise(pAppraise);
		}
		this.update(appraise);
	}

	/**
	 * 追评
	 */
	@Override
	@Transactional
	public void rappraise(User user, OrderAppraise appraise) {
		appraise.setState(OrderAppraise.DEL_FLAG_AUDIT);
		List<ProductAppraise> appraises = appraise.getAppraises();
		for(ProductAppraise pAppraise : appraises) {
			// 没有填写内容的不用评价
			if (StringUtils.isBlank(pAppraise.getContent())) {
				continue;
			}
			pAppraiseService.rappraise(pAppraise);
		}
		this.update(appraise);
	}
}