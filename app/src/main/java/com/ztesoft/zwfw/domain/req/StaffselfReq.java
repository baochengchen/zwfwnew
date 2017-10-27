package com.ztesoft.zwfw.domain.req;

import com.ztesoft.zwfw.domain.Staff;
import com.ztesoft.zwfw.domain.User;

/**
 * Created by BaoChengchen on 2017/9/7.
 */

public class StaffselfReq {

    String attrData;
    User user;
    Staff staff;

    public String getAttrData() {
        return attrData;
    }

    public void setAttrData(String attrData) {
        this.attrData = attrData;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }
}
