package test.log.simplelog;

import redis.clients.jedis.Jedis;

import com.ewe.exec.executor.DataExecutor;
import com.ewe.framework.redis.RedisUtils;

public class MockRedisExecutor implements DataExecutor<String>{

	private static final String Prefix_Id = "data:log:id_log:";
	
	@Override
	public boolean execute(String data) {
		Jedis jedis = null;
		try {
			jedis = RedisUtils.getJedis();
			Long id = jedis.incr("idkey");
			jedis.set(Prefix_Id+id, data);
		} catch (Exception e) {
		}finally{
			RedisUtils.closeJedis(jedis);
		}
		return true;
	}

	@Override
	public String getName() {
		return "test";
	}

}
