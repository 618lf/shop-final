package com.shop.config.searcher;

import java.util.List;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.tmt.common.searcher.BaseSearcher;

/**
 * 查询的配置
 * 
 * @author lifeng
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
@Configuration
@EnableConfigurationProperties(SearcherProperties.class)
@ConditionalOnProperty(prefix = "spring.application", name = "enableSearcher", matchIfMissing = true)
public class SearcherAutoConfiguration {
    
	@Autowired
	private SearcherProperties properties;
	List<BaseSearcher> searchers;
	
	public SearcherAutoConfiguration(ObjectProvider<BaseSearcher> searchers) {
		this.searchers = (List<BaseSearcher>) searchers.getIfAvailable();
		setSearcherStorager();
	}
	
	private void setSearcherStorager() {
		for(BaseSearcher searcher: searchers) {
			searcher.setLUCENE_INDEX_PATH(properties.getStorager());
		}
	}
}
