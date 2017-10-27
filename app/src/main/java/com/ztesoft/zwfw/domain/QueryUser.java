package com.ztesoft.zwfw.domain;

import java.io.Serializable;

/**
 * Created by BaoChengchen on 2017/9/14.
 */


public class QueryUser implements Serializable{

    String createdDate;
    String forceLogin;
    String isLocked;
    String loginFail;
    String pwd;
    String rn;
    String spId;
    String state;
    String stateDate;
    String userCode;
    String userEffDate;
    String userId;
    String userName;

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getForceLogin() {
        return forceLogin;
    }

    public void setForceLogin(String forceLogin) {
        this.forceLogin = forceLogin;
    }

    public String getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(String isLocked) {
        this.isLocked = isLocked;
    }

    public String getLoginFail() {
        return loginFail;
    }

    public void setLoginFail(String loginFail) {
        this.loginFail = loginFail;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getRn() {
        return rn;
    }

    public void setRn(String rn) {
        this.rn = rn;
    }

    public String getSpId() {
        return spId;
    }

    public void setSpId(String spId) {
        this.spId = spId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStateDate() {
        return stateDate;
    }

    public void setStateDate(String stateDate) {
        this.stateDate = stateDate;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserEffDate() {
        return userEffDate;
    }

    public void setUserEffDate(String userEffDate) {
        this.userEffDate = userEffDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}

