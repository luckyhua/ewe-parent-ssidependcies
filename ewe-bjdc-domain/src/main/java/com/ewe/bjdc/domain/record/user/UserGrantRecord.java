package com.ewe.bjdc.domain.record.user;

import java.util.Date;

public class UserGrantRecord {
    private Integer id;

    private Integer userId;

    private String cid;

    private String name;

    private String phone;

    private String orgName;

    private String serviceNoList;

    private String authTerm;

    private Date authTime;

    private Integer authStatus;

    private Date createTime;

    private Integer createStaff;

    private Date updateTime;

    private Integer updateStaff;

    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid == null ? null : cid.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName == null ? null : orgName.trim();
    }

    public String getServiceNoList() {
        return serviceNoList;
    }

    public void setServiceNoList(String serviceNoList) {
        this.serviceNoList = serviceNoList == null ? null : serviceNoList.trim();
    }

    public String getAuthTerm() {
        return authTerm;
    }

    public void setAuthTerm(String authTerm) {
        this.authTerm = authTerm == null ? null : authTerm.trim();
    }

    public Date getAuthTime() {
        return authTime;
    }

    public void setAuthTime(Date authTime) {
        this.authTime = authTime;
    }

    public Integer getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(Integer authStatus) {
        this.authStatus = authStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getCreateStaff() {
        return createStaff;
    }

    public void setCreateStaff(Integer createStaff) {
        this.createStaff = createStaff;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getUpdateStaff() {
        return updateStaff;
    }

    public void setUpdateStaff(Integer updateStaff) {
        this.updateStaff = updateStaff;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}