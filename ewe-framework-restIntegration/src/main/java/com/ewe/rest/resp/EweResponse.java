package com.ewe.rest.resp;

import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface EweResponse<T> {
	
	public static final Logger log = LoggerFactory.getLogger(EweResponse.class);

	public T getContentData();
	
	public HttpResponse getHttpResponse();
}
