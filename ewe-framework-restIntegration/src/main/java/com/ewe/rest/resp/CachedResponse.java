package com.ewe.rest.resp;

import java.io.IOException;
import java.util.UUID;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;

public class CachedResponse<T> implements EweResponse<T> {

	private String id;

	private HttpResponse resp;

	private ObjectMapper om;

	private Class<T> entityClass;

	private T data;

	public static <T> CachedResponse<T> create(Class<T> entityClass, ObjectMapper om){
		
		CachedResponse<T> resp = new CachedResponse<T>();
		String id = UUID.randomUUID().toString().replace("-", "");
		resp.id = id;
		resp.om = om;
		resp.entityClass = entityClass;
		
		return resp;
	}
	
	public CachedResponse<T> buildContentData(HttpResponse response) {
		
		this.resp = response;

		try {
			String string = EntityUtils.toString(response.getEntity());
			
			if(log.isInfoEnabled())
				log.info("try to parse return data:{}",string);
			
			if(string==null || string.isEmpty())
				return this;
			
			if (this.entityClass == String.class) {
				this.data = (T) string;
			}
			
			T data = om.readValue(string, entityClass);
		} catch (ParseException | IOException e) {
			log.error("error when create response data:"+e.getMessage(),e);
		}

		return this;
	}

	@Override
	public T getContentData() {
		return data;
	}
	
	public void setContentData(T data){
		this.data = data;
	}
	
	public void setHttpResponse(HttpResponse response){
		this.resp = response;
	}

	public String getId() {
		return id;
	}

	@Override
	public HttpResponse getHttpResponse() {
		return resp;
	}

}
