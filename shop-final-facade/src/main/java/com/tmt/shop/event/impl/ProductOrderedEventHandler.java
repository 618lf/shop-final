package com.tmt.shop.event.impl;

import java.util.Map;

import com.tmt.common.staticize.StaticUtils;
import com.tmt.common.utils.FreemarkerUtils;
import com.tmt.common.utils.Maps;
import com.tmt.common.utils.SpringContextHolder;
import com.tmt.common.utils.StringUtil3;
import com.tmt.shop.entity.NoticeSetting;
import com.tmt.shop.entity.NoticeTask;
import com.tmt.shop.entity.OrderEvent;
import com.tmt.shop.entity.Product;
import com.tmt.shop.entity.Store;
import com.tmt.shop.event.EventHandler;
import com.tmt.shop.service.NoticeSettingServiceFacade;
import com.tmt.shop.service.NoticeTaskServiceFacade;
import com.tmt.shop.service.ProductServiceFacade;
import com.tmt.shop.utils.StoreUtils;
import com.tmt.system.entity.Message;
import com.tmt.system.entity.User;
import com.tmt.system.service.MessageServiceFacade;
import com.tmt.system.service.SystemServiceFacade;
import com.tmt.wechat.entity.App;
import com.tmt.wechat.utils.WechatUtils;

/**
 * 商品到货提醒
 * @author lifeng
 */
public class ProductOrderedEventHandler implements EventHandler{

	private ProductServiceFacade productService;
	private NoticeSettingServiceFacade noticeSettingService;
	private SystemServiceFacade systemService;
	private NoticeTaskServiceFacade noticeTaskService;
	private MessageServiceFacade messageService;
	
	public ProductOrderedEventHandler() {
		productService = SpringContextHolder.getBean(ProductServiceFacade.class);
		noticeSettingService = SpringContextHolder.getBean(NoticeSettingServiceFacade.class);
		noticeTaskService = SpringContextHolder.getBean(NoticeTaskServiceFacade.class);
		messageService = SpringContextHolder.getBean(MessageServiceFacade.class);
		systemService = SpringContextHolder.getBean(SystemServiceFacade.class);
	}
	
	@Override
	public Boolean doHandler(OrderEvent event) {
		try {
			
			// 商品
			Product product = productService.get(event.getId());
			
			// 支付成功
			NoticeSetting setting = noticeSettingService.get((byte)98);
			
			// 基本消息
			Map<String, Object> root = Maps.newHashMap();
			root.put("product", product);
			
			// 模板消息
			if (setting.getTemplateMsg() == 1) {
				tempalet_msg(setting.getTmTemplate(), product, root);
			}
			
			// 站内信
			if (setting.getSiteMsg() == 1) {
				site_msg(setting.getSmTemplate(), product, root);
			}
		}catch(Exception e) {}
		
		return Boolean.TRUE;
	}
	
	/**
	 * 暂时获取时这样处理
	 * 支付的模板消息
	 * @param template
	 * @param order
	 * @param root
	 */
	protected void tempalet_msg(String template, Product order, Map<String, Object> root) {
		Store store = StoreUtils.getDefaultStore();
		App app = WechatUtils.get(store.getWxApp());
		User user = new User(); user.setId(order.getCreateId());
		String openId = systemService.getUserWechatOpenId(user, app.getId());
        root.put("touser", openId);
        root.put("url", StaticUtils.touchStaticizePage(app, "product", order));
		if (StringUtil3.isNotBlank(openId)) {
			String msgString = FreemarkerUtils.processNoTemplate(template, root);
		    NoticeTask noticeTask = new NoticeTask();
		    noticeTask.setApp(app.getId());
		    noticeTask.setMsg(msgString);
		    noticeTask.setType((byte)1);
		    noticeTask.setState(NoticeTask.NO);
		    noticeTaskService.save(noticeTask);
		}
	}
	
	/**
	 * 站内信
	 * @param template
	 * @param order
	 * @param root
	 */
	protected void site_msg(String template, Product order, Map<String, Object> root) {
		Store store = StoreUtils.getDefaultStore();
		User to = systemService.getUserById(order.getCreateId());
		String content = FreemarkerUtils.processNoTemplate(template, root);
		String url = StaticUtils.touchStaticizePage(store, "product", order);
		Message _template = new Message();
		_template.setReceiverUserId(to.getId());
		_template.setReceiverUserName(to.getName());
		_template.setContent(content);
		_template.setTitle(content);
		_template.setRelaTitle(content);
		_template.setRelaUrl(url);
		_template.setSendUserId(null);
		_template.setSendUserName("系统通知");
		messageService.send(_template);
	}

	@Override
	public void setNextHandler(EventHandler handler) {}
}
