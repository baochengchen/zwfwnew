package com.ztesoft.zwfw.moudle.warning;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ztesoft.zwfw.R;
import com.ztesoft.zwfw.adapter.TaskAdapter;
import com.ztesoft.zwfw.domain.resp.QueryTaskListResp;
import com.ztesoft.zwfw.moudle.Config;
import com.ztesoft.zwfw.moudle.LoginActivity;
import com.ztesoft.zwfw.moudle.todo.TaskDetailActivity;
import com.ztesoft.zwfw.base.BaseActivity;
import com.ztesoft.zwfw.domain.Task;
import com.ztesoft.zwfw.utils.APPPreferenceManager;
import com.ztesoft.zwfw.utils.SessionUtils;
import com.ztesoft.zwfw.utils.http.RequestManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EarlyWarningActivity extends BaseActivity {


    LinearLayout mNoDataLayout;
    PullToRefreshListView mTaskLv;
    private List<Task> mTasks = new ArrayList<>();
    private TaskAdapter mTaskAdapter;

    private int curPage = 0;

    private int type = 2;

    private String curRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_early_warning);
        TextView csTitile = (TextView) findViewById(R.id.cs_title);

        type = getIntent().getIntExtra("type", 2);
        switch (type) {

            case 2:
                csTitile.setText(getString(R.string.alarm_task));
                break;
            case 3:
                csTitile.setText(getString(R.string.time_out_task));
                break;
        }
        findViewById(R.id.cs_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        curRole = APPPreferenceManager.getInstance().getString(mContext, Config.CURRENT_ROLE);

        mNoDataLayout = (LinearLayout) findViewById(R.id.no_data_layout);
        mTaskLv = (PullToRefreshListView) findViewById(R.id.task_lv);
        mTaskLv.setMode(PullToRefreshBase.Mode.BOTH);
        mTaskLv.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载更多");
        mTaskLv.getLoadingLayoutProxy(false, true).setReleaseLabel("松开以加载");

        mTaskAdapter = new TaskAdapter(mContext, mTasks);
        mTaskLv.setAdapter(mTaskAdapter);

        mTaskLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, TaskDetailActivity.class);
                intent.putExtra("task", mTasks.get(position - 1));
                startActivity(intent);
            }
        });

        mTaskLv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                curPage = 0;
                requestData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                curPage++;
                requestData();
            }
        });

        mTaskLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, TaskDetailActivity.class);
                intent.putExtra("data", mTasks.get(position - 1));
                intent.putExtra(TaskDetailActivity.FLAG_BUTTON,false);
                startActivity(intent);
            }
        });
        requestData();
    }


    private void requestData() {

        Map<String, String> map = new HashMap();

        if (TextUtils.equals(curRole, Config.RoleType.OSP.getName())) {
            map.put("roleType", "" + Config.RoleType.OSP.getIndex());
        } else if (TextUtils.equals(curRole, Config.RoleType.OJD.getName())) {
            map.put("roleType", "" + Config.RoleType.OJD.getIndex());
        } else {
            map.put("roleType", "" + Config.RoleType.AJD.getIndex());
        }

        RequestManager.getInstance().postHeader(Config.BASE_URL + (type == 2 ? Config.URL_QUERYWARN : Config.URL_QUERYTIMEOUT) + "?page=" + curPage + "&size=20", JSON.toJSONString(map), new RequestManager.RequestListener() {
            @Override
            public void onRequest(String url, int actionId) {
            }

            @Override
            public void onSuccess(String response, String url, int actionId) {

                mTaskLv.onRefreshComplete();
                QueryTaskListResp resp = JSON.parseObject(response, QueryTaskListResp.class);
                if (resp.getContent() != null) {
                    if (resp.isFirst()) {
                        mTasks.clear();
                        mTasks.addAll(resp.getContent());
                        mTaskAdapter.notifyDataSetChanged();
                        if(resp.getContent().size()==0){
                            mNoDataLayout.setVisibility(View.VISIBLE);
                        }else{
                            mNoDataLayout.setVisibility(View.GONE);
                        }
                    } else {
                        if (resp.getContent().size() == 0) {
                            curPage--;
                            Toast.makeText(mContext, "没有更多数据了", Toast.LENGTH_SHORT).show();
                        } else {
                            mTasks.addAll(resp.getContent());
                            mTaskAdapter.notifyDataSetChanged();
                        }
                    }

                }
            }

            @Override
            public void onError(String errorMsg, String url, int actionId) {
                mTaskLv.onRefreshComplete();
            }
        }, curPage);
    }

}
