package com.ztesoft.zwfw.domain.resp;

import com.ztesoft.zwfw.domain.Consult;
import com.ztesoft.zwfw.domain.Supervise;

import java.util.List;

/**
 * Created by BaoChengchen on 2017/8/31.
 */

public class QuerySuperviseListResp extends BasePageResp {

    public List<Supervise> content;

    public List<Supervise> getContent() {
        return content;
    }

    public void setContent(List<Supervise> content) {
        this.content = content;
    }
}
