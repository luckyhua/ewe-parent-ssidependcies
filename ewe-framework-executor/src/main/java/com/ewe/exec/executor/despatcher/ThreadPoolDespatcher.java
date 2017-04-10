package com.ewe.exec.executor.despatcher;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.ewe.exec.context.ExecutException;
import com.ewe.exec.executor.DataExecutor;

public class ThreadPoolDespatcher implements Despatcher{

	private Map<String, DataExecutor<?>> executors = new ConcurrentHashMap<String, DataExecutor<?>>();
	
	private Executor despatcher = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*4);
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> void doDespatcher(List<T> datas, String executorName) {
		DataExecutor<T> executor = null;
		try {
			executor =  (DataExecutor<T>) executors.get(executorName);
		} catch (Exception e) {
			ExecutException.createAndThrow("the executor:"+executorName+",is not type of the data!");
		}
		
		for (T data : datas) {
			ExecutorTask<T> task = ExecutorTask.create(data, executor);
			this.despatcher.execute(task);
		}
	}

	@Override
	public void addExecutor(DataExecutor<?> executor) {
		if(executor==null)
			return;
		String name = executor.getName();
		if(name==null||name.isEmpty())
			return;
		this.executors.put(name, executor);
	}

}
