package com.ewe.rest.req;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicNameValuePair;

import com.ewe.rest.resp.EweResponse;
import com.ewe.rest.sender.HttpSender;

public class EwePost<T> implements EweRequest<T>{

	private HttpPost post;
	
	private Map<String, String> params;
	
	private HttpSender<T> sender;
	
	private String url;
	
	private byte[] body;
	
	public static <T> EwePost<T> create(String url,HttpSender<T> sender){
		if(url==null||url.isEmpty())
			throw new RuntimeException("url could not be null!");
		if(sender==null)
			throw new RuntimeException("sender could not be null!");
		
		EwePost<T> post = new EwePost<>();
		
		post.sender = sender;
		post.url = url;
		post.params = new HashMap<String, String>();
		post.post = new HttpPost(url);
		return post;
	}
	
	@Override
	public EwePost<T> addHeader(String name, String value) {
		if(name==null||name.isEmpty())
			return this;
		post.addHeader(name,value);
		return this;
	}

	@Override
	public EwePost<T> addParam(String name, String value) {
		if(name==null||name.isEmpty())
			return this;
		String encode;
		try {
//			encode = URLEncoder.encode(value,"utf-8");
			this.params.put(name, value);
		} catch (Exception e) {
			log.error("error when encode value:"+e.getMessage(),e);
		}
		return this;
	}
	
	public EweRequest<T> addParam(byte[] body){
		if(body==null||body.length==0)
			return this;
		this.body = body;
		return this;
	}
	
	@Override
	public EweRequest<T> addParam(String name, String value, boolean enCoded) {
		if(name==null||name.isEmpty())
			return this;
		try {
			if(!enCoded)
				value = URLEncoder.encode(value,"utf-8");
			this.params.put(name, value);
		} catch (UnsupportedEncodingException e) {
			log.error("error when encode value:"+e.getMessage(),e);
		}
		return this;
	}

	@Override
	public HttpRequestBase build() {
		
		
		EntityBuilder builder = EntityBuilder.create().setContentEncoding("utf-8").setContentType(ContentType.create("application/x-www-form-urlencoded", Consts.UTF_8));
		
		
		List<NameValuePair> setParmas = new ArrayList<>();
		for(String name:this.params.keySet()){
			setParmas.add(new BasicNameValuePair(name, this.params.get(name)));
		}
		builder.setParameters(setParmas);
		
		if(body!=null){
			builder.setBinary(body);
		}
		
		post.setEntity(builder.build());
		
		return post;
	}

	@Override
	public EweResponse<T> send() {
		return this.sender.sendRequest(this);
	}

	
}
