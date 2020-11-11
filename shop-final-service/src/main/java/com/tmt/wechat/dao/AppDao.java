package com.tmt.wechat.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.wechat.entity.App;

/**
 * 微信公众号 管理
 * @author 超级管理员
 * @date 2016-09-04
 */
@Repository("wechatAppDao")
public class AppDao extends BaseDaoImpl<App,String> {}
