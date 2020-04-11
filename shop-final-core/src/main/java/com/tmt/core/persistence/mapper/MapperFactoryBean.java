package com.tmt.core.persistence.mapper;

import org.springframework.beans.factory.FactoryBean;

/**
 * 代理创建具体的 Mapper
 * 
 * @author lifeng
 */
public class MapperFactoryBean<T> implements FactoryBean<T> {

	private Class<T> mapperInterface;

	public MapperFactoryBean() {
		// intentionally empty
	}

	public MapperFactoryBean(Class<T> mapperInterface) {
		this.mapperInterface = mapperInterface;
	}

	/**
	 * Sets the mapper interface of the MyBatis mapper
	 *
	 * @param mapperInterface class of the interface
	 */
	public void setMapperInterface(Class<T> mapperInterface) {
		this.mapperInterface = mapperInterface;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T getObject() throws Exception {
		return MapperProxy.newProxy(mapperInterface);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> getObjectType() {
		return this.mapperInterface;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSingleton() {
		return true;
	}
}
