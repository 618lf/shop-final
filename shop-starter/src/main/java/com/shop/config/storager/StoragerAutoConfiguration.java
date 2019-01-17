package com.shop.config.storager;

import static com.shop.Application.APP_LOGGER;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import com.tmt.common.exception.BaseRuntimeException;
import com.tmt.common.utils.storager.LocalStorager;
import com.tmt.common.utils.storager.Storager;

/**
 * 存储的配置
 * 
 * @author lifeng
 */
@Configuration
@EnableConfigurationProperties(StoragerProperties.class)
@ConditionalOnProperty(prefix = "spring.application", name = "enableStorager", matchIfMissing = true)
public class StoragerAutoConfiguration {

	@Autowired
	private ResourceLoader resourceLoader;
	
	public StoragerAutoConfiguration() {
		APP_LOGGER.debug("Loading Storager");
	}
	
	/**
	 * 存储
	 * 
	 * @param properties
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean(Storager.class)
	public LocalStorager storager(StoragerProperties properties) {
		LocalStorager storager = new LocalStorager();
		storager.setStoragePath(loadStoragePath(properties));
		storager.setDomain(properties.getDomain());
		return storager;
	}

	/**
	 * 加载资源文件
	 * 
	 * @return
	 */
	private String loadStoragePath(StoragerProperties properties) {
		String location = properties.getStoragePath();
		try {
			Resource resource = resourceLoader.getResource(location);
			return resource.getFile().getAbsolutePath();
		} catch (Exception e) {
			throw new BaseRuntimeException(e);
		}
	}
}
