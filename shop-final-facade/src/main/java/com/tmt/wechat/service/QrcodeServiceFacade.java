package com.tmt.wechat.service;

import java.util.List;

import com.tmt.common.service.BaseServiceFacade;
import com.tmt.wechat.entity.Qrcode;

/**
 * 二维码 管理
 * @author 超级管理员
 * @date 2016-10-01
 */
public interface QrcodeServiceFacade extends BaseServiceFacade<Qrcode,Long> {
	
	/**
	 * 保存
	 */
	public void save(Qrcode qrcode);
	
	/**
	 * 校验唯一性
	 * @param qrcode
	 * @return
	 */
	public int checkSceneStr(Qrcode qrcode);
	
	/**
	 * 删除
	 */
	public void delete(List<Qrcode> qrcodes);
	
	/**
	 * 根据唯一标识获取二维码
	 * @return
	 */
	public Qrcode getBySceneStr(String qrscene);
	
}