package com.ztesoft.zwfw.moudle.workchat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ztesoft.zwfw.R;
import com.ztesoft.zwfw.base.BaseActivity;
import com.ztesoft.zwfw.domain.QueryUser;
import com.ztesoft.zwfw.domain.req.SearchUserReq;
import com.ztesoft.zwfw.domain.resp.QueryUserResp;
import com.ztesoft.zwfw.moudle.Config;
import com.ztesoft.zwfw.utils.http.RequestManager;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SearchUsersActivity extends BaseActivity {

    public static final int RESULT_CODE_SELECT = 100;

    EditText mSearchEdt;
    PullToRefreshListView mUserLv;
    private int curPage = 0;
    private int pageSize = 40;
    private String queryStr;
    private List<QueryUser> queryUsers = new ArrayList<>();
    private UserListAdapter mUserListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_users);


        mSearchEdt = (EditText) findViewById(R.id.search_edit);

        mSearchEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                queryStr = s.toString();
                if (TextUtils.isEmpty(queryStr)) {
                    // clear list
                    queryUsers.clear();
                    mUserListAdapter.notifyDataSetChanged();

                } else {
                    curPage = 0;
                    mSearchRunnable.startSearch();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mUserLv = (PullToRefreshListView) findViewById(R.id.userLv);
        mUserLv.setMode(PullToRefreshBase.Mode.BOTH);
        mUserLv.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载更多");
        mUserLv.getLoadingLayoutProxy(false, true).setReleaseLabel("松开以加载");
        mUserListAdapter = new UserListAdapter();
        mUserLv.setAdapter(mUserListAdapter);


        mUserLv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                curPage = 0;
                requestUsers();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                curPage++;
                requestUsers();
            }
        });

        mUserLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("queryUser",queryUsers.get(position-1));
                setResult(RESULT_CODE_SELECT,intent);
                finish();
            }
        });
    }


    Handler mHandler = new Handler();


    SearchRunnable mSearchRunnable = new SearchRunnable();

    class SearchRunnable implements Runnable {


        public void startSearch() {
            mHandler.removeCallbacks(this);
            mHandler.post(this);
        }

        @Override
        public void run() {
            requestUsers();
        }
    }

    private void requestUsers() {
        SearchUserReq searchUserReq = new SearchUserReq();
        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher matcher = pattern.matcher(queryStr);
        if(matcher.matches()){
            searchUserReq.setUserCode(queryStr);
        }else{
            searchUserReq.setUserName(queryStr);
        }
        searchUserReq.setPageIndex(curPage);
        searchUserReq.setPageLen(pageSize);

        RequestManager.getInstance().postHeader(Config.BASE_URL + Config.URL_USERS_QUERY, JSON.toJSONString(searchUserReq), new RequestManager.RequestListener() {
            @Override
            public void onRequest(String url, int actionId) {

            }

            @Override
            public void onSuccess(String response, String url, int actionId) {
                mUserLv.onRefreshComplete();
                try {
                    QueryUserResp queryUserResp = JSON.parseObject(response, QueryUserResp.class);
                    if(null != queryUserResp){
                        if(curPage == 0){
                            queryUsers.clear();
                            queryUsers.addAll(queryUserResp.getContent());
                            mUserListAdapter.notifyDataSetChanged();
                        }else{
                            if(queryUserResp.getContent().size()== 0){
                                curPage--;
                                Toast.makeText(mContext, "没有更多数据了", Toast.LENGTH_SHORT).show();
                            }else{
                                queryUsers.addAll(queryUserResp.getContent());
                                mUserListAdapter.notifyDataSetChanged();
                            }
                        }

                    }
                    mUserListAdapter.notifyDataSetChanged();
                }catch (JSONException e){
                    queryUsers.clear();
                    mUserListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(String errorMsg, String url, int actionId) {
                mUserLv.onRefreshComplete();
            }
        }, 0);
    }


    class UserListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return queryUsers.size();
        }

        @Override
        public Object getItem(int position) {
            return queryUsers.size() == 0 ? null : queryUsers.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            QueryUser queryUser = queryUsers.get(position);
            if (null == convertView) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent, false);
            }
            TextView userCodeTv = (TextView) convertView.findViewById(R.id.userCode_tv);
            TextView userNameTv = (TextView) convertView.findViewById(R.id.userName_tv);
            userCodeTv.setText(queryUser.getUserCode());
            userNameTv.setText(queryUser.getUserName());
            return convertView;
        }
    }


    public void onCancel(View view){
        onBackPressed();
    }
}
