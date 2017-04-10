package com.ewe.common.utils;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

public class JsonUtils {

	public static String jsonToString(Object json){
		try {
			String string = m.writeValueAsString(json);
			return string;
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static ObjectMapper m = new ObjectMapper();
	static{
		m.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, true);
	}
	public static <T> T jsonToObject(String json,Class<T> beanClass){
		try {
			if(json==null)
				return null;
			T bean = m.readValue(json, beanClass);
			return bean;
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
