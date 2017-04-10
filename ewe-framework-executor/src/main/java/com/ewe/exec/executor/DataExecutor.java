package com.ewe.exec.executor;

public interface DataExecutor<T> {

	public boolean execute(T data);
	
	public String getName();
	
}
