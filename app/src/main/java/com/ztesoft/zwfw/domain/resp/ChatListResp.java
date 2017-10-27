package com.ztesoft.zwfw.domain.resp;

import com.ztesoft.zwfw.domain.Chat;

import java.util.List;

/**
 * Created by BaoChengchen on 2017/9/15.
 */

public class ChatListResp extends BasePageResp{

    List<Chat> content;

    public List<Chat> getContent() {
        return content;
    }

    public void setContent(List<Chat> content) {
        this.content = content;
    }
}
