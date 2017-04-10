package com.ewe.rest.sender;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.ewe.rest.req.EweGet;
import com.ewe.rest.req.EweMultiPost;
import com.ewe.rest.req.EwePost;
import com.ewe.rest.req.EweRequest;
import com.ewe.rest.resp.EweResponse;
import com.ewe.rest.resp.SimpleResponse;

public class SimpleSender<T> implements HttpSender<T>{

	private HttpClient httpClient;
	
	private Class<T> entityClass;
	
	private TypeReference<T> type;
	
	private ObjectMapper om;
	
	@Override
	public EweResponse<T> sendRequest(EweRequest<T> request) {
		try {
			
			HttpResponse response = httpClient.execute(request.build());
			if(entityClass!=null)
				return SimpleResponse.create(entityClass, response, om);
			else {
				return SimpleResponse.create(type, response, om);
			}
			
		} catch (ClientProtocolException e) {
			log.error("error when send request:"+e.getMessage(),e);
		} catch (IOException e) {
			log.error("error when send request:"+e.getMessage(),e);
		}
		return null;
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

	public HttpClient getHttpClient() {
		return httpClient;
	}

	public void setHttpClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}

	public Class<T> getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	public ObjectMapper getOm() {
		return om;
	}

	public void setOm(ObjectMapper om) {
		this.om = om;
	}

	public TypeReference<T> getType() {
		return type;
	}

	public void setType(TypeReference<T> type) {
		this.type = type;
	}
}
