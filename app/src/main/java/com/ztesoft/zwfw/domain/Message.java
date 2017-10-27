package com.ztesoft.zwfw.domain;


import java.io.Serializable;

/**
 * Created by baoChengchen on 2017/8/14.
 */

public class Message  implements Serializable{


    public String id;
    public String sender;
    public String senderName;
    public Type revType;
    public String revId;
    public String title;
    public String content;
    public String sendDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public Type getRevType() {
        return revType;
    }

    public void setRevType(Type revType) {
        this.revType = revType;
    }

    public String getRevId() {
        return revId;
    }

    public void setRevId(String revId) {
        this.revId = revId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSendDate() {
        return sendDate;
    }

    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }
}
