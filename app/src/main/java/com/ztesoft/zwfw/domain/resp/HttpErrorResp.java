package com.ztesoft.zwfw.domain.resp;

/**
 * Created by BaoChengchen on 2017/9/13.
 */

public class HttpErrorResp {

    String code;
    String message;
    String type;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
