package com.shop.config;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import com.shop.starter.ApplicationProperties;
import com.tmt.common.config.Globals;
import com.tmt.common.persistence.incrementer.IdGen;
import com.tmt.common.utils.StringUtil3;
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
@EnableConfigurationProperties(ApplicationProperties.class)
public class ApplicationAutoConfiguration {

	@Autowired
	private ResourceLoader resourceLoader;

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
		Globals.temps = this.tempsDiv(properties.getWeb().getTemps());
	}

	// 基本的工作目录
	private File tempsDiv(String temp) {
		if (StringUtil3.isNotBlank(temp)) {
			try {
				Resource resource = resourceLoader.getResource(temp);
				return resource.getFile().getAbsoluteFile();
			} catch (Exception e) {
			}
		}
		return null;
	}
}