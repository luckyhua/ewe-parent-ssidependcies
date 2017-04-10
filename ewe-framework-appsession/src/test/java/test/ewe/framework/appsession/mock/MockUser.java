package test.ewe.framework.appsession.mock;

import com.ewe.framework.appsession.subject.UserSubject;

public class MockUser implements UserSubject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Integer getId() {
		return 100;
	}

}
