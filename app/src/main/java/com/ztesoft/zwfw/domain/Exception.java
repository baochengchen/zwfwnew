package com.ztesoft.zwfw.domain;

import java.io.Serializable;

/**
 * Created by BaoChengchen on 2017/10/24 0024.
 */

public class Exception implements Serializable{
    String exceptionId;
    String work_Id;
    String workNo;
    String holderNo;
    String holderId;
    String itemOrThemeId;
    String itemOrThemeName;//事项名称
    String applicantName;//申请人姓名
    String applyTime;//申请时间
    // ApplySource applySource;//办件来源
    String promiseDate;//承诺时间
    String exceptionTemplateId;
    String exceptionTaskListId;
    String exceptionInstanceId;//
    String exceptionHolderNo;//异常办件的holderno
    String exceptionCurrentTask;//异常办件的当前环节名称
    String exceptionType;//异常类型
    String exceptionTypeName;//异常类型名称
    String exceptionApplyTime;//异常申请时间


    public String getExceptionTypeName() {
        return exceptionTypeName;
    }

    public void setExceptionTypeName(String exceptionTypeName) {
        this.exceptionTypeName = exceptionTypeName;
    }

    public String getExceptionId() {
        return exceptionId;
    }

    public void setExceptionId(String exceptionId) {
        this.exceptionId = exceptionId;
    }

    public String getWork_Id() {
        return work_Id;
    }

    public void setWork_Id(String work_Id) {
        this.work_Id = work_Id;
    }

    public String getWorkNo() {
        return workNo;
    }

    public void setWorkNo(String workNo) {
        this.workNo = workNo;
    }

    public String getHolderNo() {
        return holderNo;
    }

    public void setHolderNo(String holderNo) {
        this.holderNo = holderNo;
    }

    public String getHolderId() {
        return holderId;
    }

    public void setHolderId(String holderId) {
        this.holderId = holderId;
    }

    public String getItemOrThemeId() {
        return itemOrThemeId;
    }

    public void setItemOrThemeId(String itemOrThemeId) {
        this.itemOrThemeId = itemOrThemeId;
    }

    public String getItemOrThemeName() {
        return itemOrThemeName;
    }

    public void setItemOrThemeName(String itemOrThemeName) {
        this.itemOrThemeName = itemOrThemeName;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public String getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(String applyTime) {
        this.applyTime = applyTime;
    }

    public String getPromiseDate() {
        return promiseDate;
    }

    public void setPromiseDate(String promiseDate) {
        this.promiseDate = promiseDate;
    }

    public String getExceptionTemplateId() {
        return exceptionTemplateId;
    }

    public void setExceptionTemplateId(String exceptionTemplateId) {
        this.exceptionTemplateId = exceptionTemplateId;
    }

    public String getExceptionTaskListId() {
        return exceptionTaskListId;
    }

    public void setExceptionTaskListId(String exceptionTaskListId) {
        this.exceptionTaskListId = exceptionTaskListId;
    }

    public String getExceptionInstanceId() {
        return exceptionInstanceId;
    }

    public void setExceptionInstanceId(String exceptionInstanceId) {
        this.exceptionInstanceId = exceptionInstanceId;
    }

    public String getExceptionHolderNo() {
        return exceptionHolderNo;
    }

    public void setExceptionHolderNo(String exceptionHolderNo) {
        this.exceptionHolderNo = exceptionHolderNo;
    }

    public String getExceptionCurrentTask() {
        return exceptionCurrentTask;
    }

    public void setExceptionCurrentTask(String exceptionCurrentTask) {
        this.exceptionCurrentTask = exceptionCurrentTask;
    }

    public String getExceptionType() {
        return exceptionType;
    }

    public void setExceptionType(String exceptionType) {
        this.exceptionType = exceptionType;
    }

    public String getExceptionApplyTime() {
        return exceptionApplyTime;
    }

    public void setExceptionApplyTime(String exceptionApplyTime) {
        this.exceptionApplyTime = exceptionApplyTime;
    }
}

