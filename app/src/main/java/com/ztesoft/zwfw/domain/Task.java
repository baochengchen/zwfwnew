package com.ztesoft.zwfw.domain;

import java.io.Serializable;

/**
 * Created by BaoChengchen on 2017/8/16.
 */

public class Task implements Serializable {

    private String id;
    private String workNo;
    private String holderNo;
    private String holderId;
    private String itemOrThemeId;
    private String itemOrThemeName;
    private String applicantName;
    private String applyTime;
   // private String applySource;
    private String promiseDate;
    private String templateId;
    private String taskListId;
    private String taskname;
    private String instanceId;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getTaskListId() {
        return taskListId;
    }

    public void setTaskListId(String taskListId) {
        this.taskListId = taskListId;
    }

    public String getTaskname() {
        return taskname;
    }

    public void setTaskname(String taskname) {
        this.taskname = taskname;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }
}
