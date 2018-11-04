package com.tmt.system.service;

import java.util.List;

import com.tmt.system.entity.Todo;
import com.tmt.system.entity.User;

/**
 * 待办服务
 * @author root
 *
 */
public interface TodoServiceFacade {

	/**
	 * 返回需要显示的待办
	 * @return
	 */
	public List<Todo> fetchTodos(User user);
}