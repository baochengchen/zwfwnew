package com.ztesoft.zwfw.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ztesoft.zwfw.R;
import com.ztesoft.zwfw.domain.Chat;
import com.ztesoft.zwfw.domain.WorkChatBean;
import com.ztesoft.zwfw.moudle.Config;
import com.ztesoft.zwfw.utils.UnicodeUtils;

import java.util.List;

/**
 * Created by Baochengchen on 2017/8/27.
 */

public class WorkChatAdapter extends BaseAdapter {


    private Context context;
    private List<Chat> datas;
    private int type;

    public WorkChatAdapter(Context context, List<Chat> datas, int type) {
        this.context = context;
        this.datas = datas;
        this.type = type;
    }

    @Override
    public int getCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas == null ? null : datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.work_chat_item, parent, false);
        }
        TextView titleTv = (TextView) convertView.findViewById(R.id.title_tv);
        TextView contentTv = (TextView) convertView.findViewById(R.id.content_tv);
        TextView creatorTv = (TextView) convertView.findViewById(R.id.creator_tv);
        TextView readStateTv = (TextView) convertView.findViewById(R.id.read_state_tv);

        Chat chat = datas.get(position);
        if (type == Config.TYPE_MINE) {
            if (TextUtils.equals("0", chat.getByReadState())) {
                readStateTv.setVisibility(View.VISIBLE);
            } else {
                readStateTv.setVisibility(View.GONE);
            }
            if (null != chat.getToUserName())
                creatorTv.setText("发给：" + chat.getToUserName());
        } else if (type == Config.TYPE_TOME) {
            if (TextUtils.equals("0", chat.getToReadState())) {
                readStateTv.setVisibility(View.VISIBLE);
            } else {
                readStateTv.setVisibility(View.GONE);
            }
            creatorTv.setText("来自：" + chat.getByUserName());
        } else {
            creatorTv.setText("来自：" + chat.getByUserName());
        }
        titleTv.setText(chat.getTitle());
        contentTv.setText(UnicodeUtils.unicode2String(chat.getContent()));

        return convertView;
    }


}
