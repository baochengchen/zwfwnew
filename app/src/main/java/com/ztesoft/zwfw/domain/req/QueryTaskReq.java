package com.ztesoft.zwfw.domain.req;

import java.io.Serializable;

/**
 * Created by BaoChengchen on 2017/9/11.
 */

public class QueryTaskReq implements Serializable{

    private String queryNo;
    private String itemName;
    private String userName;
    private String beginDate;
    private String endDate;

    public String getQueryNo() {
        return queryNo;
    }

    public void setQueryNo(String queryNo) {
        this.queryNo = queryNo;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
