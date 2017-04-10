package com.ewe.framework.utils;

import java.text.ParseException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ewe.framework.exception.BaseException;
import com.ewe.framework.exception.utils.ExceptionUtils;

/**
 * 
 * @author Yo-Lee:catreekk@sohu.com
 * @version 2014年11月21日下午5:32:17
 */
public class AssertUtils {
	
	private static final Logger log = LoggerFactory.getLogger(AssertUtils.class);

	public static final String Ids_Pattern = "^,([1-9][0-9]*,)([1-9][0-9]*,)*";

	/**
	 * check the ids must be match the patter:
	 * @see #Ids_Pattern<p>
	 * for example : ,1,2,3,4,
	 * @param code if ids is not match the pattern then throw exception
	 * @param ids need check string
	 * @throws BaseException
	 */
	public static void isIds(int code, String ids)throws BaseException{
		if(ids==null)
			ExceptionUtils.throwSimpleEx(code);
		if(!ids.matches(Ids_Pattern))
			ExceptionUtils.throwSimpleEx(code);
	}
	
	/**
	 * check targets is null,if null throws BaseException use code,
	 * code must be register in message.props
	 * @param code message.props code
	 * @param targets needed check value
	 * @throws BaseException
	 */
	public static void notNull(int code, Object... args) throws BaseException{
		if (null == args)  
			ExceptionUtils.throwSimpleEx(code);
    	for(Object arg : args){
    		if (null == arg) {  
    			ExceptionUtils.throwSimpleEx(code);
            }  
            if (arg instanceof List) {  
                int count = ((List<?>) arg).size();  
                for(int i = 0; i< count ; i++){  
                    ((List<?>) arg).remove(null);  
                } 
                if((List<?>)arg == null || ((List<?>)arg).size() == 0)
                	ExceptionUtils.throwSimpleEx(code);
            }  
            if (arg instanceof Collection && ((Collection<?>) arg).size() == 0) {  
            	ExceptionUtils.throwSimpleEx(code);
            }  
            if (arg instanceof Map && ((Map<?,?>) arg).size() == 0) { 
            	ExceptionUtils.throwSimpleEx(code);
            }  
    	}
	}
	
	/**
	 * check targets is null,if null throws BaseException use code,
	 * code must be register in message.props
	 * @param code message.props code
	 * @param targets needed check value
	 * @throws BaseException
	 */
	public static void notNull(int code, Object args,String param) throws BaseException{
		if (null == args)  
			ExceptionUtils.throwSimpleEx(code,param);
	}
	
	/**
     * 验证参数为空
     * @param args
     * @throws BaseException      
     *
     */
    public static void isNull(int code, Object ... args) 
    		throws BaseException{
    	if (null == args)  
			return;
    	for(Object arg : args){
    		if (null != arg)  
    			ExceptionUtils.throwSimpleEx(code);
    		if (arg instanceof String && arg != "")
    			ExceptionUtils.throwSimpleEx(code);
            if (arg instanceof List && ((List<?>)arg).size() > 0)  
            	ExceptionUtils.throwSimpleEx(code);
            if (arg instanceof Collection && ((Collection<?>) arg).size() > 0)   
            	ExceptionUtils.throwSimpleEx(code);
            if (arg instanceof Map && ((Map<?,?>) arg).size() > 0) 
            	ExceptionUtils.throwSimpleEx(code);
    	}
    }
    
	/**
	 * target must be true ,if not throw {@link BaseException}
	 * @param code use this code to throw {@link BaseException}
	 * @param target
	 * @throws BaseException
	 */
	public static void isTrue(int code, boolean target)
			throws BaseException{
		if(!target)
			ExceptionUtils.throwSimpleEx(code);
	}
	
	/**
	 * target must be false ,if not throw {@link BaseException}
	 * @param code
	 * @param target
	 * @throws BaseException
	 */
	public static void isFalse(int code, boolean target)
			throws BaseException{
		if(target)
			ExceptionUtils.throwSimpleEx(code);
	}
	
