package com.tmt.wechat.handler.impl;

import java.util.List;

import com.tmt.common.staticize.DomainSer;
import com.tmt.common.staticize.StaticUtils;
import com.tmt.common.utils.Lists;
import com.tmt.common.utils.SpringContextHolder;
import com.tmt.wechat.bean.base.Article;
import com.tmt.wechat.bean.base.WechatConfig;
import com.tmt.wechat.bean.base.WechatConstants;
import com.tmt.wechat.bean.message.MsgHead;
import com.tmt.wechat.bean.message.RespMsg;
import com.tmt.wechat.bean.message.RespMsgNews;
import com.tmt.wechat.entity.MetaRich;
import com.tmt.wechat.entity.MetaRichRela;
import com.tmt.wechat.handler.Handler;
import com.tmt.wechat.service.MetaRichServiceFacade;
import com.tmt.wechat.utils.WechatUtils;

/**
 * 图文处理
 * @author root
 */
public class RichHandler implements Handler{

	Handler handler;
	
	@Override
	public RespMsg doHandler(MsgHead request, WechatConfig app, Byte type, String config) {
		if (type == WechatConstants.HANDLER_rich) {
			Long richId = Long.parseLong(config);
			List<Article> articles = WechatUtils.getCacheMetaRich(richId);
			if (articles == null) {
				MetaRichServiceFacade richService = SpringContextHolder.getBean(MetaRichServiceFacade.class);
				MetaRich metaRich = richService.getWithRelas(richId);
				
				// 必须也实现DomainSer
				DomainSer ser = (DomainSer)app;
				
				articles = Lists.newArrayList();
				
				// 静态化的地址
				String url = StaticUtils.touchStaticizePage(ser, "metaRich", metaRich);
				Article article = new Article(metaRich.getTitle(), metaRich.getRemarks(), StaticUtils.staticDomain(ser).append(metaRich.getImage()).toString(), url);
				articles.add(article);
				
				// 大小限制
				int size = 9;
				
				// 子图文
				List<MetaRichRela> relas = metaRich.getRelas();
				for(MetaRichRela rela: relas) {
					MetaRich _MetaRich = richService.getWithRelas(rela.getRelaId());
					url = StaticUtils.touchStaticizePage(ser, "metaRich", _MetaRich);
					article = new Article(_MetaRich.getTitle(), _MetaRich.getRemarks(), StaticUtils.staticDomain(ser).append(metaRich.getImage()).toString(), url);
					articles.add(article);
					if (--size <= 0) {break;}
				}
				
				// 缓存
				WechatUtils.cachedMetaRich(richId, articles);
			}
			return new RespMsgNews(request, articles);
		}
		if (handler != null) {
			return handler.doHandler(request, app, type, config);
		}
		return null;
	}

	@Override
	public void setNextHandler(Handler handler) {
		this.handler = handler;
	}
}