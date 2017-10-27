package com.ztesoft.zwfw.utils;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.ztesoft.zwfw.domain.resp.HttpErrorResp;
import com.ztesoft.zwfw.moudle.Config;
import com.ztesoft.zwfw.moudle.LoginActivity;

/**
 * Created by 董睿 on 2017/9/13.
 */

public class SessionUtils {

    public static boolean invalid(String errorMsg){
        try{
            HttpErrorResp httpErrorResp = JSON.parseObject(errorMsg,HttpErrorResp.class);
            if(TextUtils.equals(httpErrorResp.getCode(),"S-SYS-00027")){
                return true;
            }
        }catch (JSONException e){
           e.printStackTrace();
        }
        return false;
    }
}
