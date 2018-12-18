package com.shop.config.jdbc;

import com.shop.config.jdbc.database.*;
import com.shop.config.jdbc.mybatis.MybatisProperties;
import com.shop.config.jdbc.transaction.DataSourceTransactionManagerAutoConfiguration;
import com.shop.config.jdbc.transaction.TransactionAutoConfiguration;
import com.tmt.common.persistence.JdbcSqlExecutor;
import com.tmt.common.persistence.QueryCondition;
import com.tmt.common.persistence.dialect.*;
import com.tmt.common.persistence.mybatis.ExecutorInterceptor;
import com.tmt.common.security.utils.StringUtils;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.autoconfigure.jdbc.JdbcProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.List;

/**
 * 会判断是否引入了数据库组件
 * 
 * @author lifeng
 */
@org.springframework.context.annotation.Configuration
@ConditionalOnClass(JdbcTemplate.class)
@ConditionalOnProperty(prefix = "spring.application", name = "enableDataBase", matchIfMissing = true)
public class DataBaseAutoConfiguration {

	/**
	 * 数据源
	 * 
	 * @author lifeng
	 */
	@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
	@org.springframework.context.annotation.Configuration
	@ConditionalOnClass(JdbcTemplate.class)
	@ConditionalOnMissingBean(DataSource.class)
	@EnableConfigurationProperties(DataSourceProperties.class)
	@Import({ SqlLiteDataSourceAutoConfiguration.class, DruidDataSourceAutoConfiguration.class,
			HikariDataSourceAutoConfiguration.class })
	public static class DataSourceAutoConfiguration {
	}

	/**
	 * JDBC 操作模板
	 * 
	 * @author lifeng
	 */
	@org.springframework.context.annotation.Configuration
	@ConditionalOnClass(JdbcTemplate.class)
	@ConditionalOnSingleCandidate(DataSource.class)
	@AutoConfigureAfter({DataSourceAutoConfiguration.class,SqlLiteDataSourceAutoConfiguration.class, DruidDataSourceAutoConfiguration.class,
			HikariDataSourceAutoConfiguration.class})
	@EnableConfigurationProperties(JdbcProperties.class)
	public static class JdbcTemplateAutoConfiguration {

		@org.springframework.context.annotation.Configuration
		static class JdbcTemplateConfiguration {

			private final DataSource dataSource;

			private final JdbcProperties properties;

			JdbcTemplateConfiguration(DataSource dataSource, JdbcProperties properties) {
				this.dataSource = dataSource;
				this.properties = properties;
			}

			@Bean
			@Primary
			@ConditionalOnMissingBean(JdbcOperations.class)
			public JdbcTemplate jdbcTemplate() {
				JdbcTemplate jdbcTemplate = new JdbcTemplate(this.dataSource);
				JdbcProperties.Template template = this.properties.getTemplate();
				jdbcTemplate.setFetchSize(template.getFetchSize());
				jdbcTemplate.setMaxRows(template.getMaxRows());
				if (template.getQueryTimeout() != null) {
					jdbcTemplate.setQueryTimeout((int) template.getQueryTimeout().getSeconds());
				}
				return jdbcTemplate;
			}
		}

		@org.springframework.context.annotation.Configuration
		@Import(JdbcTemplateConfiguration.class)
		static class NamedParameterJdbcTemplateConfiguration {

			@Bean
			@Primary
			@ConditionalOnSingleCandidate(JdbcTemplate.class)
			@ConditionalOnMissingBean(NamedParameterJdbcOperations.class)
			public NamedParameterJdbcTemplate namedParameterJdbcTemplate(JdbcTemplate jdbcTemplate) {
				NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
				JdbcSqlExecutor.setJdbcTemplate(template);
				return template;
			}
		}
	}

	/**
	 * 数据库事务 -- 使用spring boot 的配置
	 * 
	 * @author lifeng
	 */
	@org.springframework.context.annotation.Configuration
	@ConditionalOnClass({ JdbcTemplate.class, PlatformTransactionManager.class })
	@ConditionalOnSingleCandidate(DataSource.class)
	@Import({ DataSourceTransactionManagerAutoConfiguration.class, TransactionAutoConfiguration.class })
	@AutoConfigureAfter({DataSourceAutoConfiguration.class,SqlLiteDataSourceAutoConfiguration.class, DruidDataSourceAutoConfiguration.class,
			HikariDataSourceAutoConfiguration.class})
	public static class DataSourceTransactionManagerConfiguration {
	}

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
	public static class MybatisAutoConfiguration {
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
}
