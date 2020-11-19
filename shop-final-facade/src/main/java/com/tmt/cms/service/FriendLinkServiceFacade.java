package com.tmt.cms.service;

import java.util.List;

import com.tmt.cms.entity.FriendLink;
import com.tmt.core.service.BaseServiceFacade;

/**
 * 友情链接 管理
 * @author 超级管理员
 * @date 2016-07-20
 */
public interface FriendLinkServiceFacade extends BaseServiceFacade<FriendLink,Long> {
	
	/**
	 * 保存
	 */
	public void save(FriendLink friendLink);
	
	/**
	 * 删除
	 */
	public void delete(List<FriendLink> friendLinks);
	
}
