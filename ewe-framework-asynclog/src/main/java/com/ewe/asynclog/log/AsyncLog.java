package com.ewe.asynclog.log;

import com.ewe.exec.executor.DataExecutor;

public interface AsyncLog<T> {
	
	public void write(String key,T log);
	
	public void log(T log);

	public void setExecutor(DataExecutor<T> executor);
	
	public String getName(String name);
	
	public String setName(String name);
}
