package com.tmt.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.tmt.core.utils.Lists;
import com.tmt.shop.entity.NoticeTask;
import com.tmt.shop.service.NoticeTaskServiceFacade;
import com.tmt.system.entity.Task;
import com.tmt.system.service.TaskExecutor;
import com.tmt.wechat.bean.template.TemplateMessageResult;
import com.tmt.wechat.entity.App;
import com.tmt.wechat.service.WechatOptionService;
import com.tmt.wechat.utils.WechatUtils;

/**
 * 消息通知任务
 * 
 * @author root
 */
public class NoticeTask$Task implements TaskExecutor {

	@Autowired
	private NoticeTaskServiceFacade noticeTaskService;
	@Autowired
	private WechatOptionService wechatService;

	@Override
	public Boolean doTask(Task arg0) {
		// 每次更新500条数据(批量的更新)
		List<NoticeTask> updates = noticeTaskService.queryNoticeAbles(100);

		List<NoticeTask> errors = Lists.newArrayList();
		List<NoticeTask> oks = Lists.newArrayList();
		for (NoticeTask data : updates) {
			Boolean flag = false;
			// 微信模板消息
			if (1 == data.getType()) {
				flag = this.sendTemplate(data);
			}
			// 短信消息
			else if (2 == data.getType()) {
				flag = this.sendSms(data);
			}

			// 发送状态
			if (flag) {
				oks.add(data);
			} else {
				errors.add(data);
			}
		}

		// 发送成功的删除
		noticeTaskService.sendSuccess(oks);
		// 发送失败的重置状态 -- 最多三次
		noticeTaskService.sendFail(errors);
		return Boolean.TRUE;
	}

	// 发送模板消息
	private boolean sendTemplate(NoticeTask notice) {
		try {
			App app = WechatUtils.get(notice.getApp());
			TemplateMessageResult result = wechatService.bind(app).sendTemplateMessage(notice.getMsg());

			// 发送成功后的处理
			if (result != null && "ok".equals(result.getErrmsg())) {
				return Boolean.TRUE;
			}
			return Boolean.FALSE;
		} catch (Exception e) {
			return Boolean.FALSE;
		}
	}

	// 发送模板消息
	private boolean sendSms(NoticeTask notice) {
//		try {
//			return SmsUtils.sendMessage(notice.getApp(), notice.getMsg());
//		}catch(Exception e) {
//			return Boolean.FALSE;
//		}
		return Boolean.FALSE;
	}

	@Override
	public String getName() {
		return "消息通知";
	}
}