package com.tmt.wechat.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.wechat.entity.MetaImage;

/**
 * 图片 管理
 * @author 超级管理员
 * @date 2017-01-12
 */
@Repository("wechatMetaImageDao")
public class MetaImageDao extends BaseDaoImpl<MetaImage,Long> {}
