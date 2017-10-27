package com.ztesoft.zwfw.moudle;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ztesoft.zwfw.R;
import com.ztesoft.zwfw.base.BaseActivity;
import com.ztesoft.zwfw.moudle.todo.TaskFragment;
import com.ztesoft.zwfw.moudle.user.PersonalFragment;

import java.lang.ref.WeakReference;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    public static boolean isForeground = false;

    public MainFragment mainFragment;
    private MyTaskFragment myTaskFragment;
    private PersonalFragment personalFragment;
    private FragmentManager mFragmentManager;
    private static final int TAG_FRAG_MYTASK= 0;
    private static final int TAG_FRAG_MAIN = 1;
    private static final int TAG_FRAG_PERSONAL = 2;

    private int curTag = 0;

    TextView tabTextWdbj;
    TextView tabTextPersonal;
    ImageView tabImgWdbj;
    ImageView tabImgGrzx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.tab_wdbj).setOnClickListener(this);
        findViewById(R.id.tab_home).setOnClickListener(this);
        findViewById(R.id.tab_personal).setOnClickListener(this);
        tabTextWdbj = (TextView) findViewById(R.id.tab_text_wdbj);
        tabTextPersonal = (TextView) findViewById(R.id.tab_text_personal);
        tabImgWdbj = (ImageView) findViewById(R.id.img_wdbj);
        tabImgGrzx = (ImageView) findViewById(R.id.img_grzx);

        mFragmentManager = getSupportFragmentManager();
        if(savedInstanceState == null){
            curTag = TAG_FRAG_MAIN;
            mainFragment = (MainFragment) mFragmentManager.findFragmentByTag(""+TAG_FRAG_MAIN);
            myTaskFragment = (MyTaskFragment) mFragmentManager.findFragmentByTag(""+TAG_FRAG_MYTASK);
            personalFragment = (PersonalFragment) mFragmentManager.findFragmentByTag(""+TAG_FRAG_PERSONAL);
        }else{
            curTag = savedInstanceState.getInt("curTag",TAG_FRAG_MAIN);
        }
        setTab(curTag);

        registerMessageReceiver();  // used for receive msg


    }


    private void setTab(int tag) {
        FragmentTransaction fm = mFragmentManager.beginTransaction();
        hideAllFragMent(fm);
        switch (tag){
            case TAG_FRAG_MYTASK:
                if(myTaskFragment == null){
                    myTaskFragment = MyTaskFragment.newInstance();
                    fm.add(R.id.fragContainer, myTaskFragment,""+TAG_FRAG_MYTASK);
                }
                fm.show(myTaskFragment);
                break;
            case TAG_FRAG_MAIN:
                if(mainFragment== null){
                    mainFragment = MainFragment.newInstance();
                    fm.add(R.id.fragContainer,mainFragment,""+TAG_FRAG_MAIN);
                }
                fm.show(mainFragment);
                break;
            case TAG_FRAG_PERSONAL:
                if(personalFragment== null){
                    personalFragment = PersonalFragment.newInstance();
                    fm.add(R.id.fragContainer,personalFragment,""+TAG_FRAG_PERSONAL);
                }
                fm.show(personalFragment);
                break;

        }
        fm.commit();
        curTag = tag;
        updateBottom();
    }

    private void updateBottom() {
        tabTextWdbj.setTextColor(getResources().getColor(R.color.bottom_tab_color));
        tabImgWdbj.setImageResource(R.mipmap.ic_wdbj);
        tabTextPersonal.setTextColor(getResources().getColor(R.color.bottom_tab_color));
        tabImgGrzx.setImageResource(R.mipmap.ic_grzx);
        switch (curTag){
            case TAG_FRAG_MYTASK:
                tabTextWdbj.setTextColor(getResources().getColor(R.color.white));
                tabImgWdbj.setImageResource(R.mipmap.ic_wdbj_tab);
                break;
            case TAG_FRAG_MAIN:
              //  tabTextHome.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
                break;
            case TAG_FRAG_PERSONAL:
                tabTextPersonal.setTextColor(getResources().getColor(R.color.white));
                tabImgGrzx.setImageResource(R.mipmap.ic_grzx_tab);
                break;
        }
    }

    private void hideAllFragMent(FragmentTransaction fm) {

        if(myTaskFragment != null)
            fm.hide(myTaskFragment);

        if(mainFragment != null)
            fm.hide(mainFragment);

        if(personalFragment != null)
            fm.hide(personalFragment);

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("curTag",curTag);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tab_wdbj:
                if(curTag != TAG_FRAG_MYTASK)
                    setTab(TAG_FRAG_MYTASK);
                break;
            case R.id.tab_home:
                if(curTag != TAG_FRAG_MAIN)
                    setTab(TAG_FRAG_MAIN);
                break;

            case R.id.tab_personal:
                if(curTag != TAG_FRAG_PERSONAL)
                    setTab(TAG_FRAG_PERSONAL);
                break;
        }
    }

    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();
    }

    @Override
    protected void onStop() {
        isForeground = false;
        super.onStop();
    }


    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    //for receive customer msg from jpush server
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.ztesoft.zwfw.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                    String messge = intent.getStringExtra(KEY_MESSAGE);
                    String extras = intent.getStringExtra(KEY_EXTRAS);
                    StringBuilder showMsg = new StringBuilder();
                    showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                    if (!TextUtils.isEmpty(extras)) {
                        showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                    }
                    //update
                   // Toast.makeText(mContext,showMsg.toString(),Toast.LENGTH_SHORT).show();
                    if(null != updateMsgCountListener)
                        updateMsgCountListener.onUpdate();

                }
            } catch (Exception e){
            }
        }
    }



    public interface UpdateMsgCountListener{
        public void onUpdate();
    }

    private UpdateMsgCountListener updateMsgCountListener;

    public void setUpdateMsgCountListener(UpdateMsgCountListener updateMsgCountListener) {
        this.updateMsgCountListener = updateMsgCountListener;
    }
}
