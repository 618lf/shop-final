package com.sample.config;

import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.shop.config.jdbc.MybatisAutoConfiguration;
import com.shop.config.jdbc.MybatisProperties;
import com.shop.config.jdbc.database.ConfigurationCustomizer;
import com.shop.config.jdbc.database.DataSourceProperties;
import com.shop.config.jdbc.database.HikariDataSourceAutoConfiguration;
import com.tmt.Constants;
import com.tmt.core.persistence.dialect.Dialect;
import com.tmt.core.persistence.mapper.MapperScan;
import com.tmt.core.utils.Sets;

/**
 * 代替之前的配置 <br>
 * 
 * @author lifeng
 */
@Configuration
@MapperScan({ "com.tmt.system.dao", "com.tmt.gen.dao" })
@ConditionalOnProperty(prefix = Constants.APPLICATION_PREFIX, name = "enableMultipleDb", matchIfMissing = false)
public class PrimaryMapperConfig {

	// ************ 主定义主配置的资源配置 ****************

	@ConfigurationProperties(prefix = "spring.datasource")
	class PrimaryDataSourceProperties extends DataSourceProperties {

	}

	@ConfigurationProperties(prefix = "spring.mybatis")
	class PrimaryMybatisProperties extends MybatisProperties {

		@Override
		public Set<String> parseAutoMapperScanPackages() {
			return Sets.newHashSet("com.tmt.system.dao", "com.tmt.gen.dao");
		}
	}

	// ************ 1. 主数据源配置 这部分配置之后默认的数据源将失效，但事务配置没有失效 （关键点@Primary）
	// ****************

	@Bean
	public PrimaryDataSourceProperties primaryDataSourceProperties() {
		return new PrimaryDataSourceProperties();
	}

	@Bean
	@Primary
	public DataSource primaryDataSource(PrimaryDataSourceProperties properties) {
		return new HikariDataSourceAutoConfiguration().primaryDataSource(properties);
	}

	// ************ 2. 数据源的事务管理器 ****************

	@Bean
	@Primary
	public DataSourceTransactionManager primaryTransactionManager(DataSource primaryDataSource,
			ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers) {
		DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(primaryDataSource);
		transactionManagerCustomizers.ifAvailable((customizers) -> customizers.customize(transactionManager));
		return transactionManager;
	}

	// ************ 3. 主 Mybatis 配置 这部分配置后默认的 Mybatis将失效，但是需要配置一个 @Primary
	// SqlSessionTemplate 因为默认的Dao 中是通过注解获取的 ****************

	@Bean
	public PrimaryMybatisProperties primaryMybatisProperties() {
		return new PrimaryMybatisProperties();
	}

	@Bean
	public MybatisAutoConfiguration primaryMybatisConfiguration(PrimaryMybatisProperties properties,
			PrimaryDataSourceProperties dbProperties, ObjectProvider<Interceptor[]> interceptorsProvider,
			ResourceLoader resourceLoader, ObjectProvider<DatabaseIdProvider> databaseIdProvider,
			ObjectProvider<List<ConfigurationCustomizer>> configurationCustomizersProvider) {
		return new MybatisAutoConfiguration(properties, dbProperties, interceptorsProvider, resourceLoader,
				databaseIdProvider, configurationCustomizersProvider);
	}

	@Bean
	@Primary
	public Dialect primaryDbDialect(MybatisAutoConfiguration primaryMybatisConfiguration) {
		return primaryMybatisConfiguration.primaryDbDialect();
	}

	@Bean
	public SqlSessionFactory primarySqlSessionFactory(DataSource primaryDataSource, Dialect primaryDbDialect,
			MybatisAutoConfiguration primaryMybatisConfiguration) throws Exception {
		return primaryMybatisConfiguration.sqlSessionFactory(primaryDataSource, primaryDbDialect);
	}

	@Bean
	@Primary
	public SqlSessionTemplate primarySessionTemplate(SqlSessionFactory primarySqlSessionFactory,
			MybatisAutoConfiguration primaryMybatisConfiguration) {
		return primaryMybatisConfiguration.primarySessionTemplate(primarySqlSessionFactory);
	}
}