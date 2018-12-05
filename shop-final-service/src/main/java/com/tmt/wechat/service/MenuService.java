package com.tmt.wechat.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.common.persistence.BaseDao;
import com.tmt.common.persistence.incrementer.IdGen;
import com.tmt.common.service.BaseService;
import com.tmt.common.utils.Lists;
import com.tmt.wechat.bean.base.WechatConstants;
import com.tmt.wechat.dao.MenuDao;
import com.tmt.wechat.entity.Menu;
import com.tmt.wechat.utils.WechatUtils;

/**
 * 自定义菜单 管理
 * 
 * @author 超级管理员
 * @date 2016-09-13
 */
@Service("wechatMenuService")
public class MenuService extends BaseService<Menu, Long> implements MenuServiceFacade {

	@Autowired
	private MenuDao menuDao;

	@Override
	protected BaseDao<Menu, Long> getBaseDao() {
		return menuDao;
	}

	/**
	 * 保存
	 */
	@Transactional
	public void save(Menu template, List<Menu> menus) {
		List<Menu> inserts = Lists.newArrayList();
		int sort = 1;
		for (int i = 0; i < menus.size(); i++) {
			Menu menu = menus.get(i);
			menu.setSort((byte) sort++);
			menu.setStatus((byte) 1); // 默认显示
			menu.setAppId(template.getAppId());
			menu.setCreateId(template.getCreateId());
			menu.setCreateName(template.getCreateName());
			if (IdGen.isInvalidId(menu.getId()) || menu.getId() < 0) { // 设置负数为临时ID
				menu.setId((Long) IdGen.key());
			}

			// 纠正消息
			if (menu.getType() == null) {
				menu.setType(WechatConstants.HANDLER_none);
			}

			inserts.add(menu);

			// 子菜单
			List<Menu> children = menu.getMenus();
			if (children != null && children.size() != 0) {
				for (int j = 0; j < children.size(); j++) {
					Menu child = children.get(j);
					child.setParentId(menu.getId());
					child.setSort((byte) sort++);
					child.setAppId(menu.getAppId());
					child.setStatus((byte) 1); // 默认显示
					child.setAppId(template.getAppId());
					child.setCreateId(template.getCreateId());
					child.setCreateName(template.getCreateName());
					if (IdGen.isInvalidId(child.getId()) || child.getId() < 0) { // 设置负数为临时ID
						child.setId((Long) IdGen.key());
					}

					// 纠正消息
					if (menu.getType() == null) {
						menu.setType(WechatConstants.HANDLER_none);
					}

					inserts.add(child);
				}
			}
		}

		// 以前的数据删除
		List<Menu> olds = this.queryMenusByAppId(menus.get(0).getAppId());
		this.delete(olds);
		this.batchInsert(inserts);

		// 删除缓存
		WechatUtils.clearCache();
	}

	/**
	 * 删除
	 */
	@Transactional
	public void delete(List<Menu> menus) {
		this.batchDelete(menus);

		// 删除缓存
		WechatUtils.clearCache();
	}

	/**
	 * 查询公众号的菜单
	 * 
	 * @param appId
	 * @return
	 */
	public List<Menu> queryMenusByAppId(String appId) {
		return this.queryForList("queryMenusByAppId", appId);
	}
}