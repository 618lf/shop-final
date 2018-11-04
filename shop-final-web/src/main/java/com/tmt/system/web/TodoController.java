package com.tmt.system.web;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmt.common.entity.AjaxResult;
import com.tmt.common.utils.Lists;
import com.tmt.common.utils.SpringContextHolder;
import com.tmt.common.web.BaseController;
import com.tmt.system.entity.Todo;
import com.tmt.system.entity.User;
import com.tmt.system.service.MessageServiceFacade;
import com.tmt.system.service.TodoServiceFacade;
import com.tmt.system.utils.UserUtils;

/**
 * 系统代办
 * @author root
 */
@Controller
@RequestMapping(value = "${adminPath}/system/todo")
public class TodoController extends BaseController {

	/**
	 * 待办列表
	 * @return
	 */
	@ResponseBody
	@RequestMapping("")
	public AjaxResult todos() {
		User user = UserUtils.getUser();
		List<Todo> todos = Lists.newArrayList();
		Map<String, TodoServiceFacade> todoSvs = SpringContextHolder.getBeans(TodoServiceFacade.class);
		Iterator<TodoServiceFacade> it = todoSvs.values().iterator();
		while (it.hasNext()) {
			TodoServiceFacade todoSv = it.next();
			todos.addAll(todoSv.fetchTodos(user));
		}
		Collections.sort(todos);
		return AjaxResult.success(todos);
	}
	
	/**
	 * 未处理的消息数
	 * @return
	 */
	@ResponseBody
	@RequestMapping("count_unread_msg")
	public Integer unreadMsg() { 
		User user = UserUtils.getUser();
		MessageServiceFacade messageService = SpringContextHolder.getBean(MessageServiceFacade.class);
		return messageService.getUnreadCount(user);
	}
}