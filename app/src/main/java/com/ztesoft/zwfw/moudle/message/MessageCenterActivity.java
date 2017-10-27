package com.ztesoft.zwfw.moudle.message;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.ztesoft.zwfw.moudle.Config;
import com.ztesoft.zwfw.R;
import com.ztesoft.zwfw.base.BaseActivity;
import com.ztesoft.zwfw.domain.Message;
import com.ztesoft.zwfw.moudle.LoginActivity;
import com.ztesoft.zwfw.utils.APPPreferenceManager;
import com.ztesoft.zwfw.utils.SessionUtils;
import com.ztesoft.zwfw.utils.http.RequestManager;
import com.ztesoft.zwfw.widget.slidedeletelistview.SlideItem;
import com.ztesoft.zwfw.widget.slidedeletelistview.SlideListView;
import com.ztesoft.zwfw.widget.slidedeletelistview.SlideView;

import java.util.ArrayList;
import java.util.List;

public class MessageCenterActivity extends BaseActivity implements SlideView.OnSlideListener{

    ListView mMsgListView;
    LinearLayout mNoDataLayout;
    private SlideAdapter mMsgListAdapter;
    private List<Message> mMessages = new ArrayList<>();

    private SlideView mLastSlideViewWithStatusOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_center);
        TextView csTitile = (TextView) findViewById(R.id.cs_title);
        csTitile.setText(getString(R.string.msg_center));
        findViewById(R.id.cs_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        mNoDataLayout = (LinearLayout) findViewById(R.id.no_data_layout);

        mMsgListView = (ListView) findViewById(R.id.msg_lv);
        mMsgListAdapter = new SlideAdapter();
        mMsgListView.setAdapter(mMsgListAdapter);

        mMsgListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext,MessageDetailActivity.class);
                intent.putExtra(MessageDetailActivity.EXTRA_MSG,mMessages.get(position));
                startActivity(intent);
            }
        });

        requestData();
    }


        private void requestData() {
            RequestManager.getInstance().postHeader(Config.BASE_URL + Config.URL_MESSAGE_LIST,"{}",new RequestManager.RequestListener() {
                @Override
                public void onRequest(String url, int actionId) {

                }

                @Override
                public void onSuccess(String response, String url, int actionId) {
                    List<Message> messages = JSON.parseArray(response,Message.class);
                    if(messages != null && messages.size() > 0){
                        mNoDataLayout.setVisibility(View.GONE);
                        mMsgListView.setVisibility(View.VISIBLE);
                        mMessages.addAll(messages);
                        mMsgListAdapter.notifyDataSetChanged();
                    }else{
                        mNoDataLayout.setVisibility(View.VISIBLE);
                        mMsgListView.setVisibility(View.GONE);
                    }

                }

                @Override
                public void onError(String errorMsg, String url, int actionId) {
                    if(SessionUtils.invalid(errorMsg)){
                        Toast.makeText(mContext,"会话超时",Toast.LENGTH_SHORT).show();
                        APPPreferenceManager.getInstance().saveObject(mContext, Config.IS_LOGIN, false);
                        startActivity(new Intent(mContext,LoginActivity.class));
                        finish();
                    }else{
                        Toast.makeText(mContext, "请求数据失败", Toast.LENGTH_SHORT).show();
                    }
                }
            },0);
        mMsgListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSlide(View view, int status) {
        if (mLastSlideViewWithStatusOn != null && mLastSlideViewWithStatusOn != view) {
            mLastSlideViewWithStatusOn.shrink();
        }

        if (status == SLIDE_STATUS_ON) {
            mLastSlideViewWithStatusOn = (SlideView) view;
        }
    }



    public class SlideAdapter extends BaseAdapter{

        //private List<String> dataList;
        //private Context context;
        //private LayoutInflater inflater;
        public SlideAdapter(){

        }

        public SlideAdapter(Context context, List<String> dataList) {
           // this.context = context;
            //this.dataList = dataList;
            //this.inflater=LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mMessages.size();
        }

        @Override
        public Object getItem(int position) {
            return mMessages.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            ViewHolder vHolder=null;
            if (convertView==null){
                View content=LayoutInflater.from(mContext).inflate(R.layout.slip_msg_list_item,null);
                View menu=LayoutInflater.from(mContext).inflate(R.layout.delete_slide,null);
                vHolder=new ViewHolder(content,menu);
                SlideItem slideItem=new SlideItem(mContext);
                slideItem.setContentView(content,menu);
                convertView=slideItem;
                convertView.setTag(vHolder);
            }else {
                vHolder= (ViewHolder) convertView.getTag();
            }

            vHolder.tvType.setText("消息类型："+mMessages.get(position).getRevType().getTitle());
            vHolder.tvTitle.setText(mMessages.get(position).getTitle());
            vHolder.tvSendTime.setText(mMessages.get(position).getSendDate());
            vHolder.tvSender.setText(mMessages.get(position).getSender());

            vHolder.itemDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mMessages.remove(position);
                    notifyDataSetChanged();
                }
            });
            return convertView;
        }

        class ViewHolder{
            TextView  tvType;
            TextView  tvTitle;
            TextView  tvSendTime;
            TextView  tvSender;
            ViewGroup itemDelete;


            public ViewHolder(View content,View menu) {
                tvType = (TextView) content.findViewById(R.id.tv_type);
                tvTitle = (TextView) content.findViewById(R.id.tv_title);
                tvSendTime = (TextView) content.findViewById(R.id.tv_sendTime);
                tvSender = (TextView) content.findViewById(R.id.tv_sender);
                this.itemDelete = (ViewGroup) menu.findViewById(R.id.holder);

            }
        }

    }



}
