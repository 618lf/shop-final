package com.shop.config.searcher;

import static com.shop.Application.APP_LOGGER;

import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
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
@ConditionalOnClass(Analyzer.class)
@EnableConfigurationProperties(SearcherProperties.class)
@ConditionalOnProperty(prefix = "spring.application", name = "enableSearcher", matchIfMissing = true)
public class SearcherAutoConfiguration {

	SearcherProperties properties;
	List<BaseSearcher> searchers;

	public SearcherAutoConfiguration(ObjectProvider<List<BaseSearcher>> searchers, SearcherProperties properties) {
		APP_LOGGER.debug("Loading Searcher");
		this.searchers = searchers.getIfAvailable();
		this.properties = properties;
		setSearcherStorager();
	}

	private void setSearcherStorager() {
		if (searchers != null) {
			for (BaseSearcher searcher : searchers) {
				searcher.setLUCENE_INDEX_PATH(properties.getStorager());
			}
		}
	}
}