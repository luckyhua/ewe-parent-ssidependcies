package com.ewe.framework.cache.data;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

/**
 * default json convertor used jackson to convertor json
 * @author Lee-yo
 * 2015-6-8
 */
public class DefaultJsonConvertor implements JsonConvertor{

	/**
	 * jackson object mapper
	 */
	private ObjectMapper objectMapper = new ObjectMapper();
	
	private static final Log log = LogFactory.getLog(DefaultJsonConvertor.class);
	
	public <T> T jsonToBean(String json, Class<T> beanClass) {
		
		try {
			T bean = objectMapper.readValue(json, beanClass);
			
			return bean;
		} catch (Exception e){
			log.error("json parse error!for json:"+json+" beanClass"+String.valueOf(beanClass),e);
		}
		
		return null;
	}

	public String beanToJson(Object bean) {
		
		if(bean==null)
			return null;
		
		try {
			String json = objectMapper.writeValueAsString(bean);
			return json;
		} catch (Exception e){
			log.error("resolve beans to json error!\r\njson:"+String.valueOf(bean),e);
		}
		
		return null;
	}

	public <T> List<T> jsonToBeanList(String json, Class<T> beanClass) {
		try {
			
			List<T> data = objectMapper.readValue(json, new TypeReference<List<T>>() {}); 
			
			return data;
			
		} catch (Exception e){
			log.error("resolve json to beans error!\r\njson:"+json,e);
		}
		
		return null;
	}

}
