package com.tmt.wechat.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.wechat.entity.MetaRich;

/**
 * 图文回复 管理
 * @author 超级管理员
 * @date 2016-09-30
 */
@Repository("wechatMetaRichDao")
public class MetaRichDao extends BaseDaoImpl<MetaRich,Long> {}
