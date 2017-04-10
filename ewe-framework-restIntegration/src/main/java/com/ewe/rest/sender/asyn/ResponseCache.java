package com.ewe.rest.sender.asyn;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ewe.rest.resp.CachedResponse;
import com.ewe.rest.sender.HttpSender;

public class ResponseCache {

	private  Map<HttpSender<?>, Map<String, CachedResponse<?>>> cached = new ConcurrentHashMap<>();
	
	public static ResponseCache create(){
		return InstanceHolder.instance;
	}
	
	public <T> void add(HttpSender<T> sender,CachedResponse<T> response){
		synchronized (this) {
			Map<String, CachedResponse<?>> map = this.cached.get(sender);
			if(map==null){
				map = new ConcurrentHashMap<>();
				map.put(response.getId(), response);
				this.cached.put(sender, map);
			}
		}
	}
	
	public <T> void clear(HttpSender<T> sender,String responseId){
		synchronized (this) {
			Map<String, CachedResponse<?>> map = this.cached.get(sender);
			if(map!=null){
				map.remove(responseId);
			}
		}
	}
	
	
	private static class InstanceHolder{
		public static ResponseCache instance = new ResponseCache();
	}
}
