package com.ztesoft.zwfw.utils;

/**
 * Created by BaoChengchen on 2017/9/22.
 */

public class UnicodeUtils {


    public static String unicode2String(String unicode) {

        StringBuffer string = new StringBuffer();

        String[] hex = unicode.split("\\\\u");

        for (int i = 1; i < hex.length; i++) {

            // 转换出每一个代码点
            int data = Integer.parseInt(hex[i], 16);

            // 追加成string
            string.append((char) data);
        }

        return string.toString();
    }


    /**
     * 字符串转换unicode
     */
    public static String string2Unicode(String string) {

        StringBuffer unicode = new StringBuffer();

        for (int i = 0; i < string.length(); i++) {

            // 取出每一个字符
            char c = string.charAt(i);
            if(c < 256)//ASC11表中的字符码值不够4位,补00
            {
                unicode.append("\\u00");
            }
            else
            {
                unicode.append("\\u");
            }
            // 转换为unicode
            unicode.append(Integer.toHexString(c));
        }

        return unicode.toString();
    }

}
