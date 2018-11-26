package com.shop.config;

import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import com.shop.starter.ApplicationProperties;
import com.tmt.common.config.Globals;
import com.tmt.common.persistence.incrementer.IdGen;
import com.tmt.common.utils.XSpringContextHolder;
import com.tmt.common.utils.serializer.JavaSerializer;
import com.tmt.common.utils.serializer.KryoPoolSerializer;
import com.tmt.common.utils.serializer.KryoSerializer;
import com.tmt.common.utils.serializer.SerializationUtils;
import com.tmt.common.utils.serializer.Serializer;

/**
 * 基础组件
 * 
 * @author lifeng
 */
@Configuration
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE + 150)
@Order(Ordered.HIGHEST_PRECEDENCE + 150)
@EnableConfigurationProperties(ApplicationProperties.class)
public class ApplicationAutoConfiguration {
	
	public ApplicationAutoConfiguration(ApplicationProperties properties) {
		serializer(properties);
		globals(properties);
	}

	@Bean
	public XSpringContextHolder springContextHolder(ApplicationContext context) {
		return new XSpringContextHolder();
	}

	@Bean
	public IdGen idGenerator(ApplicationProperties properties) {
		IdGen idGen = new IdGen();
		idGen.setServerSn(properties.getServerSn());
		return idGen;
	}

	/**
	 * 序列化
	 * 
	 * @return
	 */
	public void serializer(ApplicationProperties properties) {
		String ser = properties.getSerialization();
		Serializer g_ser = null;
		if (ser.equals("java")) {
			g_ser = new JavaSerializer();
		} else if (ser.equals("kryo")) {
			g_ser = new KryoSerializer();
		} else if (ser.equals("kryo_pool")) {
			g_ser = new KryoPoolSerializer();
		} else {
			g_ser = new JavaSerializer();
		}

		// 公共引用
		SerializationUtils.g_ser = g_ser;
	}
	
	/**
	 * 全局参数
	 * 
	 * @return
	 */
	public void globals(ApplicationProperties properties) {
		Globals.adminPath = properties.getWeb().getAdmin();
		Globals.frontPath = properties.getWeb().getFront();
		Globals.index = properties.getWeb().getIndex();
		Globals.version = properties.getVersion();
		Globals.domain = properties.getWeb().getDomain();
	}
}