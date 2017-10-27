package com.ztesoft.zwfw.moudle;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ztesoft.zwfw.R;
import com.ztesoft.zwfw.adapter.TaskAdapter;
import com.ztesoft.zwfw.base.BaseFragment;
import com.ztesoft.zwfw.domain.Task;
import com.ztesoft.zwfw.domain.resp.QueryTaskListResp;
import com.ztesoft.zwfw.moudle.todo.TaskDetailActivity;
import com.ztesoft.zwfw.utils.APPPreferenceManager;
import com.ztesoft.zwfw.utils.SessionUtils;
import com.ztesoft.zwfw.utils.http.RequestManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BaoChengchen on 2017/8/7.
 */

public class MyTaskFragment extends BaseFragment{

    View mView;
    LinearLayout mNoDataLayout;
    PullToRefreshListView mTaskLv;
    private List<Task> mTasks = new ArrayList<>();
    private TaskAdapter mTaskAdapter;

    private int curPage = 0;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_layout_my_task,container,false);
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mView.findViewById(R.id.tt_layout).setBackgroundResource(R.mipmap.sy_top);
        TextView csTitile = (TextView) mView.findViewById(R.id.cs_title);
        csTitile.setText(getString(R.string.approve_center));
        csTitile.setTextColor(getResources().getColorStateList(R.color.white));
        mView.findViewById(R.id.cs_back).setVisibility(View.GONE);

        mNoDataLayout = (LinearLayout) mView.findViewById(R.id.no_data_layout);
        mTaskLv = (PullToRefreshListView) mView.findViewById(R.id.task_lv);
        mTaskLv.setMode(PullToRefreshBase.Mode.BOTH);

        mTaskLv.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载更多");
        mTaskLv.getLoadingLayoutProxy(false, true).setReleaseLabel("松开以加载");
        mTaskAdapter = new TaskAdapter(getActivity(),mTasks);
        mTaskLv.setAdapter(mTaskAdapter);


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
                Intent intent = new Intent(getActivity(),TaskDetailActivity.class);
                intent.putExtra("data",mTasks.get(position-1));
                intent.putExtra(TaskDetailActivity.FLAG_BUTTON,false);
                startActivity(intent);
            }
        });


        requestData();


    }


    private void requestData() {

        RequestManager.getInstance().postHeader(Config.BASE_URL + Config.URL_QUERYMYWORKTASKS + "?page=" + curPage + "&size=20", "{}", new RequestManager.RequestListener() {
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
                            Toast.makeText(getActivity(), "没有更多数据了", Toast.LENGTH_SHORT).show();
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
                if(SessionUtils.invalid(errorMsg)){
                    Toast.makeText(getActivity(),"会话超时",Toast.LENGTH_SHORT).show();
                    APPPreferenceManager.getInstance().saveObject(getActivity(), Config.IS_LOGIN, false);
                    startActivity(new Intent(getActivity(),LoginActivity.class));
                    getActivity().finish();
                }
            }
        }, curPage);
    }

     public static MyTaskFragment newInstance() {

        Bundle args = new Bundle();
        MyTaskFragment fragment = new MyTaskFragment();
        fragment.setArguments(args);
        return fragment;
    }


}
