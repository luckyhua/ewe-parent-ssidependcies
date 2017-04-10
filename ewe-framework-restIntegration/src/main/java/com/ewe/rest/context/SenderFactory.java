package com.ewe.rest.context;


import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ewe.rest.sender.HttpSender;
import com.ewe.rest.sender.SimpleSender;
import com.ewe.rest.sender.asyn.AsynSender;
import com.ewe.rest.sender.asyn.HttpCallBack;
import com.ewe.rest.sender.asyn.ResponseCache;

public class SenderFactory {
	
	private static List<AsynSender<?>> senders = new ArrayList<>();
	
	private static final Logger log = LoggerFactory.getLogger(SenderFactory.class);

	public static <T> HttpSender<T> createSimple(Class<T> entityClass) {
		SimpleSender<T> sender = new SimpleSender<>();

		sender.setEntityClass(entityClass);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		sender.setOm(objectMapper);
		sender.setHttpClient(getClient());

		return sender;
	}
	
	public static <T> HttpSender<T> createSimple() {
		SimpleSender<T> sender = new SimpleSender<>();

		sender.setEntityClass(null);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		sender.setOm(objectMapper);
		sender.setHttpClient(getClient());

		return sender;
	}
	
	public static <T> HttpSender<T> createSimple(TypeReference<T> type){
		SimpleSender<T> sender = new SimpleSender<>();

		sender.setType(type);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		sender.setOm(objectMapper);
		sender.setHttpClient(getClient());

		return sender;
	}

	public static <T> HttpSender<T> createAsynSender(Class<T> entityClass,
			HttpCallBack<T> caller) {
		synchronized (SenderFactory.class) {
			AsynSender<T> sender = new AsynSender<>(); 
			sender.setCache(ResponseCache.create());
			sender.setSender(createSimple(entityClass));
			sender.setCaller(caller);
			
			senders.add(sender);
			return sender;
		}
	}

	private static HttpClient getClient() {
		SocketConfig sc = SocketConfig.custom().setSoTimeout(2*1000).build();

		
		X509TrustManager xtm = new X509TrustManager() {

			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
			@Override
			public void checkClientTrusted(
					java.security.cert.X509Certificate[] arg0, String arg1)
					throws java.security.cert.CertificateException {

			}
			@Override
			public void checkServerTrusted(
					java.security.cert.X509Certificate[] arg0, String arg1)
					throws java.security.cert.CertificateException {
			}
		};

		try {
			SSLContext ctx = SSLContext.getInstance("TLS");

			ctx.init(null, new TrustManager[] { xtm }, null);


			RequestConfig config = RequestConfig.custom()
					.setConnectionRequestTimeout(5*1000).setSocketTimeout(5*1000)
					.setConnectTimeout(5*1000).build();
			
			final HttpClient client = HttpClientBuilder.create()
					.setMaxConnPerRoute(20).setMaxConnTotal(20)
					.setDefaultRequestConfig(config).setSslcontext(ctx).setDefaultSocketConfig(sc)
					.build();
			
			return client;
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	
	public static void destory(){
		for(AsynSender<?> sender:senders){
			if(log.isInfoEnabled())
				log.info("try to stop one sender!:{}",sender);
			sender.shutDown();
		}
	}
}
