package com.tmt.wechat.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.core.persistence.BaseDao;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.service.BaseService;
import com.tmt.wechat.dao.QrcodeDao;
import com.tmt.wechat.entity.Qrcode;
import com.tmt.wechat.utils.WechatUtils;

/**
 * 二维码 管理
 * @author 超级管理员
 * @date 2016-10-01
 */
@Service("wechatQrcodeService")
public class QrcodeService extends BaseService<Qrcode,Long> implements QrcodeServiceFacade{
	
	@Autowired
	private QrcodeDao qrcodeDao;
	
	@Override
	protected BaseDao<Qrcode, Long> getBaseDao() {
		return qrcodeDao;
	}
	
	/**
	 * 保存
	 */
	@Transactional
	public void save(Qrcode qrcode) {
		if(IdGen.isInvalidId(qrcode.getId())) {
			this.insert(qrcode);
		} else {
			this.update(qrcode);
		}
		
		// 删除缓存
		WechatUtils.clearQrcode(qrcode.getSceneStr());
	}
	
	/**
	 * 删除
	 */
	@Transactional
	public void delete(List<Qrcode> qrcodes) {
		this.batchDelete(qrcodes);
	}

	/**
	 * 根据唯一码获取
	 */
	@Override
	public Qrcode getBySceneStr(String qrscene) {
		return this.queryForObject("getBySceneStr", qrscene);
	}

	@Override
	public int checkSceneStr(Qrcode qrcode) {
		return this.qrcodeDao.countByCondition("checkSceneStr",qrcode);
	}
}