package com.tmt.shop.front;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmt.core.entity.AjaxResult;
import com.tmt.shop.entity.Product;
import com.tmt.shop.entity.ProductNotify;
import com.tmt.shop.service.ProductNotifyServiceFacade;
import com.tmt.shop.utils.ProductUtils;
import com.tmt.system.entity.User;
import com.tmt.system.utils.UserUtils;

/**
 * 缺货登记
 * @author root
 */
@Controller
@RequestMapping(value = "${frontPath}/member/shop/product")
public class ProductRemindController {

	@Autowired
	private ProductNotifyServiceFacade notifyService;
	
	/**
	 * 登记缺货
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/remind/{id}.json")
	public AjaxResult remind(@PathVariable Long id, HttpServletResponse response) {
		User user = UserUtils.getUser();
		Product product = ProductUtils.getProduct(id);
		ProductNotify notify = new ProductNotify();
		notify.userOptions(user);
		notify.setProductId(product.getId());
		notify.setProductName(product.getName() + product.getTip());
		notifyService.save(notify);
		return AjaxResult.success();
	}
}
