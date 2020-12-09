package com.tmt.resolver;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tmt.core.web.SimpleFreeMarkerView;
import com.tmt.shop.entity.Store;
import com.tmt.shop.utils.StoreUtils;

/**
 * 添加对 store 的支持
 * @author lifeng
 */
public class StoreFreemarkerView extends SimpleFreeMarkerView {

	/**
	 * 添加店铺
	 */
	@SuppressWarnings({ "rawtypes", "unchecked"})
	@Override
	protected void renderMergedTemplateModel(Map model, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		// 基本参数
		Store store = StoreUtils.getDefaultStore();
		model.put("ser", store);
		
		// 实例化模板
		getConfiguration().getTemplate(getUrl()).process(model, response.getWriter());
	}
}