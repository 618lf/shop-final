package com.shop.config.jdbc;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.jdbc.core.JdbcTemplate;

import com.shop.config.jdbc.database.DataSourceProperties;
import com.shop.config.jdbc.database.DruidDataSourceAutoConfiguration;
import com.shop.config.jdbc.database.HikariDataSourceAutoConfiguration;
import com.shop.config.jdbc.database.SqlLiteDataSourceAutoConfiguration;

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
public class DataSourceAutoConfiguration {
}
