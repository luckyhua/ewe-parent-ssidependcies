package test.ewe.framework.appsession;

import junit.framework.TestCase;

import org.junit.Test;

import com.ewe.framework.appsession.AppSession;
import com.ewe.framework.appsession.context.AppSessionContext;
import com.ewe.framework.appsession.context.SessionManager;

public class SessionTestCase {

	SessionManager manager = new AppSessionContext().getSessionManager();
	
	@Test
	public void testCreate(){
		AppSession session = manager.createAnonymouseSession();
		session.addData("vcode", "1233");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
		}
		// 顶级系统 子系统 模块 业务两位
		//qb 2    1    1  01
		//qb 2 1 1 01 
		AppSession session2 = manager.getSession(session.getToken());
	
		TestCase.assertEquals(session, session2);
		TestCase.assertEquals(session2.getData("vcode"), "1233");
		
		manager.clearSession(session.getToken());
		
		AppSession session3 = manager.getSession(session.getToken());
		TestCase.assertNull(session3.getData("vcode"));
		
	}
	
	/**
	 * test add data to session
	 */
	@Test
	public void testAddData(){
	}
	
	@Test
	public void testImmediatelyUpdate(){
	}
}
