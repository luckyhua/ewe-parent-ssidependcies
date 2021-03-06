/**
 * 
 */
package com.ewe.framework.result.json;

import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ewe.framework.utils.ReflectUtils;


/**
 * @author Yo-Lee:catreekk@sohu.com
 * @version 2014年11月21日上午9:42:55
 */
public class ResultDataManager {

	public static String getBeanName(Object bean) {
		if (bean == null)
			return "vo";
		String key = bean.getClass().getSimpleName();
		key = Character.toLowerCase(key.charAt(0))
				+ key.substring(1, key.length());

		return key;
	}
	
	public static Map<String,Object> getValues(Object bean,String ... fields){
		if(bean == null)
			return null;
		if(fields ==null || fields.length==0)
			return ReflectUtils.describe(bean,true);
		
		Map<String,Object> map = new HashMap<String, Object>();
		
		for(String field:fields){
			Object value = ReflectUtils.getValue(bean, field);
			
			map.put(field, value);
		}
		
		return map;
	}
	
	public static void initIntValue(Object bean){
		if(bean == null)
			return;
		List<String> names = ReflectUtils.getFieldNames(bean.getClass());
		for (String name : names) {
			try {
				PropertyDescriptor pd = new PropertyDescriptor(name,
						bean.getClass());
				Object invoke = pd.getReadMethod().invoke(bean);
				if(invoke != null)
					continue;
				
				Class<?> returnType = pd.getReadMethod().getReturnType();
				if(returnType == Integer.class){
					ReflectUtils.setValue(bean, name, 0);
				}
				if(returnType == Double.class){
					ReflectUtils.setValue(bean, name, 0.0d);
				}
				if(returnType == Long.class){
					ReflectUtils.setValue(bean, name, 0);
				}
			} catch (Exception e) {
			}
		}
	}
	
	public static Map<String,Object> getValuesEx(Object bean,String ... exFields){
		if(bean == null)
			return null;
		if(exFields ==null || exFields.length==0)
			return ReflectUtils.describe(bean,true);
		
		Map<String,Object> map = ReflectUtils.describe(bean,true);
		
		for(String field:exFields){
			map.remove(field);
		}
		
		return map;
	}
}
