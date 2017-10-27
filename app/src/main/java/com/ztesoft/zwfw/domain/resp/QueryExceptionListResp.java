package com.ztesoft.zwfw.domain.resp;

import com.ztesoft.zwfw.domain.Exception;
import com.ztesoft.zwfw.domain.Supervise;

import java.util.List;

/**
 * Created by BaoChengchen on 2017/10/24.
 */

public class QueryExceptionListResp extends BasePageResp {

    public List<Exception> content;

    public List<Exception> getContent() {
        return content;
    }

    public void setContent(List<Exception> content) {
        this.content = content;
    }
}
