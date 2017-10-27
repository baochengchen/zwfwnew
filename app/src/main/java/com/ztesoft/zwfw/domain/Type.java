package com.ztesoft.zwfw.domain;

import java.io.Serializable;

/**
 * Created by BaoChengchen on 2017/8/31.
 */

public class Type implements Serializable{

    public String code;
    public String title;

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
