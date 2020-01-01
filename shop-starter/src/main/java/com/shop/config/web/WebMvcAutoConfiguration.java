package com.shop.config.web;

import static com.shop.Application.APP_LOGGER;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.servlet.Servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.autoconfigure.validation.ValidatorAdapter;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.format.WebConversionService;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.ClassUtils;
import org.springframework.validation.Validator;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.accept.PathExtensionContentNegotiationStrategy;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.resource.ResourceUrlProvider;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.shop.config.jdbc.DataBaseAutoConfiguration;
import com.shop.starter.ApplicationProperties;
import com.tmt.common.converter.DateConverter;
import com.tmt.common.converter.StringEscapeConverter;
import com.tmt.common.exception.DefaultExceptionHandler;
import com.tmt.common.web.SessionFlashMapManager;
import com.tmt.common.web.security.interceptor.TokenInterceptor;

/**
 * 
 * 替换默认的 WebMvcAutoConfiguration
 * 
 * @author lifeng
 */
@Configuration
@ConditionalOnWebApplication(type = Type.SERVLET)
@ConditionalOnClass({ Servlet.class, DispatcherServlet.class, WebMvcConfigurer.class })
@ConditionalOnMissingBean(WebMvcConfigurationSupport.class)
@AutoConfigureAfter({ DispatcherServletAutoConfiguration.class, DataBaseAutoConfiguration.class })
@EnableConfigurationProperties({ WebMvcProperties.class, ResourceProperties.class, ApplicationProperties.class })
public class WebMvcAutoConfiguration {

	private static Logger logger = LoggerFactory.getLogger(WebMvcAutoConfiguration.class);
	private static final String[] SERVLET_LOCATIONS = { "/" };

	public WebMvcAutoConfiguration() {
		APP_LOGGER.debug("Loading WebMvc");
	}

	/**
	 * flashMapManager
	 * 
	 * @return
	 */
	@Bean(name = DispatcherServlet.FLASH_MAP_MANAGER_BEAN_NAME)
	public SessionFlashMapManager flashMapManager() {
		return new SessionFlashMapManager();
	}

	/**
	 * 附件
	 * 
	 * @return
	 */
	@Bean(name = DispatcherServlet.MULTIPART_RESOLVER_BEAN_NAME)
	public CommonsMultipartResolver multipartResolver(ApplicationProperties properties) {
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
		commonsMultipartResolver.setDefaultEncoding(StandardCharsets.UTF_8.name());
		commonsMultipartResolver.setMaxUploadSize(properties.getWeb().getMaxUploadSize());
		commonsMultipartResolver.setMaxInMemorySize(properties.getWeb().getMaxInMemorySize());
		return commonsMultipartResolver;
	}

	/**
	 * 配置了一个默认的 WebMvcConfigurer
	 * 
	 * @author lifeng
	 */
	@Configuration
	@Import(EnableWebMvcConfiguration.class)
	@EnableConfigurationProperties({ WebMvcProperties.class, ResourceProperties.class })
	@Order(0)
	public static class WebMvcAutoConfigurationAdapter implements WebMvcConfigurer {

		private final ResourceProperties resourceProperties;
		private final WebMvcProperties mvcProperties;
		private final HttpMessageConverters messageConverters;

		public WebMvcAutoConfigurationAdapter(ResourceProperties resourceProperties, WebMvcProperties mvcProperties,
				ListableBeanFactory beanFactory, @Lazy HttpMessageConverters messageConverters) {
			this.resourceProperties = resourceProperties;
			this.mvcProperties = mvcProperties;
			this.messageConverters = messageConverters;
		}

		@Override
		public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
			converters.addAll(this.messageConverters.getConverters());
		}

