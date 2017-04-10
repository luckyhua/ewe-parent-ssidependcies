package com.ewe.framework.result;

import java.util.List;
import java.util.Map;

import com.ewe.framework.context.MsgConfig;
import com.ewe.framework.context.model.PageInfo;
import com.ewe.framework.exception.BaseException;
import com.ewe.framework.result.easyui.DataGridResult;
import com.ewe.framework.result.json.JsonResult;

/**
 * 
 * @author Yo-Lee:catreekk@sohu.com
 * @version 2014年11月21日下午5:32:35
 */
public class ResultUtils {
	public static final int Default_Success = 200;
	
	
	@Deprecated
	public static JsonResult getSuccessResult(Map<String,Object> data){
		JsonResult ret = getSuccessResult();
		
		if(data!=null)
			ret.setData(data);
		
		return ret;
	}
	
	
	public static JsonResult setMsg(JsonResult jsonResult,int code){
		
		jsonResult.setCode(code+"");
		jsonResult.setMsg(MsgConfig.getMsg(code+""));
		
		return jsonResult;
	}
	
	public static JsonResult getSuccessResult(int code){
		JsonResult jsonResult = new JsonResult();
		
		jsonResult.setCode(code+"");
		jsonResult.setMsg(MsgConfig.getMsg(code+""));
		
		return jsonResult;
	}
	
	public static JsonResult getSuccessResult(String token){
		JsonResult json = getSuccessResult();
		json.getData().put("token", token);
		return json;
	}
	
	public static JsonResult getSuccessResult(){
		return getSuccessResult(Default_Success);
	}
	
	public static JsonResult getErrorResult(int code){
		JsonResult jsonResult = new JsonResult();
		jsonResult.setCode(code+"");
		jsonResult.setMsg(MsgConfig.getMsg(code+""));
		
		return jsonResult;
	}
	
	public static JsonResult getErrorResult(Exception e){
		if(e instanceof BaseException){
			JsonResult jsonResult = new JsonResult();
			jsonResult.setCode(((BaseException) e).getCode()+"");
			jsonResult.setMsg(((BaseException) e).getMsg());
			return jsonResult;
		}
		
		return null;
	}
	
	public static DataGridResult getDataGridResult(PageInfo pageInfo,List<?> data){
		DataGridResult ret = new DataGridResult(pageInfo.getTotalCount(), data);
		return ret;
	}
}
