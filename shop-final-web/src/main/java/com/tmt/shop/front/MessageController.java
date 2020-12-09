package com.tmt.shop.front;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmt.core.entity.AjaxResult;
import com.tmt.core.persistence.Page;
import com.tmt.core.persistence.PageParameters;
import com.tmt.core.persistence.QueryCondition;
import com.tmt.core.persistence.QueryCondition.Criteria;
import com.tmt.system.entity.Message;
import com.tmt.system.entity.Message.MessageBox;
import com.tmt.system.entity.User;
import com.tmt.system.service.MessageServiceFacade;
import com.tmt.system.utils.UserUtils;

/**
 * 消息
 * @author root
 */
@Controller("frontMessageController")
@RequestMapping(value = "${frontPath}/member/message")
public class MessageController {

	@Autowired
	private MessageServiceFacade messageService;
	
	/**
	 * 列表
	 * @return
	 */
	@RequestMapping("/list.html")
	public String list(Model model) {
		return "/front/member/MessageList";
	}
	
	/**
	 * 列表数据
	 * @param page
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/list/data.json")
	public Page page(String queryStatus, Page page) {
		QueryCondition qc = new QueryCondition();
		Criteria c = qc.getCriteria();
		c.andEqualTo("OWN", UserUtils.getUser().getId());
		c.andEqualTo("MSG_BOX", MessageBox.IN);
		qc.setOrderByClause("STATUS, SEND_TIME DESC");
		PageParameters param = page.getParam();
		param.setPageSize(20);
		// 填充明细
		return this.messageService.queryForPage(qc, param);
	}
	
	/**
	 * 未读消息数量
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/unread_count.json")
	public AjaxResult unread_count() {
		User user = UserUtils.getUser();
		return AjaxResult.success(messageService.getUnreadCount(user));
	}
	
	/**
	 * 已读 不需要返回
	 */
	@ResponseBody
	@RequestMapping("/read/{id}.json")
	public void read(@PathVariable Long id) {
		Message message = this.messageService.get(id);
		messageService.read(message);
	}
}