package com.ewe.rest.sender.asyn;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.codehaus.jackson.map.ObjectMapper;

import com.ewe.rest.req.EweGet;
import com.ewe.rest.req.EweMultiPost;
import com.ewe.rest.req.EwePost;
import com.ewe.rest.req.EweRequest;
import com.ewe.rest.resp.CachedResponse;
import com.ewe.rest.resp.EweResponse;
import com.ewe.rest.sender.HttpSender;

public class AsynSender<T> implements HttpSender<T>{

	private ExecutorService despathcer = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*4);
	
	private HttpSender<T> sender;
	
	private HttpCallBack<T> caller;
	
	private ResponseCache cache;
	
	@Override
	public EweResponse<T> sendRequest(EweRequest<T> request) {
		CachedResponse<T> response = CachedResponse.create(sender.getEntityClass(), sender.getOm());
		cache.add(sender, response);
		despathcer.execute(SendTask.create(sender, request,cache,response, caller));
		return response;
	}

	@Override
	public EweGet<T> createGet(String url) {
		EweGet<T> get = EweGet.create(url, this);
		return get;
	}

	@Override
	public EwePost<T> createPost(String url) {
		EwePost<T> post = EwePost.create(url, this);
		return post;
	}

	@Override
	public EweMultiPost<T> createMultilyPost(String url) {
		EweMultiPost<T> multi = EweMultiPost.create(url, this);
		return multi;
	}

	@Override
	public Class<T> getEntityClass() {
		return sender.getEntityClass();
	}
	
	@Override
	public ObjectMapper getOm() {
		return sender.getOm();
	}

	public HttpSender<T> getSender() {
		return sender;
	}

	public void setSender(HttpSender<T> sender) {
		this.sender = sender;
	}

	public HttpCallBack<T> getCaller() {
		return caller;
	}

	public void setCaller(HttpCallBack<T> caller) {
		this.caller = caller;
	}

	public ResponseCache getCache() {
		return cache;
	}

	public void setCache(ResponseCache cache) {
		this.cache = cache;
	}
	
	public void shutDown(){
		this.despathcer.shutdown();
	}
}
