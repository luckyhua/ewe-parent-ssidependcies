package com.ewe.framework.result.json;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ewe.framework.context.model.PageInfo;

/**
 * global return result
 * @author Yo-Lee:catreekk@sohu.com
 * @version 2014年11月21日下午4:10:20
 */
public class JsonResult implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String Page_Data_Key = "pagedata";
	public static final String Page_Info_Key = "pageinfo";
	
	public static final String Success = "0";
	public static final String Info = "1";
	public static final String Warn = "2";
	public static final String Error = "3";
	
	private String code;
	private String msg;
	private Map<String,Object> data=new HashMap<String, Object>();
	
	public void setPage(PageInfo pageInfo,List<?> pageData,String ... inFields){
		this.putListData(Page_Data_Key, pageData, inFields);
		this.data.put(Page_Info_Key, pageInfo);
	}
	public void setPageInfo(PageInfo pageInfo){
		if(pageInfo==null)
			return;
		this.data.put(Page_Info_Key, pageInfo);
	}
	
	/**
	 * this method will put list to return json data using key<p>
	 * pagedata,and the fields is that bean's field need to be convert to<p>
	 * json witch in list<p>
	 * @see SysUserConfig
	 * @param data
	 * @param fields
	 */
	public void setPageData(List<?> data,String ... fields){
		this.putListData(Page_Data_Key, data, fields);
	}
	
	public void putData(String key,Object value){
		if(value instanceof Describle){
			this.data.put(key, ((Describle) value).describ());
			return;
		}
		this.data.put(key, value);
	}
	/**
	 * use the bean class simple name to key witch the first work is lower case
	 * @param bean the field that return to app
	 * @param fields
	 */
	public void putBeanData(Object bean,String ...fields){
		String key = ResultDataManager.getBeanName(bean);
		Map<String, Object> values = ResultDataManager.getValues(bean, fields);
		data.put(key, values);
	}
	/**
	 * use the user key to put bean <p>
	 * @see #putBeanData(Object, String...)
	 * @param key key in return json
	 * @param bean data
	 * @param fields fields is bean data
	 */
	public void putBeanData(String key,Object bean,String ...fields){
		if(bean instanceof Map){
			data.put(key, bean);
			return;
		}
		
		if(fields==null||fields.length==0){
			ResultDataManager.initIntValue(bean);
			data.put(key, bean);
			return;
		}
		Map<String, Object> values = ResultDataManager.getValues(bean, fields);
		data.put(key, values);
	}
	/**
	 * put list into data use customer key
	 * @see #setPageData(List, String...)
	 * @param key
	 * @param listBean
	 * @param inFields
	 */
	@SuppressWarnings("unchecked")
	public void putListData(String key,List<?> listBean,String ... inFields){
		List<Map<String,Object>> retData = new ArrayList<Map<String, Object>>();
		
		if(listBean == null){
			data.put(key, new ArrayList<Object>());
			return;
		}
		
		for(Object bean:listBean){
			if(bean instanceof Map){
				retData.add((Map<String, Object>) bean);
				continue;
			}
			
			if(bean instanceof Describle){
				Map<String, Object> values = ((Describle) bean).describ();
				retData.add(values);
				continue;
			}
			Map<String, Object> values = ResultDataManager.getValues(bean, inFields);
			retData.add(values); 
		}
		data.put(key, retData);
	}
	
	/**
	 * not suggest to use this method,this method will return the<p>
	 * field witch not in exFields
	 * @param bean
	 * @param exFields the field not return to app
	 */
	@Deprecated
	public void putBeanDataEx(Object bean,String ...exFields){
		String key = ResultDataManager.getBeanName(bean);
		Map<String, Object> values = ResultDataManager.getValuesEx(bean, exFields);
		data.put(key, values);
	}
	
	public void putBeanDataAll(Object bean){
		String key = ResultDataManager.getBeanName(bean);
		data.put(key, bean);
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public Map<String, Object> getData() {
		return data;
	}
	
	@Deprecated
	public void setData(Map<String, Object> data) {
		this.data = data;
	}
	
	@Override
	public String toString() {
		return "JsonResult [code=" + code + ", msg=" + msg
				+ ", data=" + data + "]";
	}
	
	
}
