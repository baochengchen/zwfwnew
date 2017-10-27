package com.ztesoft.zwfw.domain;

import java.io.Serializable;

/**
 * Created by BaoChengchen on 2017/10/24 0024.
 */

public class ServiceEvaluate implements Serializable {

    String id;
    String holderNo;
    String holderId;
    String TemplateId;
    String TaskListId;
    String InstanceId;
    String webUserName;
    //EvaluateType type;
    String objId;
    Type source;
    Type result;
    //EvaluateSource source;
    //EvaluateResult result;
    String opinion;
    String isAnonymous;//是否匿名
    String createDate;//创建时间,即评价时间
    String itemName;//事项名称
    String phone;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getTemplateId() {
        return TemplateId;
    }

    public void setTemplateId(String templateId) {
        TemplateId = templateId;
    }

    public String getTaskListId() {
        return TaskListId;
    }

    public void setTaskListId(String taskListId) {
        TaskListId = taskListId;
    }

    public String getInstanceId() {
        return InstanceId;
    }

    public void setInstanceId(String instanceId) {
        InstanceId = instanceId;
    }

    public String getWebUserName() {
        return webUserName;
    }

    public void setWebUserName(String webUserName) {
        this.webUserName = webUserName;
    }

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

    public Type getResult() {
        return result;
    }

    public void setResult(Type result) {
        this.result = result;
    }

    public Type getSource() {
        return source;
    }

    public void setSource(Type source) {
        this.source = source;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public String getIsAnonymous() {
        return isAnonymous;
    }

    public void setIsAnonymous(String isAnonymous) {
        this.isAnonymous = isAnonymous;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
