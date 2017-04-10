package com.ewe.exec.poller;

import java.util.List;

import com.ewe.exec.cache.ResourceCache;
import com.ewe.exec.executor.despatcher.Despatcher;

public class SimpleTimePoller{

	private ResourceCache<?> cache;
	
	private Despatcher despatcher;
	
	private Long timePeriods;
	
	private String executorName;
	
	private boolean start;
	
	public void start(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				doStart();
			}
		}).start();
	}
	
	private void doStart(){
		this.start = true;
		
		while(start){
			try {
				Thread.sleep(this.timePeriods);
			} catch (InterruptedException e) {
			}
			//get data from cache
			Integer sizes = this.cache.sizes();
			if(sizes==0)
				continue;
			//send execute
			List<?> datas = this.cache.clear();
			this.despatcher.doDespatcher(datas, executorName);
		}
	}

	public ResourceCache<?> getCache() {
		return cache;
	}

	public void setCache(ResourceCache<?> cache) {
		this.cache = cache;
	}

	public Despatcher getDespatcher() {
		return despatcher;
	}

	public void setDespatcher(Despatcher despatcher) {
		this.despatcher = despatcher;
	}

	public Long getTimePeriods() {
		return timePeriods;
	}

	public void setTimePeriods(Long timePeriods) {
		this.timePeriods = timePeriods;
	}

	public String getExecutorName() {
		return executorName;
	}

	public void setExecutorName(String executorName) {
		this.executorName = executorName;
	}

	public boolean isStart() {
		return start;
	}

	public void setStart(boolean start) {
		this.start = start;
	}
}
