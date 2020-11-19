package com.tmt.update.impl;

import java.util.List;

import com.tmt.common.utils.Lists;
import com.tmt.common.utils.SpringContextHolder;
import com.tmt.shop.entity.Goods;
import com.tmt.shop.service.GoodsSearcherFacade;
import com.tmt.shop.service.ShopStaticizerFacade;
import com.tmt.shop.utils.GoodsUtils;
import com.tmt.system.entity.UpdateData;

/**
 * 商品更新
 * @author lifeng
 */
public class GoodsUpdateHandler extends StoreUpdateHandler{
	
	private GoodsSearcherFacade goodsSearcher;
	private ShopStaticizerFacade shopStaticizer;
	
	public GoodsUpdateHandler() {
		goodsSearcher = SpringContextHolder.getBean(GoodsSearcherFacade.class);
		shopStaticizer = SpringContextHolder.getBean(ShopStaticizerFacade.class);
	}

	/**
	 * 执行处理
	 */
	@Override
	public void doInnerHandler(List<UpdateData> datas) {
		
		try {
			// 更新索引
			goods_lucene(datas);
		}catch(Exception e){e.printStackTrace();}
		
		try {
			// 静态化
			goods_static(datas);
		}catch(Exception e){e.printStackTrace();}
		
		// 清除缓存
		try {
			if (datas != null && datas.size() > 0) {
				for(UpdateData data: datas) {
					Goods goods = new Goods();
					goods.setId(data.getId());
					GoodsUtils.clear(goods);
				}
			}
		}catch(Exception e){e.printStackTrace();}
	}
	
	// 商品索引
	private void goods_lucene(List<UpdateData> goods) {
		if (goods == null || goods.size() == 0) {
			return ;
		}
		List<Long> updates = Lists.newArrayList();
		List<Long> deletes = Lists.newArrayList();
		for(UpdateData data: goods) {
			// 删除
			if (data.getOpt() == 1) {
				deletes.add(data.getId());
			}
			
			// 更新
			else {
				updates.add(data.getId());
			}
			
			if (deletes.size() >= 15) {
				goodsSearcher._delete(deletes);
				deletes.clear();
			}
			
			if (updates.size() >= 15) {
				goodsSearcher.refresh(updates);
				updates.clear();
			}
		}
		
		// 主键删除
		if (deletes.size() != 0) {
			goodsSearcher._delete(deletes);
		}
		if (updates.size() != 0) {
			goodsSearcher.refresh(updates);
		}
		
		// for gc
		updates.clear();
		deletes.clear();
		updates = null;
		deletes = null;
	}
	
	// 商品静态化
	private void goods_static(List<UpdateData> goods) {
		if (goods == null || goods.size() == 0) {
			return ;
		}
		for(UpdateData data: goods) {
			// 删除
			if (data.getOpt() == 1) {
				shopStaticizer.goods_delete(data.getId());
			}
			
			// 更新
			else {
				shopStaticizer.goods_build(data.getId());
			}
		}
	}
}