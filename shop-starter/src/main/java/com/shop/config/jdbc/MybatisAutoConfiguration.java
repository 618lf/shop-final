package com.shop.config.jdbc;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.shop.config.jdbc.DataBaseAutoConfiguration.DataSourceAutoConfiguration;
import com.shop.config.jdbc.database.ConfigurationCustomizer;
import com.shop.config.jdbc.database.DataSourceProperties;
import com.shop.config.jdbc.database.Database;
import com.shop.config.jdbc.database.DruidDataSourceAutoConfiguration;
import com.shop.config.jdbc.database.HikariDataSourceAutoConfiguration;
import com.shop.config.jdbc.database.SpringBootVFS;
import com.shop.config.jdbc.database.SqlLiteDataSourceAutoConfiguration;
import com.shop.config.jdbc.mybatis.MybatisProperties;
import com.tmt.common.persistence.QueryCondition;
import com.tmt.common.persistence.dialect.Dialect;
import com.tmt.common.persistence.dialect.H2Dialect;
import com.tmt.common.persistence.dialect.MySQLDialect;
import com.tmt.common.persistence.dialect.OracleDialect;
import com.tmt.common.persistence.dialect.SqlLiteDialect;
import com.tmt.common.persistence.mybatis.ExecutorInterceptor;
import com.tmt.common.security.utils.StringUtils;

/**
 * Mybatis
 * 
 * @author lifeng
 */
@org.springframework.context.annotation.Configuration
@ConditionalOnClass({ SqlSessionFactory.class, SqlSessionFactoryBean.class })
@EnableConfigurationProperties(MybatisProperties.class)
@AutoConfigureAfter({DataSourceAutoConfiguration.class, SqlLiteDataSourceAutoConfiguration.class, DruidDataSourceAutoConfiguration.class,
	HikariDataSourceAutoConfiguration.class})
@ConditionalOnProperty(prefix = "spring.application", name = "enableMybatis", matchIfMissing = true)
public class MybatisAutoConfiguration {
	private final DataSourceProperties dbProperties;
	private final MybatisProperties properties;
	private final Interceptor[] interceptors;
	private final ResourceLoader resourceLoader;
	private final DatabaseIdProvider databaseIdProvider;
	private final List<ConfigurationCustomizer> configurationCustomizers;

	public MybatisAutoConfiguration(MybatisProperties properties, DataSourceProperties dbProperties,
			ObjectProvider<Interceptor[]> interceptorsProvider, ResourceLoader resourceLoader,
			ObjectProvider<DatabaseIdProvider> databaseIdProvider,
			ObjectProvider<List<ConfigurationCustomizer>> configurationCustomizersProvider) {
		this.properties = properties;
		this.dbProperties = dbProperties;
		this.interceptors = interceptorsProvider.getIfAvailable();
		this.resourceLoader = resourceLoader;
		this.databaseIdProvider = databaseIdProvider.getIfAvailable();
		this.configurationCustomizers = configurationCustomizersProvider.getIfAvailable();
	}

	@PostConstruct
	public void checkConfigFileExists() {
		if (this.properties.isCheckConfigLocation() && StringUtils.hasText(this.properties.getConfigLocation())) {
			Resource resource = this.resourceLoader.getResource(this.properties.getConfigLocation());
			Assert.state(resource.exists(), "Cannot find config location: " + resource
					+ " (please add config file or check your Mybatis configuration)");
		}
	}

	/**
	 * 数据库的方言
	 * 
	 * @return
	 */
	@Bean
	public Dialect getDialect() {
		Database db = this.dbProperties.getDb();
		if (db == Database.h2) {
			return new H2Dialect();
		} else if (db == Database.mysql) {
			return new MySQLDialect();
		} else if (db == Database.oracle) {
			return new OracleDialect();
		} else if (db == Database.sqlite) {
			return new SqlLiteDialect();
		}
		return new MySQLDialect();
	}

	@Bean
	@ConditionalOnMissingBean
	public SqlSessionFactory sqlSessionFactory(DataSource dataSource, Dialect dialect) throws Exception {
		SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
		factory.setDataSource(dataSource);
		factory.setVfs(SpringBootVFS.class);
		if (StringUtils.hasText(this.properties.getConfigLocation())) {
			factory.setConfigLocation(this.resourceLoader.getResource(this.properties.getConfigLocation()));
		}
		Configuration configuration = this.properties.getConfiguration();
		if (configuration == null && !StringUtils.hasText(this.properties.getConfigLocation())) {
			configuration = new Configuration();
		}
		if (configuration != null && !CollectionUtils.isEmpty(this.configurationCustomizers)) {
			for (ConfigurationCustomizer customizer : this.configurationCustomizers) {
				customizer.customize(configuration);
			}
		}
		factory.setConfiguration(configuration);
		if (this.properties.getConfigurationProperties() != null) {
			factory.setConfigurationProperties(this.properties.getConfigurationProperties());
		}
		if (!ObjectUtils.isEmpty(this.interceptors)) {
			factory.setPlugins(this.interceptors);
		}
		if (this.databaseIdProvider != null) {
			factory.setDatabaseIdProvider(this.databaseIdProvider);
		}
		if (StringUtils.hasLength(this.properties.getTypeAliasesPackage())) {
			factory.setTypeAliasesPackage(this.properties.getTypeAliasesPackage());
		}
		if (StringUtils.hasLength(this.properties.getTypeHandlersPackage())) {
			factory.setTypeHandlersPackage(this.properties.getTypeHandlersPackage());
		}
		if (!ObjectUtils.isEmpty(this.properties.resolveMapperLocations())) {
			factory.setMapperLocations(this.properties.resolveMapperLocations());
		}

		// 默认配置
		ExecutorInterceptor interceptor = new ExecutorInterceptor();
		interceptor.setDialect(dialect);
		configuration.addInterceptor(interceptor);
		configuration.getTypeAliasRegistry().registerAlias("queryCondition", QueryCondition.class);
		return factory.getObject();
	}

	@Bean
	@ConditionalOnMissingBean
	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
		ExecutorType executorType = this.properties.getExecutorType();
		if (executorType != null) {
			return new SqlSessionTemplate(sqlSessionFactory, executorType);
		} else {
			return new SqlSessionTemplate(sqlSessionFactory);
		}
	}
}