package com.tmt.wechat.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.common.persistence.BaseDaoImpl;
import com.tmt.wechat.entity.Qrcode;

/**
 * 二维码 管理
 * @author 超级管理员
 * @date 2016-10-01
 */
@Repository("wechatQrcodeDao")
public class QrcodeDao extends BaseDaoImpl<Qrcode,Long> {}
