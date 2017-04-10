package com.ewe.framework.context;

import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * ClassName:MsgConfig
 * Description:读取配置文件
 *
 * @author luckyhua
 * @Date 2015年6月14日下午8:08:34
 * @version 
 *
 */
public class MsgConfig {

	private static Map<String,String> msgs;
	private static Log log = LogFactory.getLog(MsgConfig.class);
	private static Map<String,Map<String,String>> msgCache = new HashMap<String,Map<String,String>>();
	
	static{
		msgs = new HashMap<String, String>();
		try {
			ResourceBundle bundle = ResourceBundle.getBundle("config/messages");
			Enumeration<String> keys = bundle.getKeys();
			
			while(keys.hasMoreElements()){
				String key = keys.nextElement();
				msgs.put(key, bundle.getString(key));
				
				if(log.isDebugEnabled())
					log.debug("init one msg key:"+key+";msg:"+bundle.getString(key));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("msg init fail!",e);
		}
	}
	
	/**
	 * 
	  * @Title: getMsg
	  * @Description: 根据code取值
	  * @param code
	  * @return    
	  * @return String   
	  *
	 */
	public static String getMsg(String code){
		if(code==null||code.isEmpty())
			return null;
		return msgs.get(code);
	}
	
	/**
	 * 
	  * @Title: getMsg
	  * @Description: 根据code取值，传入配置参数
	  * @param code
	  * @param params
	  * @return    
	  * @return String   
	  *
	 */
	public static String getMsg(String code, Object ... params){
		String pattern = getMsg(code);
		String msg = MessageFormat.format(pattern, params);
		return msg;
	}
	
	/**
	 * this method to get one value from properties find by the path<P>
	 * this will cache the all key-value in properties to mem <P>
	 * that mean one path only read one times
	 * @param path	properties bundle location,like core/message
	 * @param key
	 * @return
	 */
	public static String getMsgFromResource(String path, String key){
		Map<String, String> oldMap = msgCache.get(path);
		if(oldMap != null){
			String value = oldMap.get(key);
			if(value != null)
				return value;
		}
		try {
			ResourceBundle bundle = ResourceBundle.getBundle(path);
			Enumeration<String> keys = bundle.getKeys();
			Map<String,String> newMsg = new HashMap<>();
			while(keys.hasMoreElements()){
				String _key = keys.nextElement();
				newMsg.put(_key, bundle.getString(_key));
				if(log.isDebugEnabled())
					log.debug("init one msg key:"+key+";msg:"+bundle.getString(key));
			}
			msgCache.put(path, newMsg);
			return newMsg.get(key);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("msg init fail!",e);
		}
		return null;
	}
}
