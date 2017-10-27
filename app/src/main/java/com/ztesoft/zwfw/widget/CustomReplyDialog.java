package com.ztesoft.zwfw.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ztesoft.zwfw.R;

/**
 * Created by BaoChengchen on 2017/9/6.
 */

public class CustomReplyDialog extends Dialog implements View.OnClickListener {



    private TextView titleTv;
    private EditText replyEdt;
    private Button positiveBt, negativeBt;

    private String stateCode;

    public CustomReplyDialog(Context context, int themeResId) {
        super(context, themeResId);
    }


    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consult_reply_dialog);

        getWindow().setGravity(Gravity.CENTER);
        WindowManager wm = getWindow().getWindowManager();
        Display d = wm.getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = (int) (d.getWidth() * 0.8);
        lp.height = (int) (d.getHeight() * 0.5);
        getWindow().setAttributes(lp);
        titleTv = (TextView) findViewById(R.id.title_tv);
        replyEdt = (EditText) findViewById(R.id.reply_edt);
        positiveBt = (Button) findViewById(R.id.positive_bt);
        negativeBt = (Button) findViewById(R.id.negative_bt);

        positiveBt.setOnClickListener(this);
        negativeBt.setOnClickListener(this);
    }


    public String getReplyText() {
        return replyEdt.getText().toString();
    }


    @Override
    public void onClick(View v) {
        if (null != onCustomReplyClickListener)
            switch (v.getId()) {
                case R.id.positive_bt:
                    onCustomReplyClickListener.onCustomReplyClick(true, this);
                    break;
                case R.id.negative_bt:
                    onCustomReplyClickListener.onCustomReplyClick(false, this);
                    break;
            }
    }

    private OnCustomReplyClickListener onCustomReplyClickListener;

    public void setOnConsultDialogClickListener(OnCustomReplyClickListener onCustomReplyClickListener) {
        this.onCustomReplyClickListener = onCustomReplyClickListener;
    }

    public interface OnCustomReplyClickListener {

        void onCustomReplyClick(boolean confirm, CustomReplyDialog dialog);
    }

    public void setTiTleText(String title) {
        if (null != titleTv)
            titleTv.setText(title);
    }
}
