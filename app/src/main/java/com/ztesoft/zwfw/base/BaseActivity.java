package com.ztesoft.zwfw.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.alibaba.fastjson.JSON;
import com.ztesoft.zwfw.moudle.Config;
import com.ztesoft.zwfw.R;
import com.ztesoft.zwfw.domain.User;
import com.ztesoft.zwfw.utils.APPPreferenceManager;
import com.ztesoft.zwfw.widget.CustomDialog;

/**
 * Created by BaoChengchen on 2017/8/6.
 */

public class BaseActivity extends FragmentActivity {

    private CustomDialog mCustomDialog;
    public Context mContext;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
    }


    public void showProgressDialog(String text) {
        if (mCustomDialog == null) {
            mCustomDialog = new CustomDialog(this, R.style.customDialog);
        }
        mCustomDialog.show();
        mCustomDialog.setContent(text);
    }

    public void hideProgressDialog() {
        if (mCustomDialog != null)
            mCustomDialog.cancel();
    }

    public User getmUser() {
        mUser = JSON.parseObject(APPPreferenceManager.getInstance().getString(mContext, Config.USERINFO), User.class);
        return mUser;
    }
}
