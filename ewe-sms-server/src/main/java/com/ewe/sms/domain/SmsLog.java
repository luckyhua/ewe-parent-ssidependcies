package com.ewe.sms.domain;

import java.io.Serializable;
import java.util.Date;

import com.ewe.dbutils.dao.mapping.Table;

@Table(name="A_SMS_LOG")
public class SmsLog implements Serializable{

	private static final long serialVersionUID = -5579554984760376211L;

	private Integer id;

    private String mobile;

    private String content;

    private String businessType;

    private String operator;

    private String status;

    private Date createTime;

    private Integer sendStaff;

    private Date sendTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getSendStaff() {
        return sendStaff;
    }

    public void setSendStaff(Integer sendStaff) {
        this.sendStaff = sendStaff;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }
}