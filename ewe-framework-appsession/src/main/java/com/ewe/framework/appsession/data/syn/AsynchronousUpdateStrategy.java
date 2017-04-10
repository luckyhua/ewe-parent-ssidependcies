package com.ewe.framework.appsession.data.syn;

import com.ewe.framework.appsession.data.SessionUpdateDespatcher;
import com.ewe.framework.appsession.data.SessionUpdateTask;
import com.ewe.framework.appsession.data.UpdateStrategy;

public class AsynchronousUpdateStrategy implements UpdateStrategy{

	private SessionUpdateDespatcher despathcer = SessionUpdateDespatcher.create();
	
	private final String Code = "AsynchronousUpdateStrategy";
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Code == null) ? 0 : Code.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AsynchronousUpdateStrategy other = (AsynchronousUpdateStrategy) obj;
		if (Code == null) {
			if (other.Code != null)
				return false;
		} else if (!Code.equals(other.Code))
			return false;
		return true;
	}

	@Override
	public void onUpdateSession(SessionUpdateTask task) {
		
		if(log.isInfoEnabled())
			log.info("try to asynchronize session token:{}",task.getAppSession().getToken());
		
		despathcer.addUpdateTask(task);
	}

}
