package com.shop.config.update;

import static com.shop.Application.APP_LOGGER;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.shop.config.jdbc.DataBaseAutoConfiguration;
import com.shop.config.web.WebMvcAutoConfiguration;
import com.tmt.system.service.UpdateDataService;
import com.tmt.update.UpdateServiceFacade;

/**
 * 数据更新自动配置
 * 
 * @author lifeng
 */
@Configuration
@AutoConfigureAfter(DataBaseAutoConfiguration.class)
@AutoConfigureBefore(WebMvcAutoConfiguration.class)
public class UpdateAutoConfiguration {
	
	public UpdateAutoConfiguration() {
		APP_LOGGER.debug("Loading Updater");
	}

	/**
	 * 默认使用基于数据库的数据更新服务
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean(UpdateServiceFacade.class)
	public UpdateServiceFacade updateService() {
		return new UpdateDataService();
	}
}