package com.tmt.shop.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.shop.entity.Sms;

/**
 * 短信配置 管理
 * @author 超级管理员草集关机四的的封大夫东
 * @date 2017-09-12
 */
@Repository("shopSmsDao")
public class SmsDao extends BaseDaoImpl<Sms,Long> {}
