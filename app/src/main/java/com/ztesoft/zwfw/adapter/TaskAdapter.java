package com.ztesoft.zwfw.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ztesoft.zwfw.R;
import com.ztesoft.zwfw.domain.Task;

import java.util.List;

/**
 * Created by BaoChengchen on 2017/9/11.
 */

public class TaskAdapter extends BaseAdapter {

    private Context context;
    private List<Task> tasks;

    public TaskAdapter(Context context, List<Task> tasks) {
        this.context = context;
        this.tasks = tasks;
    }

    @Override
    public int getCount() {
        return tasks.size();
    }

    @Override
    public Object getItem(int position) {
        return tasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (null == convertView) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_my_todo, null);
        }
        TextView taskNoTv = (TextView) convertView.findViewById(R.id.task_no_tv);
        TextView taskTimelineTv = (TextView) convertView.findViewById(R.id.task_time_line_tv);
        TextView surNameTv = (TextView) convertView.findViewById(R.id.surname_tv);
        TextView nameTv = (TextView) convertView.findViewById(R.id.name_tv);
        TextView taskTitleTv = (TextView) convertView.findViewById(R.id.title_tv);
        TextView taskStatusTv = (TextView) convertView.findViewById(R.id.status_tv);

        Task task = tasks.get(position);
        taskNoTv.setText("办件编号：" + task.getWorkNo());
        if (!TextUtils.isEmpty(task.getPromiseDate()))
            taskTimelineTv.setText("办结时限：" + task.getPromiseDate());
        if (position % 3 == 0) {
            surNameTv.setBackground(context.getResources().getDrawable(R.drawable.red_corner_text_bg));
        } else if (position % 3 == 1) {
            surNameTv.setBackground(context.getResources().getDrawable(R.drawable.pink_corner_text_bg));
        } else {
            surNameTv.setBackground(context.getResources().getDrawable(R.drawable.blue_corner_text_bg));
        }
        surNameTv.setText(task.getApplicantName().substring(0, 1));
        nameTv.setText(task.getApplicantName());
        taskTitleTv.setText(task.getItemOrThemeName());
        taskStatusTv.setText(task.getTaskname());

        return convertView;
    }
}