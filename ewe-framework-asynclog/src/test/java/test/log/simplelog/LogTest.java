package test.log.simplelog;

import org.junit.Test;

import com.ewe.asynclog.log.AsyncLog;
import com.ewe.asynclog.log.SimpleLog;

public class LogTest {

	@Test
	public void test(){
		AsyncLog<String> log = SimpleLog.create(new MockRedisExecutor(), "test");
		long begin = System.currentTimeMillis();
		for(int i=0;i<10000;i++){
			log.write("data", "data:"+i);
		}
		System.out.println("run in :"+(System.currentTimeMillis()-begin));
		try {
			Thread.sleep(2000l);
		} catch (InterruptedException e) {
			
		}
	}
}
