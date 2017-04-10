package com.ewe.asynclog.log;

import com.ewe.exec.cache.ResourceCache;
import com.ewe.exec.cache.SimpleMemCache;
import com.ewe.exec.executor.DataExecutor;
import com.ewe.exec.executor.despatcher.ThreadPoolDespatcher;
import com.ewe.exec.poller.SimpleTimePoller;

public class SimpleLog<T> implements AsyncLog<T>{

	private String name;
	
	private DataExecutor<T> executor;
	
	private ResourceCache<T> cache;

	public static <T> AsyncLog<T> create(DataExecutor<T> executor,String name){
		SimpleLog<T> log = new SimpleLog<>();
		log.executor = executor;
		log.name = name;
		
		ResourceCache<T> cache = SimpleMemCache.create(name);
		log.cache = cache;
		ThreadPoolDespatcher despatcher = new ThreadPoolDespatcher();
		despatcher.addExecutor(executor);
		
		SimpleTimePoller poller = new SimpleTimePoller();
		poller.setTimePeriods(1000l);
		poller.setDespatcher(despatcher);
		poller.setCache(cache);
		poller.setExecutorName(executor.getName());
		poller.start();
		
		return log;
	}
	
	@Override
	public void write(String key, T log) {
		this.cache.add(log);
	}

	@Override
	public void setExecutor(DataExecutor<T> executor) {
		this.executor = executor;
	}

	@Override
	public String getName(String name) {
		return this.name;
	}

	@Override
	public String setName(String name) {
		this.name = name;
		return this.name;
	}

	@Override
	public void log(T log) {
		this.cache.add(log);
	}

}
