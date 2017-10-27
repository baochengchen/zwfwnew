package com.ztesoft.zwfw.domain;

/**
 * Created by BaoChengchen on 2017/9/5.
 */

public class TaskProcess {

    String taskListId;
    String taskName;
    String userID;
    String userName;
    String dealUserOrgId;
    String dealUserOrgName;
    String taskResult;
    String auditTime;
    String stateCode;
    String stateCodeName;


    public String getTaskListId() {
        return taskListId;
    }

    public void setTaskListId(String taskListId) {
        this.taskListId = taskListId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDealUserOrgId() {
        return dealUserOrgId;
    }

    public void setDealUserOrgId(String dealUserOrgId) {
        this.dealUserOrgId = dealUserOrgId;
    }

    public String getDealUserOrgName() {
        return dealUserOrgName;
    }

    public void setDealUserOrgName(String dealUserOrgName) {
        this.dealUserOrgName = dealUserOrgName;
    }

    public String getTaskResult() {
        return taskResult;
    }

    public void setTaskResult(String taskResult) {
        this.taskResult = taskResult;
    }

    public String getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(String auditTime) {
        this.auditTime = auditTime;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getStateCodeName() {
        return stateCodeName;
    }

    public void setStateCodeName(String stateCodeName) {
        this.stateCodeName = stateCodeName;
    }
}
