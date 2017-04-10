package com.ewe.rest.req;

import org.apache.http.client.methods.HttpRequestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ewe.rest.resp.EweResponse;

public interface EweRequest <T>{
	
	public static Logger log = LoggerFactory.getLogger(EweRequest.class);
	
	public EweRequest<T> addHeader(String name,String value);
	
	public EweRequest<T> addParam(String name,String value);

	public EweRequest<T> addParam(String name,String value,boolean enCoded);
	
	public HttpRequestBase build();
	
	public EweResponse<T> send();
	
}
