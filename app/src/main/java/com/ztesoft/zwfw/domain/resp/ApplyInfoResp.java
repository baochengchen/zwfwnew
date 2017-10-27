package com.ztesoft.zwfw.domain.resp;

import com.ztesoft.zwfw.domain.DynamicData;

/**
 * Created by BaoChengchen on 2017/9/5.
 */

public class ApplyInfoResp {
    String keyId;
    DynamicData dynamicData;

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public DynamicData getDynamicData() {
        return dynamicData;
    }

    public void setDynamicData(DynamicData dynamicData) {
        this.dynamicData = dynamicData;
    }
}



