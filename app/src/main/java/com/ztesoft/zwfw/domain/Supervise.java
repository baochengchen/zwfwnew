package com.ztesoft.zwfw.domain;

import java.io.Serializable;

/**
 * Created by BaoChengchen on 2017/8/31.
 */

public class Supervise implements Serializable{

    private String id;
    private String superviseNo;
    private Type functionType;
    private String superviseObject;
    private Type superviseObjectType;
    private Type superviseType;
    private String bizId;
    private Type bizType;
    private String superviseContent;
    private String superviseUserId;
    private String superviseUserName;
    private String superviseTime;
    private String superviseOrgId;
    private String superviseOrgName;
    private String supervisionHolderNo;
    private String supervisionHolderId;
    private String supervisionInstanceId;
    private String supervisionTaskListId;
    private String supervisionTaskName;
    private String supervisionTemplateId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSuperviseNo() {
        return superviseNo;
    }

    public void setSuperviseNo(String superviseNo) {
        this.superviseNo = superviseNo;
    }

    public Type getFunctionType() {
        return functionType;
    }

    public void setFunctionType(Type functionType) {
        this.functionType = functionType;
    }

    public String getSuperviseObject() {
        return superviseObject;
    }

    public void setSuperviseObject(String superviseObject) {
        this.superviseObject = superviseObject;
    }

    public Type getSuperviseObjectType() {
        return superviseObjectType;
    }

    public void setSuperviseObjectType(Type superviseObjectType) {
        this.superviseObjectType = superviseObjectType;
    }

    public Type getSuperviseType() {
        return superviseType;
    }

    public void setSuperviseType(Type superviseType) {
        this.superviseType = superviseType;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public Type getBizType() {
        return bizType;
    }

    public void setBizType(Type bizType) {
        this.bizType = bizType;
    }

    public String getSuperviseContent() {
        return superviseContent;
    }

    public void setSuperviseContent(String superviseContent) {
        this.superviseContent = superviseContent;
    }

    public String getSuperviseUserId() {
        return superviseUserId;
    }

    public void setSuperviseUserId(String superviseUserId) {
        this.superviseUserId = superviseUserId;
    }

    public String getSuperviseUserName() {
        return superviseUserName;
    }

    public void setSuperviseUserName(String superviseUserName) {
        this.superviseUserName = superviseUserName;
    }

    public String getSuperviseTime() {
        return superviseTime;
    }

    public void setSuperviseTime(String superviseTime) {
        this.superviseTime = superviseTime;
    }

    public String getSuperviseOrgId() {
        return superviseOrgId;
    }

    public void setSuperviseOrgId(String superviseOrgId) {
        this.superviseOrgId = superviseOrgId;
    }

    public String getSuperviseOrgName() {
        return superviseOrgName;
    }

    public void setSuperviseOrgName(String superviseOrgName) {
        this.superviseOrgName = superviseOrgName;
    }

    public String getSupervisionHolderNo() {
        return supervisionHolderNo;
    }

    public void setSupervisionHolderNo(String supervisionHolderNo) {
        this.supervisionHolderNo = supervisionHolderNo;
    }

    public String getSupervisionHolderId() {
        return supervisionHolderId;
    }

    public void setSupervisionHolderId(String supervisionHolderId) {
        this.supervisionHolderId = supervisionHolderId;
    }

    public String getSupervisionInstanceId() {
        return supervisionInstanceId;
    }

    public void setSupervisionInstanceId(String supervisionInstanceId) {
        this.supervisionInstanceId = supervisionInstanceId;
    }

    public String getSupervisionTaskListId() {
        return supervisionTaskListId;
    }

    public void setSupervisionTaskListId(String supervisionTaskListId) {
        this.supervisionTaskListId = supervisionTaskListId;
    }

    public String getSupervisionTaskName() {
        return supervisionTaskName;
    }

    public void setSupervisionTaskName(String supervisionTaskName) {
        this.supervisionTaskName = supervisionTaskName;
    }

    public String getSupervisionTemplateId() {
        return supervisionTemplateId;
    }

    public void setSupervisionTemplateId(String supervisionTemplateId) {
        this.supervisionTemplateId = supervisionTemplateId;
    }
}
