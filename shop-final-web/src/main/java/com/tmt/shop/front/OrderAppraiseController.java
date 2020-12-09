package com.tmt.shop.front;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmt.core.config.Globals;
import com.tmt.core.entity.AjaxResult;
import com.tmt.core.persistence.Page;
import com.tmt.core.persistence.PageParameters;
import com.tmt.core.utils.JsonMapper;
import com.tmt.core.utils.Maps;
import com.tmt.core.utils.WebUtils;
import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.OrderAppraise;
import com.tmt.shop.entity.ProductAppraise;
import com.tmt.shop.service.OrderAppraiseServiceFacade;
import com.tmt.shop.service.OrderServiceFacade;
import com.tmt.shop.service.ProductAppraiseServiceFacade;
import com.tmt.system.entity.User;
import com.tmt.system.utils.UserUtils;

/**
 * 商品预览
 * @author root
 */
@Controller
@RequestMapping(value = "${frontPath}/member/order/appraise")
public class OrderAppraiseController {

	@Autowired
	private OrderAppraiseServiceFacade appraiseService;
	@Autowired
	private OrderServiceFacade orderService;
	@Autowired
	private ProductAppraiseServiceFacade productAppraiseService;
	
	/**
	 * 列表
	 * @return
	 */
	@RequestMapping("/list.html")
	public String appraise_list() {
		return "/front/member/OrderAppraiseList"; 
	}
	
	/**
	 * 列表
	 * @return
	 */
	@RequestMapping("/list/rappraise.html")
	public String rappraise_list() {
		return "/front/member/OrderRappraiseList"; 
	}
	
	/**
	 * 列表
	 * @return
	 */
	@RequestMapping("/list/appraised.html")
	public String appraised_list() {
		return "/front/member/OrderAppraisedList"; 
	}
	
	/**
	 * 列表数据
	 * @param page
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/list/data.json")
	public Page page(Byte state, Page page) {
		if (state == null) { state = 0;}
		Long userId = UserUtils.getUser().getId();
		PageParameters param = page.getParam();
		param.setPageSize(15);
		if (state == 1){
			return page = this.appraiseService.queryUserRappraiseTask(userId, param);
		} else if(state == 2) {
			return page = this.appraiseService.queryUserAppraisedTask(userId, param);
		}
		return this.appraiseService.queryUserAppraiseTask(userId, param);
	}
	
	/**
	 * 评价的数据
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/count_appraise_state.json")
	public AjaxResult count_appraise_state() {
		Long userId = UserUtils.getUser().getId();
		int unappraise = this.appraiseService.countUserAppraiseTask(userId);
		int unrappraise = this.appraiseService.countUserRappraiseTask(userId);
		int appraised = this.appraiseService.countUserAppraisedTask(userId);
	    Map<String, Object> param = Maps.newHashMap();
        param.put("unappraise", unappraise);
        param.put("unrappraise", unrappraise);
        param.put("appraised", appraised);
        return AjaxResult.success(param);
	}
	
	/**
	 * 表单
	 * @return
	 */
	@RequestMapping("/form/{orderId}.html")
	public String form(@PathVariable Long orderId, Model model) {
		OrderAppraise oAppraise = appraiseService.get(orderId);
		
		// 如果已评价过则返回到已评价的页面
		if (oAppraise.getState() != OrderAppraise.NO) {
			return WebUtils.redirectTo(Globals.getFrontPath(), "/view/", orderId.toString(), ".html");
		}
		
		// 评价页面
		Order order = orderService.getWithOrderItems(orderId);
		model.addAttribute("order", order);
		return "/front/member/OrderAppraise"; 
	}
	
	/**
	 * 追评
	 * @return
	 */
	@RequestMapping("/rform/{orderId}.html")
	public String rform(@PathVariable Long orderId, Model model) {
		OrderAppraise oAppraise = appraiseService.get(orderId);
		
		// 如果已追评过则返回到已评价的页面
		if (oAppraise.getState() != OrderAppraise.DEL_FLAG_DELETE) {
			return WebUtils.redirectTo(Globals.getFrontPath(), "/view/", orderId.toString(), ".html");
		}
				
		List<ProductAppraise> appraises = productAppraiseService.queryByOrder(orderId);
		oAppraise.setAppraises(appraises);
		model.addAttribute("appraise", oAppraise);
		return "/front/member/OrderRappraise"; 
	}
	
	/**
	 * 评论结果
	 * @return
	 */
	@RequestMapping("/view/{orderId}.html")
	public String view(@PathVariable Long orderId, Model model) {
		OrderAppraise oAppraise = appraiseService.get(orderId);
		List<ProductAppraise> appraises = productAppraiseService.queryByOrder(orderId);
		oAppraise.setAppraises(appraises);
		model.addAttribute("appraise", oAppraise);
		return "/front/member/OrderAppraiseView"; 
	}
	
	/**
	 * 提交
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/save.json")
	public AjaxResult save(Long orderId) {
		
		// 当前的状态
		OrderAppraise oAppraise = appraiseService.get(orderId);
		if (oAppraise == null || oAppraise.getState() != OrderAppraise.NO) {
			return AjaxResult.error("此订单已评价！");
		}
		
		// 当前用户
		User cUser = UserUtils.getUser();
		if (!cUser.getId().equals(oAppraise.getCreateId())) {
			return AjaxResult.error("下单用户才能评价");
		}
		
		// 评价的数据
		String postData = WebUtils.getCleanParam("postData");
		List<ProductAppraise> appraises = JsonMapper.fromJsonToList(postData, ProductAppraise.class);
		oAppraise.setAppraises(appraises);
		appraiseService.appraise(cUser, oAppraise);
		return AjaxResult.success();
	}
	
	/**
	 * 提交
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/rsave.json")
	public AjaxResult rsave(Long orderId) {
		
		// 当前的状态
		OrderAppraise oAppraise = appraiseService.get(orderId);
		if (oAppraise == null || oAppraise.getState() != OrderAppraise.DEL_FLAG_DELETE) {
			return AjaxResult.error("此订单已追评！");
		}
		
		// 当前用户
		User cUser = UserUtils.getUser();
		if (!cUser.getId().equals(oAppraise.getCreateId())) {
			return AjaxResult.error("下单用户才能追评");
		}
		
		// 评价的数据
		String postData = WebUtils.getCleanParam("postData");
		List<ProductAppraise> appraises = JsonMapper.fromJsonToList(postData, ProductAppraise.class);
		oAppraise.setAppraises(appraises);
		appraiseService.rappraise(cUser, oAppraise);
		return AjaxResult.success();
	}
}