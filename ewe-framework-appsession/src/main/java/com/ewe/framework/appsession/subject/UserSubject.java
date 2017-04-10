package com.ewe.framework.appsession.subject;

import java.io.Serializable;

public interface UserSubject extends Serializable{

	public static final UserSubject Null_User = new NullUserSubject();
	
	
	public Integer getId();
	
	public static class NullUserSubject implements UserSubject{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private NullUserSubject(){
			
		}

		@Override
		public Integer getId() {
			return 0;
		}
	}
}
