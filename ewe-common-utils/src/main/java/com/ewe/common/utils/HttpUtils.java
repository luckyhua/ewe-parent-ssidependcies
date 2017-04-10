package com.ewe.common.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class HttpUtils {
	
	/**
	 * 
	  * @Title: getRequestUrl
	  * @Description: 拼接get请求url
	  * @param map
	  * @param actionName
	  * @return    
	  * @return String   
	  *
	 */
	public static String getRequestUrl(Map<String, String> mapParams, String reqUrl){

		StringBuilder builder = new StringBuilder();
        Boolean flag = false;
        for (Map.Entry<String, String> entry : mapParams.entrySet()) {
            if (flag)
                builder.append("&");
            builder.append(entry.getKey()).append("=").append(entry.getValue());
            flag = true;
        }

        return reqUrl + "?" + builder.toString();
	}

	/**
	 * 
	  * @Title: getRequestResult
	  * @Description: 模拟get请求得到结果
	  * @param mapParams
	  * @param actionName
	  * @return    
	  * @return String   
	  *
	 */
	public static String getRequestResult(Map<String, String> mapParams, String reqUrl) {
		
		String result = "";
		
		String requestUrl = getRequestUrl(mapParams, reqUrl);
		
        CloseableHttpResponse response = null;
        
        try {
        	CloseableHttpClient httpclient = HttpClients.createDefault();

            HttpGet httpget = new HttpGet(requestUrl);

            response = httpclient.execute(httpget);
            
            if(response.getStatusLine().getStatusCode() == 200){
            	 HttpEntity entity = response.getEntity();
            	 if (entity != null) 
            		 result = EntityUtils.toString(entity, "UTF-8");
            }  
            
            response.close();
        } catch (ClientProtocolException e) {  
            result = e.getMessage().toString();  
        } catch (IOException e) {  
            result = e.getMessage().toString();  
        } 
        return result;
	}
	
	/**
	 * 
	  * @Title: postParams
	  * @Description: 组装参数
	  * @param mapParams
	  * @return    
	  * @return List<NameValuePair>   
	  *
	 */
	public static List<NameValuePair> postParams(Map<String, String> mapParams){

		List <NameValuePair> params = new ArrayList<NameValuePair>(); 
        for(Map.Entry<String, String> entry : mapParams.entrySet()){
        	params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        return params;
	}
	
	/**
	 * 
	  * @Title: postRequestResult
	  * @Description: 模拟post提交得到结果
	  * @param mapParams
	  * @param actionName
	  * @return    
	  * @return String   
	  *
	 */
	@SuppressWarnings("deprecation")
	public static String postRequestResult(Map<String, String> mapParams, String reqUrl) {
		
		String result = "";
		
		CloseableHttpResponse response = null;
        
        try {
        	CloseableHttpClient httpclient = HttpClients.createDefault();

            HttpPost httppost = new HttpPost(reqUrl);
            
            httppost.setEntity(new UrlEncodedFormEntity(postParams(mapParams), HTTP.UTF_8));

            response = httpclient.execute(httppost);
            
            if(response.getStatusLine().getStatusCode() == 200){
            	 HttpEntity entity = response.getEntity();
            	 if (entity != null) 
            		 result = EntityUtils.toString(entity, "UTF-8");
            }  
            
            response.close();
        } catch (ClientProtocolException e) {  
            result = e.getMessage().toString();  
        } catch (IOException e) {  
            result = e.getMessage().toString();  
        } 
        return result;
	}
	
}
