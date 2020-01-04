package com.shop.config;

import static com.shop.Application.APP_LOGGER;

import java.io.File;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import com.shop.booter.AppBooter;
import com.shop.starter.ApplicationProperties;
import com.tmt.core.config.Globals;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.utils.StringUtils;
import com.tmt.core.utils.XSpringContextHolder;
import com.tmt.core.utils.serializer.JavaSerializer;
import com.tmt.core.utils.serializer.KryoPoolSerializer;
import com.tmt.core.utils.serializer.KryoSerializer;
import com.tmt.core.utils.serializer.SerializationUtils;
import com.tmt.core.utils.serializer.Serializer;

/**
 * 基础组件
 * 
 * @author lifeng
 */
@Configuration
@EnableConfigurationProperties(ApplicationProperties.class)
public class ApplicationAutoConfiguration {

	private ResourceLoader resourceLoader;

	public ApplicationAutoConfiguration(ApplicationProperties properties, ResourceLoader resourceLoader) {
		APP_LOGGER.debug("Loading App Booter");
		this.resourceLoader = resourceLoader;
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
		if (StringUtils.isNotBlank(temp)) {
			try {
				Resource resource = resourceLoader.getResource(temp);
				return resource.getFile().getAbsoluteFile();
			} catch (Exception e) {
			}
		}
		return null;
	}
	
	@Bean
	public AppBooter appBooter() {
		return new AppBooter();
	}
}