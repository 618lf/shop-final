package com.tmt.shop.dao;

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.shop.entity.NoticeSetting;

/**
 * 通知设置 管理
 * @author 超级管理员
 * @date 2016-10-04
 */
@Repository("shopNoticeSettingDao")
public class NoticeSettingDao extends BaseDaoImpl<NoticeSetting,Byte> {}
