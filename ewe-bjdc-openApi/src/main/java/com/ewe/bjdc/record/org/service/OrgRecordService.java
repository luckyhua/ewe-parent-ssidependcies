package com.ewe.bjdc.record.org.service;

import java.util.List;

import com.ewe.bjdc.domain.record.org.OrgApplyRecord;

public interface OrgRecordService {

	public void apply(OrgApplyRecord orgApplyRecord);

	public List<OrgApplyRecord> findByAuthStatus(Integer authStatus, Integer userId);
	
	public OrgApplyRecord findById(Integer id);
	
	public void save(OrgApplyRecord orgApplyRecord);

}
