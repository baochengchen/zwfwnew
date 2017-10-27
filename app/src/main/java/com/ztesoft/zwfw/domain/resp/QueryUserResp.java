package com.ztesoft.zwfw.domain.resp;

import com.ztesoft.zwfw.domain.QueryUser;

import java.util.List;

/**
 * Created by BaoChengchen on 2017/9/14.
 */

public class QueryUserResp {
    String size;
    String count;
    List<QueryUser> content;

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<QueryUser> getContent() {
        return content;
    }

    public void setContent(List<QueryUser> content) {
        this.content = content;
    }
}

