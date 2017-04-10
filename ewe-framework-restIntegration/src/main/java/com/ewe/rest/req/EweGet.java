package com.ewe.rest.req;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;

import com.ewe.rest.resp.EweResponse;
import com.ewe.rest.sender.HttpSender;


public class EweGet <T> implements EweRequest<T>{
	
	private String url;
	
	private HttpGet get;
	
	private HttpSender<T> sender;
	
	private Map<String, String> params;
	
	private Map<String, String> headers;
	
	private EweGet(){
	}
	
	public static <T> EweGet<T> create(String url,HttpSender<T> sender){
		if(url==null||url.isEmpty())
			throw new RuntimeException("url could not be null!");
		if(sender==null)
			throw new RuntimeException("sender could not be null!");
		
		EweGet<T> get = new EweGet<>();
		get.params = new HashMap<>();
		get.sender = sender;
		get.url = url;
		get.headers = new HashMap<>();
		
		return get;
	}
	
	public EweGet<T> addHeader(String name,String value){
		if(name==null||name.isEmpty())
			return this;
		headers.put(name, value);
		return this;
	}
	
	public EweGet<T> addUrlParam(String name,String value){
		if(name==null||name.isEmpty())
			return this;
		String encode;
		try {
			encode = URLEncoder.encode(value,"utf-8");
			this.url = this.url.replace("${"+name+"}", encode);
		} catch (UnsupportedEncodingException e) {
			log.error("error when encode value:"+e.getMessage(),e);
		}
		return this;
	}
	
	public EweGet<T> addParam(String name,String value){
		if(name==null||name.isEmpty())
			return this;
		String encode;
//		try {
			//name = URLEncoder.encode(value,"utf-8");
			this.params.put(name, value);
//		} catch (UnsupportedEncodingException e) {
//			log.error("error when encode value:"+e.getMessage(),e);
//		}
		return this;
	}
	
	@Override
	public EweRequest<T> addParam(String name, String value, boolean enCoded) {
		if(name==null||name.isEmpty())
			return this;
//		try {
			if(!enCoded){
				
				//value = URLEncoder.encode(value,"utf-8");
			}
			this.params.put(name, value);
//		} catch (UnsupportedEncodingException e) {
//			log.error("error when encode value:"+e.getMessage(),e);
//		}
		return this;
	}
	
	public EweResponse<T> send(){
		return sender.sendRequest(this);
	}

	@Override
	public HttpRequestBase build() {
		try {
			URIBuilder builder = new URIBuilder(this.url);
			for(String name:this.params.keySet()){
				builder.addParameter(name, this.params.get(name));
			}
			HttpGet get = new HttpGet(builder.build().toString());
			for(String name:this.headers.keySet()){
				get.addHeader(name, this.headers.get(name));
			}
			return get;
		} catch (Exception e) {
			log.error("error create request:"+e.getMessage(),e);
		}
		return null;
	}

}
