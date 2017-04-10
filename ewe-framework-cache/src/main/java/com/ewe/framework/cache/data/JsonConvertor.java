package com.ewe.framework.cache.data;

import java.util.List;

/**
 * this interface used to convert json to bean
 * @author Lee-yo
 * 2015-6-8
 */
public interface JsonConvertor {

	public <T> T jsonToBean(String json,Class<T> beanClass);
	
	public String beanToJson(Object bean);
	
	public <T> List<T> jsonToBeanList(String json,Class<T> beanClass);
}
