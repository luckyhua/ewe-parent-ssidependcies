package test.exec.cache;

import java.util.ArrayList;
import java.util.List;

import redis.clients.jedis.Jedis;

import com.ewe.exec.cache.ResourceCache;
import com.ewe.framework.redis.RedisUtils;

public class SimpleRedisCache<T> implements ResourceCache<T> {

	private static final String Prefix_Data = "test:data:";

	private static final String PREFIX_DATA_LIST = "test:datas";

	public static <T> SimpleRedisCache<T> create(String name){
		SimpleRedisCache<T> cache = new SimpleRedisCache<>();
		return cache;
	}
	
	@Override
	public Integer sizes() {
		Jedis jedis = null;
		try {
			jedis = RedisUtils.getJedis();
			Long size = jedis.llen(PREFIX_DATA_LIST);
			return size.intValue();
		} catch (Exception e) {
		} finally {
			RedisUtils.closeJedis(jedis);
		}
		return 0;
	}

	@Override
	public List<T> clear() {
		Jedis jedis = null;
		try {
			jedis = RedisUtils.getJedis();
			List<T> data = new ArrayList<>();
			synchronized (this) {
				List<String> ids = jedis.lrange(PREFIX_DATA_LIST, 0, -1);
				jedis.del(PREFIX_DATA_LIST);
				for (String id : ids) {
					String string = jedis.get(id);
					data.add((T) string);
					jedis.del(id);
				}
			}
			return data;
		} catch (Exception e) {
		} finally {
			RedisUtils.closeJedis(jedis);
		}
		return new ArrayList<>();
	}

	@Override
	public T pop() {
		return null;
	}

	@Override
	public void add(T data) {
		Jedis jedis = null;
		try {
			jedis = RedisUtils.getJedis();
			Long id = jedis.incr("idkey");
			jedis.set(Prefix_Data+id, data.toString());
			jedis.lpush(PREFIX_DATA_LIST, Prefix_Data+id);
		} catch (Exception e) {
		} finally {
			RedisUtils.closeJedis(jedis);
		}
	}

	@Override
	public String getName() {
		return "testcache2";
	}

}
