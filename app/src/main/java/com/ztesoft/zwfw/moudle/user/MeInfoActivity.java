package com.ztesoft.zwfw.moudle.user;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.ztesoft.zwfw.moudle.Config;
import com.ztesoft.zwfw.R;
import com.ztesoft.zwfw.base.BaseActivity;
import com.ztesoft.zwfw.domain.Staff;
import com.ztesoft.zwfw.domain.User;
import com.ztesoft.zwfw.domain.req.StaffselfReq;
import com.ztesoft.zwfw.utils.APPPreferenceManager;
import com.ztesoft.zwfw.utils.http.RequestManager;

public class MeInfoActivity extends BaseActivity {

    private User mUser;
    EditText mNameEdt, mPhoneEdt, mEmailEdt, mAddrEdt, mMemoEdt;
    TextView mCodeTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_info);
        TextView csTitile = (TextView) findViewById(R.id.cs_title);
        csTitile.setText(getString(R.string.edit_meInfo));
        findViewById(R.id.cs_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInfo();
            }
        });

        mNameEdt = (EditText) findViewById(R.id.name_edt);
        mCodeTv = (TextView) findViewById(R.id.code_tv);
        mPhoneEdt = (EditText) findViewById(R.id.phone_edt);
        mEmailEdt = (EditText) findViewById(R.id.email_edt);
        mAddrEdt = (EditText) findViewById(R.id.addr_edt);
        mMemoEdt = (EditText) findViewById(R.id.memo_edt);

        mUser = JSON.parseObject(APPPreferenceManager.getInstance().getString(mContext, Config.USERINFO), User.class);

        mNameEdt.setText(mUser.getUserName());
        mCodeTv.setText(mUser.getUserCode());
        mPhoneEdt.setText(mUser.getPhone());
        mEmailEdt.setText(mUser.getEmail());
        mAddrEdt.setText(mUser.getAddress());
        mMemoEdt.setText(mUser.getMemo());

    }

    private void saveUserInfo() {
        mUser.setUserName(mNameEdt.getText().toString().trim());
        mUser.setPhone(mPhoneEdt.getText().toString().trim());
        mUser.setEmail(mEmailEdt.getText().toString().trim());
        mUser.setAddress(mAddrEdt.getText().toString().trim());
        mUser.setMemo(mMemoEdt.getText().toString().trim());

        if(!changed()){
            finish();
            return;
        }

        StaffselfReq staffselfReq = new StaffselfReq();
        staffselfReq.setUser(mUser);
        Staff staff = new Staff();
        staff.setStaffId(mUser.getUserId());
        staff.setStaffName(mUser.getUserName());
        staff.setPhone(mUser.getPhone());
        staff.setEmail(mUser.getEmail());
        staff.setAddress(mUser.getAddress());
        staffselfReq.setStaff(staff);
        RequestManager.getInstance().patchHeader(Config.BASE_URL + Config.URL_SELF, JSON.toJSONString(staffselfReq), new RequestManager.RequestListener() {
            @Override
            public void onRequest(String url, int actionId) {
                showProgressDialog("正在保存个人信息");
            }

            @Override
            public void onSuccess(String response, String url, int actionId) {
                APPPreferenceManager.getInstance().saveObject(mContext,Config.USERINFO,JSON.toJSONString(mUser));
                Toast.makeText(mContext,"保存成功",Toast.LENGTH_SHORT).show();
                finish();
                hideProgressDialog();

            }

            @Override
            public void onError(String errorMsg, String url, int actionId) {
                Toast.makeText(mContext,"保存失败",Toast.LENGTH_SHORT).show();
                finish();
                hideProgressDialog();

            }
        },0);
    }

    private boolean changed() {
        return !TextUtils.equals(APPPreferenceManager.getInstance().getString(mContext, Config.USERINFO),JSON.toJSONString(mUser));
    }


    @Override
    public void onBackPressed() {
        saveUserInfo();
        //super.onBackPressed();
    }
}