		@Override
		public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
			Duration timeout = this.mvcProperties.getAsync().getRequestTimeout();
			if (timeout != null) {
				configurer.setDefaultTimeout(timeout.toMillis());
			}
		}

		@Override
		public void configurePathMatch(PathMatchConfigurer configurer) {
			configurer.setUseSuffixPatternMatch(this.mvcProperties.getPathmatch().isUseSuffixPattern());
			configurer.setUseRegisteredSuffixPatternMatch(
					this.mvcProperties.getPathmatch().isUseRegisteredSuffixPattern());
		}

		@Override
		public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
			WebMvcProperties.Contentnegotiation contentnegotiation = this.mvcProperties.getContentnegotiation();
			configurer.favorPathExtension(contentnegotiation.isFavorPathExtension());
			configurer.favorParameter(contentnegotiation.isFavorParameter());
			if (contentnegotiation.getParameterName() != null) {
				configurer.parameterName(contentnegotiation.getParameterName());
			}
			Map<String, MediaType> mediaTypes = this.mvcProperties.getContentnegotiation().getMediaTypes();
			mediaTypes.forEach(configurer::mediaType);
		}

		@Override
		public void configureViewResolvers(ViewResolverRegistry registry) {
			InternalResourceViewResolver resolver = new InternalResourceViewResolver();
			resolver.setPrefix(this.mvcProperties.getView().getPrefix());
			resolver.setSuffix(this.mvcProperties.getView().getSuffix());
			resolver.setOrder(Ordered.HIGHEST_PRECEDENCE);
			registry.viewResolver(resolver);
		}

		@Override
		public void addResourceHandlers(ResourceHandlerRegistry registry) {
			if (!this.resourceProperties.isAddMappings()) {
				logger.debug("Default resource handling disabled");
				return;
			}
			Duration cachePeriod = this.resourceProperties.getCache().getPeriod();
			CacheControl cacheControl = this.resourceProperties.getCache().getCachecontrol().toHttpCacheControl();
			if (!registry.hasMappingForPattern("/webjars/**")) {
				registry.addResourceHandler("/webjars/**")
						.addResourceLocations("classpath:/META-INF/resources/webjars/")
						.setCachePeriod(getSeconds(cachePeriod)).setCacheControl(cacheControl);
			}
			String staticPathPattern = this.mvcProperties.getStaticPathPattern();
			if (!registry.hasMappingForPattern(staticPathPattern)) {
				registry.addResourceHandler(staticPathPattern)
						.addResourceLocations(getResourceLocations(this.resourceProperties.getStaticLocations()))
						.setCachePeriod(getSeconds(cachePeriod)).setCacheControl(cacheControl);
			}
		}

		private Integer getSeconds(Duration cachePeriod) {
			return (cachePeriod != null ? (int) cachePeriod.getSeconds() : null);
		}

		@Override
		public void addFormatters(FormatterRegistry registry) {
			registry.addConverter(new DateConverter());
			registry.addConverter(new StringEscapeConverter());
		}

		@Override
		public void addInterceptors(InterceptorRegistry registry) {
			TokenInterceptor interceptor = new TokenInterceptor();
			registry.addInterceptor(interceptor).addPathPatterns("/admin/**").excludePathPatterns("/admin/login/**");
		}
	}

	/**
	 * @param staticLocations
	 * @return
	 */
	static String[] getResourceLocations(String[] staticLocations) {
		String[] locations = new String[staticLocations.length + SERVLET_LOCATIONS.length];
		System.arraycopy(staticLocations, 0, locations, 0, staticLocations.length);
		System.arraycopy(SERVLET_LOCATIONS, 0, locations, staticLocations.length, SERVLET_LOCATIONS.length);
		return locations;
	}

	/**
	 * 這個是基礎， 業務部分可以通過配置 WebMvcConfigurer，來達到配置系统的目的
	 * 
	 * Configuration equivalent to {@code @EnableWebMvc}.
	 */
	@Configuration
	public static class EnableWebMvcConfiguration extends DelegatingWebMvcConfiguration {

		private final WebMvcProperties mvcProperties;
		private final WebMvcRegistrations mvcRegistrations;

		public EnableWebMvcConfiguration(ObjectProvider<WebMvcProperties> mvcPropertiesProvider,
				ObjectProvider<WebMvcRegistrations> mvcRegistrationsProvider, ListableBeanFactory beanFactory) {
			this.mvcProperties = mvcPropertiesProvider.getIfAvailable();
			this.mvcRegistrations = mvcRegistrationsProvider.getIfUnique();
		}
		
		@Bean
		@Override
		public ContentNegotiationManager mvcContentNegotiationManager() {
			ContentNegotiationManager manager = super.mvcContentNegotiationManager();
			List<ContentNegotiationStrategy> strategies = manager.getStrategies();
			ListIterator<ContentNegotiationStrategy> iterator = strategies.listIterator();
			while (iterator.hasNext()) {
				ContentNegotiationStrategy strategy = iterator.next();
				if (strategy instanceof PathExtensionContentNegotiationStrategy) {
					iterator.set(new OptionalPathExtensionContentNegotiationStrategy(strategy));
				}
			}
			return manager;
		}

		@Bean
		@Override
		public FormattingConversionService mvcConversionService() {
			WebConversionService conversionService = new WebConversionService(this.mvcProperties.getDateFormat());
			addFormatters(conversionService);
			return conversionService;
		}

		@Bean
		@Override
		public Validator mvcValidator() {
			if (!ClassUtils.isPresent("javax.validation.Validator", getClass().getClassLoader())) {
				return super.mvcValidator();
			}
			return ValidatorAdapter.get(getApplicationContext(), getValidator());
		}

		/**
		 * 替换默认的 Adapter
		 */
		@Bean
		@Override
		public RequestMappingHandlerAdapter requestMappingHandlerAdapter(
				@Qualifier("mvcContentNegotiationManager") ContentNegotiationManager contentNegotiationManager,
				@Qualifier("mvcConversionService") FormattingConversionService conversionService,
				@Qualifier("mvcValidator") Validator validator) {
			RequestMappingHandlerAdapter adapter = super.requestMappingHandlerAdapter(contentNegotiationManager,
					conversionService, validator);
			adapter.setIgnoreDefaultModelOnRedirect(
					this.mvcProperties == null || this.mvcProperties.isIgnoreDefaultModelOnRedirect());
			return adapter;
		}

		@Override
		protected RequestMappingHandlerAdapter createRequestMappingHandlerAdapter() {
			if (this.mvcRegistrations != null && this.mvcRegistrations.getRequestMappingHandlerAdapter() != null) {
				return this.mvcRegistrations.getRequestMappingHandlerAdapter();
			}
			return super.createRequestMappingHandlerAdapter();
		}
		
		/**
		 * Mapping
		 */
		@Bean
		@Primary
		@Override
		public RequestMappingHandlerMapping requestMappingHandlerMapping(
				@Qualifier("mvcContentNegotiationManager") ContentNegotiationManager contentNegotiationManager,
				@Qualifier("mvcConversionService") FormattingConversionService conversionService,
				@Qualifier("mvcResourceUrlProvider") ResourceUrlProvider resourceUrlProvider) {
			return super.requestMappingHandlerMapping(contentNegotiationManager, conversionService,
					resourceUrlProvider);
		}

		@Override
		protected RequestMappingHandlerMapping createRequestMappingHandlerMapping() {
			if (this.mvcRegistrations != null && this.mvcRegistrations.getRequestMappingHandlerMapping() != null) {
				return this.mvcRegistrations.getRequestMappingHandlerMapping();
			}
			return super.createRequestMappingHandlerMapping();
		}

		/**
		 * 替代默认 --  异常处理
		 * 
		 * @return
		 */
		@Bean(name = DispatcherServlet.HANDLER_EXCEPTION_RESOLVER_BEAN_NAME)
		public DefaultExceptionHandler exceptionHandler() {
			return new DefaultExceptionHandler();
		}
	}

	/**
	 * Decorator to make {@link PathExtensionContentNegotiationStrategy} optional
	 * depending on a request attribute.
	 */
	static class OptionalPathExtensionContentNegotiationStrategy implements ContentNegotiationStrategy {

		private static final String SKIP_ATTRIBUTE = PathExtensionContentNegotiationStrategy.class.getName() + ".SKIP";

		private final ContentNegotiationStrategy delegate;

		OptionalPathExtensionContentNegotiationStrategy(ContentNegotiationStrategy delegate) {
			this.delegate = delegate;
		}

		@Override
		public List<MediaType> resolveMediaTypes(NativeWebRequest webRequest)
				throws HttpMediaTypeNotAcceptableException {
			Object skip = webRequest.getAttribute(SKIP_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
			if (skip != null && Boolean.parseBoolean(skip.toString())) {
				return MEDIA_TYPE_ALL_LIST;
			}
			return this.delegate.resolveMediaTypes(webRequest);
		}
	}
}
