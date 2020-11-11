package com.tmt.wechat.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.core.persistence.BaseDao;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.service.BaseService;
import com.tmt.core.utils.Lists;
import com.tmt.system.entity.UpdateData;
import com.tmt.update.UpdateServiceFacade;
import com.tmt.wechat.dao.MetaRichDao;
import com.tmt.wechat.dao.MetaRichRelaDao;
import com.tmt.wechat.entity.MetaRich;
import com.tmt.wechat.entity.MetaRichRela;
import com.tmt.wechat.update.WechatModule;
import com.tmt.wechat.utils.WechatUtils;

/**
 * 图文回复 管理
 * @author 超级管理员
 * @date 2016-09-30
 */
@Service("wechatMetaRichService")
public class MetaRichService extends BaseService<MetaRich,Long> implements MetaRichServiceFacade {
	
	@Autowired
	private MetaRichDao metaRichDao;
	@Autowired
	private MetaRichRelaDao relaDao;
	
	// 数据更新
	@Autowired
	private UpdateServiceFacade updateService;
	
	@Override
	protected BaseDao<MetaRich, Long> getBaseDao() {
		return metaRichDao;
	}
	
	/**
	 * 返回相关
	 * @param id
	 * @return
	 */
	public MetaRich getWithRelas(Long id) { 
		MetaRich rich = this.get(id);
		if (rich != null) {
			List<MetaRichRela> relas = relaDao.queryForList("getRelasByRichId", id);
			rich.setRelas(relas);
		}
		return rich;
	}
	
	/**
	 * 保存
	 */
	@Transactional
	public void save(MetaRich metaRich) {
		if(IdGen.isInvalidId(metaRich.getId())) {
			this.insert(metaRich);
		} else {
			this.update(metaRich);
		}
		
		// 子图文
		saveRelas(metaRich);
		
		// 更新其中一个
		this._update(metaRich, (byte)0);
		
		// 删除缓存
		WechatUtils.clearMetaRich(metaRich.getId());
	}
	
	/**
	 * 设置子图文
	 * @param metaRich
	 */
	private void saveRelas(MetaRich metaRich) {
		List<MetaRichRela> relas = metaRich.getRelas();
		byte sort = 1;
		for(MetaRichRela rela: relas) {
			rela.setRichId(metaRich.getId());
			rela.setSort(sort++);
		}
		
		// 删除以前的
		List<MetaRichRela> olds = relaDao.queryForList("getRelasByRichId", metaRich.getId());
		this.relaDao.batchDelete(olds);
		this.relaDao.batchInsert(relas);
	}
	
	/**
	 * 删除
	 */
	@Transactional
	public void delete(List<MetaRich> metaRichs) {
		List<MetaRichRela> all = Lists.newArrayList();
		for(MetaRich rich: metaRichs) {
			List<MetaRichRela> relas = relaDao.queryForList("getRelasByRichId", rich.getId());
			all.addAll(relas);
			this._update(rich, (byte)1);
			
			// 删除缓存
			WechatUtils.clearMetaRich(rich.getId());
		}
		this.batchDelete(metaRichs);
		this.relaDao.batchDelete(all);
	}
	
	// 设置修改项
	private void _update(MetaRich metaRich, byte opt) {
		UpdateData updateData = new UpdateData();
		updateData.setId(metaRich.getId());
		updateData.setModule(WechatModule.META);
		updateData.setOpt(opt);
		updateService.save(updateData);
	}
}