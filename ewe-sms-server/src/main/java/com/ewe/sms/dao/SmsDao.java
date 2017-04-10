package com.ewe.sms.dao;

import java.sql.Connection;

import org.apache.commons.dbutils.DbUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ewe.dbutils.dao.base.BaseDao;
import com.ewe.dbutils.dao.mapping.SqlBuilder;
import com.ewe.dbutils.dao.mapping.SqlHolder;
import com.ewe.sms.domain.SmsLog;

public class SmsDao extends BaseDao {
	
	private static final Logger logger = LoggerFactory.getLogger(SmsDao.class);

	private static SmsDao instance = new SmsDao();
	
	private SmsDao(){};
	
	public static SmsDao getInstance(){
		return instance;
	}

	public void insert(SmsLog smslog){
		
		SqlHolder sqlHolder = SqlBuilder.buildInsert(smslog);
		
		try {
			Connection conn = ds.getConnection();
			conn.setAutoCommit(false);
			update(conn, sqlHolder.getSql(), sqlHolder.getParams());
			DbUtils.commitAndClose(conn);
		} catch (Exception e) {
			logger.error(" insert appCount find exception : " + e.getMessage());
		}
		
	}
	
}
