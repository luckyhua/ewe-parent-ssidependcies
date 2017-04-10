package com.ewe.rest.req;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;

import com.ewe.rest.resp.EweResponse;
import com.ewe.rest.sender.HttpSender;

public class EweMultiPost<T> implements EweRequest<T>{
	
	private String url;
	
	private HttpSender<T> sender;
	
	private MultipartEntityBuilder builder;
	
	private HttpPost post;
	
	public static <T> EweMultiPost<T> create(String url,HttpSender<T> sender){
		if(url==null||url.isEmpty())
			throw new RuntimeException("url could not be null!");
		if(sender==null)
			throw new RuntimeException("sender could not be null!");
		
		EweMultiPost<T> multi = new EweMultiPost<>();
		multi.sender = sender;
		multi.url = url;
		multi.builder = MultipartEntityBuilder.create().setCharset(Charset.forName("utf-8"));
		multi.post = new HttpPost(url);
		
		return multi;
	}

	@Override
	public EweMultiPost<T> addHeader(String name, String value) {
		if(name==null||name.isEmpty())
			return this;
		post.addHeader(name, value);
		return this;
	}

	@Override
	public EweMultiPost<T> addParam(String name, String value) {
		if(name==null||name.isEmpty())
			return this;
		String encode;
		try {
			if(value!=null)
				encode = URLEncoder.encode(value,"utf-8");
			builder.addTextBody(name, value);
		} catch (UnsupportedEncodingException e) {
			log.error("error when encode value:"+value+" message:"+e.getMessage(),e);
		}
		
		return this;
	}
	
	@Override
	public EweRequest<T> addParam(String name, String value, boolean enCoded) {
		if(name==null||name.isEmpty())
			return this;
		try {
			if(!enCoded)
				value = URLEncoder.encode(value,"utf-8");
			builder.addTextBody(name, value,ContentType.create("text/plain", "utf-8"));
		} catch (UnsupportedEncodingException e) {
			log.error("error when encode value:"+value+" message:"+e.getMessage(),e);
		}
		
		return this;
	}
	
	public EweMultiPost<T> addParam(String name,File file){
		if(name==null||name.isEmpty())
			return this;
		if(file==null)
			return this;
		
		FileBody f = new FileBody(file);
		builder.addPart(name, f);
		return this;
	}
	
	public EweMultiPost<T> addParam(String name,byte[] value){
		if(name==null||name.isEmpty())
			return this;
		if(value==null)
			return this;
		
		
		builder.addBinaryBody(name, value);
		return this;
	}
	
	public EweMultiPost<T> addParam(String name,byte[] value,String fileName){
		if(name==null||name.isEmpty())
			return this;
		if(value==null)
			return this;
		if(fileName==null||fileName.isEmpty())
			return this;
		
		builder.addBinaryBody(name, value,ContentType.MULTIPART_FORM_DATA,fileName);
		return this;
	}

	@Override
	public HttpRequestBase build() {
		
		HttpEntity entity = builder.build();
		this.post.setEntity(entity);
		return this.post;
	}

	@Override
	public EweResponse<T> send() {
		return sender.sendRequest(this);
	}

}
