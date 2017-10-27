package com.ztesoft.zwfw.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by BaoChengchen on 2017/8/27.
 */

public class WorkChatBean implements Serializable{

    public String title;
    public String content;
    public boolean isPublic;
    public List<String> imgs;
    public String creator;
    public String reciever;

}
