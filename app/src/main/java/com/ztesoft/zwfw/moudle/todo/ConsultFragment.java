package com.ztesoft.zwfw.moudle.todo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.ztesoft.zwfw.domain.Consult;
import com.ztesoft.zwfw.domain.resp.QueryConsultListResp;
import com.ztesoft.zwfw.moudle.LoginActivity;
import com.ztesoft.zwfw.utils.APPPreferenceManager;
import com.ztesoft.zwfw.utils.SessionUtils;
import com.ztesoft.zwfw.utils.http.RequestManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BaoChengchen on 2017/8/30.
 */

public class ConsultFragment extends BaseFragment {

    private static final String TAG = ConsultFragment.class.getSimpleName();

    View rootView;
    LinearLayout mNoDataLayout;
    PullToRefreshListView mConsultLv;

    private List<Consult> mConsults = new ArrayList<>();
    private ConsultAdapter mConsultAdapter;

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
        rootView = inflater.inflate(R.layout.fragment_consult,container,false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mNoDataLayout = (LinearLayout) rootView.findViewById(R.id.no_data_layout);
        mConsultLv = (PullToRefreshListView) rootView.findViewById(R.id.consult_lv);
        mConsultLv.setMode(PullToRefreshBase.Mode.BOTH);
        mConsultLv.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载更多");
        mConsultLv.getLoadingLayoutProxy(false, true).setReleaseLabel("松开以加载");
        mConsultAdapter = new ConsultAdapter();
        mConsultLv.setAdapter(mConsultAdapter);

        mConsultLv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
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

        mConsultLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                curClickPositon = position - 1;
                curClickPage = curClickPositon / 20;
                curPageOffset = curClickPositon % 20;
                Intent intent = new Intent(getActivity(), TaskDetailActivity.class);
                intent.putExtra("data",mConsults.get(position-1));
                startActivityForResult(intent,TaskDetailActivity.REQUEST_REFRESH);
            }
        });


        requestData();

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void requestData() {
        RequestManager.getInstance().postHeader(Config.BASE_URL + Config.URL_QRYINTERACTION + "?page=" + curPage + "&size=20","{}", new RequestManager.RequestListener() {
            @Override
            public void onRequest(String url, int actionId) {

            }

            @Override
            public void onSuccess(String response, String url, int actionId) {

                mConsultLv.onRefreshComplete();
                QueryConsultListResp resp = JSON.parseObject(response, QueryConsultListResp.class);
                if (resp.getContent() != null) {
                    totalSize = Integer.parseInt(resp.getTotalElements());
                    if (resp.isFirst()) {
                        mConsults.clear();
                        mConsults.addAll(resp.getContent());
                        mConsultAdapter.notifyDataSetChanged();
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
                            mConsults.addAll(resp.getContent());
                            mConsultAdapter.notifyDataSetChanged();
                        }
                    }

                }
            }

            @Override
            public void onError(String errorMsg, String url, int actionId) {
                mConsultLv.onRefreshComplete();
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

    public static ConsultFragment newInstance() {

        Bundle args = new Bundle();
        ConsultFragment fragment = new ConsultFragment();
        fragment.setArguments(args);
        return fragment;
    }

    class ConsultAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mConsults.size();
        }

        @Override
        public Object getItem(int position) {
            return mConsults.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (null == convertView) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_consult, null);
            }
            TextView applyTimeTv = (TextView) convertView.findViewById(R.id.apply_time_tv);
            TextView surNameTv = (TextView) convertView.findViewById(R.id.surname_tv);
            TextView nameTv = (TextView) convertView.findViewById(R.id.name_tv);
            TextView taskTitleTv = (TextView) convertView.findViewById(R.id.title_tv);
            TextView taskStatusTv = (TextView) convertView.findViewById(R.id.status_tv);

            Consult consult = mConsults.get(position);
            applyTimeTv.setText("申请时间：" + consult.getCreateDate());
            if (position % 3 == 0) {
                surNameTv.setBackground(getResources().getDrawable(R.drawable.red_corner_text_bg));
            } else if (position % 3 == 1) {
                surNameTv.setBackground(getResources().getDrawable(R.drawable.pink_corner_text_bg));
            } else {
                surNameTv.setBackground(getResources().getDrawable(R.drawable.blue_corner_text_bg));
            }
            surNameTv.setText(consult.getWebUserName().substring(0, 1));
            nameTv.setText(consult.getWebUserName());
            taskTitleTv.setText(consult.getTitle());
            taskStatusTv.setText(consult.getTaskname());

            return convertView;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == TaskDetailActivity.RESULET_CUSTOM_REPLY) {
            if (TaskDetailActivity.REQUEST_REFRESH == requestCode) {
                updateItem();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateItem() {
        RequestManager.getInstance().postHeader(Config.BASE_URL + Config.URL_QRYINTERACTION + "?page=" + curClickPage + "&size=20", "{}", new RequestManager.RequestListener() {
            @Override
            public void onRequest(String url, int actionId) {
            }

            @Override
            public void onSuccess(String response, String url, int actionId) {
                QueryConsultListResp resp = JSON.parseObject(response, QueryConsultListResp.class);
                if (null != resp) {
                    if(totalSize == Integer.parseInt(resp.getTotalElements())){
                        mConsults.remove(curClickPositon);
                        if (resp.getContent().size() > 0) {
                            mConsults.add(curClickPositon, resp.getContent().get(curPageOffset));
                        }
                    }else{
                        mConsults.remove(curClickPositon);
                        totalSize = Integer.parseInt(resp.getTotalElements());
                    }
                    mConsultAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(String errorMsg, String url, int actionId) {

            }
        }, curClickPage);

    }
}
