package com.ewe.framework.appsession;

import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;

import com.ewe.common.utils.CodeUtils;
import com.ewe.framework.redis.RedisUtils;


public class SessionDataUtils {

	private static final Logger log = LoggerFactory.getLogger(SessionDataUtils.class);
	
	private static final String Non_Code = UUID.randomUUID().toString().replace("-", "");
	
	private static final int Default_VcodePeriods = 5*60;
	
	public static final int Default_ErrorTimes_Periods = 60*60;
	
	public static final int Periods_Minus = 60;
	
	public static String getVcode(String bussinessKey,String mobile){
		if(StringUtils.isBlank(mobile)||StringUtils.isBlank(bussinessKey))
			return Non_Code;
		
		Jedis jedis = null;
		try {
			if(log.isInfoEnabled())
				log.info("try find vcode!bunessinessKey:{},mobile:{}",bussinessKey,mobile);
			
			jedis = RedisUtils.getJedis();
			String vcode = jedis.get(SessionDataKey.getVcodeKey(bussinessKey, mobile));
			
			if(StringUtils.isBlank(vcode))
				vcode = Non_Code;
			
			if(log.isInfoEnabled())
				log.info("find one vcode!vcode:{}",vcode);
			
			return vcode;
		} catch (Exception e) {
			log.error("get vocde error!mobile:{},bussinessKey:{},error:{}",new Object[]{mobile,bussinessKey,e.getMessage()});
		}finally{
			RedisUtils.closeJedis(jedis);
		}
		
		return Non_Code;
	}
	
	
	
	public static String setVcode(String bussinessKey,String mobile){
		if(StringUtils.isBlank(mobile)||StringUtils.isBlank(bussinessKey))
			return null;
		
		String vcode = getVcode(bussinessKey, mobile);
		//if vcode's length is larger than 30 that mean vcode do not exists
		if(vcode.length()>30){
			vcode = CodeUtils.getVcode();
		}
		
		Jedis jedis = null;
		try {
			jedis = RedisUtils.getJedis();
			
			jedis.setex(SessionDataKey.getVcodeKey(bussinessKey, mobile), Default_VcodePeriods, vcode);
			
			incrtimes(SessionDataKey.getVcodeTimesStatusKey(bussinessKey, mobile)
					, SessionDataKey.getVcodeTimesKey(bussinessKey, mobile)
					, Default_VcodePeriods);

			return vcode;
		} catch (Exception e) {
		}finally{
			RedisUtils.closeJedis(jedis);
		}
		return null;
	}
	
	public static void delVcode(String bussinessKey,String mobile){
		if(StringUtils.isBlank(mobile)||StringUtils.isBlank(bussinessKey))
			return;
		
		String vcode = getVcode(bussinessKey, mobile);
		//if vcode's length is larger than 30 that mean vcode do not exists
		if(vcode.length()>30){
			return;
		}
		
		Jedis jedis = null;
		try {
			jedis = RedisUtils.getJedis();
			
			jedis.del(SessionDataKey.getVcodeKey(bussinessKey, mobile));
			
			return;
		} catch (Exception e) {
		}finally{
			RedisUtils.closeJedis(jedis);
		}
		return;
	}
	
	public static String setVcode(String bussinessKey,String mobile,Integer periods){
		if(periods==null || periods<=0)
			return setVcode(bussinessKey, mobile);
		
		if(StringUtils.isBlank(mobile)||StringUtils.isBlank(bussinessKey))
			return null;
		
		String vcode = getVcode(bussinessKey, mobile);
		//if vcode's length is larger than 30 that mean vcode do not exists
		if(vcode.length()>30){
			vcode = CodeUtils.getVcode();
		}
		
		Jedis jedis = null;
		try {
			jedis = RedisUtils.getJedis();
			
			jedis.setex(SessionDataKey.getVcodeKey(bussinessKey, mobile), periods, vcode);
			
			incrtimes(SessionDataKey.getVcodeTimesStatusKey(bussinessKey, mobile)
					, SessionDataKey.getVcodeTimesKey(bussinessKey, mobile)
					, periods);

			return vcode;
		} catch (Exception e) {
		}finally{
			RedisUtils.closeJedis(jedis);
		}
		return null;
	}
	
	public static int getVcodeTimes(String bussinessKey,String mobile){
		int times = getTimes(SessionDataKey.getVcodeTimesStatusKey(bussinessKey, mobile)
				, SessionDataKey.getVcodeTimesKey(bussinessKey, mobile));
		
		if(log.isInfoEnabled())
			log.info("find vcode times:{},mobile:{}",times,mobile);
		
		return times;
	}
	
	//-----------------------------password----------------------------------------
	public static int getPasswordErrorTimes(String auth){
		if(StringUtils.isBlank(auth))
			return 0;
		
		int times = getTimes(SessionDataKey.getErrorTimesStatusKey("password", auth)
				, SessionDataKey.getErrorTimesKey("password", auth));
		
		if(log.isInfoEnabled())
			log.info("find error times auth:{},times:{}",auth,times);
		
		return times;
	}
	
