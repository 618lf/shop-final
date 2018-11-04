package com.tmt.wechat.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.common.persistence.BaseDaoImpl;
import com.tmt.wechat.entity.Menu;

/**
 * 自定义菜单 管理
 * @author 超级管理员
 * @date 2016-09-13
 */
@Repository("wechatMenuDao")
public class MenuDao extends BaseDaoImpl<Menu,Long> {}
