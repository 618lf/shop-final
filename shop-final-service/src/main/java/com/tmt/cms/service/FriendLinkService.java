package com.tmt.cms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.cms.dao.FriendLinkDao;
import com.tmt.cms.entity.FriendLink;
import com.tmt.core.persistence.BaseDao;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.service.BaseService;

/**
 * 友情链接 管理
 * @author 超级管理员
 * @date 2016-07-20
 */
@Service("cmsFriendLinkService")
public class FriendLinkService extends BaseService<FriendLink,Long> implements FriendLinkServiceFacade{
	
	@Autowired
	private FriendLinkDao friendLinkDao;
	
	@Override
	protected BaseDao<FriendLink, Long> getBaseDao() {
		return friendLinkDao;
	}
	
	/**
	 * 保存
	 */
	@Transactional
	public void save(FriendLink friendLink) {
		if(IdGen.isInvalidId(friendLink.getId())) {
			this.insert(friendLink);
		} else {
			this.update(friendLink);
		}
	}
	
	
	/**
	 * 删除
	 */
	@Transactional
	public void delete(List<FriendLink> friendLinks) {
		this.batchDelete(friendLinks);
	}
	
}
