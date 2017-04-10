package com.ewe.rest.resp;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

public class SimpleResponse<T> implements EweResponse<T> {

	private HttpResponse resp;

	private ObjectMapper om;

	private Class<T> entityClass;

	private TypeReference<T> type;

	private T data;

	public static <T> EweResponse<T> create(Class<T> entityClass,
			HttpResponse response, ObjectMapper om) {

		SimpleResponse<T> resp = new SimpleResponse<T>();
		resp.entityClass = entityClass;
		resp.om = om;
		resp.resp = response;

		try {
			if (entityClass == null) {
				return resp;
			}

			String string = EntityUtils.toString(resp.resp.getEntity());

			if (log.isInfoEnabled())
				log.info("try to parse return data:{}", string);

			if (string == null || string.isEmpty())
				return resp;

			if (resp.entityClass == String.class) {
				resp.data = (T) string;
				return resp;
			}
			
			T data = om.readValue(string, entityClass);
			resp.data = data;
		} catch (ParseException | IOException e) {
			log.error("error when create response data:" + e.getMessage(), e);
		}

		return resp;
	}

	public static <T> EweResponse<T> create(TypeReference<T> type,
			HttpResponse response, ObjectMapper om) {

		SimpleResponse<T> resp = new SimpleResponse<T>();
		resp.om = om;
		resp.resp = response;
		resp.type = type;

		try {
			if (type == null) {
				return resp;
			}

			String string = EntityUtils.toString(resp.resp.getEntity());

			if (log.isInfoEnabled())
				log.info("try to parse return data:{}", string);

			if (string == null || string.isEmpty())
				return resp;

			if (resp.entityClass == String.class) {
				resp.data = (T) string;
				return resp;
			}

			T data = om.readValue(string, type);
			resp.data = data;
		} catch (ParseException | IOException e) {
			log.error("error when create response data:" + e.getMessage(), e);
		}

		return resp;
	}

	@Override
	public T getContentData() {
		return data;
	}

	@Override
	public HttpResponse getHttpResponse() {
		return resp;
	}

}
