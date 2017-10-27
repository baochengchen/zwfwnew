package com.ztesoft.zwfw.moudle.user;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.ztesoft.zwfw.moudle.Config;
import com.ztesoft.zwfw.R;
import com.ztesoft.zwfw.base.BaseActivity;
import com.ztesoft.zwfw.domain.User;
import com.ztesoft.zwfw.utils.APPPreferenceManager;
import com.ztesoft.zwfw.utils.MD5Utils;
import com.ztesoft.zwfw.utils.http.RequestManager;

import java.util.HashMap;
import java.util.Map;

public class ResetPwdActivity extends BaseActivity {

    EditText mOldPwdEdt, mNewPwdEdt, mConfirmPwdEdt;
    TextView mTipTv;
    private User mUser;

    String oldPwdStr, newPwdStr, conFirmPwdStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pwd);
        TextView csTitile = (TextView) findViewById(R.id.cs_title);
        csTitile.setText(getString(R.string.change_pwd));
        findViewById(R.id.cs_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mOldPwdEdt = (EditText) findViewById(R.id.old_pwd);
        mNewPwdEdt = (EditText) findViewById(R.id.new_pwd);
        mConfirmPwdEdt = (EditText) findViewById(R.id.confirm_pwd);

        mConfirmPwdEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(TextUtils.isEmpty(s)){
                    mTipTv.setVisibility(View.GONE);
                }else{
                    if(!TextUtils.equals(s,mNewPwdEdt.getText().toString().trim())){
                        mTipTv.setVisibility(View.VISIBLE);
                    }else{
                        mTipTv.setVisibility(View.GONE);
                    }
                }
            }
        });

        mTipTv = (TextView) findViewById(R.id.tip_tv);
        mUser = getmUser();
    }


    public void changePwd(View v){
        oldPwdStr = mOldPwdEdt.getText().toString().trim();
        newPwdStr = mNewPwdEdt.getText().toString().trim();
        conFirmPwdStr = mConfirmPwdEdt.getText().toString().trim();

        if(TextUtils.isEmpty(oldPwdStr)){
            Toast.makeText(mContext,"请输入旧密码",Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(newPwdStr)){
            Toast.makeText(mContext,"请输入新密码",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(conFirmPwdStr)){
            Toast.makeText(mContext,"请输入确认密码",Toast.LENGTH_SHORT).show();
            return;
        }


        String oldPwdBase64Str = Base64.encodeToString(oldPwdStr.getBytes(),Base64.NO_WRAP);
        if(!TextUtils.equals(mUser.getUserPwd(),MD5Utils.encrypt(oldPwdBase64Str))){
            Toast.makeText(mContext,"旧密码不正确",Toast.LENGTH_SHORT).show();
            return;
        }

        if(!TextUtils.equals(newPwdStr,conFirmPwdStr)){
            Toast.makeText(mContext,"确认密码与新密码不符",Toast.LENGTH_SHORT).show();
            return;
        }

        mUser.setUserPwd(newPwdStr);

        final Map<String,String> map = new HashMap<>();
        map.put("oldPwd",Base64.encodeToString(oldPwdStr.getBytes(), Base64.DEFAULT));
        map.put("newPwd", Base64.encodeToString(newPwdStr.getBytes(), Base64.DEFAULT));
        RequestManager.getInstance().patchHeader(Config.BASE_URL + Config.URL_SELF_PWD, JSON.toJSONString(map), new RequestManager.RequestListener() {
            @Override
            public void onRequest(String url, int actionId) {
                showProgressDialog("正在修改");
            }

            @Override
            public void onSuccess(String response, String url, int actionId) {
                hideProgressDialog();
                APPPreferenceManager.getInstance().saveObject(mContext,Config.USERINFO,JSON.toJSONString(mUser));
                Toast.makeText(mContext,"修改成功",Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onError(String errorMsg, String url, int actionId) {
                hideProgressDialog();
                Toast.makeText(mContext,"修改失败",Toast.LENGTH_SHORT).show();
            }
        },0);

    }
}
