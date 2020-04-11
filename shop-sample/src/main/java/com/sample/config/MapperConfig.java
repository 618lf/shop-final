package com.sample.config;

import org.springframework.context.annotation.Configuration;

import com.tmt.core.persistence.mapper.MapperScan;

/**
 * 定义多数据源
 * 
 * @author lifeng
 */
@Configuration
@MapperScan("com.sample.dao")
public class MapperConfig {

}
