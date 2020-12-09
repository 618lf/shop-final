package com.tmt.shop.front;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmt.bbs.entity.TopicMin;
import com.tmt.bbs.service.TopicSearcherFacade;
import com.tmt.bbs.utils.BbsUtils;
import com.tmt.bbs.utils.Stat;
import com.tmt.core.entity.AjaxResult;
import com.tmt.core.utils.Lists;
import com.tmt.core.utils.Maps;
import com.tmt.shop.entity.Complex;
import com.tmt.shop.entity.Goods;
import com.tmt.shop.entity.Product;
import com.tmt.shop.entity.Promotion;
import com.tmt.shop.utils.ComplexUtils;
import com.tmt.shop.utils.GoodsUtils;
import com.tmt.shop.utils.ProductUtils;
import com.tmt.shop.utils.PromotionUtils;

/**
 * 商品预览
 * @author root
 */
@Controller
@RequestMapping(value = "${frontPath}/shop/goods")
public class GoodsController {

	@Autowired
	private TopicSearcherFacade topicSearcher;
	
	/**
	 * 预览
	 * @return
	 */
	@RequestMapping("/{id}.html")
	public String view(@PathVariable Long id, Model model, HttpServletResponse response) {
		Product product = this.getProduct(id);
		if (product != null) {
			model.addAttribute("product", product);
			return "/front/goods/GoodsView";
		}
		try{
			response.sendError(404);
		}catch(Exception e) {}
		return "/front/goods/GoodsView";
	}
	
	/**
	 * 预览
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/{id}.json")
	public AjaxResult view(@PathVariable Long id, HttpServletResponse response) {
		Product product = this.getProduct(id);
		if (product != null) {
			return AjaxResult.success(product);
		}
		return AjaxResult.error("商品不存在");
	}
	
	private Product getProduct(Long id) {
		Product product = ProductUtils.getProduct(id);
		
		// 货品不存在
		if (product == null) {
			return null;
		}
		
		// 商品不存在
		Goods goods = GoodsUtils.get(product.getGoodsId());
		if (goods == null) {
			return null;
		}
		
		// 返回正常的货品
		product.setGoods(goods);
		return product;
	}
	
	/**
	 * 商品合并
	 * @param productId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/mutil/{productId}.json")
	public AjaxResult mutil(@PathVariable Long productId) {
		Map<String, Object> result = Maps.newHashMap();
		
		// 促销
		Map<Long, Promotion> promotons = PromotionUtils.queryProductAllEnabledPromotions(productId);
		result.put("promotons", promotons.values());
		
		// 优惠
		Map<Byte, List<Long>> complexs = ComplexUtils.queryProductEnabledComplexs(productId);
		if (complexs.containsKey((byte)1)) {
			result.put("discounts", Boolean.TRUE);
		}
		if (complexs.containsKey((byte)2)) {
			result.put("hots", Boolean.TRUE);
		}
		
		// 统计项目
		Stat stat = BbsUtils.statSection(productId);
		if (stat != null && stat.getNum() != null && stat.getNum() > 0) {
			TopicMin topic = topicSearcher.newestProduct(productId);
			result.put("stat", stat);
			result.put("topic", topic);
		}
		return AjaxResult.success(result);
	}
	
	/**
	 * 商品相关的促销
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/promotons/{productId}.json")
	public AjaxResult promotons(@PathVariable Long productId) {
		Map<Long, Promotion> promotons = PromotionUtils.queryProductAllEnabledPromotions(productId);
		// 返回促销信息
		return AjaxResult.success(promotons.values());
	}
	
	/**
	 * 商品组合
	 * @param id
	 * @return
	 */
	@RequestMapping("/{type}/{id}.html")
	public String complex(@PathVariable String type, @PathVariable Long id, Model model) {
		Map<Byte, List<Long>> complexs = ComplexUtils.queryProductEnabledComplexs(id);
		List<Complex> coms = Lists.newArrayList();
		List<Long> ids; String title = "";
		if ("disc".equals(type)) {
			ids = complexs.get((byte)1);
			title = "优惠套装";
		} else {
			ids = complexs.get((byte)2);
			title = "人气组合";
		}
		if (ids != null) {
			for(Long _id: ids) {
				coms.add(ComplexUtils.getComplex(_id));
			}
		}
		model.addAttribute("title", title);
		model.addAttribute("complexs", coms);
		return "/front/goods/GoodsComplex"; 
	}
	
	/**
	 * 商品组合
	 * @param type
	 * @param id
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/{type}/{id}.json")
	public List<Complex> complex(@PathVariable String type, @PathVariable Long id) {
		Map<Byte, List<Long>> complexs = ComplexUtils.queryProductEnabledComplexs(id);
		List<Complex> coms = Lists.newArrayList();
		List<Long> ids;
		if ("disc".equals(type)) {
			ids = complexs.get((byte)1);
		} else {
			ids = complexs.get((byte)2);
		}
		if (ids != null) {
			for(Long _id: ids) {
				coms.add(ComplexUtils.getComplex(_id));
			}
		}
		return coms;
	}
}