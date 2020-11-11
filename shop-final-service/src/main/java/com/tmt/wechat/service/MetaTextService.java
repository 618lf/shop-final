package com.tmt.wechat.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.core.persistence.BaseDao;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.service.BaseService;
import com.tmt.system.entity.UpdateData;
import com.tmt.update.UpdateServiceFacade;
import com.tmt.wechat.dao.MetaTextDao;
import com.tmt.wechat.entity.MetaText;
import com.tmt.wechat.update.WechatModule;

/**
 * 文本回复 管理
 * @author 超级管理员
 * @date 2016-09-30
 */
@Service("wechatMetaTextService")
public class MetaTextService extends BaseService<MetaText,Long> implements MetaTextServiceFacade {
	
	@Autowired
	private MetaTextDao metaTextDao;
	
	// 数据更新
	@Autowired
	private UpdateServiceFacade updateService;
	
	@Override
	protected BaseDao<MetaText, Long> getBaseDao() {
		return metaTextDao;
	}
	
	/**
	 * 保存
	 */
	@Transactional
	public void save(MetaText metaText) {
		if(IdGen.isInvalidId(metaText.getId())) {
			this.insert(metaText);
		} else {
			this.update(metaText);
		}
		
		// 更新其中一个
		this._update(metaText, (byte)0);
	}
	
	/**
	 * 删除
	 */
	@Transactional
	public void delete(List<MetaText> metaTexts) {
		for(MetaText rich: metaTexts) {
			this._update(rich, (byte)1);
		}
		this.batchDelete(metaTexts);
	}
	
	// 设置修改项
	private void _update(MetaText metaRich, byte opt) {
		UpdateData updateData = new UpdateData();
		updateData.setId(metaRich.getId());
		updateData.setModule(WechatModule.META_TEXT);
		updateData.setOpt(opt);
		updateService.save(updateData);
	}
}