	public static void incrPasswordErrorTimes(String auth){
		if(StringUtils.isBlank(auth))
			return;
		
		incrtimes(SessionDataKey.getErrorTimesStatusKey("password", auth)
				, SessionDataKey.getErrorTimesKey("password", auth)
				,Default_ErrorTimes_Periods);
	}
	
	public static int getErrorTimes(String bussinessKey,String auth){
		if(StringUtils.isBlank(auth)|| StringUtils.isBlank(bussinessKey))
			return 0;
		
		int times = getTimes(SessionDataKey.getErrorTimesStatusKey(bussinessKey,auth)
				, SessionDataKey.getErrorTimesKey(bussinessKey, auth));
		
		if(log.isInfoEnabled())
			log.info("find error times auth:{},times:{}",auth,times);
		
		return times;
	}
	
	public static void incrErrorTimes(String bussinessKey,String auth,int expires){
		if(StringUtils.isBlank(auth) || StringUtils.isBlank(bussinessKey))
			return;
		
		incrtimes(SessionDataKey.getErrorTimesStatusKey(bussinessKey, auth)
				, SessionDataKey.getErrorTimesKey(bussinessKey, auth)
				,expires);
	}
	
	
	private static int getTimes(String statusKey,String timesKey){
		if(StringUtils.isBlank(statusKey)||StringUtils.isBlank(timesKey))
			return 0;
		
		Jedis jedis = null;
		try {
			jedis = RedisUtils.getJedis();
			String str_times = jedis.get(timesKey);
			
			int times = NumberUtils.toInt(str_times,0);
			return times;
			
		} catch (Exception e) {
			log.error("get vocde times error!statusKey:{},timesKey:{},error:{}",new Object[]{statusKey,timesKey,e.getMessage()});
		}finally{
			RedisUtils.closeJedis(jedis);
		}
		
		return 0;
	}
	
	
	private static void incrtimes(String statusKey,String timesKey,int expires){
		if(expires<1)
			expires = Default_VcodePeriods;
		
		Jedis jedis = null;
		try {
			jedis = RedisUtils.getJedis();
			
			if(jedis.exists(statusKey)){
				jedis.incr(timesKey);
				return;
			}
			
			jedis.del(timesKey);
			
			jedis.setex(statusKey,expires,"status");
			jedis.setex(timesKey,expires, "1");
			
		} catch (Exception e) {
			log.error("incr times error!times key:{},status key:{},error:{}",new Object[]{timesKey,statusKey,e.getMessage()});
		}finally{
			RedisUtils.closeJedis(jedis);
		}
	}
	
	private static class SessionDataKey{
		
		private static final String VcodeKey_Prefix = "data:sessiondata:vcode:";
		
		private static final String Vcode_Times_Prefix = "data:sessiondata:vcodetimes:";
		
		private static final String Vcoe_Times_Status_Prefix = "data:sessiondata:vcodetimes:status:";
		
		private static final String Sperator = ":";
		
		private static final String Vcode_Mobile_Mapping = "mobile_vcode";
		
		private static final String Vcodetimes_Mobile_Mapping = "mobile_vcodetimes";
		
		private static final String VcodetimesStatus_Mobile_Mapping = "mobile_vcodetimesstatus";
		
		//--------------------------------------------------------
		private static final String ErrorTimes_Key = "data:sessiondata:errortimes:";
		
		private static final String ErrorTimes_Status_Key = "data:sessiondata:errortiems:status:";
		
		private static final String ErrorTimes_Status_Auth_Mapping = "authentication_errortimesstatus";
		
		private static final String ErrorTimes_Auth_Mapping = "authentication_errortimes";
		
		
		public static String getVcodeKey(String bussinessKey,String mobile){
			return VcodeKey_Prefix+bussinessKey+Sperator+Vcode_Mobile_Mapping+Sperator+mobile;
		}
		
		public static String getVcodeTimesKey(String bussinessKey,String mobile){
			return Vcode_Times_Prefix+bussinessKey+Sperator+Vcodetimes_Mobile_Mapping+Sperator+mobile;
		}
		
		public static String getVcodeTimesStatusKey(String bussinessKey,String mobile){
			return Vcoe_Times_Status_Prefix+bussinessKey+Sperator+VcodetimesStatus_Mobile_Mapping+Sperator+mobile;
		}
		
		public static String getErrorTimesKey(String bussinessKey,String auth){
			return ErrorTimes_Key+bussinessKey+Sperator+ErrorTimes_Auth_Mapping+Sperator+auth;
		}
		
		public static String getErrorTimesStatusKey(String bussinessKey,String auth){
			return ErrorTimes_Status_Key+bussinessKey+Sperator+ErrorTimes_Status_Auth_Mapping+Sperator+auth;
		}
	}
}
