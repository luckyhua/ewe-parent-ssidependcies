package com.ewe.exec.executor.despatcher;

import java.util.List;

import com.ewe.exec.executor.DataExecutor;

public interface Despatcher {

	public <T> void doDespatcher(List<T> datas,String executorName);
	
	public void addExecutor(DataExecutor<?> executor);
}
