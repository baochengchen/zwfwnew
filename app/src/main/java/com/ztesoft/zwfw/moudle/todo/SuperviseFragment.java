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
import com.ztesoft.zwfw.moudle.Config;
import com.ztesoft.zwfw.R;
import com.ztesoft.zwfw.base.BaseFragment;
import com.ztesoft.zwfw.domain.Supervise;
import com.ztesoft.zwfw.domain.resp.QuerySuperviseListResp;
import com.ztesoft.zwfw.moudle.LoginActivity;
import com.ztesoft.zwfw.utils.APPPreferenceManager;
import com.ztesoft.zwfw.utils.SessionUtils;
import com.ztesoft.zwfw.utils.http.RequestManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BaoChengchen on 2017/8/30.
 */

public class SuperviseFragment extends BaseFragment {
    private static final String TAG = SuperviseFragment.class.getSimpleName();

    View rootView;
    LinearLayout mNoDataLayout;
    PullToRefreshListView mSuperviseLv;

    private List<Supervise> mSupervises = new ArrayList<>();
    private SuperviseAdapter mSuperviseAdapter;

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
        rootView = inflater.inflate(R.layout.fragment_supervise,container,false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mNoDataLayout = (LinearLayout) rootView.findViewById(R.id.no_data_layout);
        mSuperviseLv = (PullToRefreshListView) rootView.findViewById(R.id.supervise_lv);
        mSuperviseLv.setMode(PullToRefreshBase.Mode.BOTH);
        mSuperviseLv.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载更多");
        mSuperviseLv.getLoadingLayoutProxy(false, true).setReleaseLabel("松开以加载");
        mSuperviseAdapter = new SuperviseAdapter();
        mSuperviseLv.setAdapter(mSuperviseAdapter);

        mSuperviseLv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
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


        mSuperviseLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                curClickPositon = position - 1;
                curClickPage = curClickPositon / 20;
                curPageOffset = curClickPositon % 20;
                Intent intent = new Intent(getActivity(), TaskDetailActivity.class);
                intent.putExtra("data",mSupervises.get(position-1));
                startActivityForResult(intent,TaskDetailActivity.REQUEST_REFRESH);
            }
        });

        requestData();
    }


    private void requestData() {

        RequestManager.getInstance().postHeader(Config.BASE_URL + Config.URL_QRYSUPERVISE + "?page=" + curPage + "&size=20", "{}", new RequestManager.RequestListener() {
            @Override
            public void onRequest(String url, int actionId) {

            }

            @Override
            public void onSuccess(String response, String url, int actionId) {

                mSuperviseLv.onRefreshComplete();
                QuerySuperviseListResp resp = JSON.parseObject(response, QuerySuperviseListResp.class);
                if (resp.getContent() != null) {
                    totalSize = Integer.parseInt(resp.getTotalElements());
                    if (resp.isFirst()) {
                        mSupervises.clear();
                        mSupervises.addAll(resp.getContent());
                        mSuperviseAdapter.notifyDataSetChanged();
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
                            mSupervises.addAll(resp.getContent());
                            mSuperviseAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onError(String errorMsg, String url, int actionId) {
                mSuperviseLv.onRefreshComplete();
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

    public static SuperviseFragment newInstance() {

        Bundle args = new Bundle();
        SuperviseFragment fragment = new SuperviseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    class SuperviseAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mSupervises.size();
        }

        @Override
        public Object getItem(int position) {
            return mSupervises.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (null == convertView) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_supervise, null);
            }

            TextView superviseTimeTv = (TextView) convertView.findViewById(R.id.supervise_time_tv);
            TextView typeTv = (TextView) convertView.findViewById(R.id.type_tv);
            TextView codeTv = (TextView) convertView.findViewById(R.id.code_tv);
            TextView contentTv = (TextView) convertView.findViewById(R.id.content_tv);
            TextView taskStatusTv = (TextView) convertView.findViewById(R.id.status_tv);

            Supervise supervise = mSupervises.get(position);
            superviseTimeTv.setText("崔督办时间：" + supervise.getSuperviseTime());
            if (TextUtils.equals("R",supervise.getFunctionType().getCode())) {
                typeTv.setBackground(getResources().getDrawable(R.drawable.red_corner_text_bg));
            } else {
                typeTv.setBackground(getResources().getDrawable(R.drawable.blue_corner_text_bg));
            }
            typeTv.setText(supervise.getFunctionType().getTitle());
            codeTv.setText("编码："+supervise.getSuperviseNo());
            contentTv.setText(supervise.getSuperviseContent());
            taskStatusTv.setText(supervise.getSupervisionTaskName());

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
        RequestManager.getInstance().postHeader(Config.BASE_URL + Config.URL_QRYSUPERVISE + "?page=" + curClickPage + "&size=20", "{}", new RequestManager.RequestListener() {
            @Override
            public void onRequest(String url, int actionId) {
            }

            @Override
            public void onSuccess(String response, String url, int actionId) {
                QuerySuperviseListResp resp = JSON.parseObject(response, QuerySuperviseListResp.class);
                if (null != resp) {
                    if(totalSize == Integer.parseInt(resp.getTotalElements())){
                        mSupervises.remove(curClickPositon);
                        if (resp.getContent().size() > 0) {
                            mSupervises.add(curClickPositon, resp.getContent().get(curPageOffset));
                        }
                    }else{
                        mSupervises.remove(curClickPositon);
                        totalSize = Integer.parseInt(resp.getTotalElements());
                    }
                    mSuperviseAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(String errorMsg, String url, int actionId) {

            }
        }, curClickPage);

    }
}
