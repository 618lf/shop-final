package com.tmt.task;

import org.springframework.beans.factory.annotation.Autowired;

import com.tmt.shop.service.GoodsSearcherFacade;
import com.tmt.shop.service.ShopStaticizerFacade;
import com.tmt.system.entity.Task;
import com.tmt.system.service.TaskExecutor;

/**
 * 静态化任务
 * @author root
 */
public class StaticizeTask implements TaskExecutor{

	@Autowired
	private ShopStaticizerFacade shopStaticizer;
	@Autowired
	private GoodsSearcherFacade  goodsSearcher;
	
	@Override
	public Boolean doTask(Task task) {
		// 首页
		shopStaticizer.index_build();
		
		// 搜索页
		shopStaticizer.search_build();
		
		// 所有分类
		shopStaticizer.categorys_build();
		
		// 所有商品
		shopStaticizer.goodss_build();
		
		// 所有图文素材
		shopStaticizer.metas_rich_build();
		
		// 所有文本素材
		shopStaticizer.metas_text_build();
		
		// 所有的商品
		goodsSearcher.goodss_build();
		
		// 所有的微页面
		shopStaticizer.mpages_build();
		
		// 所有的促销
		shopStaticizer.promotions_build();
		
		return null;
	}

	@Override
	public String getName() {
		return "静态化页面";
	}
}
