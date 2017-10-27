package com.ztesoft.zwfw.moudle.workchat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ztesoft.zwfw.R;
import com.ztesoft.zwfw.adapter.WorkChatAdapter;
import com.ztesoft.zwfw.base.BaseFragment;
import com.ztesoft.zwfw.domain.Chat;
import com.ztesoft.zwfw.domain.Consult;
import com.ztesoft.zwfw.domain.WorkChatBean;
import com.ztesoft.zwfw.domain.resp.ChatListResp;
import com.ztesoft.zwfw.moudle.Config;
import com.ztesoft.zwfw.moudle.LoginActivity;
import com.ztesoft.zwfw.utils.APPPreferenceManager;
import com.ztesoft.zwfw.utils.SessionUtils;
import com.ztesoft.zwfw.utils.http.RequestManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by BaoChengchen on 2017/8/25.
 */

public class WorkChatMineFragment extends BaseFragment {

    View rootView;
    LinearLayout mNoDataLayout;
    PullToRefreshListView mLv;
    private WorkChatAdapter mWorkChatAdapter;
    private List<Chat> mChats = new ArrayList<>();

    private int curPage = 0;

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
        rootView = inflater.inflate(R.layout.fragment_work_chat_mine,container,false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mNoDataLayout = (LinearLayout) rootView.findViewById(R.id.no_data_layout);
        mLv = (PullToRefreshListView) rootView.findViewById(R.id.work_chat_lv);
        mLv.setMode(PullToRefreshBase.Mode.BOTH);
        mLv.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载更多");
        mLv.getLoadingLayoutProxy(false, true).setReleaseLabel("松开以加载");
        mWorkChatAdapter = new WorkChatAdapter(getActivity(),mChats,Config.TYPE_MINE);
        mLv.setAdapter(mWorkChatAdapter);

        mLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),WorkChatDetailActivity.class);
                Chat chat = mChats.get(position-1);
                intent.putExtra("chat",chat);
                intent.putExtra("type",Config.TYPE_MINE);
                TextView stateView = (TextView) view.findViewById(R.id.read_state_tv);
                stateView.setVisibility(View.GONE);
                startActivity(intent);
            }
        });

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

        requestData();
    }

    private void requestData() {
        Map<String,Long> map = new HashMap<>();
        map.put("byUserId",Long.parseLong(((WorkChatActivity)(getActivity())).getmUser().getUserId()));
        RequestManager.getInstance().postHeader(Config.BASE_URL + Config.URL_TALK_CHATLIST, JSON.toJSONString(map), new RequestManager.RequestListener() {
            @Override
            public void onRequest(String url, int actionId) {

            }

            @Override
            public void onSuccess(String response, String url, int actionId) {
                mLv.onRefreshComplete();
                ChatListResp chatListResp = JSON.parseObject(response,ChatListResp.class);
                if (chatListResp.getContent() != null) {
                    if (chatListResp.isFirst()) {
                        mChats.clear();
                        mChats.addAll(chatListResp.getContent());
                        mWorkChatAdapter.notifyDataSetChanged();
                        if(chatListResp.getContent().size()==0){
                            mNoDataLayout.setVisibility(View.VISIBLE);
                        }else{
                            mNoDataLayout.setVisibility(View.GONE);
                        }
                    } else {
                        if (chatListResp.getContent().size() == 0) {
                            curPage--;
                            Toast.makeText(getActivity(), "没有更多数据了", Toast.LENGTH_SHORT).show();
                        } else {
                            mChats.addAll(chatListResp.getContent());
                            mWorkChatAdapter.notifyDataSetChanged();
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
                    Toast.makeText(getActivity(),errorMsg,Toast.LENGTH_SHORT).show();
                }
            }
        },0);
    }


    public static WorkChatMineFragment newInstance() {

        Bundle args = new Bundle();
        WorkChatMineFragment fragment = new WorkChatMineFragment();
        fragment.setArguments(args);
        return fragment;
    }



    public void refreshData(){
        curPage = 0;
        requestData();
    }

}
