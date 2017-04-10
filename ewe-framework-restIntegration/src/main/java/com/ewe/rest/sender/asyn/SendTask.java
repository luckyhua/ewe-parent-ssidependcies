package com.ewe.rest.sender.asyn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ewe.rest.req.EweRequest;
import com.ewe.rest.resp.CachedResponse;
import com.ewe.rest.resp.EweResponse;
import com.ewe.rest.sender.HttpSender;

public class SendTask<T> implements Runnable{

	private HttpSender<T> sender;
	
	private HttpCallBack<T> caller;
	
	private EweRequest<T> request;
	
	private CachedResponse<T> response;
	
	private ResponseCache cache;
	
	private static final Logger log = LoggerFactory.getLogger(SendTask.class);
	
	public static <T> SendTask<T> create(HttpSender<T> sender,EweRequest<T> request,ResponseCache cache,CachedResponse<T> response,HttpCallBack<T> caller){
		SendTask<T> task = new SendTask<>();
		task.sender = sender;
		task.caller = caller;
		task.request = request;
		task.response = response;
		task.cache = cache;
		return task;
	}
	
	@Override
	public void run() {
		try {
			if(log.isInfoEnabled())
				log.info("one send task run!{}",request);
			
			EweResponse<T> response = sender.sendRequest(request);
			this.response.setContentData(response.getContentData());
			this.response.setHttpResponse(response.getHttpResponse());
			if(caller!=null){
				boolean success = caller.handleResponse(response);
//				if(success){
//					this.cache.clear(sender, this.response.getId());
//				}
			}
			this.cache.clear(sender, this.response.getId());
		} catch (Exception e) {
			log.error("error send reqeust:"+e.getMessage(),e);
			this.cache.clear(sender, this.response.getId());
		}
	}

}
