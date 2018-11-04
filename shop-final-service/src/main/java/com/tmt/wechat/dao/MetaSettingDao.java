package com.tmt.wechat.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.common.persistence.BaseDaoImpl;
import com.tmt.wechat.entity.MetaSetting;

/**
 * 回复配置 管理
 * @author 超级管理员
 * @date 2016-09-27
 */
@Repository("wechatMetaSettingDao")
public class MetaSettingDao extends BaseDaoImpl<MetaSetting,Long> {}
