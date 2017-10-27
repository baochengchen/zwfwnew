package com.ztesoft.zwfw.domain.resp;

import com.ztesoft.zwfw.domain.Task;

import java.util.List;

/**
 * Created by BaoChengchen on 2017/8/31.
 */

public class QueryTaskListResp extends BasePageResp {

    public List<Task> content;

    public List<Task> getContent() {
        return content;
    }

    public void setContent(List<Task> content) {
        this.content = content;
    }
}
