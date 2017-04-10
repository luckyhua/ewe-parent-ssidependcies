package com.ewe.framework.cache.query;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import redis.clients.jedis.Jedis;

import com.ewe.framework.cache.context.CacheKey;
import com.ewe.framework.redis.RedisUtils;

/**
 * this class is the base class for query cache<p>
 * mean that the business query cache should extends this class<p>
 * this class used method template design pattern to define <p>
 * the cache save or query flow <p>
 * also provide and default method to convert data to json or inversion<p>
 * if you want make your own converter to specific your json or data<p>
 * you should override the method {@link #resolveBeansToJson(List)} and method {@link #resolveJsonToBeans(String)}<p>
 * we only support the json structure<p>
 * to use this class you will implements method {@link #getCacheStatusKey(Map)} and method {@link #getQueryKey(Map)}
 * @author Lee-yo
 * 2015-6-3
 * @param <T>
 */
public abstract class AbstractQueryCache<T> {
	
	protected final Log log = LogFactory.getLog(AbstractQueryCache.class);
	
	protected final Integer Cache_Exists_Times = 3;
	
	protected final String Cache_Status_Value = "1";
	
	protected Class<T> beanClass;
	
	private ObjectMapper mapper = new ObjectMapper();
	
	/**
	 * thread pool used to execute synchronized data task<p>
	 * init this thread pool will open the computer's processors * 4 threads<p>
	 * that mean one processors may be handle 4 thread
	 */
	protected static final ExecutorService Cache_Despathcer = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*4);
	
	
	/**
	 * this method query from the redis use the params to maked key<p>
	 * notice that this method will check the cache status key to improve efficiency<p>
	 * that mean if the status key is out of time,this method could not query the data<p>
	 * though the data may exists!
	 * @see #getCacheStatusKey(Map)
	 * @see #getQueryKey(Map)
	 * @param params the query params
	 * @return data if hits cache
	 */
	public List<T> queryByDetails(Map<String, Object> params){
		
		if(log.isInfoEnabled())
			log.info("query in query cache use params:\r\n"+String.valueOf(params));
		
		String queryKey = this.getQueryKey(params);
		
		if(!this.isInCache(params)){
			if(log.isInfoEnabled())
				log.info("cache not hits use \r\nparams:"+String.valueOf(params));
		
			return null;
		}
		
		Jedis jedis = null;
		try {
			jedis = RedisUtils.getJedis();
			
			String json = jedis.get(queryKey);
			
			if(json ==null || json.isEmpty()){
				
				if(log.isInfoEnabled())
					log.info("cache not hits use \r\nparams:"+params.toString());
				
				return null;
			}
			
			if(log.isInfoEnabled())
				log.info("cache hits with the \r\n params:"+params.toString()+"\r\n results:"+json);
			return this.resolveJsonToBeans(json);
			
		} catch (Exception e) {
			log.error("error in query in redis:\r\n params:"+params.toString(),e);
			return null;
		}finally{
			RedisUtils.closeJedis(jedis);
		}
		
	}
	
	/**
	 * this method is to add data to cache and refresh the cache status key<p>
	 * notice that this method is asynchronized method <p>
	 * that mean data may not save in cache immediately<p>
	 * @param params
	 * @param data
	 */
	public void  addToCache(final Map<String, Object> params,final List<T> data){
		
		if(this.isInCache(params))
			return;
		
		Cache_Despathcer.execute(new Runnable() {
			
			public void run() {
				
				if(data==null || data.isEmpty()){
					addForNullResults(params);
					return;
				}
				
				synAddToCache(params,data);
				if(log.isInfoEnabled())
					log.info("one add to cache task execute!\r\nparams:"+String.valueOf(params)+"\r\n data:"+String.valueOf(data));
			}
		});
	}
	
	/**
	 * this method used to add data to redis and it is synchronized method<p>
	 * @see #addToCache(Map, List)
	 * @param params
	 * @param data
	 * @return
	 */
	protected boolean synAddToCache(Map<String, Object> params,List<T> data){
		
		if(data == null || data.isEmpty())
			return this.addForNullResults(params);
		
		if(this.isInCache(params))
			return true;
		
		Jedis jedis = null;
		try {
			jedis = RedisUtils.getJedis();
			jedis.set(this.getQueryKey(params), this.resolveBeansToJson(data));
			
			return this.setCacheStatus(params);
		} catch (Exception e) {
			log.error("add to cache error!\r\n params:"+params.toString()+"\r\ndata:"+data.toString(),e);
		}finally{
			RedisUtils.closeJedis(jedis);
		}
		
		return false;
	}
	
	/**
	 * this method used to avoid cache strike<p>
	 * that mean if one params have on data to mapping<p>
	 * if use query used this params this invoke will through the cache<p>
	 * to mysql witch mysql haven't the data either<p>
	 * this situation will be avoid by this method<p>
	 * @param params
	 * @return
	 */
	protected boolean addForNullResults(Map<String, Object> params){
		if(this.isInCache(params))
			return true;
		
		Jedis jedis = null;
		try {
			jedis = RedisUtils.getJedis();
			jedis.set(this.getQueryKey(params), "");
			return this.setCacheStatus(params);
		} catch (Exception e) {
			
			log.error("add for null params error: \r\nparams:"+String.valueOf(params),e);
			
		}finally{
			RedisUtils.closeJedis(jedis);
		}
		return false;
	}
	
	/**
	 * refresh the cache status if the status key is out of time<p>
	 * to make this key system can easier to find cache have the data<p>
	 * or not and also  can improve the efficiency
	 * @param params
	 * @return
	 */
	protected boolean setCacheStatus(Map<String, Object> params){
		Jedis jedis = null;
		try {
			jedis = RedisUtils.getJedis();
			jedis.setex(this.getCacheStatusKey(params),Cache_Exists_Times, Cache_Status_Value);
			return true;
		} catch (Exception e) {
			log.error("set cache status error:\r\n params:"+params.toString(),e);
		}finally{
			RedisUtils.closeJedis(jedis);
		}
		return false;
	}
	
	protected boolean isInCache(Map<String, Object> params) {
		if(params==null)
			return false;
		
		Jedis jedis = null;
		try {
			jedis = RedisUtils.getJedis();
			
			String status = jedis.get(this.getCacheStatusKey(params));
			
			if(status==null || status.isEmpty()){
				
				if(log.isInfoEnabled())
					log.info("query cache status false for \r\nparams :"+params.toString());
				
				return false;
			}
			
			if(log.isInfoEnabled())
				log.info("query cache status true for \r\nparams :"+params.toString());
			
			return true;
			
		} catch (Exception e) {
			
			log.error("query is in cache error:\r\nqueryKey:"+params.toString(),e);
			
		}finally{
			RedisUtils.closeJedis(jedis);
		}
		
		return false;
	}
	
	protected List<T> resolveJsonToBeans(String json) {
		
		if(json==null||json.isEmpty())
			return null;
		
		try {
			
			List<T> data = mapper.readValue(json, new TypeReference<List<T>>() {}); 
			
			return data;
			
		} catch (Exception e){
			log.error("resolve json to beans error!\r\njson:"+json,e);
		}
		
		return null;
	}
	
	protected String resolveBeansToJson(List<T> data){
		if(data==null)
			return null;
		
		try {
			String json = mapper.writeValueAsString(data);
			return json;
		} catch (Exception e){
			log.error("resolve beans to json error!\r\njson:"+data,e);
		}
		
		return null;
	}
	
	/**
	 * this method is used to convert query params to queryKey<p>
	 * witch used in redis<p>
	 * notice that for satisfy the reference doc<p>
	 * your key will compose with cache system key prefix and business key and key-value mappin subfix<p>
	 * such as :<h4>cache:shop:project:querycache:queryKey_results:${queryKey}</h4>
	 * also the params is <h3>null or empty</h3> is one situation you should handle it!
	 * @see CacheKey
	 * @param params the all query params
	 * @return the cache query key used in redis full key
	 */
	public abstract String getQueryKey(Map<String, Object> params);
	
	/**
	 * this method used  to convert query params to cache status key<p>
	 * query cache will get this key to judge if the cache is exist<p>
	 * <h3>notice that you could'nt make this key the same with query key!<h3><p>
	 * 
	 * @see CacheKey
	 * @param params params the all query params
	 * @return the cache status key
	 */
	public abstract String getCacheStatusKey(Map<String, Object> params);
}
