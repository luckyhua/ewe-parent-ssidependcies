package com.ewe.exec.cache;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface ResourceCache <T>{

	public static final Logger log = LoggerFactory.getLogger(ResourceCache.class);
	
	public Integer sizes();
	
	public List<T> clear();
	
	public T pop();
	
	public void add(T data);
	
	public String getName();
}
