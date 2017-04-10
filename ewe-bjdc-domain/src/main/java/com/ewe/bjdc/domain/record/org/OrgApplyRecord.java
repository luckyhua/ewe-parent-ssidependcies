package com.ewe.bjdc.domain.record.org;

import java.util.Date;

public class OrgApplyRecord {
    private Integer id;

    private String cid;

    private String name;

    private String phone;

    private Integer appId;

    private Integer merchId;

    private String merchName;

    private String callbackUrl;

    private String orgName;

    private String serviceNoList;

    private String authTerm;

    private Date authTime;

    private Integer authStatus;

    private String successTerm;

    private Double totalPrice;

    private String errorMsg;

    private String jsonData;

    private String queryParams;

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

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public Integer getMerchId() {
        return merchId;
    }

    public void setMerchId(Integer merchId) {
        this.merchId = merchId;
    }

    public String getMerchName() {
        return merchName;
    }

    public void setMerchName(String merchName) {
        this.merchName = merchName == null ? null : merchName.trim();
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl == null ? null : callbackUrl.trim();
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

    public String getSuccessTerm() {
        return successTerm;
    }

    public void setSuccessTerm(String successTerm) {
        this.successTerm = successTerm == null ? null : successTerm.trim();
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg == null ? null : errorMsg.trim();
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData == null ? null : jsonData.trim();
    }

    public String getQueryParams() {
        return queryParams;
    }

    public void setQueryParams(String queryParams) {
        this.queryParams = queryParams == null ? null : queryParams.trim();
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