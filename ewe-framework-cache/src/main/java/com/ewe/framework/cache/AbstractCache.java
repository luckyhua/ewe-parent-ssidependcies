package com.ewe.framework.cache;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import redis.clients.jedis.Jedis;

import com.ewe.framework.cache.data.DefaultJsonConvertor;
import com.ewe.framework.cache.data.JsonConvertor;
import com.ewe.framework.redis.RedisUtils;

/**
 * abstract cache class,used to simple operation to access cache<p>
 * notice that some  business cache operations need to implements in children class<p>
 * that's why this class do not have abstract method but is abstract
 * @author Lee-yo
 * 2015-6-8
 */
public abstract class AbstractCache {

	protected static final ExecutorService Cache_Despathcer = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*4);
	
	protected static final Log log = LogFactory.getLog(AbstractCache.class);
	
	protected JsonConvertor jsonConvertor = new DefaultJsonConvertor();
	
	protected void addToCache(final String key,final Object data,final int timeOut){
		
		if(key==null|| key.isEmpty())
			return;
		if(this.isInCache(key))
			return;
		
		Cache_Despathcer.execute(new Runnable() {
			
			public void run() {
				
				if(data==null){
					addForNullData(key);
					return;
				}
				
				syncAddToCache(key,data,timeOut);
				if(log.isInfoEnabled())
					log.info("one add to cache task execute!\r\nparams:"+String.valueOf(key)+"\r\n data:"+String.valueOf(data));
			}
		});
		
	}
	
	protected <T> T queryInCache(String key,Class<T> entityClass){
		if(key==null|| key.isEmpty())
			return null;
		if(entityClass==null)
			return null;
		
		Jedis jedis = null;
		try {
			jedis = RedisUtils.getJedis();
			String json = jedis.get(key);
			T bean = jsonConvertor.jsonToBean(json, entityClass);
			return bean;
		} catch (Exception e) {
			log.error("query in cache error!key:"+key+" entityClass:"+String.valueOf(entityClass));
		}finally{
			RedisUtils.closeJedis(jedis);
		}
		
		return null;
	}
	
	protected <T> List<T> queryListInCache(String key,Class<T> entityClass){
		if(key==null|| key.isEmpty())
			return null;
		if(entityClass==null)
			return null;
		
		Jedis jedis = null;
		try {
			jedis = RedisUtils.getJedis();
			String json = jedis.get(key);
			List<T> beans = jsonConvertor.jsonToBeanList(json, entityClass);
			return beans;
		} catch (Exception e) {
			log.error("query in cache error!key:"+key+" entityClass:"+String.valueOf(entityClass));
		}finally{
			RedisUtils.closeJedis(jedis);
		}
		
		return null;
	}
	
	protected boolean clearCache(String key){
		if(key==null || key.isEmpty())
			return true;
		Jedis jedis = null;
		try {
			jedis = RedisUtils.getJedis();
			Long del = jedis.del(key);
			
			if(log.isInfoEnabled())
				log.info("del key in cache! key:"+key+" nums:"+String.valueOf(del));
			
			return true;
		} catch (Exception e) {
			log.error("clear cache error key:"+key,e);
		}finally{
			RedisUtils.closeJedis(jedis);
		}
		
		return false;
	}
	
	protected boolean isInCache(String key){
		
		if(key==null||key.isEmpty())
			return false;
		
		Jedis jedis = null;
		try {
			jedis = RedisUtils.getJedis();
			Boolean exists = jedis.exists(key);
			return exists;
		} catch (Exception e) {
			log.error("error in query is in cache key:"+key,e);
		}finally{
			RedisUtils.closeJedis(jedis);
		}
		
		return false;
	}
	
	protected boolean syncAddToCache(String key,Object data,final int timeOut){
		if(key==null|| key.isEmpty())
			return false;
		
		if(data==null)
			return this.addForNullData(key);
		
		Jedis jedis = null;
		try {
			jedis = RedisUtils.getJedis();
			jedis.setex(key,timeOut, jsonConvertor.beanToJson(data));
		} catch (Exception e) {
			log.error("add to cache error!key:"+key+" data:"+String.valueOf(data),e);
		}finally{
			RedisUtils.closeJedis(jedis);
		}
		
		return false;
	}
	
	protected boolean addForNullData(String key) {
		Jedis jedis=null;
		try {
			jedis = RedisUtils.getJedis();
			
			jedis.set(key, null);
			
			return true;
		} catch (Exception e) {
			log.error("add for null data error!key:"+key,e);
		}finally{
			RedisUtils.closeJedis(jedis);
		}
		
		return false;
	}
	
	public void setJsonConvertor(JsonConvertor jsonConvertor){
		this.jsonConvertor = jsonConvertor;
	}
}
