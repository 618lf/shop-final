package com.shop.config.jdbc;

import static com.shop.Application.APP_LOGGER;

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
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
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

import com.shop.config.jdbc.database.ConfigurationCustomizer;
import com.shop.config.jdbc.database.DataSourceProperties;
import com.shop.config.jdbc.database.DruidDataSourceAutoConfiguration;
import com.shop.config.jdbc.database.HikariDataSourceAutoConfiguration;
import com.shop.config.jdbc.database.SpringBootVFS;
import com.shop.config.jdbc.database.SqlLiteDataSourceAutoConfiguration;
import com.tmt.Constants;
import com.tmt.core.persistence.Database;
import com.tmt.core.persistence.QueryCondition;
import com.tmt.core.persistence.dialect.Dialect;
import com.tmt.core.persistence.dialect.H2Dialect;
import com.tmt.core.persistence.dialect.MsSQLDialect;
import com.tmt.core.persistence.dialect.MySQLDialect;
import com.tmt.core.persistence.dialect.OracleDialect;
import com.tmt.core.persistence.dialect.SqlLiteDialect;
import com.tmt.core.persistence.mybatis.PagingInterceptor;
import com.tmt.core.utils.Maps;
import com.tmt.core.utils.StringUtils;

/**
 * Mybatis
 * 
 * @author lifeng
 */
@org.springframework.context.annotation.Configuration(proxyBeanMethods = false)
@ConditionalOnClass({ SqlSessionFactory.class, SqlSessionFactoryBean.class })
@ConditionalOnBean(DataSource.class)
@ConditionalOnMissingBean(MybatisAutoConfiguration.class)
@EnableConfigurationProperties(MybatisProperties.class)
@AutoConfigureAfter({ DataSourceAutoConfiguration.class, SqlLiteDataSourceAutoConfiguration.class,
		DruidDataSourceAutoConfiguration.class, HikariDataSourceAutoConfiguration.class })
@ConditionalOnProperty(prefix = Constants.APPLICATION_PREFIX, name = "enableMybatis", matchIfMissing = true)
public class MybatisAutoConfiguration {
	private final MybatisProperties properties;
	private final Interceptor[] interceptors;
	private final ResourceLoader resourceLoader;
	private final DatabaseIdProvider databaseIdProvider;
	private final List<ConfigurationCustomizer> configurationCustomizers;

	public MybatisAutoConfiguration(MybatisProperties properties, ObjectProvider<Interceptor[]> interceptorsProvider,
			ResourceLoader resourceLoader, ObjectProvider<DatabaseIdProvider> databaseIdProvider,
			ObjectProvider<List<ConfigurationCustomizer>> configurationCustomizersProvider) {
		APP_LOGGER.debug("Loading Mybatis");
		this.properties = properties;
		this.interceptors = interceptorsProvider.getIfAvailable();
		this.resourceLoader = resourceLoader;
		this.databaseIdProvider = databaseIdProvider.getIfAvailable();
		this.configurationCustomizers = configurationCustomizersProvider.getIfAvailable();
	}

	@Bean
	@ConditionalOnMissingBean
	public Dialect primaryDbDialect(DataSourceProperties dbProperties) {
		Database db = dbProperties.getDb();
		if (db == Database.h2) {
			return new H2Dialect();
		} else if (db == Database.mysql) {
			return new MySQLDialect();
		} else if (db == Database.oracle) {
			return new OracleDialect();
		} else if (db == Database.sqlite) {
			return new SqlLiteDialect();
		} else if (db == Database.mssql) {
			return new MsSQLDialect();
		}
		return new MySQLDialect();
	}

	@Bean
	@ConditionalOnMissingBean
	public SqlSessionFactory sqlSessionFactory(DataSource dataSource, Dialect dialect) throws Exception {
		SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
		factory.setDataSource(dataSource);
		factory.setVfs(SpringBootVFS.class);
		if (StringUtils.isNotBlank(this.properties.getConfigLocation())) {
			factory.setConfigLocation(this.resourceLoader.getResource(this.properties.getConfigLocation()));
		}
		Configuration configuration = this.properties.getConfiguration();
		if (configuration == null && !StringUtils.isNotBlank(this.properties.getConfigLocation())) {
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
		if (StringUtils.isNotBlank(this.properties.getTypeAliasesPackage())) {
			factory.setTypeAliasesPackage(this.properties.getTypeAliasesPackage());
		}
		if (StringUtils.isNotBlank(this.properties.getTypeHandlersPackage())) {
			factory.setTypeHandlersPackage(this.properties.getTypeHandlersPackage());
		}
		if (!ObjectUtils.isEmpty(this.properties.resolveMapperLocations())) {
			factory.setMapperLocations(this.properties.resolveMapperLocations());
		}

		// 默认配置
		this.defaultConfiguration(configuration, dialect);

		// 配置全局变量
		configuration.setVariables(Maps.toProperties(dialect.variables()));

		return factory.getObject();
	}

	private void defaultConfiguration(Configuration configuration, Dialect dialect) {

		// 默认的拦截器
		PagingInterceptor interceptor = new PagingInterceptor();
		interceptor.setDialect(dialect);
		configuration.addInterceptor(interceptor);

		// 默认的别名
		configuration.getTypeAliasRegistry().registerAlias("queryCondition", QueryCondition.class);
	}

	@Bean
	@ConditionalOnMissingBean
	public SqlSessionTemplate primarySessionTemplate(SqlSessionFactory sqlSessionFactory) {
		ExecutorType executorType = this.properties.getExecutorType();
		if (executorType != null) {
			return new SqlSessionTemplate(sqlSessionFactory, executorType);
		} else {
			return new SqlSessionTemplate(sqlSessionFactory);
		}
	}

	@PostConstruct
	public void checkConfigFileExists() {
		if (this.properties.isCheckConfigLocation() && StringUtils.isNotBlank(this.properties.getConfigLocation())) {
			Resource resource = this.resourceLoader.getResource(this.properties.getConfigLocation());
			Assert.state(resource.exists(), "Cannot find config location: " + resource
					+ " (please add config file or check your Mybatis configuration)");
		}
	}
}
