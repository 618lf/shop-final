package com.shop.config.searcher;

import java.util.List;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.tmt.common.searcher.BaseSearcher;

/**
 * 查询的配置
 * 
 * @author lifeng
 */
@SuppressWarnings({ "rawtypes" })
@Configuration
@EnableConfigurationProperties(SearcherProperties.class)
@ConditionalOnProperty(prefix = "spring.application", name = "enableSearcher", matchIfMissing = true)
public class SearcherAutoConfiguration {

	SearcherProperties properties;
	List<BaseSearcher> searchers;

	public SearcherAutoConfiguration(ObjectProvider<List<BaseSearcher>> searchers, SearcherProperties properties) {
		this.searchers = searchers.getIfAvailable();
		this.properties = properties;
		setSearcherStorager();
	}

	private void setSearcherStorager() {
		for (BaseSearcher searcher : searchers) {
			searcher.setLUCENE_INDEX_PATH(properties.getStorager());
		}
	}
}