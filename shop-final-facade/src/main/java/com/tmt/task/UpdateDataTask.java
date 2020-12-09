package com.tmt.task;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.tmt.Constants;
import com.tmt.bbs.update.BbsModule;
import com.tmt.cms.update.CmsModule;
import com.tmt.core.utils.Lists;
import com.tmt.core.utils.Maps;
import com.tmt.core.utils.SpringContextHolder;
import com.tmt.core.utils.StringUtils;
import com.tmt.notice.ActualNotice;
import com.tmt.shop.update.ShopModule;
import com.tmt.system.entity.SystemConstant;
import com.tmt.system.entity.Task;
import com.tmt.system.entity.UpdateData;
import com.tmt.system.service.TaskExecutor;
import com.tmt.system.service.UpdateDataServiceFacade;
import com.tmt.update.UpdateHandler;
import com.tmt.update.impl.CategoryUpdateHandler;
import com.tmt.update.impl.GoodsUpdateHandler;
import com.tmt.update.impl.HotspotUpdateHandler;
import com.tmt.update.impl.MetaImageUpdateHandler;
import com.tmt.update.impl.MetaTextUpdateHandler;
import com.tmt.update.impl.MetaUpdatehandler;
import com.tmt.update.impl.MpageUpdateHadler;
import com.tmt.update.impl.PromotionUpdateHandler;
import com.tmt.update.impl.ReplyUpdateHandler;
import com.tmt.update.impl.TopicUpdateHandler;
import com.tmt.wechat.update.WechatModule;

/**
 * 数据更新服务 - 可重入
 * 因为SHOP没分包，所以放入这两更好
 * @author root
 */
public class UpdateDataTask implements TaskExecutor{

	@Autowired
	private UpdateDataServiceFacade updateDataService;
	
	// 处理器
	private UpdateHandler goodsUpdateHandler;
	private UpdateHandler categoryUpdateHandler;
	private UpdateHandler metaUpdatehandler;
	private UpdateHandler metaTextUpdateHandler;
	private UpdateHandler metaImageUpdateHandler;
	private UpdateHandler mpageUpdateHadler;
	private UpdateHandler promotionUpdateHandler;
	private UpdateHandler replyUpdateHandler;
	private UpdateHandler topicHotspotUpdateHandler;
	private UpdateHandler replyHotspotUpdateHandler;
	private UpdateHandler topicUpdateHandler;
	
	public UpdateDataTask() {
		goodsUpdateHandler = new GoodsUpdateHandler();
		categoryUpdateHandler = new CategoryUpdateHandler();
		metaUpdatehandler = new MetaUpdatehandler();
		metaTextUpdateHandler = new MetaTextUpdateHandler();
		metaImageUpdateHandler = new MetaImageUpdateHandler();
		mpageUpdateHadler = new MpageUpdateHadler();
		promotionUpdateHandler = new PromotionUpdateHandler();
		replyUpdateHandler = new ReplyUpdateHandler();
		topicHotspotUpdateHandler = new HotspotUpdateHandler();
		replyHotspotUpdateHandler = new HotspotUpdateHandler();
		topicUpdateHandler = new TopicUpdateHandler();
	}
	
	@Override
	public Boolean doTask(Task task) {
		// 每次更新500条数据(批量的更新)
		List<UpdateData> updates = updateDataService.queryUpdateAbles(500);
		Map<Byte, List<UpdateData>> _updates = Maps.newHashMap();
		for(UpdateData data: updates) {
			List<UpdateData> _values = _updates.get(data.getModule());
			if (_values == null) {
				_values = Lists.newArrayList();
				_updates.put(data.getModule(), _values);
			}
			_values.add(data);
		}
		
		// 1
		goodsUpdateHandler.doHandler(_updates.get(ShopModule.GOODS));
		
		// 2
		categoryUpdateHandler.doHandler(_updates.get(ShopModule.CATEGORY));
		
		// 3
		metaUpdatehandler.doHandler(_updates.get(WechatModule.META));
		
		// 4
		metaTextUpdateHandler.doHandler(_updates.get(WechatModule.META_TEXT));
		
		// 5
		metaImageUpdateHandler.doHandler(_updates.get(WechatModule.META_IMAGE));
		
		// 6
		mpageUpdateHadler.doHandler(_updates.get(CmsModule.MPAGE));
		
		// 7
		promotionUpdateHandler.doHandler(_updates.get(ShopModule.PROMOTION));
		
		// 8
		topicUpdateHandler.doHandler(_updates.get(BbsModule.TOPIC));
		
		// 9
		replyUpdateHandler.doHandler(_updates.get(BbsModule.REPLY));
		
		// 10
		topicHotspotUpdateHandler.doHandler(_updates.get(BbsModule.TOPIC_HOTSPOT));
		
		// 11
		replyHotspotUpdateHandler.doHandler(_updates.get(BbsModule.REPLY_HOTSPOT));
		
		// 127
		List<UpdateData> users = _updates.get(Constants.USER_IN);
		if (users != null && users.size() != 0) {
			ActualNotice anotice = SpringContextHolder.getBean(ActualNotice.class);
			if (anotice != null) {
				for(UpdateData data: users) {
					anotice.sendMessage(StringUtils.format("会员%s登录系统", data.getMsg()));
				}
			}
		}
		
		// 126
		users = _updates.get(Constants.USER_UP);
		if (users != null && users.size() != 0) {
			ActualNotice anotice = SpringContextHolder.getBean(ActualNotice.class);
			if (anotice != null) {
				for(UpdateData data: users) {
					anotice.sendMessage(StringUtils.format("%s注册成为会员", data.getMsg()));
				}
			}
		}
		
		// 处理完成之后删除
		updateDataService.delete(updates);
		// for gc
		updates.clear();
		updates = null;
		return Boolean.TRUE;
	}
	
	@Override
	public String getName() {
		return "数据定时更新";
	}
}