/*
 * Copyright 2012-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.shop.config.web;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpEncodingProperties;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;

import com.tmt.common.utils.Lists;

@Configuration
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@Order(Ordered.HIGHEST_PRECEDENCE)
@ConditionalOnClass(HttpMessageConverter.class)
public class HttpMessageConvertersAutoConfiguration {

	static final String PREFERRED_MAPPER_PROPERTY = "spring.http.converters.preferred-json-mapper";

	private final List<HttpMessageConverter<?>> converters;

	public HttpMessageConvertersAutoConfiguration(
			ObjectProvider<List<HttpMessageConverter<?>>> convertersProvider) {
		this.converters = convertersProvider.getIfAvailable();
	}

	@Bean
	@ConditionalOnMissingBean
	public HttpMessageConverters messageConverters() {
		return new HttpMessageConverters(
				this.converters != null ? this.converters : Collections.emptyList());
	}
	
	@Configuration
	@ConditionalOnClass(JsonHttpMessageConverter.class)
	protected static class JsonHttpMessageConverterConfiguration {
		
		@Bean
		@ConditionalOnMissingBean
		public JsonHttpMessageConverter jsonHttpMessageConverter() {
			List<MediaType> supportedMediaTypes = Lists.newArrayList();
			supportedMediaTypes.add(MediaType.valueOf("text/html;charset=UTF-8"));
			supportedMediaTypes.add(MediaType.valueOf("application/json;charset=UTF-8"));
			supportedMediaTypes.add(MediaType.valueOf("application/*+json;charset=UTF-8"));
			supportedMediaTypes.add(MediaType.valueOf("text/json"));
			JsonHttpMessageConverter converter = new JsonHttpMessageConverter();
			converter.setSupportedMediaTypes(supportedMediaTypes);
			return converter;
		}
	}
	
	
	@Configuration
	@ConditionalOnClass(StringHttpMessageConverter.class)
	@EnableConfigurationProperties(HttpEncodingProperties.class)
	protected static class StringHttpMessageConverterConfiguration {

		private final HttpEncodingProperties encodingProperties;

		protected StringHttpMessageConverterConfiguration(
				HttpEncodingProperties encodingProperties) {
			this.encodingProperties = encodingProperties;
		}

		@Bean
		@ConditionalOnMissingBean
		public StringHttpMessageConverter stringHttpMessageConverter() {
			StringHttpMessageConverter converter = new StringHttpMessageConverter(
					this.encodingProperties.getCharset());
			converter.setWriteAcceptCharset(false);
			return converter;
		}

	}
}