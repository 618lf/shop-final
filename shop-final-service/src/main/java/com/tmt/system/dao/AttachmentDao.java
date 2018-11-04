package com.tmt.system.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.common.persistence.BaseDaoImpl;
import com.tmt.system.entity.Attachment;

/**
 * 附件 管理
 * @author 超级管理员
 * @date 2015-07-30
 */
@Repository
public class AttachmentDao extends BaseDaoImpl<Attachment,Long> {}