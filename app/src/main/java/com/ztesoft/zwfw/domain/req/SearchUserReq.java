package com.ztesoft.zwfw.domain.req;

/**
 * Created by BaoChengchen on 2017/9/14.
 */

public class SearchUserReq {

    String userName;
    String userCode;
    String portalId;
    String state;
    String isLocked;
    int pageIndex;
    int pageLen;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getPortalId() {
        return portalId;
    }

    public void setPortalId(String portalId) {
        this.portalId = portalId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(String isLocked) {
        this.isLocked = isLocked;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageLen() {
        return pageLen;
    }

    public void setPageLen(int pageLen) {
        this.pageLen = pageLen;
    }
}
