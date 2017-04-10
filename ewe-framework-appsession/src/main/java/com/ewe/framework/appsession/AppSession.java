package com.ewe.framework.appsession;

import java.io.Serializable;
import java.util.Map;
import java.util.Observer;

import com.ewe.framework.appsession.subject.UserSubject;

/**
 * this class used to all system session base interface<p>
 * notice that this interface only provide the base method<p>
 * difference system should have its own implements<p>
 * @author Lee-Yo
 * 2015年7月8日
 */
public interface AppSession {

	/**
	 * get the session token that specific the session<p>
	 * @return the token mapping the session
	 */
	public String getToken();
	
	/**
	 * get if the session is online<p>
	 * that mean if one user is already login<p>
	 * the session mapping that user may be online<p>
	 * @return true if the user is already login
	 */
	public boolean isOnline();
	
	/**
	 * get if the session is in system<p>
	 * that mean if one token mapping one session<p>
	 * this session is active <p>
	 * if one session is clear this session is not active<p>
	 * @return true if the session is active
	 */
	public boolean isActive();
	
	/**
	 * get the user subject from session<p>
	 * one session could specific one subject<p>
	 * @see UserSubject
	 * @return the current user subject
	 */
	public UserSubject getUserSubject();
	
	/**
	 * set the current user related session
	 */
	public void setUserSubject(UserSubject userSubject);
	
	/**
	 * get the data from session<p>
	 * @param key the data mapping key
	 * @return the data if exists
	 */
	public Object getData(String key);
	
	/**
	 * add data to session<p>
	 * data will stored in redis with the session<p>
	 * <h3>notice that the data put in session should be implements Serializable</h3>
	 * <h3>notice that the key and data could not be null</h3>
	 * @see Serializable
	 * @param key the key mapping the data
	 * @param data the data stored in session
	 */
	public void addData(String key, Object data);
	
	/**
	 * get all data stored in session<p>
	 * <h3>notice that this method can only read data</h3><p>
	 * that mean if you put data in session,you should call<p>
	 * {@link #addData(String, Object)}
	 * @return all data one unmodifiable map
	 */
	public Map<String,Object> getAllData();
	
	/**
	 * manual set the session if changed<p>
	 * this flag used to SessionManager judge if need to update this session<p>
	 * notice that if add data to session the flag will be changed to true<p>
	 * @param isChange true if the session change
	 */
	public void setChange(boolean isChange);
	
	public void addObserver(Observer observer);
	
	public boolean equals(Object session);
	
	public int hashCode();

}
