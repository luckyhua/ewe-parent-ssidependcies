package com.ewe.bjdc.record.user.service;

import com.ewe.bjdc.domain.record.org.OrgApplyRecord;
import com.ewe.bjdc.record.org.service.OrgRecordService;
import com.ewe.bjdc.sys.base.controller.SmsController;
import com.ewe.framework.redis.RedisUtils;
import com.ewe.sms.sender.SimpleSender;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import redis.clients.jedis.Jedis;

import javax.servlet.*;
import java.io.IOException;
import java.util.List;

@SuppressWarnings("all")
public class UserRecordChecker implements Filter{

	public static boolean isStart = true;
	
	public static final String Record_Key = "bjdc:record:checker:ids";
	
	private static final long Time_24h = 1000*60*60*24;
	
	private static final long Time_48h = 1000*60*60*48;
	
	private static ObjectMapper m = new ObjectMapper();
	static{
		m.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, true);
		m.configure(SerializationConfig.Feature.WRITE_NULL_PROPERTIES, false);
		m.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
	
	
	public static void addToCheck(OrgApplyRecord record){
		if(record==null)
			return;
		if(record.getId()==null||record.getCreateTime()==null)
			return;
		
		//init data
		OrgApplyRecord json = new OrgApplyRecord();
		json.setId(record.getId());
		json.setPhone(record.getPhone());
		json.setCreateTime(record.getCreateTime());
		json.setOrgName(record.getOrgName());
		
		Jedis jedis = null;
		try {
			jedis = RedisUtils.getJedis();
			jedis.lpush(Record_Key, m.writeValueAsString(json));
		} catch (Exception e) {
		}finally{
			RedisUtils.closeJedis(jedis);
		}
	}
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(filterConfig.getServletContext());
		//get orgRecordService
		final OrgRecordService orgRecordService = ctx.getBean(OrgRecordService.class);
		//get sms service
		
		//start thread
		//poll
		new Thread(new Runnable() {
			@Override
			public void run() {
				
				bossPoll:while (isStart) {
					
					try {
						Thread.sleep(1000*60*10);
					} catch (InterruptedException e1) {
					}
					
					Jedis jedis = null;
					try {
						jedis = RedisUtils.getJedis();
						
						//TODO pagenations
						List<String> datas = jedis.lrange(Record_Key, 0, -1);
						if(datas==null||datas.isEmpty())
							continue bossPoll;
						//check the time if over 24h
						dataPoll:for (int i=datas.size()-1;i>=0;i--) {
							String json = datas.get(i);
							OrgApplyRecord data = m.readValue(json, OrgApplyRecord.class);
							long time = data.getCreateTime().getTime();
							long nowTime = System.currentTimeMillis();
							
							if(nowTime-time>=Time_24h){
								//send sms
								SimpleSender.getInstance().sendByTemp(SmsController.User_Notice, data.getPhone());
								continue dataPoll;
							}
							
							if(nowTime-time>=Time_48h){
								//set apply record authStatus to 2
								data.setAuthStatus(2);
								orgRecordService.save(data);
								//remove
								//TODO analys
								jedis.lrem(Record_Key,1, json);
								continue dataPoll;
							}
						}
						
						
					} catch (Exception e) {
					}finally{
						RedisUtils.closeJedis(jedis);
					}
				}
			}
		}).start();
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
	}

	@Override
	public void destroy() {
		
	}
	
	public static void main(String[] args) throws JsonGenerationException, JsonMappingException, IOException {
		OrgApplyRecord record = new OrgApplyRecord();
		record.setOrgName("trest");
		String json = m.writeValueAsString(record);
		System.out.println(json);
		OrgApplyRecord d = m.readValue(json, OrgApplyRecord.class);
		System.out.println(d.getOrgName());
	}
}
