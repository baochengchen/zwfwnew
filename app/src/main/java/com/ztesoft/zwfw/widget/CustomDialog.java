package com.ztesoft.zwfw.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.TextView;

import com.ztesoft.zwfw.R;

/**
 * Created by BaoChengchen on 2017/8/7.
 */

public class CustomDialog extends Dialog{

    TextView dialogText;

    public CustomDialog(Context context, int themeResId) {
        super(context, themeResId);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_progress_loading);
        getWindow().setGravity(Gravity.CENTER);
        setCanceledOnTouchOutside(false);
        WindowManager wm = getWindow().getWindowManager();
        Display d = wm.getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = (int) (d.getWidth() * 0.6);
        lp.height = (int) (d.getWidth() * 0.3);
        getWindow().setAttributes(lp);
        dialogText = (TextView) findViewById(R.id.dialog_text);
    }

    public void setContent(String text){
        if(dialogText != null){
            dialogText.setText(text);
        }
    }
}
