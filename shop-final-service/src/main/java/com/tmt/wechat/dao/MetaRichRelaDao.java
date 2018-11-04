package com.tmt.wechat.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.common.persistence.BaseDaoImpl;
import com.tmt.wechat.entity.MetaRichRela;

/**
 * 子图文 管理
 * @author 超级管理员
 * @date 2016-09-30
 */
@Repository("wechatMetaRichRelaDao")
public class MetaRichRelaDao extends BaseDaoImpl<MetaRichRela,Long> {}
