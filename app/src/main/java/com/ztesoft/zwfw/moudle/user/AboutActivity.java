package com.ztesoft.zwfw.moudle.user;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ztesoft.zwfw.R;
import com.ztesoft.zwfw.base.BaseActivity;

public class AboutActivity extends BaseActivity {

    TextView mVersionTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        TextView csTitile = (TextView) findViewById(R.id.cs_title);
        csTitile.setText(getString(R.string.about));
        findViewById(R.id.cs_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mVersionTv = (TextView) findViewById(R.id.version_tv);
        try {
            mVersionTv.setText("v"+getPackageManager().getPackageInfo(mContext.getPackageName(),0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
