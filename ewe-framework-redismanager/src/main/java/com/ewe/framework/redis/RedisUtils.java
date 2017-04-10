package com.ewe.framework.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * 
 * @author Yo-Lee:catreekk@sohu.com
 * @version 2014年11月21日下午5:30:15
 */
public class RedisUtils {
	protected static Logger log = LoggerFactory.getLogger(RedisUtils.class);

	/**
	 * 私有构造器.
	 */
	private RedisUtils() {

	}

	private static Map<String, JedisPool> pools = new HashMap<String, JedisPool>();
	private static JedisPool pool;
	private static JedisPool pool_backUp;
	private static ResourceBundle bundle = ResourceBundle.getBundle("config/cache/redis");
	private static JedisPoolConfig config;
	private static int tryTimes;

	static {
		if (bundle == null) {
			throw new IllegalArgumentException(
					"[redis.properties] is not found!");
		}
		config = new JedisPoolConfig();
		// config.setMaxActive(Integer.valueOf(bundle
		// .getString("redis.pool.maxActive")));
		config.setMaxIdle(Integer.valueOf(bundle
				.getString("redis.pool.maxIdle").trim()));
		// config.setMaxWait(Long.valueOf(bundle.getString("redis.pool.maxWait")));
		config.setTestOnBorrow(Boolean.valueOf(bundle.getString(
				"redis.pool.testOnBorrow").trim()));
		config.setTestOnReturn(Boolean.valueOf(bundle.getString(
				"redis.pool.testOnReturn").trim()));
		try {
			pool = new JedisPool(config, bundle.getString("redis.ip"),
					Integer.valueOf(bundle.getString("redis.port").trim()),
					Integer.valueOf(bundle.getString("redis.pool.timeOut")));
		} catch (Exception e) {
			log.error("could not connection pool1!",e);
			e.printStackTrace();
		}
		
		try {
			pool_backUp = new JedisPool(config, bundle.getString("redis.ip.backUp"),
					Integer.valueOf(bundle.getString("redis.port.backUp").trim()),
					Integer.valueOf(bundle.getString("redis.pool.timeOut")));
		} catch (Exception e) {
			log.error("could not connection! pool2",e);
			e.printStackTrace();
		}
		
		if(pool==null&&pool_backUp==null)
			throw new RuntimeException("redis俩个连接都用不了");
		
		
		tryTimes = Integer.valueOf(bundle.getString("redis.pool.tryTimes"));

	}

	@SuppressWarnings("unused")
	private static void initPool(){
		
	}
	
	/**
	 * 获取连接池.
	 * 
	 * @return 连接池实例
	 */
	private static JedisPool getPool(String ip, int port) {
		String key = ip + ":" + port;
		JedisPool pool = null;
		if (!pools.containsKey(key)) {
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxIdle(100);
			config.setMaxWaitMillis(100000l);
			config.setTestOnBorrow(true);
			config.setTestOnReturn(true);
			try {
				/**
				 * 如果你遇到 java.net.SocketTimeoutException: Read timed out
				 * exception的异常信息 请尝试在构造JedisPool的时候设置自己的超时值.
				 * JedisPool默认的超时时间是2秒(单位毫秒)
				 */
				pool = new JedisPool(config, ip, port, 20000);
				pools.put(key, pool);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			pool = pools.get(key);
		}
		return pool;
	}
	
	public static JedisPool getPool(){
		return pool;
	}

	/**
	 * 类级的内部类，也就是静态的成员式内部类，该内部类的实例与外部类的实例 没有绑定关系，而且只有被调用到时才会装载，从而实现了延迟加载。
	 */
	/*
	 * private static class RedisUtilHolder{
	 *//**
	 * 静态初始化器，由JVM来保证线程安全
	 */
	/*
	 * private static JedisUtil instance = new JedisUtil(); }
	 */

	/**
	 * 当getInstance方法第一次被调用的时候，它第一次读取
	 * RedisUtilHolder.instance，导致RedisUtilHolder类得到初始化；而这个类在装载并被初始化的时候，会初始化它的静
	 * 态域，从而创建RedisUtil的实例，由于是静态的域，因此只会在虚拟机装载类的时候初始化一次，并由虚拟机来保证它的线程安全性。
	 * 这个模式的优势在于，getInstance方法并没有被同步，并且只是执行一个域的访问，因此延迟初始化并没有增加任何访问成本。
	 */
	/*
	 * public static JedisUtil getInstance() { return RedisUtilHolder.instance;
	 * }
	 */

	/**
	 * 获取Redis实例.
	 * 
	 * @return Redis工具类实例
	 */
	public static Jedis getJedis(String ip, int port) {
		Jedis jedis = null;
		int count = 0;
		do {
			try {
				jedis = getPool(ip, port).getResource();
				// log.info("get redis master1!");
			} catch (Exception e) {
				log.error("get redis master1 failed!", e);
				// 销毁对象
				getPool(ip, port).returnBrokenResource(jedis);
			}
			count++;
		} while (jedis == null && count < 6);
		return jedis;
	}

	/**
	 * 取得redis连接,默认调用getJedis(0)
	 * 需要设置redis.props
	 * 两个ip依次重新连接,尝试连接次数tryTimes,如果无法连接抛出RuntimeException
	 * @return jedis操作对象Jedis,默认连接0号数据库
	 */
	public static Jedis getJedis() {
		return getJedis(0);
	}

	public static Jedis getJedis(int db) {
		Jedis jedis = null;
		int count = 0;
		do {
			try {
				jedis = pool.getResource();
				log.debug("get redis master1!ip:"+bundle.getString("redis.ip")+" jedis:"+jedis);
			} catch (Exception e) {
//				log.error("get redis master1 failed!", e);
				log.error("get redis master1 failed!ip:"+bundle.getString("redis.ip"));
				// 销毁对象
				//pool.returnBrokenResource(jedis);
				try {
					jedis = pool_backUp.getResource();
					log.debug("get redis master2!ip:"+bundle.getString("redis.ip.backUp"));
				} catch (Exception e2) {
//					log.error("get redis master2 failed!ip:127.0.0.1", e);
					log.error("get redis master2 failed!ip:"+bundle.getString("redis.ip"));
					// 销毁对象
					//pool.returnBrokenResource(jedis);
				}
			}
			if (db < 0 || db > 20)
				db = 0;
			if(jedis!=null){
				jedis.select(db);
				return jedis;
			}
			
			count++;
			log.error("无法取得连接,重新尝试,第:"+count+"次");
		} while (jedis == null && count < tryTimes);
		throw new RuntimeException("连接不可用!");
	}

	/**
	 * 释放redis实例到连接池.
	 * 
	 * @param jedis
	 *            redis实例
	 */
	public void closeJedis(Jedis jedis, String ip, int port) {
		if (jedis != null) {
			getPool(ip, port).returnResource(jedis);
			
			if(log.isDebugEnabled())
				log.debug("return one jedis:"+jedis);
		}
	}

	public static void closeJedis(Jedis jedis) {
		if(jedis==null)
			return;
		
		try {
			pool.returnResource(jedis);
			if(log.isDebugEnabled())
				log.debug("return one jedis:"+jedis);
		} catch(Exception e){
			try {
				pool_backUp.returnResource(jedis);
				if(log.isDebugEnabled())
					log.debug("return one jedis:"+jedis);
			} catch (Exception e2) {
				if(jedis!=null)
					jedis.close();
				throw new RuntimeException("未知的连接!");
			}
		}finally {
			
			jedis = null;
		}
	}
}
