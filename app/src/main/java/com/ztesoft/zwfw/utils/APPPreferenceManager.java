package com.ztesoft.zwfw.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by BaoChengchen on 2017/8/6.
 */

public class APPPreferenceManager {

    public static final String FILENAME = "ZWFW";
    public static APPPreferenceManager manager;


    public static APPPreferenceManager getInstance(){
        if(manager == null) {
            manager = new APPPreferenceManager();
        }

        return manager;
    }


    public void saveObject(Context context,String key,Object value){
        SharedPreferences preferences = context.getSharedPreferences(FILENAME,Context.MODE_PRIVATE);
        if(value instanceof String){
            preferences.edit().putString(key, (String) value).commit();
        }else if(value instanceof Integer){
            preferences.edit().putInt(key, (Integer) value).commit();
        }else if(value instanceof Boolean){
            preferences.edit().putBoolean(key, (Boolean) value).commit();
        }

    }


    public boolean getBool(Context context,String key){
        SharedPreferences preferences = context.getSharedPreferences(FILENAME,Context.MODE_PRIVATE);
        return preferences.getBoolean(key,false);
    }

    public int getInt(Context context,String key){
        SharedPreferences preferences = context.getSharedPreferences(FILENAME,Context.MODE_PRIVATE);
        return preferences.getInt(key,0);
    }
    public String getString(Context context,String key){
        SharedPreferences preferences = context.getSharedPreferences(FILENAME,Context.MODE_PRIVATE);
        return preferences.getString(key,"");
    }

}
