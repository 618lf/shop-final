package com.sample.config;

import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

import com.shop.config.jdbc.MybatisAutoConfiguration;
import com.shop.config.jdbc.MybatisProperties;
import com.shop.config.jdbc.database.ConfigurationCustomizer;
import com.shop.config.jdbc.database.DataSourceProperties;
import com.shop.config.jdbc.database.HikariDataSourceAutoConfiguration;
import com.tmt.core.persistence.dialect.Dialect;
import com.tmt.core.persistence.mapper.MapperScan;
import com.tmt.core.utils.Sets;

/**
 * 定义多数据源
 * 
 * @author lifeng
 */
@Configuration
@MapperScan(value = "com.sample.dao", sqlSessionTemplateRef = "orderSessionTemplate")
public class OrderMapperConfig {

	// ************ 订单系统 -- 配置的资源配置 ****************

	@ConfigurationProperties(prefix = "spring.datasource.order")
	class OrderDataSourceProperties extends DataSourceProperties {
	}

	@ConfigurationProperties(prefix = "spring.mybatis")
	class OrderMybatisProperties extends MybatisProperties {

		@Override
		public Set<String> parseAutoMapperScanPackages() {
			return Sets.newHashSet("com.sample.dao");
		}
	}

	// ************1. 订单系统 -- 数据源配置 ****************

	@Bean
	public OrderDataSourceProperties orderDataSourceProperties() {
		return new OrderDataSourceProperties();
	}

	@Bean
	public DataSource orderDataSource(OrderDataSourceProperties properties) {
		return new HikariDataSourceAutoConfiguration().primaryDataSource(properties);
	}

	// ************3. 订单系统 -- Mybatis 配置 ****************

	@Bean
	public OrderMybatisProperties orderMybatisProperties() {
		return new OrderMybatisProperties();
	}

	@Bean
	public MybatisAutoConfiguration orderMybatisConfiguration(OrderMybatisProperties properties,
			OrderDataSourceProperties dbProperties, ObjectProvider<Interceptor[]> interceptorsProvider,
			ResourceLoader resourceLoader, ObjectProvider<DatabaseIdProvider> databaseIdProvider,
			ObjectProvider<List<ConfigurationCustomizer>> configurationCustomizersProvider) {
		return new MybatisAutoConfiguration(properties, dbProperties, interceptorsProvider, resourceLoader,
				databaseIdProvider, configurationCustomizersProvider);
	}

	@Bean
	public Dialect orderDbDialect(MybatisAutoConfiguration orderMybatisConfiguration) {
		return orderMybatisConfiguration.primaryDbDialect();
	}

	@Bean
	public SqlSessionFactory orderSqlSessionFactory(@Qualifier("orderDataSource") DataSource orderDataSource,
			@Qualifier("orderDbDialect") Dialect orderDbDialect, MybatisAutoConfiguration orderMybatisConfiguration)
			throws Exception {
		return orderMybatisConfiguration.sqlSessionFactory(orderDataSource, orderDbDialect);
	}

	@Bean
	public SqlSessionTemplate orderSessionTemplate(
			@Qualifier("orderSqlSessionFactory") SqlSessionFactory orderSqlSessionFactory,
			MybatisAutoConfiguration orderMybatisConfiguration) {
		return orderMybatisConfiguration.primarySessionTemplate(orderSqlSessionFactory);
	}
}
