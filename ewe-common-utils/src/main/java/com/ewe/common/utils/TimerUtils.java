package com.ewe.common.utils;

import java.util.Stack;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TimerUtils {

	private static ThreadLocal<Stack<Long>> timeSource = new ThreadLocal<Stack<Long>>();
	
	private static Log log = LogFactory.getLog(TimerUtils.class);
	
	public static Long start(){
		checkTime();
		Long start = System.currentTimeMillis();
		timeSource.get().push(start);
		return start;
	}
	
	private static void checkTime(){
		if(timeSource.get()==null)
			timeSource.set(new Stack<Long>());
	}
	
	public static Long end(String msg){
		long end = System.currentTimeMillis();
		checkTime();
		Long last = timeSource.get().pop();
		long start = last==null?end:last;
		
		log.info("msg:"+msg+" run in:"+(end-start)+"ms");
		
		if(timeSource.get().isEmpty())
			timeSource.remove();
		return (end-start);
		
	}
	
	public static Long end(){
		long end = System.currentTimeMillis();
		checkTime();
		Long last = timeSource.get().pop();
		
		
		long start = last==null?end:last;
		
		log.info("sytem:"+" run in:"+(end-start)+"ms");
//		System.out.println("sytem:run in:"+(end-start)+"ms");
		
//		timeSource.remove();
		if(timeSource.get().isEmpty())
			timeSource.remove();
		return (end-start);
		
	}
	
	/*@Test
	public void test(){
		start();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		end();
		
		start();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		end();
	}
	
	@Test
	public void testMult(){
		start();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		start();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		end();
		end();
	}*/
}
