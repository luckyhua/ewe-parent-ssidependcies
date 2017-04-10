package com.ewe.common.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SerializeUtils {

	private static Log log = LogFactory.getLog(SerializeUtils.class);
	
	public static Map<byte[], byte[]> getBytesMap(Object bean) {
		Map<String, Object> map = ReflectUtils.describe(bean);
		Map<byte[], byte[]> retMap = new HashMap<byte[], byte[]>();
		for (String key : map.keySet()) {
			Object value = map.get(key);
			
			if(value instanceof Serializable){
				retMap.put(key.getBytes(), serialize(map.get(key)));
			}else{
				log.warn("bean :"+bean.getClass().getSimpleName()+" field:"+key+" do not implements serializable!");
			}
			
		}
		return retMap;
	}

	public static byte[] serialize(Object obj) {
		byte[] bytes = null;
		
		if(!( obj instanceof Serializable)){
			return null;
		}
		
		try {
			// object to bytearray
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			ObjectOutputStream oo = new ObjectOutputStream(bo);
			oo.writeObject(obj);

			oo.flush();
			bytes = bo.toByteArray();

			bo.flush();
			bo.close();
			oo.close();
		} catch (Exception e) {
			System.out.println("translation" + e.getMessage());
			e.printStackTrace();
		}
		return (bytes);
	}

	public static Object deSerialize(byte[] bytes) {
		Object obj = null;
		try {
			// bytearray to object
			ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
			ObjectInputStream oi = new ObjectInputStream(bi);

			obj = oi.readObject();

			bi.close();
			oi.close();
		} catch (Exception e) {
			System.out.println("translation" + e.getMessage());
			e.printStackTrace();
		}
		return obj;
	}
	
	public static Map<String,Object> byteMapToMap(Map<byte[],byte[]> properties){
		Map<String,Object> map = new HashMap<>();
		
		for(byte[] key : properties.keySet()){
			String str = new String(key);
			Object value = deSerialize(properties.get(key));
			
			map.put(str, value);
		}
		return map;
	}
	
	public static Map<byte[],byte[]> mapToByteMap(Map<String,Object> map){
		Map<byte[], byte[]> retMap = new HashMap<byte[], byte[]>();
		for (String key : map.keySet()) {
			Object value = map.get(key);
			
			if(value instanceof Serializable){
				retMap.put(key.getBytes(), serialize(map.get(key)));
				
			}else{
				
				log.warn(" field:"+key+" do not implements serializable!");
				
			}
		}
		return retMap;
	}
	
	public static <T> T populate(T bean,Map<byte[],byte[]> properties){
		try {
			
			Map<String,Object> map = byteMapToMap(properties);
			
			BeanUtils.populate(bean, map);
			
			return bean;
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
			log.error("not writable method:"+e.getCause());
		}
		return null;
	}
	
	public static <T> T populate(Class<T> beanClass,Map<byte[],byte[]> properties){
		try {
			T bean = beanClass.newInstance();
			return populate(bean, properties);
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			log.error("not default constructor!"+e.getCause());
		}
		return null;
	}
	
}
