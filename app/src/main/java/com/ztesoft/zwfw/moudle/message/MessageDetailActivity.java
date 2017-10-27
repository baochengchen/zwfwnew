package com.ztesoft.zwfw.moudle.message;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.TextView;

import com.ztesoft.zwfw.R;
import com.ztesoft.zwfw.base.BaseActivity;
import com.ztesoft.zwfw.domain.Message;

/**
 * Created by Baochengchen on 2017/8/14.
 */

public class MessageDetailActivity extends BaseActivity {

    TextView tvTitle;
    TextView tvSender;
    TextView tvSendTime;
    TextView tvContent;

    private Message mMsg;

    public  static String EXTRA_MSG = "message";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);
        TextView csTitile = (TextView) findViewById(R.id.cs_title);
        csTitile.setText(getString(R.string.msg_center));
        findViewById(R.id.cs_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvSender = (TextView) findViewById(R.id.tv_sender);
        tvSendTime = (TextView) findViewById(R.id.tv_sendTime);
        tvContent = (TextView) findViewById(R.id.tv_content);

        mMsg = (Message) getIntent().getSerializableExtra(EXTRA_MSG);

        tvTitle.setText(mMsg.getTitle());
        tvSender.setText("发送人："+mMsg.getSender());
        tvSendTime.setText(mMsg.getSendDate());
        tvContent.setText(mMsg.getContent());
    }
}
