package com.tmt.system.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmt.core.entity.AjaxResult;
import com.tmt.core.persistence.Page;
import com.tmt.core.persistence.PageParameters;
import com.tmt.core.persistence.QueryCondition;
import com.tmt.core.persistence.QueryCondition.Criteria;
import com.tmt.core.utils.Lists;
import com.tmt.core.utils.StringUtils;
import com.tmt.core.web.BaseController;
import com.tmt.system.entity.Message;
import com.tmt.system.entity.Message.MessageBox;
import com.tmt.system.service.MessageServiceFacade;
import com.tmt.system.utils.UserUtils;

/**
 * 站内信息管理，基础
 * 
 * @author lifeng
 */
public class BaseMessageController extends BaseController {

	@Autowired
	protected MessageServiceFacade messageService;

	/**
	 * 收件箱
	 * 
	 * @param guestBook
	 * @param model
	 * @param page
	 * @return
	 */
	@ResponseBody
	@RequestMapping("jSonInList")
	public Page jSonInList(Message message, Model model, Page page) {
		QueryCondition qc = new QueryCondition();
		PageParameters param = page.getParam();
		Criteria c = qc.getCriteria();
		if (StringUtils.isNotBlank(message.getTitle())) {
			c.andLike("TITLE", message.getTitle());
		}
		c.andEqualTo("RECEIVER_USER_ID", UserUtils.getUser().getId());
		c.andEqualTo("OWN", UserUtils.getUser().getId());
		c.andEqualTo("MSG_BOX", MessageBox.IN);
		qc.setOrderByClause("SEND_TIME DESC");
		return dealwithPage(messageService.queryForPage(qc, param));
	}

	/**
	 * 发件箱
	 * 
	 * @param guestBook
	 * @param model
	 * @param page
	 * @return
	 */
	@ResponseBody
	@RequestMapping("jSonOutList")
	public Page jSonOutList(Message message, Model model, Page page) {
		QueryCondition qc = new QueryCondition();
		PageParameters param = page.getParam();
		Criteria c = qc.getCriteria();
		if (StringUtils.isNotBlank(message.getTitle())) {
			c.andLike("TITLE", message.getTitle());
		}
		c.andEqualTo("SEND_USER_ID", UserUtils.getUser().getId());
		c.andEqualTo("OWN", UserUtils.getUser().getId());
		c.andEqualTo("MSG_BOX", MessageBox.OUT);
		qc.setOrderByClause("SEND_TIME DESC");
		return dealwithPage(messageService.queryForPage(qc, param));
	}

	/**
	 * 草稿箱
	 * 
	 * @param guestBook
	 * @param model
	 * @param page
	 * @return
	 */
	@ResponseBody
	@RequestMapping("jSonDraftList")
	public Page jSonDraftList(Message message, Model model, Page page) {
		QueryCondition qc = new QueryCondition();
		PageParameters param = page.getParam();
		Criteria c = qc.getCriteria();
		if (StringUtils.isNotBlank(message.getTitle())) {
			c.andLike("TITLE", message.getTitle());
		}
		c.andEqualTo("SEND_USER_ID", UserUtils.getUser().getId());
		c.andEqualTo("OWN", UserUtils.getUser().getId());
		c.andEqualTo("MSG_BOX", MessageBox.DRAFT);
		qc.setOrderByClause("SEND_TIME DESC");
		return dealwithPage(messageService.queryForPage(qc, param));
	}

	/**
	 * 垃圾箱
	 * 
	 * @param guestBook
	 * @param model
	 * @param page
	 * @return
	 */
	@ResponseBody
	@RequestMapping("jSonTrashList")
	@SuppressWarnings("deprecation")
	public Page jSonTrashList(Message message, Model model, Page page) {
		QueryCondition qc = new QueryCondition();
		PageParameters param = page.getParam();
		Criteria c = qc.getCriteria();
		c.andConditionSql(new StringBuilder().append("(SEND_USER_ID ='").append(UserUtils.getUser().getId()).append("'")
				.append(" OR RECEIVER_USER_ID = '").append(UserUtils.getUser().getId()).append("')").toString());
		c.andEqualTo("OWN", UserUtils.getUser().getId());
		c.andEqualTo("MSG_BOX", MessageBox.TRASH);
		if (StringUtils.isNotBlank(message.getTitle())) {
			c.andLike("TITLE", message.getTitle());
		}
		qc.setOrderByClause("SEND_TIME DESC");
		return dealwithPage(messageService.queryForPage(qc, param));
	}

	/**
	 * 移动到垃圾箱
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("toTrash")
	public AjaxResult toTrash(Long[] idList) {
		if (idList != null && idList.length != 0) {
			List<Message> messages = Lists.newArrayList();
			for (Long id : idList) {
				Message message = new Message();
				message.setId(id);
				message.setMsgBox(MessageBox.TRASH);
				messages.add(message);
			}
			this.messageService.toTrash(messages);
		}
		return AjaxResult.success();
	}

	/**
	 * 移动到垃圾箱
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("read")
	public AjaxResult read(Long id) {
		Message message = new Message();
		message.setId(id);
		this.messageService.read(message);
		return AjaxResult.success();
	}

	/**
	 * 垃圾箱中的删除
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("delete")
	public AjaxResult delete(Long[] idList) {
		if (idList != null && idList.length != 0) {
			List<Message> messages = Lists.newArrayList();
			for (Long id : idList) {
				Message message = new Message();
				message.setId(id);
				messages.add(message);
			}
			this.messageService.delete(messages);
		}
		return AjaxResult.success();
	}

	protected Page dealwithPage(Page page) {
		List<Message> messages = page.getData();
		dealwithMessages(messages);
		return page;
	}

	protected List<Message> dealwithMessages(List<Message> messages) {
		if (messages != null && !messages.isEmpty()) {
			for (Message message : messages) {
				String content = message.getContent();
				message.setContent(StringUtils.abbrHtml(content, 200));
			}
		}
		return messages;
	}
}