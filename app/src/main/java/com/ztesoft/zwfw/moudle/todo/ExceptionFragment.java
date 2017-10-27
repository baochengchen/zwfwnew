package com.ztesoft.zwfw.moudle.todo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.ztesoft.zwfw.base.BaseFragment;
import com.ztesoft.zwfw.domain.Exception;
import com.ztesoft.zwfw.domain.resp.QueryExceptionListResp;
import com.ztesoft.zwfw.moudle.Config;
import com.ztesoft.zwfw.moudle.LoginActivity;
import com.ztesoft.zwfw.utils.APPPreferenceManager;
import com.ztesoft.zwfw.utils.SessionUtils;
import com.ztesoft.zwfw.utils.http.RequestManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BaoChengchen on 2017/10/24.
 */

public class ExceptionFragment extends BaseFragment {
    private static final String TAG = ExceptionFragment.class.getSimpleName();

    View rootView;
    LinearLayout mNoDataLayout;
    PullToRefreshListView mLv;

    private List<Exception> mExceptions = new ArrayList<>();
    private ExceptionAdapter mExceptionAdapter;

    private int curPage = 0;
    private int totalSize = 0;
    private int curClickPositon = 0;
    private int curClickPage = 0;
    private int curPageOffset = 0;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_exception,container,false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mNoDataLayout = (LinearLayout) rootView.findViewById(R.id.no_data_layout);
        mLv = (PullToRefreshListView) rootView.findViewById(R.id.exception_lv);
        mLv.setMode(PullToRefreshBase.Mode.BOTH);
        mLv.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载更多");
        mLv.getLoadingLayoutProxy(false, true).setReleaseLabel("松开以加载");
        mExceptionAdapter = new ExceptionAdapter();
        mLv.setAdapter(mExceptionAdapter);

        mLv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
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


        mLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                curClickPositon = position - 1;
                curClickPage = curClickPositon / 20;
                curPageOffset = curClickPositon % 20;
                Intent intent = new Intent(getActivity(), TaskDetailActivity.class);
                intent.putExtra("data",mExceptions.get(position-1));
                startActivityForResult(intent,TaskDetailActivity.REQUEST_REFRESH);
            }
        });

        requestData();
    }


    private void requestData() {

        RequestManager.getInstance().postHeader(Config.BASE_URL + Config.URL_QRYEXCEPTION + "?page=" + curPage + "&size=20", "{}", new RequestManager.RequestListener() {
            @Override
            public void onRequest(String url, int actionId) {

            }

            @Override
            public void onSuccess(String response, String url, int actionId) {

                mLv.onRefreshComplete();
                QueryExceptionListResp resp = JSON.parseObject(response, QueryExceptionListResp.class);
                if (resp.getContent() != null) {
                    totalSize = Integer.parseInt(resp.getTotalElements());
                    if (resp.isFirst()) {
                        mExceptions.clear();
                        mExceptions.addAll(resp.getContent());
                        mExceptionAdapter.notifyDataSetChanged();
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
                            mExceptions.addAll(resp.getContent());
                            mExceptionAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onError(String errorMsg, String url, int actionId) {
                mLv.onRefreshComplete();
                if(SessionUtils.invalid(errorMsg)){
                    Toast.makeText(getActivity(),"会话超时",Toast.LENGTH_SHORT).show();
                    APPPreferenceManager.getInstance().saveObject(getActivity(), Config.IS_LOGIN, false);
                    startActivity(new Intent(getActivity(),LoginActivity.class));
                    getActivity().finish();
                }else{
                    Toast.makeText(getActivity(), "请求数据失败", Toast.LENGTH_SHORT).show();
                }
            }
        }, curPage);
    }

    public static ExceptionFragment newInstance() {

        Bundle args = new Bundle();
        ExceptionFragment fragment = new ExceptionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    class ExceptionAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mExceptions.size();
        }

        @Override
        public Object getItem(int position) {
            return mExceptions.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (null == convertView) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_my_todo, null);
            }

            TextView taskNoTv = (TextView) convertView.findViewById(R.id.task_no_tv);
            TextView taskTimelineTv = (TextView) convertView.findViewById(R.id.task_time_line_tv);
            TextView surNameTv = (TextView) convertView.findViewById(R.id.surname_tv);
            TextView nameTv = (TextView) convertView.findViewById(R.id.name_tv);
            TextView taskTitleTv = (TextView) convertView.findViewById(R.id.title_tv);
            TextView taskStatusTv = (TextView) convertView.findViewById(R.id.status_tv);

            Exception task = mExceptions.get(position);
            taskNoTv.setText("办件编号：" + task.getWorkNo());
            if (!TextUtils.isEmpty(task.getPromiseDate()))
                taskTimelineTv.setText("办结时限：" + task.getPromiseDate());
            if (position % 3 == 0) {
                surNameTv.setBackground(getActivity().getResources().getDrawable(R.drawable.red_corner_text_bg));
            } else if (position % 3 == 1) {
                surNameTv.setBackground(getActivity().getResources().getDrawable(R.drawable.pink_corner_text_bg));
            } else {
                surNameTv.setBackground(getActivity().getResources().getDrawable(R.drawable.blue_corner_text_bg));
            }
            surNameTv.setText(task.getApplicantName().substring(0, 1));
            nameTv.setText(task.getApplicantName());
            taskTitleTv.setText(task.getItemOrThemeName());
            taskStatusTv.setText(task.getExceptionCurrentTask());

            return convertView;
        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG,"onActivityResult");
        if (resultCode == TaskDetailActivity.RESULET_CUSTOM_REPLY) {
            if (TaskDetailActivity.REQUEST_REFRESH == requestCode) {
                updateItem();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateItem() {
        RequestManager.getInstance().postHeader(Config.BASE_URL + Config.URL_QRYEXCEPTION + "?page=" + curClickPage + "&size=20", "{}", new RequestManager.RequestListener() {
            @Override
            public void onRequest(String url, int actionId) {
            }

            @Override
            public void onSuccess(String response, String url, int actionId) {
                QueryExceptionListResp resp = JSON.parseObject(response, QueryExceptionListResp.class);
                if (null != resp) {
                    if(totalSize == Integer.parseInt(resp.getTotalElements())){
                        mExceptions.remove(curClickPositon);
                        if (resp.getContent().size() > 0) {
                            mExceptions.add(curClickPositon, resp.getContent().get(curPageOffset));
                        }
                    }else{
                        mExceptions.remove(curClickPositon);
                        totalSize = Integer.parseInt(resp.getTotalElements());
                    }
                    mExceptionAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(String errorMsg, String url, int actionId) {

            }
        }, curClickPage);

    }
}
