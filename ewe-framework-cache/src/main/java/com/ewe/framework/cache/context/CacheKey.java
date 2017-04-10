package com.ewe.framework.cache.context;

/**
 * this class used to all cache system key config<p>
 * notice this class could not used to one specific Cache System!<p>
 *  so that these system should have theses own CacheConfig!
 * @author Lee-yo
 * 2015-6-2
 */
public class CacheKey {

	/**
	 * this field use to assign the cache system keys prefix<p>
	 * notice that all keys in redis that used in cache system<p>
	 * must prefix with this key
	 */
	public static final String Cache_Sys_Key = "cache";
	
	/**
	 * this is the query cache sub sys key used only in query cache
	 */
	public static final String  QueryCache_SubSys_Key = "querycache";
	
	/**
	 * this key only used to details cache ,details must be mappinged by id
	 */
	public static final String DetailsCache_SubSys_Key = "detaislcache";
	/**
	 * this key used to separate the all bussiness keys or specific keys
	 */
	public static final String Cache_Separator = ":";
	
	/**
	 * this key used to separete the all keys mapping relations<p>
	 * like we will use user id to query one user details<p>
	 * the keys mapping may be like that ${prefix}:userId_userDetails:${userId}
	 */
	public static final String Cache_Mapping_Separator = "_";
	
	//such as :cache:querycache:marketplace:shop:project:queryKey_results:${queryKey}
	/**
	 * this method used to concat cache with {@link Cache_Separator}<p>
	 * notice that this method will use prefix with {@link Cache_Sys_Key}<p>
	 * and subfix {@link Cache_Sperator} <p>
	 * 
	 * for the reference doc you will suppose used to {@link #makeCacheKey(String, String, String...)}
	 * 
	 * @see CacheKey.Cache_Sys_Key
	 * @see CacheKey.Cache_Separator
	 * @see CacheKey.Cache_Mapping_Separator
	 * @param keys all bussiness keys to concat togather
	 * @return
	 */
//	@Deprecated
//	public static String makeCacheKey(String ... keys){
//		
//		return null;
//	}
	//such as : cache:querycache:marketplace:shop:project:queryKey_results:${queryKey}
	/**
	 * this method used to concat cache all mapping keys and bussiness keys<p>
	 * notice that to satisfy system reference doc the mappings keys will be<p>
	 * the last in the all concated keys<p>
	 * such as:cache:querycache:marketplace:shop:projects:queryKey_results:${queryKey}<p>
	 * that means we will use one queryKey to get results,so the mapping keys will be queryKey_results<p>
	 * <h2>this method will end with : ! you must append your specific variable</h2>
	 * @param mappingKey the key in redis key for query
	 * @param mappingValue the value in redis get by the mappingKey
	 * @param keys the bussiness keys or system  keys
	 * @return the concated keys
	 */
	public static String makeCacheKey(String subCacheSysKey,String mappingKey,String mappingValue,String ... keys){
		
		if(mappingKey == null || mappingKey.isEmpty())
			mappingKey = "non";
		if(mappingValue==null||mappingValue.isEmpty())
			mappingValue = "non";
		
		StringBuffer ret = new StringBuffer(Cache_Sys_Key+Cache_Separator+subCacheSysKey+Cache_Separator);
		
		if(keys!=null)
			for (String key : keys) {
				ret.append(key).append(Cache_Separator);
			}
		
		return ret.append(mappingKey).append(Cache_Mapping_Separator).append(mappingValue).append(Cache_Separator).toString();
	}
}
