package com.tmt.bbs.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.bbs.entity.Section;
import com.tmt.core.persistence.BaseDaoImpl;

/**
 * 主题分类 管理
 * @author 超级管理员
 * @date 2017-04-12
 */
@Repository("bbsSectionDao")
public class SectionDao extends BaseDaoImpl<Section,Long> {}
