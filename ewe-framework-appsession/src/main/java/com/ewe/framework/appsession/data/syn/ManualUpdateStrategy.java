package com.ewe.framework.appsession.data.syn;

import com.ewe.framework.appsession.data.SessionUpdateTask;
import com.ewe.framework.appsession.data.UpdateStrategy;

public class ManualUpdateStrategy implements UpdateStrategy{

	/**
	 * do nothing
	 */
	@Override
	public void onUpdateSession(SessionUpdateTask task) {
		if(log.isInfoEnabled())
			log.info("try to update one session,but need to manul call update!token:{}",task.getAppSession().getToken());
	}

}
