package test.ewe.framework.appsession;

import junit.framework.TestCase;

import org.junit.Test;

import test.ewe.framework.appsession.mock.MockUser;

import com.ewe.framework.appsession.AppSession;
import com.ewe.framework.appsession.SessionUtils;

public class SessionUtilsTestCase {

	@Test
	public void testCreate(){
		AppSession session = SessionUtils.createSession();
		session.addData("vcode", "test");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		AppSession session2 = SessionUtils.getSession(session.getToken());
		TestCase.assertEquals(session.getToken(), session2.getToken());
	}
	
	@Test
	public void testLogin() throws InterruptedException{
		MockUser user = new MockUser();
		AppSession session = SessionUtils.createSession(user);
		System.out.println("session1:"+session);
		AppSession session2 = SessionUtils.getSession(session.getToken());
		System.out.println("session2:"+session2);
		session2.addData("test", "testdata");
		Thread.sleep(2000);
		
		AppSession session3 = SessionUtils.getSession(session.getToken());
		System.out.println("session3:"+session3);
		System.out.println(session3.getAllData());
	}
}
