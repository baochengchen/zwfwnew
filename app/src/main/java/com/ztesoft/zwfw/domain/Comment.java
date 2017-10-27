package com.ztesoft.zwfw.domain;

import java.io.Serializable;

/**
 * Created by BaoChencheng on 2017/9/15.
 */

public class Comment implements Serializable{

    private Long id;
    private Long chatId;
    private String content;
    private String attachments;
    private Long toUserId;
    private Long byUserId;
    private String state ;
    private Long readState;//0未读 1 已读
    private String createDate;
    private String updateDate;
    private Long createUser;
    private Long updateUser;
    private Long orgId;
    private Long areaId;
    private String toUserName;
    private String byUserName;
    private String toUserCode;
    private String byUserCode;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAttachments() {
        return attachments;
    }

    public void setAttachments(String attachments) {
        this.attachments = attachments;
    }

    public Long getToUserId() {
        return toUserId;
    }

    public void setToUserId(Long toUserId) {
        this.toUserId = toUserId;
    }

    public Long getByUserId() {
        return byUserId;
    }

    public void setByUserId(Long byUserId) {
        this.byUserId = byUserId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Long getReadState() {
        return readState;
    }

    public void setReadState(Long readState) {
        this.readState = readState;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public Long getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }

    public Long getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Long updateUser) {
        this.updateUser = updateUser;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getByUserName() {
        return byUserName;
    }

    public void setByUserName(String byUserName) {
        this.byUserName = byUserName;
    }

    public String getToUserCode() {
        return toUserCode;
    }

    public void setToUserCode(String toUserCode) {
        this.toUserCode = toUserCode;
    }

    public String getByUserCode() {
        return byUserCode;
    }

    public void setByUserCode(String byUserCode) {
        this.byUserCode = byUserCode;
    }
}
