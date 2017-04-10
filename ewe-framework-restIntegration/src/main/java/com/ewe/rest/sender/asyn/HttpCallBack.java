package com.ewe.rest.sender.asyn;

import com.ewe.rest.resp.EweResponse;

public interface HttpCallBack<T>  {

	public boolean handleResponse(EweResponse<T> response);
}
