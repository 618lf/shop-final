package com.tmt.shop.event.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tmt.core.config.Globals;
import com.tmt.core.utils.FreemarkerUtils;
import com.tmt.core.utils.JsonMapper;
import com.tmt.core.utils.SpringContextHolder;
import com.tmt.core.utils.StringUtils;
import com.tmt.shop.entity.NoticeTask;
import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.OrderEvent;
import com.tmt.shop.event.EventHandler;
import com.tmt.shop.service.NoticeSettingServiceFacade;
import com.tmt.shop.service.NoticeTaskServiceFacade;
import com.tmt.shop.service.OrderServiceFacade;
import com.tmt.shop.service.ShopStaticizerFacade;
import com.tmt.system.entity.Message;
import com.tmt.system.entity.User;
import com.tmt.system.service.MessageServiceFacade;
import com.tmt.system.service.SystemServiceFacade;
import com.tmt.wechat.entity.App;
import com.tmt.wechat.utils.WechatUtils;

/**
 * 默认的处理器
 * 
 * @author root
 *
 */
public abstract class DefaultEventHandler implements EventHandler {

	// 相关服务
	protected OrderServiceFacade orderService;
	protected ShopStaticizerFacade shopStaticizer;
	protected NoticeSettingServiceFacade noticeSettingService;
	protected NoticeTaskServiceFacade noticeTaskService;
	protected MessageServiceFacade messageService;
	protected SystemServiceFacade systemService;

	protected Logger logger = LoggerFactory.getLogger(EventHandler.class);

	protected EventHandler handler;

	/**
	 * 初始化服务
	 */
	public DefaultEventHandler() {
		orderService = SpringContextHolder.getBean(OrderServiceFacade.class);
		shopStaticizer = SpringContextHolder.getBean(ShopStaticizerFacade.class);
		noticeSettingService = SpringContextHolder.getBean(NoticeSettingServiceFacade.class);
		noticeTaskService = SpringContextHolder.getBean(NoticeTaskServiceFacade.class);
		messageService = SpringContextHolder.getBean(MessageServiceFacade.class);
		systemService = SpringContextHolder.getBean(SystemServiceFacade.class);
	}

	@Override
	public Boolean doHandler(OrderEvent event) {
		Boolean flag = true;
		try {
			flag = this.doInnerHandler(event);
		} catch (Exception e) {
			logger.error(JsonMapper.toJson(event));
		}

		// 为true 则不执行下面的处理
		if (flag) {
			return flag;
		}

		// 有下一个处理器，则处理
		if (this.handler != null) {
			return this.handler.doHandler(event);
		}

		// 没有找到处理器
		return false;
	}

	@Override
	public void setNextHandler(EventHandler handler) {
		this.handler = handler;
	}

	/**
	 * 子类需要实现的处理器
	 * 
	 * @param event
	 * @return
	 */
	protected abstract Boolean doInnerHandler(OrderEvent event);

	/**
	 * 支付的模板消息
	 * 
	 * @param template
	 * @param order
	 * @param root
	 */
	protected void tempalet_msg(String template, Order order, Map<String, Object> root) {
		App app = WechatUtils.get(order.getShopId());
		User user = new User();
		user.setId(order.getCreateId());
		String openId = systemService.getUserWechatOpenId(user, app.getId());
		String domain = app.getDomain();
		domain = StringUtils.isBlank(domain) ? Globals.domain : new StringBuilder("http://").append(domain).toString();
		root.put("touser", openId);
		root.put("url", new StringBuilder(domain).append(Globals.frontPath).append("/member/shop/order/view/")
				.append(order.getId()).append(".html").toString());
		if (StringUtils.isNotBlank(openId)) {
			String msgString = FreemarkerUtils.processNoTemplate(template, root);
			NoticeTask noticeTask = new NoticeTask();
			noticeTask.setApp(app.getId());
			noticeTask.setMsg(msgString);
			noticeTask.setType((byte) 1);
			noticeTask.setState(NoticeTask.NO);
			noticeTaskService.save(noticeTask);
		}
	}

	/**
	 * 站内信
	 * 
	 * @param template
	 * @param order
	 * @param root
	 */
	protected void site_msg(String template, Order order, Map<String, Object> root) {
		User to = systemService.getUserById(order.getCreateId());
		String content = FreemarkerUtils.processNoTemplate(template, root);
		String url = new StringBuilder(Globals.frontPath).append("/member/shop/order/view/").append(order.getId())
				.append(".html").toString();
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

	// 短信提醒
	protected void sms_msg(String template, Order order, String phone, Map<String, Object> root) {
		String content = FreemarkerUtils.processNoTemplate(template, root);

		// 唯一消息
		NoticeTask noticeTask = new NoticeTask();
		noticeTask.setApp(phone);
		noticeTask.setMsg(content);
		noticeTask.setType((byte) 2);
		noticeTask.setState(NoticeTask.NO);
		noticeTaskService.save(noticeTask);
	}
}
