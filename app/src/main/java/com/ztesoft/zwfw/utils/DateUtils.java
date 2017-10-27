package com.ztesoft.zwfw.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 董睿 on 2017/8/29.
 */

public class DateUtils {

    public static String Date2String(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }
}
