package com.ztesoft.zwfw.domain.resp;

import com.ztesoft.zwfw.domain.DefaultPortal;
import com.ztesoft.zwfw.domain.UserApp;

import java.util.List;

/**
 * Created by 董睿 on 2017/8/7.
 */

public class LoginResp extends BaseResp{
    public DefaultPortal defaultPortal;
    public List<UserApp> userAppList;
    public String userCode;

    public DefaultPortal getDefaultPortal() {
        return defaultPortal;
    }

    public void setDefaultPortal(DefaultPortal defaultPortal) {
        this.defaultPortal = defaultPortal;
    }

    public List<UserApp> getUserAppList() {
        return userAppList;
    }

    public void setUserAppList(List<UserApp> userAppList) {
        this.userAppList = userAppList;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }
}