	/**
	 * check if source is equals target,if not throws {@link BaseException}<p>
	 * must notice that if source and target is null in the same,this method<p>
	 * will not throws exception
	 * @param code
	 * @param source
	 * @param target
	 * @throws BaseException
	 */
	public static void isEquals(int code, Object source, Object target) 
			throws BaseException{
		if(source == null && target == null)
			return;
		if(source!=null && target ==null)
			ExceptionUtils.throwSimpleEx(code);
		if(target != null && source == null)
			ExceptionUtils.throwSimpleEx(code);
		if(!source.equals(target))
			ExceptionUtils.throwSimpleEx(code);
	}
	
	/**
	 * check if source is equals target,if not throws {@link BaseException}<p>
	 * must notice that if source and target is not null in the same,this method<p>
	 * will not throws exception
	 * @param code
	 * @param source
	 * @param target
	 * @throws BaseException
	 */
	public static void notEquals(int code, Object source, Object target) 
			throws BaseException{
		if(source == null || target == null)
			return;
		if(source.equals(target))
			ExceptionUtils.throwSimpleEx(code);
	}
	
	/**
	 * if target is blank throws exception
	 * @param code
	 * @param target
	 * @throws BaseException
	 */
	public static void notBlank(int code, String target)
			throws BaseException{
		if(target==null||target.isEmpty())
			ExceptionUtils.throwSimpleEx(code);
	}
	
	/**
	 * if target is blank throws exception
	 * @param code
	 * @param target
	 * @throws BaseException
	 */
	public static void notBlank(int code, String target,String param)
			throws BaseException{
		if(target==null||target.isEmpty())
			ExceptionUtils.throwSimpleEx(code,param);
	}
	
	/**
	 *<p> check that the target must be blank, if not this will throws exception</p>
	 *<P> if target is null or empty this method will not throws exception</p>
	 * @author Lee-Yo
	 * @version 2014年11月24日上午9:17:00
	 * @param code
	 * @param target
	 * @throws BaseException
	 */
	public static void isBlank(int code, String target) 
			throws BaseException{
		if(target!=null&&!target.isEmpty())
			ExceptionUtils.throwSimpleEx(code);
	}

	/**
	 * 手机号验证
	 * @param code
	 * @param arg
	 * @throws BaseException
	 */
    public static void isMobile(int code, String arg) 
    		throws BaseException{
    	if(arg==null||arg.isEmpty())
    		ExceptionUtils.throwSimpleEx(code);
        Pattern p = Pattern.compile("^[1][3,4,5,8,7][0-9]{9}$"); // 验证手机号  
        Matcher m = p.matcher(arg);  
        if(!m.matches())
        	ExceptionUtils.throwSimpleEx(code);
    } 
    
    /**
     * 电话号码验证
     * @param code
     * @param arg
     * @throws BaseException
     */
    public static void isPhone(int code, String arg) 
    		throws BaseException{   
        Matcher m = null;  
        Pattern p1 = Pattern.compile("^[0][1-9]{2,3}-[0-9]{5,10}$");  // 验证带区号的  
        Pattern p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$");         // 验证没有区号的  
        if(arg.length() >9)   
        	m = p1.matcher(arg);  
        else 
        	m = p2.matcher(arg);  
        if(!m.matches())   
        	ExceptionUtils.throwSimpleEx(code);
    }

	public static void isUnionPayVcode(String vcode) {
		// dohandle unionpay code
		
	} 
	
	/**
	 * 身份证校验
	 * @param code
	 * @param arg
	 * @throws BaseException
	 */
	public static void isIdCard(int code, String arg) 
			throws BaseException{
		AssertUtils.notBlank(code, arg);
		try {
			String validateResult = IDCardUtil.IDCardValidate(arg);
			if(log.isInfoEnabled())
				log.info("identity number validate result : {}", validateResult );
			if(!"success".equals(validateResult))
				ExceptionUtils.throwSimpleEx(code);
		} catch (NumberFormatException e) {
			log.error("identity number validate NumberFormatException : {}", e.getMessage());
			ExceptionUtils.throwSimpleEx(code);
		} catch (ParseException e) {
			log.error("identity number validate ParseException : {}", e.getMessage());
			ExceptionUtils.throwSimpleEx(code);
		}
		
	}
}
