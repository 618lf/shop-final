package com.shop.config.jdbc;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

import com.shop.config.jdbc.database.DataSourceProperties;
import com.shop.config.jdbc.database.DruidDataSourceAutoConfiguration;
import com.shop.config.jdbc.database.HikariDataSourceAutoConfiguration;
import com.shop.config.jdbc.database.SqlLiteDataSourceAutoConfiguration;

/**
 * DataSource 数据源, 可以定义多个数据源，则数据源定义的都是主数据源
 * 
 * @author lifeng
 */
@Configuration
@ConditionalOnClass(JdbcTemplate.class)
@ConditionalOnMissingBean(value = DataSource.class, name = "primaryDataSource")
@EnableConfigurationProperties(DataSourceProperties.class)
@Import({ SqlLiteDataSourceAutoConfiguration.class, DruidDataSourceAutoConfiguration.class,
		HikariDataSourceAutoConfiguration.class })
public class DataSourceAutoConfiguration {
}
