package test.exec.cache;

import org.junit.Test;

import com.ewe.exec.cache.ResourceCache;
import com.ewe.exec.cache.SimpleMemCache;
import com.ewe.exec.executor.despatcher.ThreadPoolDespatcher;
import com.ewe.exec.poller.SimpleTimePoller;

public class CacheTest {

	@Test
	public void testcache(){
		MockExecutor executor = new MockExecutor();
		SimpleMemCache<String> cache = SimpleMemCache.create("testcache");
		ThreadPoolDespatcher despatcher = new ThreadPoolDespatcher();
		despatcher.addExecutor(executor);
		
		SimpleTimePoller poller = new SimpleTimePoller();
		poller.setTimePeriods(1000l);
		poller.setDespatcher(despatcher);
		poller.setCache(cache);
		poller.setExecutorName("test");
		
		poller.start();
		
		for (int i = 0; i < 100; i++) {
			cache.add("data:"+i);
		}
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
		}
	}
	
	@Test
	public void testrediscache(){
		MockExecutor executor = new MockExecutor();
		ResourceCache<String> cache = SimpleRedisCache.create("testcache");
		ThreadPoolDespatcher despatcher = new ThreadPoolDespatcher();
		despatcher.addExecutor(executor);
		
		SimpleTimePoller poller = new SimpleTimePoller();
		poller.setTimePeriods(1000l);
		poller.setDespatcher(despatcher);
		poller.setCache(cache);
		poller.setExecutorName("test");
		
		poller.start();
		
		for (int i = 0; i < 100; i++) {
			cache.add("data:"+i);
		}
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
		}
	}
}
