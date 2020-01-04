package com.tmt.system.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.core.entity.BaseEntity;
import com.tmt.core.persistence.BaseDao;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.service.BaseService;
import com.tmt.core.utils.Lists;
import com.tmt.core.utils.Maps;
import com.tmt.core.utils.time.DateUtils;
import com.tmt.system.dao.MessageDao;
import com.tmt.system.entity.Message;
import com.tmt.system.entity.Todo;
import com.tmt.system.entity.User;

@Service
public class MessageService extends BaseService<Message, Long> implements MessageServiceFacade, TodoServiceFacade {

	@Autowired
	private MessageDao messageDao;

	@Override
	protected BaseDao<Message, Long> getBaseDao() {
		return messageDao;
	}

	/**
	 * 保存
	 * 
	 * @param message
	 */
	@Transactional
	public void save(Message message) {
		if (IdGen.isInvalidId(message.getId())) {
			this.insert(message);
		} else {
			this.update(message);
		}
	}

	/**
	 * 发送站内信 message -- 已经具有需要的信息
	 * 
	 * @param message
	 */
	@Transactional
	public void send(Message template) {
		List<Message> messages = Lists.newArrayList();
		if (template.getSendUserId() != null) {
			messages.add(template.out());
		}
		messages.add(template.in());
		this.batchInsert(messages);
		// 删除草稿箱的信息
		if (!IdGen.isInvalidId(template.getId())) {
			this.delete(template);
		}
	}

	/**
	 * 批量移到垃圾箱
	 * 
	 * @param messages
	 */
	@Transactional
	public void toTrash(List<Message> messages) {
		this.batchUpdate("UpdateToTrash", messages);
	}

	/**
	 * 批量移到垃圾箱
	 * 
	 * @param messages
	 */
	@Transactional
	public void read(Message message) {
		message.setStatus(BaseEntity.YES);
		message.setViewTime(DateUtils.getTimeStampNow());
		this.update("UpdateToRead", message);
	}

	/**
	 * 垃圾箱批量删除
	 * 
	 * @param messages
	 */
	@Transactional
	public void delete(List<Message> messages) {
		this.batchDelete(messages);
	}

	/**
	 * 当前用户未读站内信
	 * 
	 * @return
	 */
	public Integer getUnreadCount(User user) {
		return this.countByCondition("queryUnreadCount", user.getId());
	}

	public List<Message> queryUnReadTops(User user, int size) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("RECEIVER_USER_ID", user.getId());
		return this.queryForLimitList("queryUnreadList", params, size);
	}

	/**
	 * 我的收件箱
	 */
	@Override
	public List<Todo> fetchTodos(User user) {
		int count = getUnreadCount(user);
		List<Todo> todos = Lists.newArrayList();
		Todo todo = Todo.normal("站内信", "system/message/inBox", count, 2);
		todos.add(todo);
		return todos;
	}
}