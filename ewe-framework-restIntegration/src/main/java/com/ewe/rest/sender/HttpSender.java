package com.ewe.rest.sender;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ewe.rest.req.EweGet;
import com.ewe.rest.req.EweMultiPost;
import com.ewe.rest.req.EwePost;
import com.ewe.rest.req.EweRequest;
import com.ewe.rest.resp.EweResponse;

public  interface  HttpSender<T>{

	public static final Logger log = LoggerFactory.getLogger(HttpSender.class);
	
	public EweResponse<T> sendRequest(EweRequest<T> request);
	
	public EweGet<T> createGet(String url);
	
	public EwePost<T> createPost(String url);
	
	public EweMultiPost<T> createMultilyPost(String url);
	
	public Class<T> getEntityClass();
	
	public ObjectMapper getOm();
	
}
