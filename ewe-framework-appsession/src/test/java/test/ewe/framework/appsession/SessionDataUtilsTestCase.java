package test.ewe.framework.appsession;

import junit.framework.TestCase;

import org.junit.Test;

import com.ewe.framework.appsession.SessionDataUtils;

public class SessionDataUtilsTestCase {

	@Test
	public void testVcode(){
		String bussinessKey = "test";
		String mobile = "12345";
		
		int vcodeTimes = SessionDataUtils.getVcodeTimes(bussinessKey, mobile);
		TestCase.assertEquals(vcodeTimes, 0);
		
		
		String vcode = SessionDataUtils.setVcode(bussinessKey, mobile);
		String vcode2 = SessionDataUtils.getVcode(bussinessKey, mobile);
		TestCase.assertEquals(vcode, vcode2);
		
		String setVcode = SessionDataUtils.setVcode(bussinessKey, mobile);
		TestCase.assertEquals(setVcode, vcode);
		
		int vcodeTimes2 = SessionDataUtils.getVcodeTimes(bussinessKey, mobile);
		TestCase.assertEquals(2, vcodeTimes2);
		
	}
	
	@Test
	public void testPasswordErrorTiems(){
		String auth = "1234";
		int times = SessionDataUtils.getPasswordErrorTimes(auth);
		TestCase.assertEquals(0, times);
		
		SessionDataUtils.incrPasswordErrorTimes(auth);
		
		int times2 = SessionDataUtils.getPasswordErrorTimes(auth);
		TestCase.assertEquals(1, times2);
		
	}
	
	@Test
	public void testExpired() throws InterruptedException{
		String auth = "1234";
		String bussinessKey = "testE";
		SessionDataUtils.incrErrorTimes(bussinessKey, auth, 1);
		
		Thread.sleep(2000);
		
		int times = SessionDataUtils.getErrorTimes(bussinessKey, auth);
		TestCase.assertEquals(0, times);
	}
}
