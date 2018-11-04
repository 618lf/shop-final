package com.tmt.wechat.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.common.persistence.BaseDaoImpl;
import com.tmt.wechat.entity.MetaText;

/**
 * 文本回复 管理
 * @author 超级管理员
 * @date 2016-09-30
 */
@Repository("wechatMetaTextDao")
public class MetaTextDao extends BaseDaoImpl<MetaText,Long> {}
