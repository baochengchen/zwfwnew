package com.ztesoft.zwfw.domain.resp;

import com.ztesoft.zwfw.domain.Exception;
import com.ztesoft.zwfw.domain.ServiceEvaluate;

import java.util.List;

/**
 * Created by BaoChengchen on 2017/10/24.
 */

public class QueryEvaluateListResp extends BasePageResp {

    public List<ServiceEvaluate> content;

    public List<ServiceEvaluate> getContent() {
        return content;
    }

    public void setContent(List<ServiceEvaluate> content) {
        this.content = content;
    }
}
