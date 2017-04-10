package com.ewe.exec.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SimpleMemCache<T> implements ResourceCache<T>{

	private BlockingQueue<T> cache = new LinkedBlockingQueue<>();
	
	private String name;
	
	private SimpleMemCache(){
		
	}
	
	public static <T> SimpleMemCache<T> create(String name){
		SimpleMemCache<T> instance = new SimpleMemCache<>();
		instance.name = name;
		return instance;
	}
	
	@Override
	public Integer sizes() {
		
		return cache.size();
	}

	@Override
	public List<T> clear() {
		List<T> retList = new ArrayList<>();
		
		synchronized (this) {
			retList.addAll(cache);
			this.cache.clear();
		}
		
		return retList;
	}

	@Override
	public T pop() {
		return this.cache.poll();
	}

	@Override
	public void add(T data) {
		if(data!=null){
			try {
				this.cache.put(data);
			} catch (InterruptedException e) {
				log.error("error to add cache:{}",e.getMessage());
			}
		}
	}

	@Override
	public String getName() {
		return name;
	}
	
}
