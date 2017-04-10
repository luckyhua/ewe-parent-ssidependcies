package com.ewe.bjdc.core.filter;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ewe.bjdc.domain.auth.user.User;
import com.ewe.common.utils.AStringUtils;
import com.ewe.common.utils.JsonUtils;
import com.ewe.framework.appsession.SessionUtils;
import com.ewe.framework.result.ResultUtils;
import com.ewe.framework.result.json.JsonResult;

public class AuthoriseFilter implements Filter {
	private FilterConfig config;
	private static Log log = LogFactory.getLog(AuthoriseFilter.class);

	public static final ThreadLocal<User> threadLocal = new ThreadLocal<>();
	
	private static final String Url_Prefix = "v1";

	public void init(FilterConfig config) throws ServletException {
		this.config = config;
	}

	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		
		JsonResult json = null;
		if (this.isNeedAuthentication(request)) { // 需验证区域
			String token = request.getParameter("token");

			try {
				if (token == null || token.isEmpty()) {
					json = ResultUtils.getErrorResult(9004);
					this.responseJson(json, response);
					return;
				}

				boolean active = SessionUtils.getSession(token).isActive();
				if (!active) {
					json = ResultUtils.getErrorResult(9006);
					this.responseJson(json, response);
					return;
				}
				filterChain.doFilter(request, response);
			} catch (Exception e) {
				this.responseJson(ResultUtils.getErrorResult(e), response);
				e.printStackTrace();
			}
			return;
		}
		filterChain.doFilter(request, response);
	}

	public void destroy() {
		this.config = null;
	}

	private boolean isNeedAuthentication(HttpServletRequest request){
		//if is multipart then need check in method 
		// filter not support multipart
		if(this.isMultipart(request)){
			log.info("request is multipart! need check authentication is method!");
			return false;
		}
		
		String uri = AStringUtils.nvl(request.getRequestURI(), "");
		String method = request.getMethod();
		String ip = request.getRemoteAddr();
		
		String exclude = config.getInitParameter("exclude");
		String regEx = "(?<=" + request.getContextPath() + "/)[^/]+(/[^/]+|$)";
		String act = "";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(uri.replace("/"+Url_Prefix+"/", "/"));

		int paramAt = uri.lastIndexOf('?') == -1 ? uri.length() : uri
				.lastIndexOf('?');
		String source = uri.substring(uri.indexOf("/"+Url_Prefix) + (Url_Prefix.length()+1), paramAt);
		
		
		if (matcher.find())
			act = matcher.group();
		
		if(log.isDebugEnabled()){
			log.debug("in auth filter exclude:"+exclude);
			log.debug("request ACT:" + act + " ip" + ip + " method:" + method
					+ " uri:" + uri + "  source:" + source);
		}
		
		
		return (act.isEmpty() || !exclude.contains(source) && !exclude.contains("$"+act+"$"));
	}
	
	private boolean isExclude(String act,String source){
		String exclude = config.getInitParameter("exclude");
		//ACT:sys/auth ip192.168.100.251 method:GET uri:/amiee/rest/sys/auth/user/boundMobile  source:/sys/auth/user/boundMobile
		if(act.isEmpty())
			return true;
		if(exclude.contains(source))
			return true;
		//check first
		if(exclude.contains("$"+act+"$"))
			return true;
		//check second
		String[] split = source.split("/");
		if(split.length >= 3){
			String second = split[0]+"/"+split[1]+"/"+split[2];
			if(exclude.contains("#"+second+"#"))
				return true;
		}
		return false;
	}
	
	private boolean isMultipart(HttpServletRequest request){
		String type = request.getContentType();
		if(type!=null&&type.split(";")[0]!=null&&type.split(";")[0].equals("multipart/form-data")){
			return true;
		}
		return false;
	}
	
	private void responseJson(JsonResult json, HttpServletResponse response)
			throws IOException {
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(JsonUtils.jsonToString(json));
		response.getWriter().flush();
	}
}
