package com.ztesoft.zwfw.widget;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.DatePicker;
import android.widget.TextView;

import com.ztesoft.zwfw.R;

import java.util.Calendar;

/**
 * Created by BaoChengchen on 2017/8/29.
 */

public class TimeSelectDialog {

    private Context context;

    DatePickerDialog datePickerDialog;

    private int year, month, day;


    public TimeSelectDialog(Context context) {
        this.context = context;
        initDatePickerDialog();
    }

    private void initDatePickerDialog() {

        Calendar mycalendar = Calendar.getInstance();
        year = mycalendar.get(Calendar.YEAR); //获取Calendar对象中的年
        month = mycalendar.get(Calendar.MONTH);//获取Calendar对象中的月
        day = mycalendar.get(Calendar.DAY_OF_MONTH);//获取这个月的第几天

        datePickerDialog = new DatePickerDialog(context, datelistener, year, month, day);
    }


    public void show() {
        datePickerDialog.show();
    }

    private DatePickerDialog.OnDateSetListener datelistener = new DatePickerDialog.OnDateSetListener() {

        /**params：view：该事件关联的组件
         * params：myyear：当前选择的年
         * params：monthOfYear：当前选择的月
         * params：dayOfMonth：当前选择的日
         */
        @Override
        public void onDateSet(DatePicker view, int myear, int mmonth, int dayOfMonth) {

            //修改year、month、day的变量值，以便以后单击按钮时，DatePickerDialog上显示上一次修改后的值
            year = myear;
            month = mmonth;
            day = dayOfMonth;
            //更新日期
            updateDate();
        }

        //当DatePickerDialog关闭时，更新日期显示
        private void updateDate() {
            //在TextView上显示日期
            //showdate.setText("当前日期：" + year + "-" + (month + 1) + "-" + day);
        }
    };


}
