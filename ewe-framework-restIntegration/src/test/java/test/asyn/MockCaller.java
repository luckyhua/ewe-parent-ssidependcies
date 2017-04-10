package test.asyn;

import com.ewe.rest.resp.EweResponse;
import com.ewe.rest.sender.asyn.HttpCallBack;

public class MockCaller implements HttpCallBack<String>{

	@Override
	public boolean handleResponse(EweResponse<String> response) {
		
		System.out.println(response.getContentData());
		
		return true;
	}

}
