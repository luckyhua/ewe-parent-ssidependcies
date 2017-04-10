package com.ewe.exec.executor.despatcher;

import com.ewe.exec.executor.DataExecutor;

public class ExecutorTask <T> implements Runnable{

	private T data;
	
	private DataExecutor<T> executor;

	public static <T> ExecutorTask<T> create(T data,DataExecutor<T> executor){
		ExecutorTask<T> task = new ExecutorTask<>();
		task.data = data;
		task.executor = executor;
		return task;
	}
	
	@Override
	public void run() {
		executor.execute(data);
	}
}
