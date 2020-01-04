package com.shop.config.freemarker;

import static com.shop.Application.APP_LOGGER;

import java.util.Map;
import java.util.Properties;

import javax.servlet.Servlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactory;

import com.shop.config.web.WebMvcAutoConfiguration;
import com.tmt.core.utils.Maps;
import com.tmt.core.utils.freemarker.FreeMarkerConfig;
import com.tmt.core.utils.freemarker.FreeMarkerConfigurer;
import com.tmt.core.web.SimpleFreeMarkerViewResolver;
import com.tmt.core.web.tld.functions.Functions;

/**
 * Configuration for FreeMarker when used in a servlet web context.
 *
 * @author Brian Clozel
 * @author Andy Wilkinson
 */
@Configuration
@ConditionalOnClass({ Servlet.class, FreeMarkerConfigurer.class })
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
@EnableConfigurationProperties(FreeMarkerProperties.class)
public class FreeMarkerAutoConfiguration {

	@Autowired
	private FreeMarkerProperties properties;
	
	public FreeMarkerAutoConfiguration() {
		APP_LOGGER.debug("Loading FreeMarker");
	}

	@Bean
	@ConditionalOnMissingBean(FreeMarkerConfig.class)
	public FreeMarkerConfigurer freeMarkerConfigurer() {
		FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();
		applyProperties(configurer);
		return configurer;
	}

	protected void applyProperties(FreeMarkerConfigurationFactory factory) {
		factory.setTemplateLoaderPaths(this.properties.getTemplateLoaderPath());
		factory.setPreferFileSystemAccess(this.properties.isPreferFileSystemAccess());
		factory.setDefaultEncoding(this.properties.getCharsetName());
		Properties settings = new Properties();
		settings.putAll(this.properties.getSettings());
		factory.setFreemarkerSettings(settings);
		factory.setFreemarkerVariables(variables());
	}

	protected Map<String, Object> variables() {
		Map<String, Object> variables = Maps.newHashMap();
		variables.put("base", Functions.getBasePath());
		variables.put("version", Functions.getVersion());
		variables.put("webRoot", Functions.getWebRoot());
		variables.put("ctx", Functions.getAdminPath());
		variables.put("ctxStatic", Functions.getWebRoot() + "/static/");
		variables.put("ctxModules", Functions.getWebRoot() + "/static/modules");
		variables.put("ctxFront", Functions.getFrontPath());
		return variables;
	}

	/**
	 * 必须排在jsp 的前面
	 * 
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean(name = "freeMarkerViewResolver")
	@ConditionalOnProperty(name = "spring.freemarker.enabled", matchIfMissing = true)
	public SimpleFreeMarkerViewResolver freeMarkerViewResolver() {
		SimpleFreeMarkerViewResolver resolver = new SimpleFreeMarkerViewResolver();
		properties.applyToMvcViewResolver(resolver);
		resolver.setExposeRequestAttributes(false);
		resolver.setExposeSessionAttributes(false);
		resolver.setExposeSpringMacroHelpers(true);
		return resolver;
	}
}
