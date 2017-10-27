package com.ztesoft.zwfw.domain.req;

import com.ztesoft.zwfw.domain.ReportDynamicDatas;

/**
 * Created by BaoChengchen on 2017/9/6.
 */

public class ReplyReq {

    String taskResult;
    String taskListId;
    String stateCode;
    String keyId;
    ReportDynamicDatas dynamicDatas;


    public String getTaskResult() {
        return taskResult;
    }

    public void setTaskResult(String taskResult) {
        this.taskResult = taskResult;
    }

    public String getTaskListId() {
        return taskListId;
    }

    public void setTaskListId(String taskListId) {
        this.taskListId = taskListId;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public ReportDynamicDatas getDynamicDatas() {
        return dynamicDatas;
    }

    public void setDynamicDatas(ReportDynamicDatas dynamicDatas) {
        this.dynamicDatas = dynamicDatas;
    }
}
