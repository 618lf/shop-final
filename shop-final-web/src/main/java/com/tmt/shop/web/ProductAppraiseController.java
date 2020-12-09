package com.tmt.shop.web;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmt.core.entity.AjaxResult;
import com.tmt.core.persistence.Page;
import com.tmt.core.persistence.PageParameters;
import com.tmt.core.persistence.QueryCondition;
import com.tmt.core.web.BaseController;
import com.tmt.shop.entity.OrderAppraise;
import com.tmt.shop.entity.ProductAppraise;
import com.tmt.shop.entity.ProductAppraiseContent;
import com.tmt.shop.service.OrderAppraiseServiceFacade;
import com.tmt.shop.service.ProductAppraiseServiceFacade;

/**
 * 商品评价 管理
 * @author 超级管理员
 * @date 2017-04-12
 */
@Controller("shopProductAppraiseController")
@RequestMapping(value = "${adminPath}/shop/product/appraise")
public class ProductAppraiseController extends BaseController{
	
	@Autowired
	private ProductAppraiseServiceFacade productAppraiseService;
	@Autowired
	private OrderAppraiseServiceFacade orderAppraiseService;
	
	/**
	 * 列表初始化页面
	 * @param model
	 */
	@RequestMapping("list")
	public String list(ProductAppraise productAppraise, Model model){
		return "/shop/ProductAppraiseList";
	}
	
	/**
	 * 列表页面的数据 
	 * @param productAppraise
	 * @param model
	 * @param page
	 * @return Page Json
	 */
	@ResponseBody
	@RequestMapping("page")
	public Page page(ProductAppraise productAppraise, Model model, Page page){
        QueryCondition qc = new QueryCondition();
		PageParameters param = page.getParam();
		Criteria c = qc.getCriteria();
		             this.initQc(productAppraise, c);
		qc.setOrderByClause("CREATE_DATE DESC");
	    page = productAppraiseService.queryForPage(qc, param);
	    List<ProductAppraise> appraises = page.getData();
	    for(ProductAppraise appraise: appraises) {
	    	ProductAppraiseContent content = productAppraiseService.getContent(appraise.getId());
	    	appraise.setPcontent(content);
	    }
		return page;
	}
	
	/**
	 * 表单
	 * @param productAppraise
	 * @param model
	 */
	@ResponseBody
	@RequestMapping("form")
	public ProductAppraise form(ProductAppraise productAppraise) {
		productAppraise = this.productAppraiseService.get(productAppraise.getId());
		ProductAppraiseContent content = productAppraiseService.getContent(productAppraise.getId());
		OrderAppraise oAppraise = orderAppraiseService.get(productAppraise.getOrderId());
		productAppraise.setPcontent(content);
		productAppraise.setState(oAppraise.getState());
		return productAppraise;
	}
	
	/**
	 * 保存
	 * @param category
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@ResponseBody
	@RequestMapping("save")
	public AjaxResult save(ProductAppraise productAppraise) {
		// 没有追评则无追评的内容
		OrderAppraise oAppraise = orderAppraiseService.get(productAppraise.getOrderId());
		if (productAppraise.getState() != oAppraise.getState()) {
			return AjaxResult.error("状态不一致");
		}
		// 如果没有追评，则不能修改追评内容
		if (oAppraise.getState() != OrderAppraise.DEL_FLAG_AUDIT) {
			productAppraise.setAddContent(null);
		} else if(StringUtils.isBlank(productAppraise.getAddContent())){
			return AjaxResult.error("追评内容不能为空");
		}
		this.productAppraiseService.save(productAppraise);
		return AjaxResult.success();
	}
	
	/**
	 * 赠送积分（只能累加）
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/points/{id}")
	public AjaxResult points(@PathVariable Long id, Integer points, Boolean mode) {
		ProductAppraise appraise = this.productAppraiseService.get(id);
		appraise.setPoints(points);
		boolean flag = productAppraiseService.addPoints(appraise, mode);
		if (flag) {
			return AjaxResult.success();
		}
		return AjaxResult.error("已经增送过积分，如需再次赠送，请关闭首次保护锁");
	}
	
	//查询条件
	private void initQc(ProductAppraise productAppraise, Criteria c) {
        if (StringUtils.isNotBlank(productAppraise.getProductName())) {
            c.andLike("PRODUCT_NAME", productAppraise.getProductName());
        }
        if (productAppraise.getOrderId() != null) {
            c.andEqualTo("ORDER_ID", productAppraise.getOrderId());
        }
        if (StringUtils.isNotBlank(productAppraise.getTags())) {
            c.andLike("TAGS", productAppraise.getTags());
        }
        if (productAppraise.getProductGrade() != null) {
        	c.andEqualTo("PRODUCT_GRADE", productAppraise.getProductGrade());
        }
        if (productAppraise.getPackageGrade() != null) {
        	c.andEqualTo("PACKAGE_GRADE", productAppraise.getPackageGrade());
        }
        if (productAppraise.getDeliveryGrade() != null) {
        	c.andEqualTo("DELIVERY_GRADE", productAppraise.getDeliveryGrade());
        }
        if (productAppraise.getCreateDate()!= null) {
        	Date startDate = DateUtils.getDayFirstTime(productAppraise.getCreateDate());
        	Date endDate = DateUtils.getDayLastTime(productAppraise.getCreateDate());
        	c.andDateBetween("CREATE_DATE", startDate, endDate);
        }
        if (productAppraise.getPoints() != null && productAppraise.getPoints() == ProductAppraise.YES) {
        	c.andIsNotNull("POINTS");
        } else if(productAppraise.getPoints() != null) {
        	c.andIsNull("POINTS");
        }
	}
}