package com.ztesoft.zwfw.domain.resp;

import com.ztesoft.zwfw.domain.Consult;
import com.ztesoft.zwfw.domain.Task;

import java.util.List;

/**
 * Created by 董睿 on 2017/8/31.
 */

public class QueryConsultListResp extends BasePageResp {

    public List<Consult> content;

    public List<Consult> getContent() {
        return content;
    }

    public void setContent(List<Consult> content) {
        this.content = content;
    }
}
