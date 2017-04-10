package test.ewe.redismanager;

import org.junit.Test;

import com.ewe.framework.redis.RedisUtils;

public class JedisManagerTestCast {

	@Test
	public void testGetJedis(){
		RedisUtils.getJedis();
	}
}
